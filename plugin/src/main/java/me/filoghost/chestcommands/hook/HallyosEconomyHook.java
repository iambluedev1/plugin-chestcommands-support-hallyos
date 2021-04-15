/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.hook;

import fr.iambluedev.hallyos.economy.HallyosEconomy;
import fr.iambluedev.hallyos.economy.i18n.Lang;
import fr.iambluedev.hallyos.economy.manager.PlayerManager;
import fr.iambluedev.hallyos.economy.model.ETransactionType;
import fr.iambluedev.hallyos.economy.model.Transaction;
import fr.iambluedev.hallyos.economy.model.User;
import fr.iambluedev.hallyos.economy.repository.TransactionRepository;
import fr.iambluedev.hallyos.economy.repository.UserRepository;
import me.filoghost.fcommons.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public enum HallyosEconomyHook implements PluginHook {

    INSTANCE;

    private boolean enabled = false;

    @Override
    public void setup() {
        if (Bukkit.getPluginManager().getPlugin("HallyosEconomy") == null) {
            return;
        }

        enabled = true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public static Integer getMoney(Player player) {
        INSTANCE.checkEnabledState();
        return PlayerManager.get().get(player).getMoney();
    }

    public static boolean hasMoney(Player player, Integer minimum) {
        INSTANCE.checkEnabledState();
        checkPositiveAmount(minimum);

        double balance = PlayerManager.get().get(player).getMoney();
        return balance >= minimum;
    }

    /*
     * Returns true if the operation was successful.
     */
    public static boolean takeMoney(Player player, Integer amount) {
        INSTANCE.checkEnabledState();
        checkPositiveAmount(amount);

        if(hasMoney(player, amount)) {
            User user = PlayerManager.get().get(player);
            user.setMoney(user.getMoney() - amount);
            new UserRepository().save(user);

            player.sendMessage(HallyosEconomy.get().getTranslator().translate(Lang.FRENCH, "economy_receive_remove_success", String.valueOf(amount)));

            Transaction t = new Transaction();
            t.setType(ETransactionType.SHOP);
            t.setSender(user);
            t.setAmount(amount);
            t.setReceiver(null);
            t.setAt(System.currentTimeMillis() / 1000);
            new TransactionRepository().save(t);

            return true;
        }

        return false;
    }

    public static boolean giveMoney(Player player, Integer amount) {
        INSTANCE.checkEnabledState();
        checkPositiveAmount(amount);

        User user = PlayerManager.get().get(player);
        user.setMoney(user.getMoney() + amount);
        new UserRepository().save(user);

        player.sendMessage(HallyosEconomy.get().getTranslator().translate(Lang.FRENCH, "economy_receive_success", String.valueOf(amount)));

        Transaction t = new Transaction();
        t.setType(ETransactionType.GIVE);
        t.setSender(null);
        t.setAmount(amount);
        t.setReceiver(user);
        t.setAt(System.currentTimeMillis() / 1000);
        new TransactionRepository().save(t);

        if (HallyosEconomy.get().getCustomConfig().getFileConfig().getInt("transaction.alert") <= amount) {
            for (Player staff : Bukkit.getOnlinePlayers()) {
                if (staff.hasPermission("hallyos.economy.alert")) {
                    staff.sendMessage(HallyosEconomy.get().getTranslator().translate(Lang.FRENCH, "economy_alert", String.valueOf(amount), "-ChestCommands-", user.getLogin()));
                }
            }
        }

        return true;
    }

    private static void checkPositiveAmount(Integer amount) {
        Preconditions.checkArgument(amount >= 0, "amount cannot be negative");
    }

    public static String formatMoney(Integer amount) {
        return Integer.toString(amount);
    }
}
