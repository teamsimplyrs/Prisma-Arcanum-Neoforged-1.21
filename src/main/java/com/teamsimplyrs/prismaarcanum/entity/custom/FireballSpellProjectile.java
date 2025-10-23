package com.teamsimplyrs.prismaarcanum.entity.custom;

import com.teamsimplyrs.prismaarcanum.api.spell.spells.common.AbstractSpellProjectile;
import com.teamsimplyrs.prismaarcanum.registry.PAEntityRegistry;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class FireballSpellProjectile extends AbstractSpellProjectile {

    private int bounceCount = 0;

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

    }

    @Override
    public void startHitFX() {

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
            discard();
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
