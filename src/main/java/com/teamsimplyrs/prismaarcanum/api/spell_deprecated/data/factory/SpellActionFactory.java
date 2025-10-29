package com.teamsimplyrs.prismaarcanum.api.spell_deprecated.data.factory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.teamsimplyrs.prismaarcanum.api.spell_deprecated.actions.*;
import com.teamsimplyrs.prismaarcanum.api.spell_deprecated.SpellUtilsDeprecated;

import java.util.ArrayList;

@Deprecated
public class SpellActionFactory {
    public static ArrayList<GenericAction> parseActions(JsonArray json) {
        ArrayList<GenericAction> actions = new ArrayList<>();
        for (JsonElement action_element: json) {
            JsonObject action_object = action_element.getAsJsonObject();
            String type = action_object.get(SpellUtilsDeprecated.SpellActionProperties.TYPE).getAsString();

            actions.add(createSpellAction(action_object, type));
        }
        return actions;
    }

    private static GenericAction createSpellAction(JsonObject json, String type) {
        return switch (type) {
            case SpellUtilsDeprecated.SpellActionNames.AREA_OF_EFFECT -> createAOEAction(json);
            case SpellUtilsDeprecated.SpellActionNames.BEAM -> createBeamAction(json);
            case SpellUtilsDeprecated.SpellActionNames.CHAIN_EFFECT -> createChainAction(json);
            case SpellUtilsDeprecated.SpellActionNames.DAMAGE_INSTANT -> createDamageAction(json, false);
            case SpellUtilsDeprecated.SpellActionNames.DAMAGE_OVER_TIME -> createDamageAction(json, true);
            case SpellUtilsDeprecated.SpellActionNames.DASH -> createDashAction(json);
            case SpellUtilsDeprecated.SpellActionNames.HEAL_INSTANT -> createHealAction(json, false);
            case SpellUtilsDeprecated.SpellActionNames.HEAL_OVER_TIME -> createHealAction(json, true);
            case SpellUtilsDeprecated.SpellActionNames.INFLICTION -> createInflictionAction(json);
            case SpellUtilsDeprecated.SpellActionNames.KNOCKBACK -> createKnockbackAction(json);
            case SpellUtilsDeprecated.SpellActionNames.PROJECTILE -> createProjectileAction(json);
            default -> createEmptyAction();
        };

    }

    ///<region>Individual Spell Action Creators</region>
    //region
    private static GenericAction createAOEAction(JsonObject json) {
        float radius_x = json.get(SpellUtilsDeprecated.SpellActionProperties.RADIUS_X).getAsFloat();
        float radius_y = json.get(SpellUtilsDeprecated.SpellActionProperties.RADIUS_Y).getAsFloat();
        float radius_z = json.get(SpellUtilsDeprecated.SpellActionProperties.RADIUS_Z).getAsFloat();
        boolean isBlocking = json.get(SpellUtilsDeprecated.SpellActionProperties.IS_BLOCKING).getAsBoolean();
        GenericAction hitAction;

        if (json.has(SpellUtilsDeprecated.SpellActionProperties.HIT_ACTION)) {
            JsonObject hitActionObject = json.get(SpellUtilsDeprecated.SpellActionProperties.HIT_ACTION).getAsJsonObject();
            String type = hitActionObject.get(SpellUtilsDeprecated.SpellActionProperties.TYPE).getAsString();
            hitAction = createSpellAction(hitActionObject, type);
        } else {
            hitAction = createEmptyAction();
        }

        return new AOEAction(radius_x, radius_y, radius_z, hitAction, isBlocking);
    }

    private static GenericAction createBeamAction(JsonObject json) {
        float width = json.get(SpellUtilsDeprecated.SpellActionProperties.WIDTH).getAsFloat();
        float height = json.get(SpellUtilsDeprecated.SpellActionProperties.HEIGHT).getAsFloat();
        float range = json.get(SpellUtilsDeprecated.SpellActionProperties.RANGE).getAsFloat();
        boolean isBlocking = json.get(SpellUtilsDeprecated.SpellActionProperties.IS_BLOCKING).getAsBoolean();
        
        JsonObject hitActionObject = json.get(SpellUtilsDeprecated.SpellActionProperties.HIT_ACTION).getAsJsonObject();
        String type = hitActionObject.get(SpellUtilsDeprecated.SpellActionProperties.TYPE).getAsString();
        GenericAction hitAction = createSpellAction(hitActionObject, type);

        return new BeamAction(width, height, range, hitAction, isBlocking);
    }

    private static GenericAction createChainAction(JsonObject json) {
        int hops = json.get(SpellUtilsDeprecated.SpellActionProperties.HOPS).getAsInt();
        float radius_x = json.get(SpellUtilsDeprecated.SpellActionProperties.RADIUS_X).getAsFloat();
        float radius_y = json.get(SpellUtilsDeprecated.SpellActionProperties.RADIUS_Y).getAsFloat();
        float radius_z = json.get(SpellUtilsDeprecated.SpellActionProperties.RADIUS_Z).getAsFloat();
        float effect_dropoff = json.get(SpellUtilsDeprecated.SpellActionProperties.EFFECT_DROPOFF).getAsFloat();
        boolean isBlocking = json.get(SpellUtilsDeprecated.SpellActionProperties.IS_BLOCKING).getAsBoolean();

        return new ChainAction(hops, radius_x, radius_y, radius_z, effect_dropoff, isBlocking);
    }

