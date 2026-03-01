package com.teamsimplyrs.prismaarcanum.api.casting;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamsimplyrs.prismaarcanum.registry.SpellRegistry;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class SpellComboChainData {
    private static final Logger LOGGER = LogUtils.getLogger();

    protected ResourceLocation spellID;
    protected long latestCastGameTime;
    protected int currentComboCount;
    protected int maxChainCount;
    protected int internalComboCooldownTicks;
    protected int remainingComboWindowTicks;
    protected boolean isActive;

    public static final Codec<SpellComboChainData> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.LONG.fieldOf("latestCastGameTime").forGetter(SpellComboChainData::getLatestCastGameTime),
            Codec.INT.fieldOf("currentComboCount").forGetter(SpellComboChainData::getCurrentComboCount),
            Codec.INT.fieldOf("maxChainCount").forGetter(SpellComboChainData::getMaxChainCount),
            Codec.INT.fieldOf("internalComboCooldownTicks").forGetter(SpellComboChainData::getInternalComboCooldownTicks),
            Codec.INT.fieldOf("remainingComboWindowTicks").forGetter(SpellComboChainData::getRemainingComboWindowTicks),
            Codec.BOOL.fieldOf("isActive").forGetter(SpellComboChainData::isActive)
    ).apply(i, SpellComboChainData::new));

    public SpellComboChainData(long latestCastGameTime, int currentComboCount, int maxChainCount, int internalComboCooldownTicks, int remainingComboWindowTicks, boolean isActive) {
        this.latestCastGameTime = latestCastGameTime;
        this.currentComboCount = currentComboCount;
        this.maxChainCount = maxChainCount;
        this.internalComboCooldownTicks = internalComboCooldownTicks;
        this.remainingComboWindowTicks = remainingComboWindowTicks;
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

        if (internalComboCooldownTicks > 0) {
            internalComboCooldownTicks--;
            return false;
        }

        if (remainingComboWindowTicks > 0) {
            remainingComboWindowTicks--;
            if (remainingComboWindowTicks <= 0) {
                isActive = false;
                return false;
            }
        }


        return true;
    }

    public boolean tryIncrementCombo() {
        if (spellID == null ||
                !isActive ||
                internalComboCooldownTicks > 0 ||
                currentComboCount > maxChainCount ||
                remainingComboWindowTicks <= 0) {
            return false;
        }

        currentComboCount++;
        isActive = currentComboCount < maxChainCount;

        return true;
    }

    public void reset() {

    }

    // region Region: Getters

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

    public int getRemainingComboWindowTicks() {
        return remainingComboWindowTicks;
    }

    public boolean isActive() {
        return isActive;
    }

    // endregion
}
