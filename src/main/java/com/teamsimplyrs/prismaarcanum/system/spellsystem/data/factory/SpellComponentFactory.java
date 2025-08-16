package com.teamsimplyrs.prismaarcanum.system.spellsystem.data.factory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.components.*;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.interfaces.ISpellAction;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.utils.SpellUtils;

import java.util.ArrayList;

public class SpellComponentFactory {
    public static ArrayList<ISpellAction> parseComponents(JsonArray json) {
        ArrayList<ISpellAction> components = new ArrayList<>();
        for (JsonElement component_element: json) {
            JsonObject component_object = component_element.getAsJsonObject();
            String type = component_object.get(SpellUtils.SpellComponentProperties.TYPE).getAsString();

            components.add(createSpellComponent(component_object, type));
        }
        return components;
    }

    private static ISpellAction createSpellComponent(JsonObject json, String type) {
        return switch (type) {
            case SpellUtils.SpellComponentNames.AREA_OF_EFFECT -> createAOEComponent(json);
            case SpellUtils.SpellComponentNames.BEAM -> createBeamComponent(json);
            case SpellUtils.SpellComponentNames.CHAIN_EFFECT -> createChainComponent(json);
            case SpellUtils.SpellComponentNames.DAMAGE_INSTANT -> createDamageComponent(json, false);
            case SpellUtils.SpellComponentNames.DAMAGE_OVER_TIME -> createDamageComponent(json, true);
            case SpellUtils.SpellComponentNames.DASH -> createDashComponent(json);
            case SpellUtils.SpellComponentNames.HEAL_INSTANT -> createHealComponent(json, false);
            case SpellUtils.SpellComponentNames.HEAL_OVER_TIME -> createHealComponent(json, true);
            case SpellUtils.SpellComponentNames.INFLICTION -> createInflictionComponent(json);
            case SpellUtils.SpellComponentNames.KNOCKBACK -> createKnockbackComponent(json);
            default -> createEmptyComponent();
        };

    }

    ///<region>Individual Spell Component Creators</region>
    //region
    private static ISpellAction createAOEComponent(JsonObject json) {
        float radius_x = json.get(SpellUtils.SpellComponentProperties.RADIUS_X).getAsFloat();
        float radius_y = json.get(SpellUtils.SpellComponentProperties.RADIUS_Y).getAsFloat();
        float radius_z = json.get(SpellUtils.SpellComponentProperties.RADIUS_Z).getAsFloat();
        ISpellAction hit_component;

        if (json.has(SpellUtils.SpellComponentProperties.HIT_COMPONENT)) {
            JsonObject hit_component_object = json.get(SpellUtils.SpellComponentProperties.HIT_COMPONENT).getAsJsonObject();
            String type = hit_component_object.get(SpellUtils.SpellComponentProperties.TYPE).getAsString();
            hit_component = createSpellComponent(hit_component_object, type);
        } else {
            hit_component = createEmptyComponent();
        }

        return new AOEAction(radius_x, radius_y, radius_z, hit_component);
    }

    private static ISpellAction createBeamComponent(JsonObject json) {
        float width = json.get(SpellUtils.SpellComponentProperties.WIDTH).getAsFloat();
        float height = json.get(SpellUtils.SpellComponentProperties.HEIGHT).getAsFloat();
        float range = json.get(SpellUtils.SpellComponentProperties.RANGE).getAsFloat();

        JsonObject hit_component_object = json.get(SpellUtils.SpellComponentProperties.HIT_COMPONENT).getAsJsonObject();
        String type = hit_component_object.get(SpellUtils.SpellComponentProperties.TYPE).getAsString();
        ISpellAction hit_component = createSpellComponent(hit_component_object, type);

        return new BeamAction(width, height, range, hit_component);
    }

    private static ISpellAction createChainComponent(JsonObject json) {
        int hops = json.get(SpellUtils.SpellComponentProperties.HOPS).getAsInt();
        float radius_x = json.get(SpellUtils.SpellComponentProperties.RADIUS_X).getAsFloat();
        float radius_y = json.get(SpellUtils.SpellComponentProperties.RADIUS_Y).getAsFloat();
        float radius_z = json.get(SpellUtils.SpellComponentProperties.RADIUS_Z).getAsFloat();
        float effect_dropoff = json.get(SpellUtils.SpellComponentProperties.EFFECT_DROPOFF).getAsFloat();

        return new ChainAction(hops, radius_x, radius_y, radius_z, effect_dropoff);
    }

    private static ISpellAction createDamageComponent(JsonObject json, boolean isOverTime) {
        float amount = json.get(SpellUtils.SpellComponentProperties.AMOUNT).getAsFloat();

        if (isOverTime) {
            float duration = json.get(SpellUtils.SpellComponentProperties.DURATION).getAsFloat();
            float interval = json.get(SpellUtils.SpellComponentProperties.INTERVAL).getAsFloat();

            return new DamageOverTimeAction(amount, duration, interval);
        }

        return new DamageInstantAction(amount);
    }

    private static ISpellAction createDashComponent(JsonObject json) {
        float distance = json.get(SpellUtils.SpellComponentProperties.DISTANCE).getAsFloat();
        float speed = json.get(SpellUtils.SpellComponentProperties.SPEED).getAsFloat();

        return new DashAction(distance, speed);
    }

    private static ISpellAction createEmptyComponent() {
        return new EmptyAction();
    }

    private static ISpellAction createHealComponent(JsonObject json, boolean isOverTime) {
        float amount = json.get(SpellUtils.SpellComponentProperties.AMOUNT).getAsFloat();

        if (isOverTime) {
            float duration = json.get(SpellUtils.SpellComponentProperties.DURATION).getAsFloat();
            float interval = json.get(SpellUtils.SpellComponentProperties.INTERVAL).getAsFloat();

            return new HealOverTimeAction(amount, duration, interval);
        }

        return new HealInstantAction(amount);
    }

    private static ISpellAction createInflictionComponent(JsonObject json) {
        float duration = json.get(SpellUtils.SpellComponentProperties.DURATION).getAsFloat();

        JsonObject hit_component_object = json.get(SpellUtils.SpellComponentProperties.HIT_COMPONENT).getAsJsonObject();
        String type = hit_component_object.get(SpellUtils.SpellComponentProperties.TYPE).getAsString();
        ISpellAction hit_component = createSpellComponent(hit_component_object, type);

        return new InflictionAction(duration, hit_component);
    }

    private static ISpellAction createKnockbackComponent(JsonObject json) {
        float strength = json.get(SpellUtils.SpellComponentProperties.STRENGTH).getAsFloat();
        return new KnockbackAction(strength);
    }

    //endregion
}
