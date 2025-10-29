package com.teamsimplyrs.prismaarcanum.item;

import com.mojang.logging.LogUtils;
import com.teamsimplyrs.prismaarcanum.api.spell.spells.common.AbstractSpell;
import com.teamsimplyrs.prismaarcanum.api.utils.Element;
import com.teamsimplyrs.prismaarcanum.component.PADataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.slf4j.Logger;

public class SpellPrismItem extends Item {
    public static final String NAME = "spell_prism";
    public static final Properties PROPERTIES = new Properties();

    public Element element;

    private static final Logger LOGGER = LogUtils.getLogger();

    public SpellPrismItem() {
        super(PROPERTIES);
    }
}
