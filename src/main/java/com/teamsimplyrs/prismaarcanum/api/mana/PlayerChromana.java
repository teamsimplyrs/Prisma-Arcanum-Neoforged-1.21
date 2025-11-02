package com.teamsimplyrs.prismaarcanum.api.mana;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class PlayerChromana {
    public static final int BASE_MAX = 100;
    public static final float BASE_REGEN = 0.25f; // per tick
    public static final float BASE_REGEN_COOLDOWN = 2.5f; // seconds

    public int current;
    public int max;
    public float regen; // per tick
    public float regenCooldown;

    public PlayerChromana() {
        current = BASE_MAX;
        max = BASE_MAX;
        regen = BASE_REGEN;
        regenCooldown = BASE_REGEN_COOLDOWN;
    }

    public PlayerChromana(int current, int max, float regen, float regenCooldown) {
        this.current = current;
        this.max = max;
        this.regen = regen;
        this.regenCooldown = regenCooldown;
    }

    public static final Codec<PlayerChromana> CODEC = RecordCodecBuilder.create(i -> i.group(
            Codec.INT.fieldOf("current").forGetter(PlayerChromana::getCurrent),
            Codec.INT.fieldOf("max").forGetter(PlayerChromana::getMax),
            Codec.FLOAT.fieldOf("regen").forGetter(PlayerChromana::getRegen),
            Codec.FLOAT.fieldOf("regen_cooldown").forGetter(PlayerChromana::getRegenCooldown)
    ).apply(i, PlayerChromana::new));


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

    public float getRegenCooldown() {
        return regenCooldown;
    }

    // --- SETTERS ---

    public void addMana(int toAdd) {
        current = Math.clamp(current + toAdd, 0, getMax());
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

    public void setRegenCooldown(float newRegenCooldown) {
        regenCooldown = newRegenCooldown;
    }

    // --- UTILS ---

    public void regenMana() {
        regenMana(1);
    }

    public void regenMana(int ticks) {
        int toAdd = (int)Math.floor(regen * ticks);
        addMana(toAdd);
    }
}
