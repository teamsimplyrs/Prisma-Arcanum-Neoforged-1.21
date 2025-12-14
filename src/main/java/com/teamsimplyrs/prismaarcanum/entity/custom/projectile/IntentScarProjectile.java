package com.teamsimplyrs.prismaarcanum.entity.custom.projectile;

import com.lowdragmc.photon.client.fx.EntityEffectExecutor;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXHelper;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.spell.spells.common.AbstractSpellProjectile;
import com.teamsimplyrs.prismaarcanum.registry.PAEntityRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class IntentScarProjectile extends AbstractSpellProjectile {
    public IntentScarProjectile(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    public IntentScarProjectile(LivingEntity caster, Level level, ResourceLocation spellID) {
        super(PAEntityRegistry.INTENT_SCAR_PROJECTILE.get(), level);
        this.setOwner(caster);
        this.setLevel(level);
        this.setParentSpell(spellID);
        this.setNoGravity(false);
    }

    @Override
    public void launch(Vec3 rot) {
        super.launch(rot);
        this.refreshDimensions();
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
    public void startLaunchFX(Vec3 rot) {

    }

    @Override
    public void startTrailFX() {
        if (level().isClientSide) {
            FX fx = FXHelper.getFX(getTrailFXid());
            EntityEffectExecutor entityFX = new EntityEffectExecutor(fx, level(), this, EntityEffectExecutor.AutoRotate.NONE);
            var rot = getEffectRotation();
//            rot = new Vec3(Math.toDegrees(rot.x), Math.toDegrees(rot.y), 0f);
            entityFX.setRotation(new Quaternionf()
                    .rotateY((float)Math.toRadians(-rot.y))
                    .rotateX((float)Math.toRadians(rot.x)));
            entityFX.setOffset(new Vector3f(0f, -0.25f, 0f));
            entityFX.start();
        }
    }

    @Override
    public void startHitFX() {

    }

    @Override
    protected ResourceLocation getTrailFXid() {
        return ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "intent_scar");
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.scalable(1.25f, 0.5f);
    }
}
