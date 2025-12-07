package com.teamsimplyrs.prismaarcanum.api.spell.spells.common;

import com.mojang.logging.LogUtils;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.casting.PlayerSpellCooldowns;
import com.teamsimplyrs.prismaarcanum.api.casting.SpellLifetimeTracker;
import com.teamsimplyrs.prismaarcanum.api.casting.spell_events.AbstractSpellTimedEvent;
import com.teamsimplyrs.prismaarcanum.api.casting.spell_events.PlaySoundSpellEvent;
import com.teamsimplyrs.prismaarcanum.api.mana.PlayerChromana;
import com.teamsimplyrs.prismaarcanum.api.utils.Element;
import com.teamsimplyrs.prismaarcanum.api.utils.School;
import com.teamsimplyrs.prismaarcanum.entity.custom.SpellEffectAreaEntity;
import com.teamsimplyrs.prismaarcanum.network.payload.CastPayload;
import com.teamsimplyrs.prismaarcanum.network.payload.ManaSyncPayload;
import com.teamsimplyrs.prismaarcanum.network.payload.OnCastingStartedPayload;
import com.teamsimplyrs.prismaarcanum.network.payload.SpellLifetimeSyncPayload;
import com.teamsimplyrs.prismaarcanum.registry.PADataAttachmentsRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractSpell implements ISpell {
    protected String spellID;
    protected Element element;
    protected School school;

    protected int tier;
    protected int basicManaCost;
    protected int basicCooldown;
    protected int startDelay;

    protected boolean hasEvolution;

    protected Map<Integer, AbstractSpellTimedEvent> events = new HashMap<>();
    protected final Logger LOGGER = LogUtils.getLogger();

    protected AbstractSpell() {

    }

    public AbstractSpell(String spellID) {
        this.spellID = spellID;
    }


    protected AbstractSpell(String spellID, Element element, School school, int tier, int basicManaCost, int basicCooldown, int startDelay, boolean hasEvolution) {
        this.spellID = spellID;
        this.element = element;
        this.school = school;
        this.tier = tier;
        this.basicManaCost = basicManaCost;
        this.basicCooldown = basicCooldown;
        this.startDelay = startDelay;
        this.hasEvolution = hasEvolution;

        this.addEvent(0, new PlaySoundSpellEvent(SoundEvents.EVOKER_CAST_SPELL, 1f, 1f));
    }

    public boolean tryCast(Level world, Player player) {
        if (world.isClientSide) {
            return false;
        }

        ServerPlayer serverPlayer = (ServerPlayer) player;
        if (serverPlayer == null) {
            return false;
        }

        PlayerChromana chromana = serverPlayer.getData(PADataAttachmentsRegistry.CHROMANA.get());

        if (chromana.getCurrent() < getManaCost()) {
            serverPlayer.sendSystemMessage(Component.literal("Insufficient Chromana"));
            return false;
        }

        PlayerSpellCooldowns cooldowns = serverPlayer.getData(PADataAttachmentsRegistry.SPELL_COOLDOWNS.get());
        ResourceLocation spellID = getResourceLocation();

        if (cooldowns.isOnCooldown(spellID)) {
            float cooldownSecs = cooldowns.getCooldownSeconds(spellID);
            player.sendSystemMessage(Component.literal(String.format("%s is on cooldown for %.2f seconds", getDisplayName(), cooldownSecs)));
            return false;
        }

        chromana.useMana(getManaCost(), true);
        cooldowns.setCooldown(getResourceLocation(), getCooldownTicks());

        PacketDistributor.sendToPlayer(serverPlayer, new ManaSyncPayload(serverPlayer.getUUID(), chromana));

        SpellLifetimeTracker lifetimes = serverPlayer.getData(PADataAttachmentsRegistry.SPELL_LIFETIMES.get());
        lifetimes.setLifetime(spellID);
        PacketDistributor.sendToPlayer(serverPlayer, new SpellLifetimeSyncPayload(lifetimes.getLifetimesMap()));

        PacketDistributor.sendToServer(new CastPayload(serverPlayer.getUUID(), getResourceLocation()));
        return true;
    }

    public void cast(ServerPlayer player, Level world) {
        ResourceLocation resLoc = getResourceLocation();
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, new OnCastingStartedPayload(player.getUUID(), resLoc));
    }

    public void onCastingStarted(Player player, Level world) {
//        player.sendSystemMessage(Component.literal(String.format("%s: OnCastingStarted called", getDisplayName())));
    }

    public void onCastingFinished(Player player, Level world) {
//        player.sendSystemMessage(Component.literal(String.format("%s: OnCastingFinished called", getDisplayName())));
    }

    public int getManaCost() {
        return basicManaCost;
    }

    public int getCooldownTicks() {
        return basicCooldown;
    }

    public MutableComponent getDisplayNameComponent() {
        return Component.translatable(getTranslatableComponent());
    }

    public String getDisplayName() {
        return getDisplayNameComponent().getString();
    }

    public Element getElement() {
        return element;
    }

    public String getElementAsString() {
        return element.toString().toLowerCase();
    }

    public MutableComponent getDescription() {
        String descriptionTranslatable = String.format("%s.%s", getTranslatableComponent(), "description");
        return Component.translatable(descriptionTranslatable);
    }

    protected String getTranslatableComponent() {
        return String.format("%s.%s.%s.%s", "spells", getResourceLocation().getNamespace(), getElementAsString(), getResourceLocation().getPath());
    }

    /** Override and specify the animation location using ResourceLocation */
    public ResourceLocation getAnimationLocation() {
        return null;
    }

    public void hitboxTick(SpellEffectAreaEntity hitbox){};

    public ResourceLocation getResourceLocation() {
        return ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, spellID);
    }

    public int getStartDelay() {
        return startDelay;
    }

    ///Override to set spell lifetime for tracking
    public int getLifetime() {
        return 20;
    }

    public void addEvent(Integer eventTick, AbstractSpellTimedEvent event) {
        events.put(eventTick, event);
    }

    public boolean checkTickEvent(Integer eventTick) {
        return events.containsKey(eventTick);
    }

    public void runEventAtTick(Integer eventTick, Player player){
        events.get(eventTick).run(player);
    }
}

