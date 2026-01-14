package com.teamsimplyrs.prismaarcanum.api.utils;

import com.teamsimplyrs.prismaarcanum.api.casting.AbstractCastable;
import com.teamsimplyrs.prismaarcanum.registry.SpellRegistry;
import com.teamsimplyrs.prismaarcanum.spells.common.AbstractSpell;
import com.teamsimplyrs.prismaarcanum.component.PADataComponents;
import com.teamsimplyrs.prismaarcanum.item.SpellPrismItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class SpellUtils {

    public static void writeSpellsToCastableItem(ItemStack castableItem, List<ResourceLocation> spellsList) {
        if (castableItem.getItem() instanceof AbstractCastable) {
            castableItem.set(PADataComponents.SPELLS_BOUND, spellsList);
            resetCurrentSpellIndexForCastableItem(castableItem);
        }
    }

    public static void resetCurrentSpellIndexForCastableItem(ItemStack castableItem) {
        if (castableItem.getItem() instanceof AbstractCastable) {
            List<ResourceLocation> spellsBound = castableItem.get(PADataComponents.SPELLS_BOUND.get());
            int val = (spellsBound == null || spellsBound.isEmpty()) ? -1 : 0;
            castableItem.set(PADataComponents.CURRENT_SPELL_INDEX.get(), val);
        }
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

    public static Element getSpellElement(ResourceLocation spellID) {
        AbstractSpell spell = SpellRegistry.getSpell(spellID);
        if (spell == null) {
            return Element.None;
        }

        return spell.getElement();
    }
}
