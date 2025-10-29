package com.teamsimplyrs.prismaarcanum.api.spell_deprecated.actions;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ProjectileAction extends GenericAction {
    public float speed;
    public float lifetime;
    public float width;
    public float height;
    public float effect_dropoff;
    public boolean has_gravity;
    public GenericAction hit_action;

    public ProjectileAction(float speed, float lifetime, float width, float height, float effectDropoff, boolean hasGravity, GenericAction hitAction, boolean isBlocking) {
        super(isBlocking);
        this.speed = speed;
        this.lifetime = lifetime;
        this.width = width;
        this.height = height;
        this.effect_dropoff = effectDropoff;
        this.has_gravity = hasGravity;
        this.hit_action = hitAction;
    }

    @Override
    public void play(Level level, Player player) {
        super.play(level, player);
        if (!level.isClientSide) {

        }
    }

    @Override
    public void tick(Level level, Player player) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void abort() {

    }
}
