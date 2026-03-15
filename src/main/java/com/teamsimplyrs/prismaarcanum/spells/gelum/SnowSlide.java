package com.teamsimplyrs.prismaarcanum.spells.gelum;

import com.lowdragmc.photon.client.fx.EntityEffectExecutor;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXHelper;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.utils.Element;
import com.teamsimplyrs.prismaarcanum.api.utils.School;
import com.teamsimplyrs.prismaarcanum.registry.PASpellEffectRegistry;
import com.teamsimplyrs.prismaarcanum.spells.common.AbstractSpell;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class SnowSlide extends AbstractSpell {
    public static final String SPELL_ID = "snow_slide";

    protected static final Element ELEMENT = Element.Gelum;
    protected static final School SCHOOL = School.Motion;
    protected static final int TIER = 1;

    protected static int basicManaCost = 10;
    protected static int basicCooldown = 250;

    protected static final boolean hasEvolution = true;

    public SnowSlide() {
        super(ELEMENT, SCHOOL, TIER, basicManaCost, basicCooldown, 0, hasEvolution);
    }

    @Override
    public void cast(ServerPlayer player, Level world) {
        super.cast(player, world);
        var effect = new MobEffectInstance(PASpellEffectRegistry.SNOW_SLIDE_EFFECT, getDurationTicks(), 1, false, false, true);
        player.addEffect(effect);
    }

    @Override
    public void onCastingStarted(Player player, Level world) {
        super.onCastingStarted(player, world);
        if (world.isClientSide) {
        }
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, SPELL_ID);
    }

    @Override
    public ResourceLocation getFXid() {
        return ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "snow_slide_fx");
    }

    @Override
    public int getDurationTicks() {
        return 200;
    }
}
