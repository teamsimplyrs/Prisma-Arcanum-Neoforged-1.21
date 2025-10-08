package com.teamsimplyrs.prismaarcanum.api.utils;

import com.teamsimplyrs.prismaarcanum.component.PADataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class SpellUtils {

    public static void applySpellsToCastableItem(ItemStack castableItem, List<ResourceLocation> spellsList, boolean resetExistingSpellsWithEmptySlots) {
        if (resetExistingSpellsWithEmptySlots) {
            // clear stack components here
        }

        for (var spellID: spellsList) {
            castableItem.set(PADataComponents.SPELL_ID, spellID);
        }
    }
}
