package com.teamsimplyrs.prismaarcanum.event.common;

import com.mojang.logging.LogUtils;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.casting.AbstractCastable;
import com.teamsimplyrs.prismaarcanum.api.casting.PlayerSpellCooldowns;
import com.teamsimplyrs.prismaarcanum.api.combo.SpellComboChainData;
import com.teamsimplyrs.prismaarcanum.api.casting.SpellLifetimeTracker;
import com.teamsimplyrs.prismaarcanum.api.mana.PlayerChromana;
import com.teamsimplyrs.prismaarcanum.api.states.EntitySpellControlStateComponent;
import com.teamsimplyrs.prismaarcanum.network.payload.ManaSyncPayload;
import com.teamsimplyrs.prismaarcanum.network.payload.PlayerSpellCooldownsSyncPayload;
import com.teamsimplyrs.prismaarcanum.network.payload.SpellLifetimeSyncPayload;
import com.teamsimplyrs.prismaarcanum.registry.PADataAttachmentsRegistry;
import com.teamsimplyrs.prismaarcanum.registry.SpellRegistry;
import com.teamsimplyrs.prismaarcanum.spells.common.AbstractSpell;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.slf4j.Logger;

@EventBusSubscriber(modid = PrismaArcanum.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class CommonEvents {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {

    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            PlayerChromana manaData = player.getData(PADataAttachmentsRegistry.CHROMANA.get());
            PlayerSpellCooldowns spellCooldowns = player.getData(PADataAttachmentsRegistry.SPELL_COOLDOWNS.get());
            SpellLifetimeTracker lifetimeTracker = player.getData(PADataAttachmentsRegistry.SPELL_LIFETIMES.get());
            SpellComboChainData spellComboChain = player.getData(PADataAttachmentsRegistry.SPELL_COMBO_CHAIN_DATA.get());

            if (manaData.tick()) {
                if (player.tickCount % 5 == 0  || manaData.getCurrent() == manaData.getMax()) {
                    PacketDistributor.sendToPlayer(player, new ManaSyncPayload(player.getUUID(), manaData));
                }
            }

            if (spellCooldowns.tick(player)) {
                if (player.tickCount % 50 == 0 || spellCooldowns.isMarkedDirty()) {
                    spellCooldowns.unmarkDirty();
                    PacketDistributor.sendToPlayer(player, new PlayerSpellCooldownsSyncPayload(spellCooldowns.getCooldownMap()));
                }
            }

            if (lifetimeTracker.tick(player)) {
                if(lifetimeTracker.isMarkedDirty()){
                    lifetimeTracker.unmarkDirty();
                    PacketDistributor.sendToPlayer(player, new SpellLifetimeSyncPayload(lifetimeTracker.getLifetimesMap()));
                }
            }

            var comboingSpell = spellComboChain.getSpellID();
            if (comboingSpell != null && !comboingSpell.equals(SpellRegistry.getEmpty())) {
                spellComboChain.tick();
                if (spellComboChain.getRemainingComboWindowTicks() <= 0 && !spellCooldowns.isOnCooldown(comboingSpell)) {
                    AbstractSpell comboingSpellData = SpellRegistry.getSpell(comboingSpell);
                    if (comboingSpellData == null) {
                        LOGGER.error("[PrismaArcanum] PlayerTickEvent: Invalid/unregistered spell encountered while ticking combo chain data");
                    } else {
                        spellCooldowns.setCooldown(comboingSpell, comboingSpellData.getCooldownTicks());
                        PacketDistributor.sendToPlayer(player, new PlayerSpellCooldownsSyncPayload(spellCooldowns.getCooldownMap()));
                        spellComboChain.reset();
                    }
                }
            }
        }
        else if(event.getEntity() instanceof LocalPlayer player) {
            SpellLifetimeTracker lifetimeTracker = player.getData(PADataAttachmentsRegistry.SPELL_LIFETIMES.get());
            PlayerSpellCooldowns spellCooldowns = player.getData(PADataAttachmentsRegistry.SPELL_COOLDOWNS.get());
            if (lifetimeTracker.tick(player)) {
                if(lifetimeTracker.isMarkedDirty()){
                    lifetimeTracker.unmarkDirty();
                }
            }
            if (spellCooldowns.tick(player)) {
                if (player.tickCount % 100 == 0 || spellCooldowns.isMarkedDirty()) {
                    spellCooldowns.unmarkDirty();
                }
            }

        }
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        if (player.getMainHandItem().getItem() instanceof AbstractCastable item) {
            item.use(event.getLevel(), player, event.getHand());
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
        }
    }


    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        Entity entity = event.getEntity();
        if (!entity.level().isClientSide) {
            if (entity instanceof LivingEntity livingEntity) {
                EntitySpellControlStateComponent spellControlStateData = livingEntity.getData(PADataAttachmentsRegistry.SPELL_CONTROL_STATE.get());
                spellControlStateData.tick(entity);
            }
        }
    }
}
