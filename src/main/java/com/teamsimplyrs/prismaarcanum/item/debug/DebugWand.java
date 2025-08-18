package com.teamsimplyrs.prismaarcanum.item.debug;

import com.mojang.logging.LogUtils;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.system.castingsystem.CastingEngine;
import com.teamsimplyrs.prismaarcanum.system.castingsystem.CastingEngineManager;
import com.teamsimplyrs.prismaarcanum.system.castingsystem.interfaces.ICastable;
import com.teamsimplyrs.prismaarcanum.system.castingsystem.interfaces.IMultiSpellHolder;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.data.model.SpellDataModel;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.registry.SpellRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class DebugWand extends Item implements ICastable, IMultiSpellHolder {
    public static final String name = "debug_wand";
    public static final Item.Properties properties = new Item.Properties();

    private static final Logger LOGGER = LogUtils.getLogger();

    public List<SpellDataModel> spells;
    private int currentSpellIndex;

    public DebugWand() {
        super(properties);
    }

    public void onSpellsReloaded() {
        this.spells = new ArrayList<>(SpellRegistry.getAllSpellData());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (!level.isClientSide()) {
            LOGGER.info("Debug Wand used");
            if (spells == null || spells.isEmpty()) {
                return super.use(level, player, usedHand);
            }

            cast(level, player, spells.get(currentSpellIndex));
        }

        return super.use(level, player, usedHand);
    }

    @Override
    public void cast(Level world, Player player, SpellDataModel spellData) {
        // HitResult hit = raycast(world, player, ClipContext.Fluid.NONE, 20f);
        player.sendSystemMessage(Component.literal("Called cast for spell: " + spellData.spell_display_name));
        CastingEngine castingEngine = new CastingEngine(world, player, spells.get(currentSpellIndex));
        CastingEngineManager.startEngine(castingEngine);
    }

    @Override
    public HitResult raycast(Level level, Player player, ClipContext.Fluid fluidMode, float distance) {
        EntityHitResult entityHit = entityRaycast(level, player, fluidMode, distance);
        if (entityHit != null && entityHit.getType() != HitResult.Type.MISS) {
            player.sendSystemMessage(Component.literal("Entity raycast hit: " + entityHit.getType()));
            return entityHit;
        }

        BlockHitResult blockHit = blockRaycast(level, player, fluidMode, distance);
        if (blockHit.getType() != HitResult.Type.MISS) {
            player.sendSystemMessage(Component.literal("Block raycast hit: " + blockHit.getType()));
            return blockHit;
        }

        return null;
    }

    @Override
    public BlockHitResult blockRaycast(Level level, Player player, ClipContext.Fluid fluidMode, float distance) {
        Vec3 start = player.getEyePosition(1.0F);
        Vec3 look = player.getLookAngle();
        Vec3 end = start.add(look.scale(distance));

        return level.clip(new ClipContext(
            start,
            end,
            ClipContext.Block.OUTLINE,
            fluidMode,
            player
        ));
    }

    @Override
    public EntityHitResult entityRaycast(Level level, Player player, ClipContext.Fluid fluidMode, float distance) {
        Vec3 start = player.getEyePosition(1.0F);
        Vec3 look = player.getLookAngle();
        Vec3 end = start.add(look.scale(distance));

        AABB box = player.getBoundingBox().expandTowards(look.scale(distance)).inflate(1.0D);

        return ProjectileUtil.getEntityHitResult(
            level,
            player,
            start,
            end,
            box,
            entity -> entity.isAlive()
        );
    }

    @Override
    public void upgrade() {

    }

    @Override
    public void cycleSpells(int skip) {
        if (spells.size() <= 1) {
            return;
        }
        currentSpellIndex = (currentSpellIndex + skip) % spells.size();
    }

    @Override
    public void nextSpell() {
        cycleSpells(1);
    }

    @Override
    public void previousSpell() {
        cycleSpells(-1);
    }

    @Override
    public void handleScrollCycling(float scrollDeltaX, float scrollDeltaY) {
        if (scrollDeltaY > 0) {
            nextSpell();
        } else if (scrollDeltaY < 0) {
            previousSpell();
        }
    }

    @Override
    public SpellDataModel getCurrentSpell() {
        return spells.get(currentSpellIndex);
    }

    @Override
    public void setSpell(SpellDataModel spell, int index) {
        if (index < 0 || index >= spells.size() || spell == null) {
            LOGGER.error("[PrismaArcanum] Tried to set invalid spell data, or with an invalid index! This is not allowed.");
            return;
        }
        try {
            spells.set(index, spell);
        } catch (Exception e) {
            LOGGER.error("[PrismaArcanum/Exception] Error while trying to set spell data. Exception:\n" + e.getLocalizedMessage());
        }
    }

    @Override
    public void setSpells(List<SpellDataModel> spellsList) {
        spells = spellsList;
    }
}
