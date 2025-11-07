package com.teamsimplyrs.prismaarcanum.api.mana;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class PlayerChromana {
    public final int BASE_MAX = 100;
    public final float BASE_REGEN = 0.25f; // per tick
    public final int BASE_REGEN_COOLDOWN = 50; // ticks

    protected int current;
    protected int max;
    protected float regen; // per tick
    protected int regenCooldown;


    private float accumulatedFractionalMana = 0;
    private int regenCooldownActiveTicks = 0;

    public PlayerChromana() {
        current = BASE_MAX;
        max = BASE_MAX;
        regen = BASE_REGEN;
        regenCooldown = BASE_REGEN_COOLDOWN;
    }

    public PlayerChromana(int current, int max, float regen, int regenCooldown) {
        this.current = current;
        this.max = max;
        this.regen = regen;
        this.regenCooldown = regenCooldown;
    }

    public static final Codec<PlayerChromana> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.INT.fieldOf("current").forGetter(PlayerChromana::getCurrent),
            Codec.INT.fieldOf("max").forGetter(PlayerChromana::getMax),
            Codec.FLOAT.fieldOf("regen").forGetter(PlayerChromana::getRegen),
            Codec.INT.fieldOf("regen_cooldown").forGetter(PlayerChromana::getRegenCooldown)
    ).apply(i, PlayerChromana::new));


    // server tick to perform mana operations
    public boolean tick() {
        boolean changed = false;
        final int before = this.current;
        final int max = getMax();

        if (regenCooldownActiveTicks > 0) {
            regenCooldownActiveTicks--;
        } else {

            if (before < max) {
                regenMana();
                if (this.current != before) {
                    changed = true;
                }
            }
        }

        return changed;
    }

    // --- GETTERS ---

    public int getCurrent() {
        return current;
    }

    public int getMax() {
        return max;
    }

    public float getRegen() {
        return regen;
    }

    public int getRegenCooldown() {
        return regenCooldown;
    }

    // --- SETTERS ---

    public void addMana(int toAdd) {
        current = Math.clamp(current + toAdd, 0, getMax());
    }

    public void useMana(int toUse, boolean applyPreRegenCooldown) {
        addMana(-toUse);
        regenCooldownActiveTicks = applyPreRegenCooldown ? getRegenCooldown() : 0;
    }

    public void setCurrent(int newCurrent) {
        current = Math.clamp(newCurrent, 0, getMax());
    }

    public void setMax(int newMax) {
        max = newMax;
    }

    public void setRegen(float newRegen) {
        regen = newRegen;
    }

    public void setRegenCooldown(int newRegenCooldown) {
        regenCooldown = newRegenCooldown;
    }

    // --- UTILS ---

    public void regenMana() {
        regenMana(1);
    }

    public void regenMana(int ticks) {
        accumulatedFractionalMana += regen * ticks;
        int flooredAccumulated = (int)Math.floor(accumulatedFractionalMana);

        if (flooredAccumulated != 0) {
            addMana(flooredAccumulated);
            accumulatedFractionalMana -= flooredAccumulated;
        }
    }
}
