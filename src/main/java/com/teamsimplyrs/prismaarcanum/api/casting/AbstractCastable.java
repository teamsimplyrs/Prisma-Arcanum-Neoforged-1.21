package com.teamsimplyrs.prismaarcanum.api.casting;

import com.mojang.logging.LogUtils;
import com.teamsimplyrs.prismaarcanum.api.casting.interfaces.ICastable;
import com.teamsimplyrs.prismaarcanum.api.casting.interfaces.IMultiSpellHolder;
import com.teamsimplyrs.prismaarcanum.api.spell.registry.SpellRegistry;
import com.teamsimplyrs.prismaarcanum.api.spell.spells.common.AbstractSpell;
import com.teamsimplyrs.prismaarcanum.component.PADataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;

public class AbstractCastable extends Item implements ICastable, IMultiSpellHolder {
    protected static final Logger LOGGER = LogUtils.getLogger();
    protected int maxSpells = 5;

    public AbstractCastable(Properties properties) {
        super(properties);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        var spells = stack.get(PADataComponents.SPELLS_BOUND.get());
        Integer currentSpellIndex = stack.get(PADataComponents.CURRENT_SPELL_INDEX.get());

        if (!level.isClientSide()) {
            if (spells == null || spells.isEmpty() || currentSpellIndex == null || currentSpellIndex >= spells.size()) {
                LOGGER.error("[Prisma Arcanum / Exception]: (Wand) Spells list is null or empty, or spell index is greater than list size");
                return super.use(level, player, usedHand);
            }

            cast(level, player, spells.get(currentSpellIndex));
        }

        return super.use(level, player, usedHand);
    }

    public int getMaxSpells() {
        return maxSpells;
    }

    @Override
    public void cast(Level world, Player player, ResourceLocation spellID) {
        AbstractSpell spell = SpellRegistry.getSpell(spellID);
        if (spell == null) {
            LOGGER.error("[Prisma Arcanum] User tried to cast a null spell: Spell was not found in the registry");
            return;
        }

        spell.tryCast(world, player);
    }

    // TO-DO: Implement spell cast delay. Implement Data Component to store delay and tick down here.
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if(isSelected) {
            //LOGGER.info("Wand selected");
        }
    }

    @Override
    public void upgrade() {

    }
}
