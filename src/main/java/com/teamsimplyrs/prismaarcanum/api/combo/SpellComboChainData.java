package com.teamsimplyrs.prismaarcanum.api.combo;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamsimplyrs.prismaarcanum.registry.SpellRegistry;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class SpellComboChainData {


    private static final Logger LOGGER = LogUtils.getLogger();

    protected ResourceLocation spellID = SpellRegistry.getEmpty();
    protected long latestCastGameTime;
    protected int currentComboCount;
    protected int maxChainCount;
    protected int internalComboCooldownTicks;
    protected int remainingInternalComboCooldownTicks;
    protected int remainingComboWindowTicks;
    protected boolean isActive;

    public static final Codec<SpellComboChainData> CODEC = RecordCodecBuilder.create(i -> i.group(
            ResourceLocation.CODEC.fieldOf("spellID").forGetter(SpellComboChainData::getSpellID),
            Codec.LONG.fieldOf("latestCastGameTime").forGetter(SpellComboChainData::getLatestCastGameTime),
            Codec.INT.fieldOf("currentComboCount").forGetter(SpellComboChainData::getCurrentComboCount),
            Codec.INT.fieldOf("maxChainCount").forGetter(SpellComboChainData::getMaxChainCount),
            Codec.INT.fieldOf("internalComboCooldownTicks").forGetter(SpellComboChainData::getInternalComboCooldownTicks),
            Codec.INT.fieldOf("remainingInternalComboCooldownTicks").forGetter(SpellComboChainData::getRemainingInternalComboCooldownTicks),
            Codec.INT.fieldOf("remainingComboWindowTicks").forGetter(SpellComboChainData::getRemainingComboWindowTicks),
            Codec.BOOL.fieldOf("isActive").forGetter(SpellComboChainData::isActive)
    ).apply(i, SpellComboChainData::new));

    public SpellComboChainData(ResourceLocation spellID, long latestCastGameTime, int currentComboCount, int maxChainCount, int internalComboCooldownTicks, int remainingInternalComboCooldownTicks, int comboWindowTicks, boolean isActive) {
        this.spellID = spellID == null ? SpellRegistry.getEmpty() : spellID;
        this.latestCastGameTime = latestCastGameTime;
        this.currentComboCount = currentComboCount;
        this.maxChainCount = maxChainCount;
        this.internalComboCooldownTicks = internalComboCooldownTicks;
        this.remainingInternalComboCooldownTicks = remainingInternalComboCooldownTicks;
        this.remainingComboWindowTicks = comboWindowTicks;
        this.isActive = isActive;
    }

    public SpellComboChainData() {}

    public boolean tick() {
        if (this.spellID == null || SpellRegistry.getSpell(this.spellID) == null) {
            LOGGER.error("[PrismaArcanum] Ticked SpellComboChainData with a null or invalid/unobtainable spell.");
            return false;
        }

        if (!isActive) {
            return false;
        }

        if (remainingInternalComboCooldownTicks > 0) {
            remainingInternalComboCooldownTicks--;
            return false;
        }

        if (remainingComboWindowTicks > 0) {
            remainingComboWindowTicks--;
            if (remainingComboWindowTicks <= 0) {
                return false;
            }
        }

        return true;
    }

    public ComboAttemptResult tryIncrementCombo() {
        if (spellID.equals(SpellRegistry.getEmpty()) || !isActive) {
            return ComboAttemptResult.INACTIVE;
        }

        if (remainingInternalComboCooldownTicks > 0) {
            return ComboAttemptResult.INTERNAL_COOLDOWN;
        }

        if (currentComboCount > maxChainCount) {
            return ComboAttemptResult.MAX_COMBO_CHAIN_REACHED;
        }

        if (remainingComboWindowTicks <= 0) {
            return ComboAttemptResult.WINDOW_EXPIRED;
        }

        currentComboCount++;
        remainingInternalComboCooldownTicks = internalComboCooldownTicks;
        isActive = currentComboCount < maxChainCount;

        return ComboAttemptResult.SUCCESS;
    }

    public void reset() {
        if (!isActive) {
            return; // no need to keep resetting if combo is marked inactive
        }

        isActive = false;
        spellID = SpellRegistry.getEmpty();
        currentComboCount = 0;
        maxChainCount = 0;
        remainingComboWindowTicks = 0;
        internalComboCooldownTicks = 0;
    }

    // region Region: Getters

    public ResourceLocation getSpellID() {
        return spellID;
    }

    public long getLatestCastGameTime() {
        return latestCastGameTime;
    }

    public int getCurrentComboCount() {
        return currentComboCount;
    }

    public int getMaxChainCount() {
        return maxChainCount;
    }

    public int getInternalComboCooldownTicks() {
        return internalComboCooldownTicks;
    }

    public int getRemainingInternalComboCooldownTicks() {
        return remainingInternalComboCooldownTicks;
    }

    public int getRemainingComboWindowTicks() {
        return remainingComboWindowTicks;
    }

    public boolean isActive() {
        return isActive;
    }

    // endregion
}
