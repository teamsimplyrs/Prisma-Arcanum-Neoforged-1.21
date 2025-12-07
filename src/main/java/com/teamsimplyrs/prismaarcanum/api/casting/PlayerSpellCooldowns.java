package com.teamsimplyrs.prismaarcanum.api.casting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;

public class PlayerSpellCooldowns {

    public Map<ResourceLocation, Integer> cooldowns = new HashMap<>();
    public static final Codec<Map<ResourceLocation, Integer>> COOLDOWN_MAP_CODEC =
            Codec.unboundedMap(ResourceLocation.CODEC, Codec.INT);

    public static final Codec<PlayerSpellCooldowns> CODEC = RecordCodecBuilder.create(i -> i.group(
            COOLDOWN_MAP_CODEC.fieldOf("cooldowns").forGetter(PlayerSpellCooldowns::getCooldownMap)
    ).apply(i, PlayerSpellCooldowns::new));

    private boolean dirty = false;

    public PlayerSpellCooldowns()  {

    }

    public PlayerSpellCooldowns(Map<ResourceLocation, Integer> map) {
        if (map != null) this.cooldowns.putAll(map);
        ClientCooldownManager.get().setCooldowns(map);
    }

    public boolean tick(ServerPlayer player) {
        if (cooldowns.isEmpty()) {
            return false;
        }
        boolean changed = false;
        var it = cooldowns.entrySet().iterator();

        while (it.hasNext()) {
            var entry = it.next();
            int newCooldown = entry.getValue() - 1;
            if (newCooldown <= 0) {
                it.remove();
                changed = true;
                dirty = true;
            } else {
                entry.setValue(newCooldown);

            }
        }

        return changed;
    }

    public Map<ResourceLocation, Integer> getCooldownMap() {
        return cooldowns;
    }

    public int getCooldown(ResourceLocation spellID) {
        return cooldowns.getOrDefault(spellID, -1);
    }

    public float getCooldownSeconds(ResourceLocation spellID) {
        return getCooldown(spellID) / 20.0f;
    }

    public boolean isOnCooldown(ResourceLocation spellID) {
        return cooldowns.containsKey(spellID) && getCooldown(spellID) > 0;
    }

    public void clearCooldown(ResourceLocation spellID) {
        cooldowns.remove(spellID);
        dirty = true;
    }

    public void setCooldown(ResourceLocation spellID, int ticks) {
        cooldowns.put(spellID, ticks);
        ClientCooldownManager.get().setCooldown(spellID, ticks);
        dirty = true;
    }

    public void setCooldowns(Map<ResourceLocation, Integer> cooldowns) {
        this.cooldowns.putAll(cooldowns);
        dirty = true;
    }

    public boolean isMarkedDirty() {
        return dirty;
    }

    public void unmarkDirty() {
        dirty = false;
    }
}
