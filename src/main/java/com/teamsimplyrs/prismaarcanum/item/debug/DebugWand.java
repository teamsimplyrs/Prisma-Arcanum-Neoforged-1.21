package com.teamsimplyrs.prismaarcanum.item.debug;

import com.mojang.logging.LogUtils;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
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
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
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
            cast(level, player, null);
        }

        return super.use(level, player, usedHand);
    }

    @Override
    public void cast(Level world, Player player, SpellDataModel spellData) {
        HitResult hit = raycast(world, player, ClipContext.Fluid.NONE, 20f);
        if (hit == null) {
            player.sendSystemMessage(Component.literal("Raycast miss"));
        } else {
            player.sendSystemMessage(Component.literal("Raycast hit: Result = " + hit.toString()));
        }
    }

    @Override
    public HitResult raycast(Level level, Player player, ClipContext.Fluid fluidMode, float distance) {
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
