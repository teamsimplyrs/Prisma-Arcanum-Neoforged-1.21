package com.teamsimplyrs.prismaarcanum.entity.custom;

import com.lowdragmc.photon.client.fx.BlockEffectExecutor;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXHelper;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.registry.PASpellEffectRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class SpellEffectAreaEntity extends Entity {

    private int lifetime;
    private ResourceLocation spellID;
    private float size;
    private int effectDuration = 60; // default 2 seconds status effect
    private int amplifier = 0;
    private boolean particleEmitted = false;
    private boolean refreshedDimensionsOnce = false;

    private static final EntityDataAccessor<Float> DATA_WIDTH =
            SynchedEntityData.defineId(SpellEffectAreaEntity.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> DATA_HEIGHT =
            SynchedEntityData.defineId(SpellEffectAreaEntity.class, EntityDataSerializers.FLOAT);

    public SpellEffectAreaEntity(EntityType<?> type, Level level) {
        super(type, level);
    }


    public void configure(ResourceLocation spellID, int lifetime, float width, float height) {
        this.spellID = spellID;
        this.lifetime = lifetime;
        this.entityData.set(DATA_WIDTH, width);
        this.entityData.set(DATA_HEIGHT, height);
        this.refreshDimensions(); // update server BB; client updates via onSyncedDataUpdated
    }

    @Override
    public void tick() {
        super.tick();

        // CLIENT: one-shot FX (don’t decrement lifetime here)
        if (level().isClientSide) {
            if (!particleEmitted) {
                FX napalmSprout = FXHelper.getFX(ResourceLocation.fromNamespaceAndPath(
                        PrismaArcanum.MOD_ID, "napalm_sprout"));
                particleEmitted = true;
                new BlockEffectExecutor(napalmSprout, level(), blockPosition()).start();
            }
            return; // client stops here; server continues below
        }

        // SERVER: apply effect & own the lifetime
        AABB box = this.getBoundingBox();
        for (Entity e : level().getEntities(this, box)) {
            if (e instanceof LivingEntity living && living.isAlive()) {
                // Only add if missing
                if (!living.hasEffect((PASpellEffectRegistry.NAPALM_BURN))) {
                    living.addEffect(new MobEffectInstance(
                            PASpellEffectRegistry.NAPALM_BURN, // Holder#get() returns the effect
                            effectDuration, amplifier,
                            false, // ambient
                            false, // show vanilla particles
                            true   // show icon
                    ));
                }
            }
        }

        // lifetime only on server
        lifetime--;
        if (lifetime <= 0) {
            this.discard(); // server-side discard; clients follow via destroy packet
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_WIDTH, 1.0f);  // default width
        builder.define(DATA_HEIGHT, 1.0f); // default height
    }

    /** When the DATA_SIZE changes, update the client’s bounding box */
    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (key.equals(DATA_WIDTH) || key.equals(DATA_HEIGHT)) {
            this.refreshDimensions();
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putFloat("Width", this.entityData.get(DATA_WIDTH));
        tag.putFloat("Height", this.entityData.get(DATA_HEIGHT));
        tag.putInt("Lifetime", this.lifetime);
        if (this.spellID != null) tag.putString("SpellID", this.spellID.toString());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("Width")) this.entityData.set(DATA_WIDTH, tag.getFloat("Width"));
        if (tag.contains("Height")) this.entityData.set(DATA_HEIGHT, tag.getFloat("Height"));
        this.lifetime = tag.getInt("Lifetime");
        if (tag.contains("SpellID")) this.spellID = ResourceLocation.tryParse(tag.getString("SpellID"));
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        float w = this.entityData.get(DATA_WIDTH);
        float h = this.entityData.get(DATA_HEIGHT);
        return EntityDimensions.fixed(w, h);
    }

}

