package com.teamsimplyrs.prismaarcanum.system.castingsystem.interfaces;

import com.teamsimplyrs.prismaarcanum.system.spellsystem.data.model.SpellDataModel;

import java.util.List;

public interface IMultiSpellHolder {
    public void cycleSpells(int skip);
    public void nextSpell();
    public void previousSpell();
    public void handleScrollCycling(float scrollDeltaX, float scrollDeltaY);

    public SpellDataModel getCurrentSpell();
    public void setSpell(SpellDataModel spell, int index);
    public void setSpells(List<SpellDataModel> spellsList);
}
