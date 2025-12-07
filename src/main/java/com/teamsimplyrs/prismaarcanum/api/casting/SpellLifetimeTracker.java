package com.teamsimplyrs.prismaarcanum.api.casting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamsimplyrs.prismaarcanum.api.spell.registry.SpellRegistry;
import com.teamsimplyrs.prismaarcanum.api.spell.spells.common.AbstractSpell;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

public class SpellLifetimeTracker {
    public Map<ResourceLocation, Integer> lifetimes = new HashMap<>();
    public static final Codec<Map<ResourceLocation, Integer>> SPELL_LIFETIME_MAP_CODEC =
            Codec.unboundedMap(ResourceLocation.CODEC, Codec.INT);

    public static final Codec<SpellLifetimeTracker> CODEC = RecordCodecBuilder.create(i -> i.group(
            SPELL_LIFETIME_MAP_CODEC.fieldOf("lifetimes").forGetter(SpellLifetimeTracker::getLifetimesMap)
    ).apply(i, SpellLifetimeTracker::new));

    private boolean dirty = false;

    public SpellLifetimeTracker()  {

    }

    public SpellLifetimeTracker(Map<ResourceLocation, Integer> map) {
        if (map != null) this.lifetimes.putAll(map);
    }

    public boolean tick(Player player) {
        if (lifetimes.isEmpty()) {
            return false;
        }
        boolean changed = false;
        var it = lifetimes.entrySet().iterator();

        while (it.hasNext()) {
            var entry = it.next();
            int currentLifetime = entry.getValue();
            int newLifetime = currentLifetime + 1;
            var spell = SpellRegistry.getSpell(entry.getKey());

            if(spell.checkTickEvent(currentLifetime)) {
                spell.runEventAtTick(currentLifetime,player);
            }

            if (currentLifetime == spell.getLifetime()) {
                it.remove();
                changed = true;
                dirty = true;
            } else {
                entry.setValue(newLifetime);

            }
        }

        return changed;
    }

    public Map<ResourceLocation, Integer> getLifetimesMap() {
        return lifetimes;
    }

    public int getLifetime(ResourceLocation spellID) {
        return lifetimes.getOrDefault(spellID, -1);
    }

    public float getLifetimeSeconds(ResourceLocation spellID) {
        return getLifetime(spellID) / 20.0f;
    }

    public boolean isTracked(ResourceLocation spellID) {
        return lifetimes.containsKey(spellID);
    }

    public void clearTracking(ResourceLocation spellID) {
        lifetimes.remove(spellID);
        dirty = true;
    }

    public void setLifetime(ResourceLocation spellID) {
        AbstractSpell spell = SpellRegistry.getSpell(spellID);
        //Doesn't track if getLifetime is not overridden.
        if(spell.getLifetime() == -1){
            return;
        }
        lifetimes.put(spellID,0);
        dirty = true;
    }

    public void setAllLifetimes(Map<ResourceLocation, Integer> lifetimes) {
        this.lifetimes.putAll(lifetimes);
        dirty = true;
    }

    public boolean isMarkedDirty() {
        return dirty;
    }

    public void unmarkDirty() {
        dirty = false;
    }
}
