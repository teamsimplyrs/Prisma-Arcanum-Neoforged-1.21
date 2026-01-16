package com.teamsimplyrs.prismaarcanum.entity.custom.projectile;

import com.lowdragmc.photon.client.fx.EntityEffectExecutor;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXHelper;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.spells.common.AbstractSpellProjectile;
import com.teamsimplyrs.prismaarcanum.registry.PAEntityRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class FireballSpellProjectile extends AbstractSpellProjectile {

    public FireballSpellProjectile(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    public FireballSpellProjectile(LivingEntity caster, Level level, ResourceLocation spellID) {
        super(PAEntityRegistry.FIREBALL_SPELL_PROJECTILE.get(), level);
        this.setOwner(caster);
        this.setLevel(level);
        this.setParentSpell(spellID);
        this.setNoGravity(false);

        this.refreshDimensions();
    }

    public void setData(int bounceCount) {
        this.bounceCount = bounceCount;
    }

    @Override
    public void launch(Vec3 rot) {

        super.launch(rot);
    }

    @Override
    public void startLaunchFX(Vec3 rot) {

    }

    @Override
    public void startTrailFX() {
        if (level().isClientSide) {
            FX fireballFX = FXHelper.getFX(getTrailFXid());
            EntityEffectExecutor entityFX = new EntityEffectExecutor(fireballFX, level(), this, EntityEffectExecutor.AutoRotate.NONE);
//            entityFX.getRuntime().destroy();
            entityFX.setOffset(new Vector3f(0f, 0f, 0f));
            entityFX.start();
        }
    }

    @Override
    public void startHitFX() {

    }

    @Override
    protected ResourceLocation getTrailFXid() {
        return ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "fireball_main_fx");
    }

    @Override
    protected ResourceLocation getBlockImpactFXid() {
        return ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "impact/impact_ignis_small_1");
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity owner = getOwner();
        Entity target = result.getEntity();

        if (owner == null || target == null) {
            return;
        }

        if (target != owner) {
            target.hurt(this.damageSources().magic(), damage);
            remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    public void tick() {
        super.tick();
//        setRot(getYRot() + 10, getXRot() + 10);
    }

    @Override
    protected double getDefaultGravity() {
        return 0.05;
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.scalable(0.75f, 0.75f);
    }
}
