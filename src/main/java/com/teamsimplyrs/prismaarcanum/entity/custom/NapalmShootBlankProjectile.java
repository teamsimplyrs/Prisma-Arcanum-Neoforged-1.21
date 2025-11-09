package com.teamsimplyrs.prismaarcanum.entity.custom;

import com.lowdragmc.photon.client.fx.BlockEffectExecutor;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXHelper;
import com.mojang.logging.LogUtils;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.spell.spells.common.AbstractSpellProjectile;
import com.teamsimplyrs.prismaarcanum.registry.PAEntityRegistry;
import com.teamsimplyrs.prismaarcanum.registry.PASpellEffectRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class NapalmShootBlankProjectile extends AbstractSpellProjectile {
    protected final Logger LOGGER = LogUtils.getLogger();
    FX napalmSpray = FXHelper.getFX(
            ResourceLocation.fromNamespaceAndPath(
                    PrismaArcanum.MOD_ID, "napalm_sprout"
            )
    );

    public NapalmShootBlankProjectile(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
        this.setNoGravity(false);
    }

    public NapalmShootBlankProjectile(LivingEntity shooter, Level level) {
        super(PAEntityRegistry.NAPALM_BLANK.get(), level);
        this.setOwner(shooter);
        lifetime = 100;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected void onHitEntity(net.minecraft.world.phys.EntityHitResult result) {
        super.onHitEntity(result);
        LOGGER.info("hit entity");
        Entity target = result.getEntity();
        Entity owner = getOwner();

        if (!(target instanceof LivingEntity living)) return;
        //if (owner != null && target == owner) return; // prevent self-hit

        living.addEffect(new MobEffectInstance(
                (Holder<MobEffect>) PASpellEffectRegistry.NAPALM_BURN,
                100,   // duration in ticks
                0,    // amplifier
                false, // ambient
                false,  // showParticles (must be true to sync to client!)
                true   // showIcon
        ));

        // Now play hit FX visually
        if (level().isClientSide) {
            new BlockEffectExecutor(
                    napalmSpray,
                    level(),
                    BlockPos.containing(living.getX(), living.getY(), living.getZ())
            ).start();
        }

        // Remove the projectile after applying effect
        if (!level().isClientSide) {
            //discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if(onGround()){
            discard();
        }
        BlockPos pos = result.getBlockPos();
        if(level().isClientSide() && !onGround()){
            //LOGGER.info("Empty projectile hit ground at {}", pos);
            new BlockEffectExecutor(
                    napalmSpray,
                    level(),            // world
                    pos

            ).start();
            setOnGround(true);
            //LOGGER.info("Effect should start");
        }
        refreshDimensions();
        //discard();
    }

    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull Pose pose) {
        return EntityDimensions.scalable(0.2f, 0.2f); // small hitbox
    }

    @Override
    protected double getDefaultGravity() {
        return 0.05;
    }


    @Override
    public void startLaunchFX(Vec3 rot) {

    }

    @Override
    public void startTrailFX() {

    }

    @Override
    public void startHitFX() {

    }
}
