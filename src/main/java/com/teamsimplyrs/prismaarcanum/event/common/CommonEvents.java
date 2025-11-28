package com.teamsimplyrs.prismaarcanum.event.common;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.casting.AbstractCastable;
import com.teamsimplyrs.prismaarcanum.api.casting.PlayerSpellCooldowns;
import com.teamsimplyrs.prismaarcanum.api.mana.PlayerChromana;
import com.teamsimplyrs.prismaarcanum.network.payload.ManaSyncPayload;
import com.teamsimplyrs.prismaarcanum.network.payload.PlayerSpellCooldownsSyncPayload;
import com.teamsimplyrs.prismaarcanum.registry.PADataAttachmentsRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = PrismaArcanum.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class CommonEvents {

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {

    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            PlayerChromana manaData = player.getData(PADataAttachmentsRegistry.CHROMANA.get());
            PlayerSpellCooldowns spellCooldowns = player.getData(PADataAttachmentsRegistry.SPELL_COOLDOWNS.get());

            if (manaData.tick()) {
                if (player.tickCount % 5 == 0  || manaData.getCurrent() == manaData.getMax()) {
                    PacketDistributor.sendToPlayer(player, new ManaSyncPayload(player.getUUID(), manaData));
                }
            }

            if (spellCooldowns.tick(player)) {
                if (player.tickCount % 100 == 0 || spellCooldowns.isMarkedDirty()) {
                    spellCooldowns.unmarkDirty();
                    PacketDistributor.sendToPlayer(player, new PlayerSpellCooldownsSyncPayload(spellCooldowns.getCooldownMap()));
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
}
