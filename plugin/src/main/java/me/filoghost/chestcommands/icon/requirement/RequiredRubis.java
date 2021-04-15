/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.icon.requirement;

import com.google.common.base.Preconditions;
import fr.iambluedev.hallyos.economy.HallyosEconomy;
import me.filoghost.chestcommands.hook.HallyosEconomyHook;
import me.filoghost.chestcommands.logging.Errors;
import org.bukkit.entity.Player;

public class RequiredRubis implements Requirement {

    private final Integer rubisAmount;

    public RequiredRubis(Integer moneyAmount) {
        Preconditions.checkArgument(moneyAmount > 0, "rubis amount must be positive");
        this.rubisAmount = moneyAmount;
    }

    @Override
    public boolean hasCost(Player player) {
        if (!HallyosEconomyHook.INSTANCE.isEnabled()) {
            player.sendMessage(Errors.User.configurationError(
                    "the item has a price, but HallyosEconomy was not found. "
                            + "For security, the action has been blocked"));
            return false;
        }

        if (!HallyosEconomyHook.hasMoney(player, rubisAmount)) {
            player.sendMessage(HallyosEconomy.get().getTranslator().translate(fr.iambluedev.hallyos.economy.i18n.Lang.FRENCH, "economy_error_hasnt_money", ""));
            return false;
        }

        return true;
    }

    @Override
    public boolean takeCost(Player player) {
        boolean success = HallyosEconomyHook.takeMoney(player, rubisAmount);

        if (!success) {
            player.sendMessage(Errors.User.configurationError("a rubis transaction couldn't be executed"));
        }

        return success;
    }

}
