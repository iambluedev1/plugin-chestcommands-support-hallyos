/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.attribute;

import me.filoghost.chestcommands.icon.InternalConfigurableIcon;
import me.filoghost.chestcommands.logging.Errors;
import me.filoghost.chestcommands.parsing.ParseException;

public class RubisAttribute implements IconAttribute {

    private final Integer rubis;

    public RubisAttribute(Integer rubis, AttributeErrorHandler errorHandler) throws ParseException {
        if (rubis < 0) {
            throw new ParseException(Errors.Parsing.zeroOrPositive);
        }
        this.rubis = rubis;
    }

    @Override
    public void apply(InternalConfigurableIcon icon) {
        icon.setRequiredRubis(rubis);
    }

}
