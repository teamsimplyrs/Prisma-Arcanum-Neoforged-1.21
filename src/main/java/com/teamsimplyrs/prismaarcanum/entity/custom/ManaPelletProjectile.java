package com.teamsimplyrs.prismaarcanum.entity.custom;

import com.lowdragmc.photon.client.fx.EntityEffectExecutor;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXHelper;
import com.mojang.logging.LogUtils;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.network.payload.OnCastingFinishedPayload;
import com.teamsimplyrs.prismaarcanum.registry.PAEntityRegistry;
import com.teamsimplyrs.prismaarcanum.api.spell.spells.common.AbstractSpell;
import com.teamsimplyrs.prismaarcanum.api.spell.spells.common.AbstractSpellProjectile;
import com.teamsimplyrs.prismaarcanum.api.utils.ProjectileMotionType;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.Vector3f;
import org.slf4j.Logger;

public class ManaPelletProjectile extends AbstractSpellProjectile {
    public ManaPelletProjectile(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    public ManaPelletProjectile(LivingEntity caster, Level level, ResourceLocation spellID) {
        super(PAEntityRegistry.MANA_PELLET_PROJECTILE.get(), level);
        this.setNoGravity(true);
        this.setOwner(caster);
        this.setLevel(level);
        this.setParentSpell(spellID);
    }

    @Override
    public void setSpellData(float damage, float lifetime, float velocity, ProjectileMotionType motionType) {
        super.setSpellData(damage, lifetime, velocity, motionType);
    }

    @Override
    public void launch(Vec3 rot) {
        super.launch(rot);
        this.refreshDimensions();
    }

    @Override
    public void startLaunchFX(Vec3 rot) {
    }

    @Override
    public void startTrailFX() {
        if (level().isClientSide) {
            FX manaPelletTrail = FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "mana_pellet_trail"));
            EntityEffectExecutor entityFX = new EntityEffectExecutor(manaPelletTrail, level(), this, EntityEffectExecutor.AutoRotate.LOOK);
            entityFX.setOffset(this.position().toVector3f());
//            entityFX.setOffset();
            entityFX.start();
        }
    }

    @Override
    public void startHitFX() {

    }

    public void onHit(HitResult result) {
        super.onHit(result);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        LOGGER.info("Mana Pellet Projectile: Entity hit");
        
        Entity entity = result.getEntity();
        if (entity.isAlive()) {
            entity.hurt(this.damageSources().magic(), damage);
        }
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte)3);
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        kill();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    public void onRemovedFromLevel() {
        super.onRemovedFromLevel();
        Entity owner = this.getOwner();
        if (level() != null && !level().isClientSide && owner != null) {
            PacketDistributor.sendToPlayersTrackingEntityAndSelf(this, new OnCastingFinishedPayload(owner.getUUID(), parentSpellID));
        }
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.scalable(0.2F, 0.2F);
    }

}
