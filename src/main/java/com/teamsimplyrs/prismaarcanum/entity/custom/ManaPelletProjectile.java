package com.teamsimplyrs.prismaarcanum.entity.custom;

import com.lowdragmc.photon.client.fx.EntityEffectExecutor;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXHelper;
import com.mojang.logging.LogUtils;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.network.payload.OnCastingFinishedPayload;
import com.teamsimplyrs.prismaarcanum.registry.PAEntityRegistry;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.registry.SpellRegistry;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.spells.common.AbstractSpell;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.spells.common.AbstractSpellProjectile;
import com.teamsimplyrs.prismaarcanum.system.utils.ProjectileMotionType;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.Quaternionf;
import org.slf4j.Logger;

public class ManaPelletProjectile extends AbstractSpellProjectile {
    private static final Logger LOGGER = LogUtils.getLogger();

    private float damage;
    private float lifetime;
    private AbstractSpell parentSpell;
    private Level world;

    public ManaPelletProjectile(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    public ManaPelletProjectile(LivingEntity caster, Level level, AbstractSpell spell) {
        super(PAEntityRegistry.MANA_PELLET_PROJECTILE.get(), level);
        this.setNoGravity(true);
        this.setOwner(caster);


        this.world = level;
        this.parentSpell = spell;
    }

    @Override
    public void setSpellData(float damage, float lifetime, float velocity, ProjectileMotionType motionType) {
        super.setSpellData(damage, lifetime, velocity, motionType);
    }

    @Override
    public void launch(Vec3 rot) {
        super.launch(rot);
        this.refreshDimensions();

        particlesOnLaunch(rot);
    }

    @Override
    protected void particlesOnLaunch(Vec3 rot) {
        FX manaPelletTrail = FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "mana_pellet_trail"));
        EntityEffectExecutor entityFX = new EntityEffectExecutor(manaPelletTrail, world, this, EntityEffectExecutor.AutoRotate.NONE);
        Vec3 rotToDeg = new Vec3(Math.toDegrees(rot.x), Math.toDegrees(rot.y), Math.toDegrees(rot.z));
        entityFX.setRotation(new Quaternionf().rotationXYZ((float) rotToDeg.x, (float) rotToDeg.y, (float) rotToDeg.z));
        entityFX.start();
    }

    @Override
    protected void particlesTrailing() {

    }

    @Override
    protected void particlesOnHit() {

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
        if (this.world != null && !this.world.isClientSide && owner != null) {
            PacketDistributor.sendToPlayersNear((ServerLevel)world, null, owner.getX(), owner.getY(), owner.getZ(), 15, new OnCastingFinishedPayload(owner.getUUID(), this.parentSpell.getResourceLocation()));
        }
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.scalable(0.2F, 0.2F);
    }

}
