package com.teamsimplyrs.prismaarcanum.api.spell.spells.ventus;

import com.lowdragmc.photon.client.fx.EntityEffectExecutor;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXHelper;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.spell.spells.common.AbstractSpell;
import com.teamsimplyrs.prismaarcanum.api.utils.Element;
import com.teamsimplyrs.prismaarcanum.api.utils.School;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class TailwindSpell extends AbstractSpell {
    public static final String spellID = "tailwind_spell";
    public static final Element element = Element.Ventus;
    public static final School school = School.Motion;

    public static final int tier = 1;
    public static final int basicManaCost = 30;
    public static final int basicCooldown = 300;

    public static final boolean hasEvolution = true;

    private static final float baseDamage = 0f;

    public TailwindSpell() { super(spellID, element, school, tier, basicManaCost, basicCooldown, hasEvolution); }

    @Override
    public void cast(ServerPlayer player, Level world) {
        if (!world.isClientSide) {
            super.cast(player, world);
            Vec3 launchRotation = player.getLookAngle();
            Vec3 newMotion = player.getDeltaMovement().add(launchRotation.scale(getSpeed()));
            player.setDeltaMovement(newMotion);
            player.fallDistance = 0F;

            player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING,
                    getDurationTicks() * 2,
                    0,
                    true,
                    true
                    )
            );

            // player movement gets overwritten by client input process.
            // sends a clientbound sync packet to tell the client input process to shut up for a sec
            player.connection.send(new ClientboundSetEntityMotionPacket(player));
            player.hurtMarked = true;
        }
    }

    @Override
    public void onCastingStarted(Player player, Level world) {
        super.onCastingStarted(player, world);

        FX fx = FXHelper.getFX(getFXid());
        EntityEffectExecutor fxExec = new EntityEffectExecutor(fx, world, player, EntityEffectExecutor.AutoRotate.NONE);
        fxExec.setOffset(new Vector3f(0f, 0f, 0f));
        fxExec.start();
    }

    @Override
    public void onCastingFinished(Player player, Level world) {
        super.onCastingFinished(player, world);

    }

    @Override
    public int getDurationTicks() {
        // 1s duration
        return 20;
    }

    @Override
    public int getCooldownTicks() {
        return basicCooldown;
    }

    @Override
    public int getManaCost() {
        return basicManaCost;
    }

    public float getSpeed() {
        return 2f;
    }

    @Override
    public ResourceLocation getFXid() {
        return ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "tailwind_fx");
    }
}
