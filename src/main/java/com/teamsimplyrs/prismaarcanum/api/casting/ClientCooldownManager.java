package com.teamsimplyrs.prismaarcanum.api.casting;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@EventBusSubscriber(modid = PrismaArcanum.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public class ClientCooldownManager {
    private final Map<ResourceLocation, Integer> cooldowns = new HashMap<>();
    private static ClientCooldownManager INSTANCE;

    private ClientCooldownManager() {}

    public static ClientCooldownManager get() {
        if (INSTANCE == null) INSTANCE = new ClientCooldownManager();
        return INSTANCE;
    }

    public Map<ResourceLocation, Integer> getCooldowns() {
        return cooldowns;
    }

    public boolean isOnCooldown(ResourceLocation spellId) {
        return cooldowns.containsKey(spellId);
    }

    public int getCooldown(ResourceLocation spellId) {
        return cooldowns.getOrDefault(spellId, -1);
    }

    public float getCooldownSeconds(ResourceLocation spellId) {
        return cooldowns.getOrDefault(spellId, 0) / 20.0f;
    }

    public void setCooldowns(Map<ResourceLocation, Integer> cooldowns) {
        this.cooldowns.clear();
        this.cooldowns.putAll(cooldowns);
    }

    public void setCooldown(ResourceLocation spellID, int ticks) {
        this.cooldowns.put(spellID, ticks);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        ClientCooldownManager mgr = get();
        if (mgr.cooldowns.isEmpty()) {
            return;
        }

        Iterator<Map.Entry<ResourceLocation, Integer>> it = mgr.cooldowns.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<ResourceLocation, Integer> e = it.next();
            int r = e.getValue() - 1;
            if (r <= 0) {
                it.remove();
            }
            else {
                e.setValue(r);
            }
        }
    }
}