    private static GenericAction createDamageAction(JsonObject json, boolean isOverTime) {
        float amount = json.get(SpellUtilsDeprecated.SpellActionProperties.AMOUNT).getAsFloat();
        boolean isBlocking = json.get(SpellUtilsDeprecated.SpellActionProperties.IS_BLOCKING).getAsBoolean();

        if (isOverTime) {
            float duration = json.get(SpellUtilsDeprecated.SpellActionProperties.DURATION).getAsFloat();
            float interval = json.get(SpellUtilsDeprecated.SpellActionProperties.INTERVAL).getAsFloat();

            return new DamageOverTimeAction(amount, duration, interval, isBlocking);
        }

        return new DamageInstantAction(amount, isBlocking);
    }

    private static GenericAction createDashAction(JsonObject json) {
        float distance = json.get(SpellUtilsDeprecated.SpellActionProperties.DISTANCE).getAsFloat();
        float duration = json.get(SpellUtilsDeprecated.SpellActionProperties.DURATION).getAsFloat();
        boolean isBlocking = json.get(SpellUtilsDeprecated.SpellActionProperties.IS_BLOCKING).getAsBoolean();
        
        return new DashAction(distance, duration, isBlocking);
    }

    private static GenericAction createEmptyAction() {
        return new EmptyAction();
    }

    private static GenericAction createHealAction(JsonObject json, boolean isOverTime) {
        float amount = json.get(SpellUtilsDeprecated.SpellActionProperties.AMOUNT).getAsFloat();
        boolean isBlocking = json.get(SpellUtilsDeprecated.SpellActionProperties.IS_BLOCKING).getAsBoolean();

        if (isOverTime) {
            float duration = json.get(SpellUtilsDeprecated.SpellActionProperties.DURATION).getAsFloat();
            float interval = json.get(SpellUtilsDeprecated.SpellActionProperties.INTERVAL).getAsFloat();

            return new HealOverTimeAction(amount, duration, interval, isBlocking);
        }

        return new HealInstantAction(amount, isBlocking);
    }

    private static GenericAction createInflictionAction(JsonObject json) {
        float duration = json.get(SpellUtilsDeprecated.SpellActionProperties.DURATION).getAsFloat();
        boolean isBlocking = json.get(SpellUtilsDeprecated.SpellActionProperties.IS_BLOCKING).getAsBoolean();
        
        JsonObject hitActionObject = json.get(SpellUtilsDeprecated.SpellActionProperties.HIT_ACTION).getAsJsonObject();
        String type = hitActionObject.get(SpellUtilsDeprecated.SpellActionProperties.TYPE).getAsString();
        GenericAction hitAction = createSpellAction(hitActionObject, type);

        return new InflictionAction(duration, hitAction, isBlocking);
    }

    private static GenericAction createKnockbackAction(JsonObject json) {
        float strength = json.get(SpellUtilsDeprecated.SpellActionProperties.STRENGTH).getAsFloat();
        boolean isBlocking = json.get(SpellUtilsDeprecated.SpellActionProperties.IS_BLOCKING).getAsBoolean();
        
        return new KnockbackAction(strength, isBlocking);
    }

    private static GenericAction createProjectileAction(JsonObject json) {
        float speed = json.get(SpellUtilsDeprecated.SpellActionProperties.SPEED).getAsFloat();
        float lifetime = json.get(SpellUtilsDeprecated.SpellActionProperties.LIFETIME).getAsFloat();
        float width = json.get(SpellUtilsDeprecated.SpellActionProperties.WIDTH).getAsFloat();
        float height = json.get(SpellUtilsDeprecated.SpellActionProperties.HEIGHT).getAsFloat();
        float effectDropoff = json.get(SpellUtilsDeprecated.SpellActionProperties.EFFECT_DROPOFF).getAsFloat();
        boolean hasGravity = json.get(SpellUtilsDeprecated.SpellActionProperties.HAS_GRAVITY).getAsBoolean();
        boolean isBlocking = json.get(SpellUtilsDeprecated.SpellActionProperties.IS_BLOCKING).getAsBoolean();

        JsonObject hitActionObject = json.get(SpellUtilsDeprecated.SpellActionProperties.HIT_ACTION).getAsJsonObject();
        String type = hitActionObject.get(SpellUtilsDeprecated.SpellActionProperties.TYPE).getAsString();
        GenericAction hitAction = createSpellAction(hitActionObject, type);

        return new ProjectileAction(speed, lifetime, width, height, effectDropoff, hasGravity, hitAction, isBlocking);
    }

    //endregion
}
