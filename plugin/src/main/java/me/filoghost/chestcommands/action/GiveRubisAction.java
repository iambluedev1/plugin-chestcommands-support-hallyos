/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.action;

import me.filoghost.chestcommands.hook.HallyosEconomyHook;
import me.filoghost.chestcommands.logging.Errors;
import me.filoghost.chestcommands.parsing.NumberParser;
import me.filoghost.chestcommands.parsing.ParseException;
import org.bukkit.entity.Player;

public class GiveRubisAction implements Action {

    private final Integer rubisToGive;

    public GiveRubisAction(String serializedAction) throws ParseException {
        rubisToGive = NumberParser.getStrictlyPositiveInteger(serializedAction);
    }

    @Override
    public void execute(Player player) {
        if (HallyosEconomyHook.INSTANCE.isEnabled()) {
            HallyosEconomyHook.giveMoney(player, rubisToGive);
        } else {
            player.sendMessage(Errors.User.configurationError("HallyosEconomyHook  plugin not found"));
        }
    }

}
