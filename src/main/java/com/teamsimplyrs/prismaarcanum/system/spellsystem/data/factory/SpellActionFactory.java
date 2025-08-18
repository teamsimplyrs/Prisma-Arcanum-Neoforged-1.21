package com.teamsimplyrs.prismaarcanum.system.spellsystem.data.factory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.actions.*;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.utils.SpellUtils;

import java.util.ArrayList;

public class SpellActionFactory {
    public static ArrayList<GenericAction> parseActions(JsonArray json) {
        ArrayList<GenericAction> actions = new ArrayList<>();
        for (JsonElement action_element: json) {
            JsonObject action_object = action_element.getAsJsonObject();
            String type = action_object.get(SpellUtils.SpellActionProperties.TYPE).getAsString();

            actions.add(createSpellAction(action_object, type));
        }
        return actions;
    }

    private static GenericAction createSpellAction(JsonObject json, String type) {
        return switch (type) {
            case SpellUtils.SpellActionNames.AREA_OF_EFFECT -> createAOEAction(json);
            case SpellUtils.SpellActionNames.BEAM -> createBeamAction(json);
            case SpellUtils.SpellActionNames.CHAIN_EFFECT -> createChainAction(json);
            case SpellUtils.SpellActionNames.DAMAGE_INSTANT -> createDamageAction(json, false);
            case SpellUtils.SpellActionNames.DAMAGE_OVER_TIME -> createDamageAction(json, true);
            case SpellUtils.SpellActionNames.DASH -> createDashAction(json);
            case SpellUtils.SpellActionNames.HEAL_INSTANT -> createHealAction(json, false);
            case SpellUtils.SpellActionNames.HEAL_OVER_TIME -> createHealAction(json, true);
            case SpellUtils.SpellActionNames.INFLICTION -> createInflictionAction(json);
            case SpellUtils.SpellActionNames.KNOCKBACK -> createKnockbackAction(json);
            default -> createEmptyAction();
        };

    }

    ///<region>Individual Spell Action Creators</region>
    //region
    private static GenericAction createAOEAction(JsonObject json) {
        float radius_x = json.get(SpellUtils.SpellActionProperties.RADIUS_X).getAsFloat();
        float radius_y = json.get(SpellUtils.SpellActionProperties.RADIUS_Y).getAsFloat();
        float radius_z = json.get(SpellUtils.SpellActionProperties.RADIUS_Z).getAsFloat();
        boolean isBlocking = json.get(SpellUtils.SpellActionProperties.IS_BLOCKING).getAsBoolean();
        GenericAction hit_action;

        if (json.has(SpellUtils.SpellActionProperties.HIT_ACTION)) {
            JsonObject hit_action_object = json.get(SpellUtils.SpellActionProperties.HIT_ACTION).getAsJsonObject();
            String type = hit_action_object.get(SpellUtils.SpellActionProperties.TYPE).getAsString();
            hit_action = createSpellAction(hit_action_object, type);
        } else {
            hit_action = createEmptyAction();
        }

        return new AOEAction(radius_x, radius_y, radius_z, hit_action, isBlocking);
    }

    private static GenericAction createBeamAction(JsonObject json) {
        float width = json.get(SpellUtils.SpellActionProperties.WIDTH).getAsFloat();
        float height = json.get(SpellUtils.SpellActionProperties.HEIGHT).getAsFloat();
        float range = json.get(SpellUtils.SpellActionProperties.RANGE).getAsFloat();
        boolean isBlocking = json.get(SpellUtils.SpellActionProperties.IS_BLOCKING).getAsBoolean();
        
        JsonObject hit_action_object = json.get(SpellUtils.SpellActionProperties.HIT_ACTION).getAsJsonObject();
        String type = hit_action_object.get(SpellUtils.SpellActionProperties.TYPE).getAsString();
        GenericAction hit_action = createSpellAction(hit_action_object, type);

        return new BeamAction(width, height, range, hit_action, isBlocking);
    }

    private static GenericAction createChainAction(JsonObject json) {
        int hops = json.get(SpellUtils.SpellActionProperties.HOPS).getAsInt();
        float radius_x = json.get(SpellUtils.SpellActionProperties.RADIUS_X).getAsFloat();
        float radius_y = json.get(SpellUtils.SpellActionProperties.RADIUS_Y).getAsFloat();
        float radius_z = json.get(SpellUtils.SpellActionProperties.RADIUS_Z).getAsFloat();
        float effect_dropoff = json.get(SpellUtils.SpellActionProperties.EFFECT_DROPOFF).getAsFloat();
        boolean isBlocking = json.get(SpellUtils.SpellActionProperties.IS_BLOCKING).getAsBoolean();

        return new ChainAction(hops, radius_x, radius_y, radius_z, effect_dropoff, isBlocking);
    }

    private static GenericAction createDamageAction(JsonObject json, boolean isOverTime) {
        float amount = json.get(SpellUtils.SpellActionProperties.AMOUNT).getAsFloat();
        boolean isBlocking = json.get(SpellUtils.SpellActionProperties.IS_BLOCKING).getAsBoolean();

        if (isOverTime) {
            float duration = json.get(SpellUtils.SpellActionProperties.DURATION).getAsFloat();
            float interval = json.get(SpellUtils.SpellActionProperties.INTERVAL).getAsFloat();

            return new DamageOverTimeAction(amount, duration, interval, isBlocking);
        }

        return new DamageInstantAction(amount, isBlocking);
    }

    private static GenericAction createDashAction(JsonObject json) {
        float distance = json.get(SpellUtils.SpellActionProperties.DISTANCE).getAsFloat();
        float duration = json.get(SpellUtils.SpellActionProperties.DURATION).getAsFloat();
        boolean isBlocking = json.get(SpellUtils.SpellActionProperties.IS_BLOCKING).getAsBoolean();
        
        return new DashAction(distance, duration, isBlocking);
    }

    private static GenericAction createEmptyAction() {
        return new EmptyAction();
    }

    private static GenericAction createHealAction(JsonObject json, boolean isOverTime) {
        float amount = json.get(SpellUtils.SpellActionProperties.AMOUNT).getAsFloat();
        boolean isBlocking = json.get(SpellUtils.SpellActionProperties.IS_BLOCKING).getAsBoolean();

        if (isOverTime) {
            float duration = json.get(SpellUtils.SpellActionProperties.DURATION).getAsFloat();
            float interval = json.get(SpellUtils.SpellActionProperties.INTERVAL).getAsFloat();

            return new HealOverTimeAction(amount, duration, interval, isBlocking);
        }

        return new HealInstantAction(amount, isBlocking);
    }

    private static GenericAction createInflictionAction(JsonObject json) {
        float duration = json.get(SpellUtils.SpellActionProperties.DURATION).getAsFloat();
        boolean isBlocking = json.get(SpellUtils.SpellActionProperties.IS_BLOCKING).getAsBoolean();
        
        JsonObject hit_action_object = json.get(SpellUtils.SpellActionProperties.HIT_ACTION).getAsJsonObject();
        String type = hit_action_object.get(SpellUtils.SpellActionProperties.TYPE).getAsString();
        GenericAction hit_action = createSpellAction(hit_action_object, type);

        return new InflictionAction(duration, hit_action, isBlocking);
    }

    private static GenericAction createKnockbackAction(JsonObject json) {
        float strength = json.get(SpellUtils.SpellActionProperties.STRENGTH).getAsFloat();
        boolean isBlocking = json.get(SpellUtils.SpellActionProperties.IS_BLOCKING).getAsBoolean();
        
        return new KnockbackAction(strength, isBlocking);
    }

    //endregion
}
