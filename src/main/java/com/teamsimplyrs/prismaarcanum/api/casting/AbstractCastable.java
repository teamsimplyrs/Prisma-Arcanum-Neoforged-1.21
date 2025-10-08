package com.teamsimplyrs.prismaarcanum.api.casting;

import com.mojang.logging.LogUtils;
import com.teamsimplyrs.prismaarcanum.api.casting.interfaces.ICastable;
import com.teamsimplyrs.prismaarcanum.api.casting.interfaces.IMultiSpellHolder;
import com.teamsimplyrs.prismaarcanum.api.spell.registry.SpellRegistry;
import com.teamsimplyrs.prismaarcanum.api.spell.spells.common.AbstractSpell;
import com.teamsimplyrs.prismaarcanum.component.PADataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class AbstractCastable extends Item implements ICastable, IMultiSpellHolder {
    protected List<ResourceLocation> spells;
    protected int currentSpellIndex;
    protected static final Logger LOGGER = LogUtils.getLogger();
    protected int maxSpells = 5;

    public AbstractCastable(Properties properties) {
        super(properties);
    }

    @Override
    public void cast(Level world, Player player, ResourceLocation spellID) {
        AbstractSpell spell = SpellRegistry.getSpell(spellID);
        if (spell == null) {
            LOGGER.error("[Prisma Arcanum] User tried to cast a null spell: Spell was not found in the reigstry");
        }
        spell.tryCast(world, player);
    }

    @Override
    public void upgrade() {

    }

    @Override
    public void cycleSpells(int skip) {
        if (spells == null || spells.size() <= 1) {
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
    public ResourceLocation getCurrentSpell() {
        if (spells == null || spells.isEmpty()) {
            return null;
        }

        if (currentSpellIndex >= spells.size() || currentSpellIndex < 0) {
            LOGGER.error("[Prisma Arcanum] Spell Index of given spell holding item is invalid.");
        }

        return spells.get(currentSpellIndex);
    }

    @Override
    public void setSpell(ResourceLocation spell, int index) {
        if (spells == null) {
            spells = new ArrayList<>(maxSpells);
        }

        if (spells.isEmpty()) {
            spells.add(spell);
            return;
        }

        spells.set(index, spell);
    }
}
