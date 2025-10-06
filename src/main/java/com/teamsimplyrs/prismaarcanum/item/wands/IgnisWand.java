package com.teamsimplyrs.prismaarcanum.item.wands;

import com.teamsimplyrs.prismaarcanum.api.casting.AbstractCastable;
import net.minecraft.world.item.Item;

public class IgnisWand extends AbstractCastable {
    public static final String NAME = "ignis_wand";
    public static final Item.Properties PROPERTIES = new Item.Properties();

    public IgnisWand() {
        super(PROPERTIES);
    }
}
