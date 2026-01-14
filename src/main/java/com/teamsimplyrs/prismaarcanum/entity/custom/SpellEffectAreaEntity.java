package com.teamsimplyrs.prismaarcanum.entity.custom;

import com.lowdragmc.photon.client.fx.EntityEffectExecutor;
import com.mojang.logging.LogUtils;
import com.teamsimplyrs.prismaarcanum.registry.SpellRegistry;
import com.teamsimplyrs.prismaarcanum.spells.common.AbstractSpell;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;

import java.util.List;

public class SpellEffectAreaEntity extends Entity {

    public int lifetime;
    private ResourceLocation spellID;
    private float size;
    public int effectDuration = 60;
    public int amplifier = 0;
    public boolean particleEmitted = false;
    public ResourceLocation fxID;

    protected static final Logger LOGGER = LogUtils.getLogger();

    private static final EntityDataAccessor<Float> DATA_WIDTH =
            SynchedEntityData.defineId(SpellEffectAreaEntity.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> DATA_HEIGHT =
            SynchedEntityData.defineId(SpellEffectAreaEntity.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<String> DATA_SPELL_ID =
            SynchedEntityData.defineId(SpellEffectAreaEntity.class, EntityDataSerializers.STRING);

    public SpellEffectAreaEntity(EntityType<?> type, Level level) {
        super(type, level);
    }


    public void configure(ResourceLocation spellID, int lifetime, float width, float height) {
        this.spellID = spellID;
        this.lifetime = lifetime;
        this.entityData.set(DATA_WIDTH, width);
        this.entityData.set(DATA_HEIGHT, height);
        this.entityData.set(DATA_SPELL_ID, spellID.toString());
        this.refreshDimensions();
    }

    public void setFxID(ResourceLocation resourceLocation) {
       fxID = resourceLocation;
    }

    /**
     * Implement hitbox tick function by overriding AbstractSpell.hitboxTick in your own spell
     */
    @Override
    public void tick() {
        super.tick();

        if (spellID == null || spellID.toString().isEmpty()) {
            spellID = ResourceLocation.tryParse(this.entityData.get(DATA_SPELL_ID));
        }

        AbstractSpell spell = SpellRegistry.getSpell(spellID);
        if (spell != null) spell.hitboxTick(this);

        if (!level().isClientSide) {
            lifetime--;
            if (lifetime <= 0) remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    public void remove(RemovalReason reason) {
        ResourceLocation fxId = getFXid();
        if (fxId != null) {
            var CACHE = EntityEffectExecutor.CACHE;
            List<EntityEffectExecutor> effects = CACHE.get(this);
            if (effects != null && !effects.isEmpty()) {
                var iterator = effects.iterator();

                while(iterator.hasNext()) {
                    EntityEffectExecutor exec = iterator.next();
                    //Not sure if getFxLocation and getRuntime would become null or not
                    if(exec.fx.getFxLocation().equals(fxId)){
                        var runtime = exec.getRuntime();
                        runtime.destroy(true);
                        iterator.remove();
                    }
                }
                if ((CACHE.get(this)).isEmpty()) {
                    CACHE.remove(this);
                }

                EntityEffectExecutor.CACHE = CACHE;
            }
        }

        super.remove(reason);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_WIDTH, 1.0f);  // default width
        builder.define(DATA_HEIGHT, 1.0f); // default height
        builder.define(DATA_SPELL_ID, "");
    }

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
        tag.putString("SpellID", this.entityData.get(DATA_SPELL_ID));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("Width")) this.entityData.set(DATA_WIDTH, tag.getFloat("Width"));
        if (tag.contains("Height")) this.entityData.set(DATA_HEIGHT, tag.getFloat("Height"));
        this.lifetime = tag.getInt("Lifetime");
        if (tag.contains("SpellID")) {
            String id = tag.getString("SpellID");
            this.entityData.set(DATA_SPELL_ID, id);
        }
        this.spellID = ResourceLocation.tryParse(this.entityData.get(DATA_SPELL_ID));
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        float w = this.entityData.get(DATA_WIDTH);
        float h = this.entityData.get(DATA_HEIGHT);
        return EntityDimensions.fixed(w, h);
    }

    public ResourceLocation getFXid() {
        return fxID;
    }
}

