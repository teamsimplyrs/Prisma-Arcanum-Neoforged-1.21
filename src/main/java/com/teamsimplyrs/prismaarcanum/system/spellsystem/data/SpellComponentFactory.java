package com.teamsimplyrs.prismaarcanum.system.spellsystem.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.components.AOEComponent;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.components.DashComponent;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.interfaces.ISpellComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpellComponentFactory {
    public static ISpellComponent[] parse(JsonObject json) {
        String type = json.get("type").getAsString();
        return createSpellComponents(json, type);
    }

    private static ISpellComponent[] createSpellComponents(JsonObject json, String type) {
        List<ISpellComponent> components = new ArrayList<>();

        switch (type) {
            case "dash":
                components.add(createDashComponent(json));
                break;
            case "aoe":
                components.add((createAOEComponent(json)));
        }

        return components.toArray(ISpellComponent[]::new);
    }

    private static ISpellComponent createDashComponent(JsonObject json) {
        float distance = json.has("distance") ? json.get("distance").getAsFloat() : 0f;
        float speed = json.has("speed") ? json.get("speed").getAsFloat() : 0f;

        return new DashComponent(distance, speed);
    }

    private static ISpellComponent createAOEComponent(JsonObject json) {
        float radiusX = json.has("radius_x") ? json.get("radius_x").getAsFloat() : 2f;
        float radiusY = json.has("radius_y") ? json.get("radius_y").getAsFloat() : 2f;
        float radiusZ = json.has("radius_z") ? json.get("radius_z").getAsFloat() : 2f;

        List<ISpellComponent> hitComponents = new ArrayList<>();
        JsonArray hitArray = json.getAsJsonArray("hit_components");

        for (JsonElement element : hitArray) {
            JsonObject compJson = element.getAsJsonObject();
            ISpellComponent[] parsed = SpellComponentFactory.parse(compJson);

            // If a parser supports nested compound components
            Collections.addAll(hitComponents, parsed);
        }

        return new AOEComponent(radiusX, radiusY, radiusZ, hitComponents.toArray(ISpellComponent[]::new));
    }
}
