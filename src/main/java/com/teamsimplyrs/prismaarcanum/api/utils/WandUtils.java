package com.teamsimplyrs.prismaarcanum.api.utils;

import com.teamsimplyrs.prismaarcanum.api.casting.AbstractCastable;
import com.teamsimplyrs.prismaarcanum.component.PADataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class WandUtils {
    public static void cycleSpellsOnScroll(ItemStack castable, float scrollDX, float scrollDY) {
        if (!(castable.getItem() instanceof AbstractCastable)) {
            return;
        }

        var spells = castable.get(PADataComponents.SPELLS_BOUND.get());
        if (spells == null || spells.isEmpty()) {
            return;
        }

        Integer curIndex = castable.get(PADataComponents.CURRENT_SPELL_INDEX.get());
        if (curIndex != null && curIndex >= 0 && scrollDY != 0) {
            curIndex = (scrollDY > 0 ? curIndex + 1 : curIndex - 1) % spells.size();
            castable.set(PADataComponents.CURRENT_SPELL_INDEX, curIndex);
        }
    }


    public static void resetCurrentSpellIndex(ItemStack castable, boolean onlyIfComponentMissing) {
        if (castable == null || !(castable.getItem() instanceof AbstractCastable)) {
            return;
        }

        Integer curIdx = castable.get(PADataComponents.CURRENT_SPELL_INDEX.get());
        var spells = castable.get(PADataComponents.SPELLS_BOUND.get());

        if (onlyIfComponentMissing && curIdx != null) {
            return;
        }

        castable.set(PADataComponents.CURRENT_SPELL_INDEX, spells == null ? -1 : 0);
    }

    public static ResourceLocation getCurrentSpell(ItemStack stack) {
        if (stack == null || !(stack.getItem() instanceof AbstractCastable)) {
            return null;
        }

        var spells = stack.get(PADataComponents.SPELLS_BOUND.get());
        if (spells == null || spells.isEmpty()) {
            return null;
        }

        Integer curIdx = stack.get(PADataComponents.CURRENT_SPELL_INDEX.get());
        if (curIdx == null || curIdx > spells.size() || curIdx < 0) {
            return spells.getFirst();
        }

        return spells.get(curIdx);
    }
}
