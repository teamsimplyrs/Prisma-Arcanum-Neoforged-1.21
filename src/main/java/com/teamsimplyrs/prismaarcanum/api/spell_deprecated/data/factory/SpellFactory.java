package com.teamsimplyrs.prismaarcanum.api.spell_deprecated.data.factory;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.teamsimplyrs.prismaarcanum.api.spell_deprecated.data.model.SpellDataModel;
import com.teamsimplyrs.prismaarcanum.api.utils.SpellUtilsDeprecated;

@Deprecated
public class SpellFactory {
    public static SpellDataModel parseSpell(JsonObject json) {
        SpellDataModel spell = new SpellDataModel();

        spell.spell_display_name = json.get(SpellUtilsDeprecated.SpellPropertyConstants.SPELL_DISPLAY_NAME).getAsString();
        spell.spell_description = json.get(SpellUtilsDeprecated.SpellPropertyConstants.SPELL_DESCRIPTION).getAsString();
        spell.element = json.get(SpellUtilsDeprecated.SpellPropertyConstants.ELEMENT).getAsString();
        spell.school = json.get(SpellUtilsDeprecated.SpellPropertyConstants.SCHOOL).getAsString();

        spell.tier = json.get(SpellUtilsDeprecated.SpellPropertyConstants.TIER).getAsInt();
        spell.base_mana_cost = json.get(SpellUtilsDeprecated.SpellPropertyConstants.BASE_MANA_COST).getAsInt();
        spell.cooldown = json.get(SpellUtilsDeprecated.SpellPropertyConstants.COOLDOWN).getAsInt();


        spell.has_evolution = json.get(SpellUtilsDeprecated.SpellPropertyConstants.HAS_EVOLUTION).getAsBoolean();

        JsonArray componentsJson = json.get(SpellUtilsDeprecated.SpellPropertyConstants.SPELL_ACTIONS).getAsJsonArray();
        spell.spell_actions = SpellActionFactory.parseActions(componentsJson);

        return spell;
    }
}
