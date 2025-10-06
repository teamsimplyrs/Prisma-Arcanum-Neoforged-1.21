package com.teamsimplyrs.prismaarcanum.api.spell_deprecated.data.model;

import com.teamsimplyrs.prismaarcanum.api.spell_deprecated.actions.GenericAction;
import com.teamsimplyrs.prismaarcanum.api.spell_deprecated.vfx.SpellParticles;
import com.teamsimplyrs.prismaarcanum.api.spell_deprecated.vfx.SpellVFX;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;

@Deprecated
public class SpellDataModel {
    // There's no need to add an "id" field in spell jsons. This field's value is automatically assigned by the spell data loader during runtime.
    public transient ResourceLocation id;

    public String spell_display_name;
    public String spell_description;
    public String element;
    public String school;

    public int tier;
    public int base_mana_cost;
    public float cooldown;

    public boolean has_evolution;

    public ArrayList<GenericAction> spell_actions;

    public SpellVFX[] vfx;
    public SpellParticles[] particles;
}
