package net.ilexiconn.lawnmower.api;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum LawnType implements IStringSerializable {
    LIGHT,
    DARK;

    @Override
    public String getName() {
        return this.name().toLowerCase(Locale.ENGLISH);
    }
}
