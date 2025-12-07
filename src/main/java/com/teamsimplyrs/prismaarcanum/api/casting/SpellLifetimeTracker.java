package com.teamsimplyrs.prismaarcanum.api.casting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;

public class SpellLifetimeTracker {
    public Map<ResourceLocation, Integer> lifetimes = new HashMap<>();
    public static final Codec<Map<ResourceLocation, Integer>> SPELL_LIFETIME_MAP_CODEC =
            Codec.unboundedMap(ResourceLocation.CODEC, Codec.INT);

    public static final Codec<SpellLifetimeTracker> CODEC = RecordCodecBuilder.create(i -> i.group(
            SPELL_LIFETIME_MAP_CODEC.fieldOf("cooldowns").forGetter(SpellLifetimeTracker::getCooldownMap)
    ).apply(i, SpellLifetimeTracker::new));

    private boolean dirty = false;

    public SpellLifetimeTracker()  {

    }

    public SpellLifetimeTracker(Map<ResourceLocation, Integer> map) {
        if (map != null) this.lifetimes.putAll(map);
        ClientCooldownManager.get().setCooldowns(map);
    }

    public boolean tick(ServerPlayer player) {
        if (lifetimes.isEmpty()) {
            return false;
        }
        boolean changed = false;
        var it = lifetimes.entrySet().iterator();

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
        return lifetimes;
    }

    public int getCooldown(ResourceLocation spellID) {
        return lifetimes.getOrDefault(spellID, -1);
    }

    public float getCooldownSeconds(ResourceLocation spellID) {
        return getCooldown(spellID) / 20.0f;
    }

    public boolean isOnCooldown(ResourceLocation spellID) {
        return lifetimes.containsKey(spellID) && getCooldown(spellID) > 0;
    }

    public void clearCooldown(ResourceLocation spellID) {
        lifetimes.remove(spellID);
        dirty = true;
    }

    public void setCooldown(ResourceLocation spellID, int ticks) {
        lifetimes.put(spellID, ticks);
        ClientCooldownManager.get().setCooldown(spellID, ticks);
        dirty = true;
    }

    public void setCooldowns(Map<ResourceLocation, Integer> cooldowns) {
        this.lifetimes.putAll(cooldowns);
        dirty = true;
    }

    public boolean isMarkedDirty() {
        return dirty;
    }

    public void unmarkDirty() {
        dirty = false;
    }
}
