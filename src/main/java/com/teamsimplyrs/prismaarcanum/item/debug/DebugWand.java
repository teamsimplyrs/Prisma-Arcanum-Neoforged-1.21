package com.teamsimplyrs.prismaarcanum.item.debug;

import com.teamsimplyrs.prismaarcanum.api.casting.AbstractCastable;
import com.teamsimplyrs.prismaarcanum.api.spell.registry.SpellRegistry;
import com.teamsimplyrs.prismaarcanum.api.spell.spells.common.AbstractSpell;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DebugWand extends AbstractCastable {
    public static final String name = "debug_wand";
    public static final Item.Properties properties = new Item.Properties();


    public DebugWand() {
        super(properties);
    }

    public void loadSpells() {
        var allSpells = SpellRegistry.getAllSpells();
        setSpells(allSpells);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
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
        super.cast(world, player, spell);
    }

    @Override
    public void upgrade() {

    }
}
