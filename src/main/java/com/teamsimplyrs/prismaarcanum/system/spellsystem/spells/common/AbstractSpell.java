package com.teamsimplyrs.prismaarcanum.system.spellsystem.spells.common;

import com.mojang.logging.LogUtils;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.network.payload.CastPayload;
import com.teamsimplyrs.prismaarcanum.network.payload.OnCastingStartedPayload;
import com.teamsimplyrs.prismaarcanum.system.utils.Element;
import com.teamsimplyrs.prismaarcanum.system.utils.School;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.slf4j.Logger;

public abstract class AbstractSpell implements ISpell {
    protected static String spellID;
    protected static Element element;
    protected static School school;

    protected static int tier;
    protected static float basicManaCost;
    protected static float basicCooldown;

    protected static boolean hasEvolution;
    protected static String prevolutionSpellID;
    protected static String evolutionSpellID;

    protected static final Logger LOGGER = LogUtils.getLogger();

    protected AbstractSpell() {

    }

    protected AbstractSpell(String spellID, Element element, School school, int tier, float basicManaCost, float basicCooldown, boolean hasEvolution, String prevolutionSpellID, String evolutionSpellID) {
        this.spellID = spellID;
        this.element = element;
        this.school = school;
        this.tier = tier;
        this.basicManaCost = basicManaCost;
        this.basicCooldown = basicCooldown;
        this.hasEvolution = hasEvolution;
        this.prevolutionSpellID = prevolutionSpellID;
        this.evolutionSpellID = evolutionSpellID;
    }

    public boolean tryCast(Level world, Player player) {
        if (world.isClientSide) {
            return false;
        }

        ServerPlayer serverPlayer = (ServerPlayer) player;
        if (serverPlayer == null) {
            return false;
        }

        LOGGER.info("AbstractSpell: Sending Cast Packet");
        PacketDistributor.sendToServer(new CastPayload(serverPlayer.getUUID(), getResourceLocation()));
        return true;
    }

    public void cast(ServerPlayer player, Level world) {
        ResourceLocation resLoc = getResourceLocation();
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, new OnCastingStartedPayload(player.getUUID(), resLoc));
    }

    public void onCastingStarted() {

    }

    public void onCastingFinished() {

    }

    public MutableComponent getDisplayName() {
        return Component.translatable(getTranslatableComponent());
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

    public ResourceLocation getResourceLocation() {
        return ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, spellID);
    }
}
