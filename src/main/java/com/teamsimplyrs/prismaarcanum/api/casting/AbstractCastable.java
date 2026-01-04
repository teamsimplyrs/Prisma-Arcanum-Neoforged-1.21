package com.teamsimplyrs.prismaarcanum.api.casting;

import com.lowdragmc.photon.client.fx.EntityEffectExecutor;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXHelper;
import com.mojang.logging.LogUtils;
import com.teamsimplyrs.prismaarcanum.api.casting.interfaces.ICastable;
import com.teamsimplyrs.prismaarcanum.api.casting.interfaces.IMultiSpellHolder;
import com.teamsimplyrs.prismaarcanum.api.spell.registry.SpellRegistry;
import com.teamsimplyrs.prismaarcanum.api.spell.spells.common.AbstractSpell;
import com.teamsimplyrs.prismaarcanum.api.utils.SpellUtils;
import com.teamsimplyrs.prismaarcanum.api.utils.WandUtils;
import com.teamsimplyrs.prismaarcanum.component.PADataComponents;
import com.teamsimplyrs.prismaarcanum.registry.PADataAttachmentsRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;

import java.util.ArrayList;

public class AbstractCastable extends Item implements ICastable, IMultiSpellHolder {
    protected static final Logger LOGGER = LogUtils.getLogger();
    protected int maxSpells = 5;

    public AbstractCastable(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        var spells = stack.get(PADataComponents.SPELLS_BOUND.get());
        Integer currentSpellIndex = stack.get(PADataComponents.CURRENT_SPELL_INDEX.get());

        if (!level.isClientSide()) {
            AbstractSpell currentSpell = WandUtils.getCurrentSpell(stack);
            if (currentSpell == null) {
                LOGGER.error("[Prisma Arcanum / Exception]: (AbstractCastable::use) Could not get current spell ID for stack: {} (fetched spell ID is null)!", stack);
                return super.use(level, player, usedHand);
            }

            if (currentSpell.allowsCharging()) {
                player.startUsingItem(usedHand);
                return InteractionResultHolder.consume(stack);
            } else {
                cast(level, player, spells.get(currentSpellIndex), 0);
            }
        }

        return super.use(level, player, usedHand);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
        super.releaseUsing(stack, level, livingEntity, timeCharged);

        if (!(livingEntity instanceof Player player)) return;

        if (!level.isClientSide) {
            var currentSpellID = WandUtils.getCurrentSpellID(stack);

            if (currentSpellID == null) {
                LOGGER.error("[Prisma Arcanum / Exception]: (AbstractCastable::releaseUsing) Could not get current spell ID for stack: {} (fetched spell ID is null)!", stack);
                return;
            }

            cast(level, player, currentSpellID, timeCharged);
        }
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        super.onUseTick(level, livingEntity, stack, remainingUseDuration);

        if (level.isClientSide) {
            AbstractSpell currentSpell = WandUtils.getCurrentSpell(stack);

            if (currentSpell == null) {
                LOGGER.error("[Prisma Arcanum / Exception]: (AbstractCastable::onUseTick) Could not get current spell for stack: {} (fetched spell data is null)!", stack);
                return;
            }

            var chargingFX = currentSpell.getChargingFXid();
            if (chargingFX != null) {
                FX fx  = FXHelper.getFX(chargingFX);
                if (fx == null) {
                    LOGGER.error("[Prisma Arcanum / Exception]: (AbstractCastable::onUseTick) Could not get charging FX for fx id: {} !", chargingFX);
                    return;
                }

                EntityEffectExecutor fxExec = new EntityEffectExecutor(fx, level, livingEntity, EntityEffectExecutor.AutoRotate.LOOK);
                // TO DO: fxExec.start only if it hasn't started yet -> might need a player data attachment for this.
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    public int getMaxSpells() {
        return maxSpells;
    }

    @Override
    public void cast(Level world, Player player, ResourceLocation spellID, int chargedTicks) {
        AbstractSpell spell = SpellRegistry.getSpell(spellID);
        if (spell == null) {
            LOGGER.error("[Prisma Arcanum] (AbstractCastable::cast) User tried to cast a null spell: Spell was not found in the registry");
            return;
        }

        spell.tryCast(world, player);
    }

    @Override
    public void upgrade() {

    }
}
