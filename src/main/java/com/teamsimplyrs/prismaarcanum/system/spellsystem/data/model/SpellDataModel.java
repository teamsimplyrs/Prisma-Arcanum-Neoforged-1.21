package com.teamsimplyrs.prismaarcanum.system.spellsystem.data.model;

import com.teamsimplyrs.prismaarcanum.system.spellsystem.interfaces.ISpellComponent;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.vfx.SpellParticles;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.vfx.SpellVFX;

import java.util.ArrayList;

public class SpellDataModel {
    public int id;
    public int tier;
    public int base_mana_cost;
    public float cooldown;

    public String spell_name;
    public String spell_display_name;

    public String element;
    public String school;

    public boolean has_evolution;

    public ArrayList<ISpellComponent> spell_components;

    public SpellVFX[] vfx;
    public SpellParticles[] particles;
}
