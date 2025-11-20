package com.teamsimplyrs.prismaarcanum.api.utils;

import com.teamsimplyrs.prismaarcanum.api.casting.AbstractCastable;
import com.teamsimplyrs.prismaarcanum.component.PADataComponents;
import com.teamsimplyrs.prismaarcanum.network.payload.OnSelectedSpellChangedPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

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
        int newIndex = 0;

        if (curIndex != null && curIndex >= 0 && scrollDY != 0) {
            // for whatever reason, Java returns negative values for ~negative % positive~. clamp to 0 before applying the index
            int base = curIndex + (int)Math.signum(scrollDY);

            newIndex = Math.floorMod(base, spells.size());
            castable.set(PADataComponents.CURRENT_SPELL_INDEX, newIndex);

        }

        PacketDistributor.sendToServer(new OnSelectedSpellChangedPayload(newIndex));
    }


    public static void resetCurrentSpellIndex(ItemStack castable, boolean onlyIfComponentMissing) {
        if (castable == null || !(castable.getItem() instanceof AbstractCastable)) {
            return;
        }

        Integer curIdx = castable.get(PADataComponents.CURRENT_SPELL_INDEX.get());
        var spells = castable.get(PADataComponents.SPELLS_BOUND.get());

        if (onlyIfComponentMissing && curIdx != null && curIdx < spells.size()) {
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
