package com.teamsimplyrs.prismaarcanum.system.castingsystem;

import com.teamsimplyrs.prismaarcanum.system.spellsystem.actions.GenericAction;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.data.model.SpellDataModel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

import java.util.*;

public class CastingEngine {
    private Queue<GenericAction> actionQueue;
    private GenericAction currentAction;
    private Player player;
    private Level world;

    public CastingEngine(Level world, Player player, SpellDataModel spellData) {
        this.world = world;
        this.player = player;

        this.actionQueue = new ArrayDeque<>(spellData.spell_actions);
    }

    public void tick() {
        // If no current action, grab the next one
        if (currentAction == null && !actionQueue.isEmpty()) {
            currentAction = actionQueue.poll();
            player.sendSystemMessage(Component.literal("Playing action: " + currentAction.getClass().getName()));
            currentAction.play(world, player);
        }
        currentAction.tick(world, player);

        // If it's non-blocking, immediately mark it done so we move on next tick
        if (!currentAction.isBlocking) {
            player.sendSystemMessage(Component.literal("Ticking and completing non-blocking action: " + currentAction.getClass().getName()));
            currentAction = null;
        } else {
            if (currentAction.isCompleted || currentAction.isCancelled) {
                player.sendSystemMessage(Component.literal("Completing action: " + currentAction.getClass().getName()));
                currentAction = null; // free up for the next action
            }
        }
    }

    public boolean isCastingComplete() {
        return currentAction == null && actionQueue.isEmpty();
    }
}
