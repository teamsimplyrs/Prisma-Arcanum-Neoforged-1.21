package com.teamsimplyrs.prismaarcanum.entity.custom;

import com.mojang.logging.LogUtils;
import com.teamsimplyrs.prismaarcanum.api.spell.spells.common.AbstractSpellProjectile;
import com.teamsimplyrs.prismaarcanum.registry.PAEntityRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class NapalmShootBlankProjectile extends AbstractSpellProjectile {
    protected final Logger LOGGER = LogUtils.getLogger();

    public NapalmShootBlankProjectile(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
        this.setNoGravity(false);
    }

    public NapalmShootBlankProjectile(LivingEntity shooter, Level level, ResourceLocation spellID) {
        super(PAEntityRegistry.NAPALM_BLANK.get(), level);
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
                    40,                     // lifetime in ticks (2 seconds)
                    2f,
                    8f
            );

            level().addFreshEntity(aoe);
        }

        remove(RemovalReason.DISCARDED);
    }

    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull Pose pose) {
        return EntityDimensions.fixed(4f, 4f); // small hitbox
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
