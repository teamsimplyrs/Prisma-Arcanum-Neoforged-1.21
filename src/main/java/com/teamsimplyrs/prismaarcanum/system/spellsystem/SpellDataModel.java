package com.teamsimplyrs.prismaarcanum.system.spellsystem;

import com.teamsimplyrs.prismaarcanum.system.spellsystem.interfaces.ISpellComponent;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.utils.ElementNames;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.utils.SchoolNames;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.utils.SpellTypes;
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

    public ElementNames element;
    public SchoolNames school;
    public SpellTypes type;

    public boolean has_evolution;

    public ArrayList<ISpellComponent> spell_components;

    public SpellVFX vfx;
    public SpellParticles particles;

    public SpellDataModel spell_prevolution_data;
    public SpellDataModel spell_evolution_data;
}
