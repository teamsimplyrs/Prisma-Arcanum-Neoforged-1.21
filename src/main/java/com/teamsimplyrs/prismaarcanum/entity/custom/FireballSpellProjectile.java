package com.teamsimplyrs.prismaarcanum.entity.custom;

import com.teamsimplyrs.prismaarcanum.api.spell.spells.common.AbstractSpellProjectile;
import com.teamsimplyrs.prismaarcanum.registry.PAEntityRegistry;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
        if (owner == null) {
            return;
        }

        if (result.getEntity() != owner) {
            kill();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        Entity owner = getOwner();
        if (owner == null) {
            return;
        }

        if (bounceCount <= 0) {
            kill();
            return;
        }

        bounceCount--;
        Vec3 delta = getDeltaMovement();
        Vec3i normal = result.getDirection().getNormal();
    }

    @Override
    protected double getDefaultGravity() {
        return 2.0;
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.scalable(0.75f, 0.75f);
    }
}
