package com.teamsimplyrs.prismaarcanum.api.casting.interfaces;

import com.teamsimplyrs.prismaarcanum.api.spell.spells.common.AbstractSpell;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IMultiSpellHolder {
    public void cycleSpells(int skip);
    public void nextSpell();
    public void previousSpell();
    public void handleScrollCycling(float scrollDeltaX, float scrollDeltaY);

    public ResourceLocation getCurrentSpell();
    public void setSpell(ResourceLocation spell, int index);
}
