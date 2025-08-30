package com.teamsimplyrs.prismaarcanum.item.debug;

import com.mojang.logging.LogUtils;
import com.teamsimplyrs.prismaarcanum.system.castingsystem.interfaces.ICastable;
import com.teamsimplyrs.prismaarcanum.system.castingsystem.interfaces.IMultiSpellHolder;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.registry.SpellRegistry;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.spells.common.AbstractSpell;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;

import java.util.List;

public class DebugWand extends Item implements ICastable, IMultiSpellHolder {
    public static final String name = "debug_wand";
    public static final Item.Properties properties = new Item.Properties();

    private static final Logger LOGGER = LogUtils.getLogger();

    protected List<AbstractSpell> spells;
    private int currentSpellIndex;

    public DebugWand() {
        super(properties);
        loadSpells();
    }

    public void loadSpells() {
        setSpells(SpellRegistry.getAllSpells());
        if (spells != null && !spells.isEmpty()) {
            setSpell(spells.getFirst(), 0);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (spells.isEmpty()) {
            loadSpells();
        }

        if (!level.isClientSide()) {
            LOGGER.info("Debug Wand used");
            if (spells == null || spells.isEmpty() || currentSpellIndex >= spells.size()) {
                LOGGER.error("[Prisma Arcanum / Exception]: (Debug Wand) Spells list is null or empty, or spell index is greater than list size");
                return super.use(level, player, usedHand);
            }

            cast(level, player, spells.get(currentSpellIndex));
        }

        return super.use(level, player, usedHand);
    }

    @Override
    public void cast(Level world, Player player, AbstractSpell spell) {
        // HitResult hit = raycast(world, player, ClipContext.Fluid.NONE, 20f);
        player.sendSystemMessage(Component.literal("Called cast for spell: " + spell.getDisplayName()));
        spell.tryCast(world, player);
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
    public AbstractSpell getCurrentSpell() {
        if (currentSpellIndex < 0 || spells == null || spells.isEmpty() || currentSpellIndex >= spells.size()) {
            return null;
        }
        return spells.get(currentSpellIndex);
    }

    @Override
    public void setSpell(AbstractSpell spell, int index) {
        if (index < 0 || index >= spells.size() || spell == null) {
            LOGGER.error("[PrismaArcanum / Exception] Tried to set invalid spell data, or with an invalid index! This is not allowed.");
            return;
        }
        try {
            spells.set(index, spell);
        } catch (Exception e) {
            LOGGER.error("[PrismaArcanum / Exception] Error while trying to set spell data. Exception:\n" + e.getLocalizedMessage());
        }
    }

    @Override
    public void setSpells(List<AbstractSpell> spellsList) {
        spells = spellsList;
    }
}
