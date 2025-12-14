package com.teamsimplyrs.prismaarcanum.entity.custom;

import com.teamsimplyrs.prismaarcanum.api.spell.spells.common.AbstractSpellProjectile;
import com.teamsimplyrs.prismaarcanum.registry.PAEntityRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class WindPoolBlankProjectile extends AbstractSpellProjectile {
    public WindPoolBlankProjectile(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
        setNoGravity(false);
    }

    public WindPoolBlankProjectile(LivingEntity shooter, Level level, ResourceLocation spellID) {
        super(PAEntityRegistry.WINDPOOL_BLANK.get(), level);
        this.setOwner(shooter);
        this.setParentSpell(spellID);
        lifetime = 100;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);

        if (!level().isClientSide) {
            SpellEffectAreaEntity aoe = PAEntityRegistry.SPELL_EFFECT_AREA.get().create(level());
            aoe.setPos(result.getBlockPos().getX() + 0.5, result.getBlockPos().getY() + 1, result.getBlockPos().getZ() + 0.5);

            aoe.configure(
                    this.parentSpellID,  // <-- spellID
                    100,                     // lifetime in ticks (2 seconds)
                    4f,
                    2f
            );

            level().addFreshEntity(aoe);
        }

        discard();
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

    @Override
    protected ResourceLocation getTrailFXid() {
        return null;
    }
}
