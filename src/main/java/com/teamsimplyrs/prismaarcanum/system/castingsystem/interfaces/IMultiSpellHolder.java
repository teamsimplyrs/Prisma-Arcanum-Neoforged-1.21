package com.teamsimplyrs.prismaarcanum.system.castingsystem.interfaces;

import com.teamsimplyrs.prismaarcanum.system.spellsystem.spells.common.AbstractSpell;
import com.teamsimplyrs.prismaarcanum.system.spellsystem_deprecated.data.model.SpellDataModel;

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
