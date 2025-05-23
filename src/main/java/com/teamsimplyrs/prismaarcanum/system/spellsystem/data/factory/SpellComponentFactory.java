package com.teamsimplyrs.prismaarcanum.system.spellsystem.data.factory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.components.*;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.interfaces.ISpellComponent;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.utils.SpellUtils;

import java.util.ArrayList;

public class SpellComponentFactory {
    public static ArrayList<ISpellComponent> parseComponents(JsonArray json) {
        ArrayList<ISpellComponent> components = new ArrayList<>();
        for (JsonElement component_element: json) {
            JsonObject component_object = component_element.getAsJsonObject();
            String type = component_object.get(SpellUtils.SpellComponentProperties.TYPE).getAsString();

            components.add(createSpellComponent(component_object, type));
        }
        return components;
    }

    private static ISpellComponent createSpellComponent(JsonObject json, String type) {
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
            case SpellUtils.SpellComponentNames.PARTICLE_EFFECT -> createParticlesComponent(json);
            default -> createEmptyComponent();
        };

    }

    ///<region>Individual Spell Component Creators</region>
    //region
    private static ISpellComponent createAOEComponent(JsonObject json) {
        float radius_x = json.get(SpellUtils.SpellComponentProperties.RADIUS_X).getAsFloat();
        float radius_y = json.get(SpellUtils.SpellComponentProperties.RADIUS_Y).getAsFloat();
        float radius_z = json.get(SpellUtils.SpellComponentProperties.RADIUS_Z).getAsFloat();
        ISpellComponent hit_component;

        if (json.has(SpellUtils.SpellComponentProperties.HIT_COMPONENT)) {
            JsonObject hit_component_object = json.get(SpellUtils.SpellComponentProperties.HIT_COMPONENT).getAsJsonObject();
            String type = hit_component_object.get(SpellUtils.SpellComponentProperties.TYPE).getAsString();
            hit_component = createSpellComponent(hit_component_object, type);
        } else {
            hit_component = createEmptyComponent();
        }

        return new AOEComponent(radius_x, radius_y, radius_z, hit_component);
    }

    private static ISpellComponent createBeamComponent(JsonObject json) {
        float width = json.get(SpellUtils.SpellComponentProperties.WIDTH).getAsFloat();
        float height = json.get(SpellUtils.SpellComponentProperties.HEIGHT).getAsFloat();
        float range = json.get(SpellUtils.SpellComponentProperties.RANGE).getAsFloat();

        JsonObject hit_component_object = json.get(SpellUtils.SpellComponentProperties.HIT_COMPONENT).getAsJsonObject();
        String type = hit_component_object.get(SpellUtils.SpellComponentProperties.TYPE).getAsString();
        ISpellComponent hit_component = createSpellComponent(hit_component_object, type);

        return new BeamComponent(width, height, range, hit_component);
    }

    private static ISpellComponent createChainComponent(JsonObject json) {
        int hops = json.get(SpellUtils.SpellComponentProperties.HOPS).getAsInt();
        float radius_x = json.get(SpellUtils.SpellComponentProperties.RADIUS_X).getAsFloat();
        float radius_y = json.get(SpellUtils.SpellComponentProperties.RADIUS_Y).getAsFloat();
        float radius_z = json.get(SpellUtils.SpellComponentProperties.RADIUS_Z).getAsFloat();
        float effect_dropoff = json.get(SpellUtils.SpellComponentProperties.EFFECT_DROPOFF).getAsFloat();

        return new ChainComponent(hops, radius_x, radius_y, radius_z, effect_dropoff);
    }

    private static ISpellComponent createDamageComponent(JsonObject json, boolean isOverTime) {
        float amount = json.get(SpellUtils.SpellComponentProperties.AMOUNT).getAsFloat();

        if (isOverTime) {
            float duration = json.get(SpellUtils.SpellComponentProperties.DURATION).getAsFloat();
            float interval = json.get(SpellUtils.SpellComponentProperties.INTERVAL).getAsFloat();

            return new DamageOverTimeComponent(amount, duration, interval);
        }

        return new DamageInstantComponent(amount);
    }

    private static ISpellComponent createDashComponent(JsonObject json) {
        float distance = json.get(SpellUtils.SpellComponentProperties.DISTANCE).getAsFloat();
        float speed = json.get(SpellUtils.SpellComponentProperties.SPEED).getAsFloat();

        return new DashComponent(distance, speed);
    }

    private static ISpellComponent createEmptyComponent() {
        return new EmptyComponent();
    }

    private static ISpellComponent createHealComponent(JsonObject json, boolean isOverTime) {
        float amount = json.get(SpellUtils.SpellComponentProperties.AMOUNT).getAsFloat();

        if (isOverTime) {
            float duration = json.get(SpellUtils.SpellComponentProperties.DURATION).getAsFloat();
            float interval = json.get(SpellUtils.SpellComponentProperties.INTERVAL).getAsFloat();

            return new HealOverTimeComponent(amount, duration, interval);
        }

        return new HealInstantComponent(amount);
    }

    private static ISpellComponent createInflictionComponent(JsonObject json) {
        float duration = json.get(SpellUtils.SpellComponentProperties.DURATION).getAsFloat();

        JsonObject hit_component_object = json.get(SpellUtils.SpellComponentProperties.HIT_COMPONENT).getAsJsonObject();
        String type = hit_component_object.get(SpellUtils.SpellComponentProperties.TYPE).getAsString();
        ISpellComponent hit_component = createSpellComponent(hit_component_object, type);

        return new InflictionComponent(duration, hit_component);
    }

    private static ISpellComponent createKnockbackComponent(JsonObject json) {
        float strength = json.get(SpellUtils.SpellComponentProperties.STRENGTH).getAsFloat();
        return new KnockbackComponent(strength);
    }

    private static ISpellComponent createParticlesComponent(JsonObject json) {
        String type = json.get(SpellUtils.SpellComponentProperties.TYPE).getAsString();
        int count = json.get(SpellUtils.SpellComponentProperties.COUNT).getAsInt();

        return new ParticlesComponent(type, count);
    }

    //endregion
}
