package com.teamsimplyrs.prismaarcanum.api.utils;

import com.teamsimplyrs.prismaarcanum.component.PADataComponents;
import com.teamsimplyrs.prismaarcanum.item.SpellPrismItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SpellUtils {

    public static void writeSpellsToCastableItem(ItemStack castableItem, List<ResourceLocation> spellsList) {
        castableItem.set(PADataComponents.SPELLS_BOUND, spellsList);
    }

    public static List<ResourceLocation> readSpellsFromCastableItem(ItemStack castableItem) {
        if (castableItem == null) {
            return List.of();
        }
        return castableItem.get(PADataComponents.SPELLS_BOUND.get());
    }

    public static ResourceLocation checkPrismItemStackForSpell(ItemStack stack) {
        if (!(stack.getItem() instanceof SpellPrismItem)) {
            return null;
        }

        return stack.get(PADataComponents.SPELL_ID.get());
    }
}
