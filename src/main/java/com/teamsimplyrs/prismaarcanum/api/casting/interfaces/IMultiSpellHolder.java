package com.teamsimplyrs.prismaarcanum.api.casting.interfaces;

import com.teamsimplyrs.prismaarcanum.api.spell.spells.common.AbstractSpell;

import java.util.List;

public interface IMultiSpellHolder {
    public void cycleSpells(int skip);
    public void nextSpell();
    public void previousSpell();
    public void handleScrollCycling(float scrollDeltaX, float scrollDeltaY);

    public AbstractSpell getCurrentSpell();
    public void setSpell(AbstractSpell spell, int index);
    public void setSpells(List<AbstractSpell> spellsList);
}
