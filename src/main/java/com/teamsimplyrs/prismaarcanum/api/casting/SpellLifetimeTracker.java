package com.teamsimplyrs.prismaarcanum.api.casting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamsimplyrs.prismaarcanum.api.casting.spell_events.SpellInstance;
import com.teamsimplyrs.prismaarcanum.registry.SpellRegistry;
import com.teamsimplyrs.prismaarcanum.spells.common.AbstractSpell;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.*;

public class SpellLifetimeTracker {
    public Map<ResourceLocation, List<SpellInstance>> lifetimes = new HashMap<>();

    public static final Codec<SpellInstance> SPELL_INSTANCE_CODEC =
            RecordCodecBuilder.create(i -> i.group(
                    Codec.INT.fieldOf("lifetime").forGetter(s -> s.lifetime),
                    UUIDUtil.CODEC.fieldOf("id").forGetter(s -> s.id)
            ).apply(i, SpellInstance::new));

    public static final Codec<Map<ResourceLocation, List<SpellInstance>>> SPELL_LIFETIME_MAP_CODEC =
            Codec.unboundedMap(ResourceLocation.CODEC, SPELL_INSTANCE_CODEC.listOf());

    public static final Codec<SpellLifetimeTracker> CODEC = RecordCodecBuilder.create(i -> i.group(
            SPELL_LIFETIME_MAP_CODEC.fieldOf("lifetimes").forGetter(SpellLifetimeTracker::getLifetimesMap)
    ).apply(i, SpellLifetimeTracker::new));

    private boolean dirty = false;

    public SpellLifetimeTracker()  {

    }

    public SpellLifetimeTracker(Map<ResourceLocation, List<SpellInstance>> map) {
        if (map != null) this.lifetimes.putAll(map);
    }

    public boolean tick(Player player) {
        if (lifetimes.isEmpty()) return false;

        boolean changed = false;

        for (var entry : lifetimes.entrySet()) {
            ResourceLocation spellID = entry.getKey();
            List<SpellInstance> list = entry.getValue();
            AbstractSpell spell = SpellRegistry.getSpell(spellID);

            Iterator<SpellInstance> it = list.iterator();

            while (it.hasNext()) {
                SpellInstance inst = it.next();
                int time = inst.lifetime++;

                if (spell.checkTickEvent(time)) {
                    spell.runEventAtTick(time, player);
                }

                if (time >= spell.getLifetime()) {
                    it.remove();
                    changed = true;
                    dirty = true;
                }
            }

            // cleanup empty spell groups
            if (list.isEmpty()) {
                lifetimes.remove(spellID);
            }
        }

        return changed;
    }

    public Map<ResourceLocation, List<SpellInstance>> getLifetimesMap() {
        return lifetimes;
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
        if (spell.getLifetime() == -1) return;

        lifetimes
                .computeIfAbsent(spellID, k -> new ArrayList<>())
                .add(new SpellInstance());

        dirty = true;
    }

    public void setAllLifetimes(Map<ResourceLocation, List<SpellInstance>> lifetimes) {
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
