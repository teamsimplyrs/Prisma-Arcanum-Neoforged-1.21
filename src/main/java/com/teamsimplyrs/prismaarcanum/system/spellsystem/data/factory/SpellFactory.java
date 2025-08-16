package com.teamsimplyrs.prismaarcanum.system.spellsystem.data.factory;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.data.model.SpellDataModel;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.utils.SpellUtils;

public class SpellFactory {
    public static SpellDataModel parseSpell(JsonObject json) {
        SpellDataModel spell = new SpellDataModel();

        spell.spell_display_name = json.get(SpellUtils.SpellPropertyConstants.SPELL_DISPLAY_NAME).getAsString();
        spell.spell_description = json.get(SpellUtils.SpellPropertyConstants.SPELL_DESCRIPTION).getAsString();
        spell.element = json.get(SpellUtils.SpellPropertyConstants.ELEMENT).getAsString();
        spell.school = json.get(SpellUtils.SpellPropertyConstants.SCHOOL).getAsString();

        spell.tier = json.get(SpellUtils.SpellPropertyConstants.TIER).getAsInt();
        spell.base_mana_cost = json.get(SpellUtils.SpellPropertyConstants.BASE_MANA_COST).getAsInt();
        spell.cooldown = json.get(SpellUtils.SpellPropertyConstants.COOLDOWN).getAsInt();


        spell.has_evolution = json.get(SpellUtils.SpellPropertyConstants.HAS_EVOLUTION).getAsBoolean();

        JsonArray componentsJson = json.get(SpellUtils.SpellPropertyConstants.SPELL_COMPONENTS).getAsJsonArray();
        spell.spell_components = SpellComponentFactory.parseComponents(componentsJson);

        return spell;
    }
}
