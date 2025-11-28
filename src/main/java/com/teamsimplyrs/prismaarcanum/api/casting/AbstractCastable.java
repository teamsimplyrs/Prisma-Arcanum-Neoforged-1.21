package com.teamsimplyrs.prismaarcanum.api.casting;

import com.mojang.logging.LogUtils;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.casting.interfaces.ICastable;
import com.teamsimplyrs.prismaarcanum.api.casting.interfaces.IMultiSpellHolder;
import com.teamsimplyrs.prismaarcanum.api.spell.registry.SpellRegistry;
import com.teamsimplyrs.prismaarcanum.api.spell.spells.common.AbstractSpell;
import com.teamsimplyrs.prismaarcanum.api.utils.WandUtils;
import com.teamsimplyrs.prismaarcanum.component.PADataComponents;
import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.animation.layered.modifier.MirrorIfLeftHandModifier;
import com.zigythebird.playeranim.api.PlayerAnimationAccess;
import com.zigythebird.playeranimcore.api.firstPerson.FirstPersonConfiguration;
import com.zigythebird.playeranimcore.api.firstPerson.FirstPersonMode;
import net.minecraft.client.player.AbstractClientPlayer;
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

        if (spells == null || spells.isEmpty() || currentSpellIndex == null || currentSpellIndex >= spells.size()) {
            LOGGER.error("[Prisma Arcanum / Exception]: (Wand) Spells list is null or empty, or spell index is greater than list size");
            return super.use(level, player, usedHand);
        }

        var currentSpell = SpellRegistry.getSpell(WandUtils.getCurrentSpell(stack));

        ResourceLocation spellAnimationLocation = currentSpell.getAnimationLocation();
        //Animation player
        if (level.isClientSide() && spellAnimationLocation != null) {
            PlayerAnimationController controller = (PlayerAnimationController) PlayerAnimationAccess.getPlayerAnimationLayer(
                    (AbstractClientPlayer) player, ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "spell_caster"));
            assert controller != null;
            controller.setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL);
            controller.addModifier(new MirrorIfLeftHandModifier(), 0);

            FirstPersonConfiguration config = new FirstPersonConfiguration(true, true, true, true, false);
            controller.setFirstPersonConfiguration(config);
            controller.triggerAnimation(spellAnimationLocation);
        }
        int spellDelay = currentSpell.getStartDelay();

        //Spell delay and casting handler (makes cast redundant)
        if (!level.isClientSide()) {

            if (stack.get(PADataComponents.SPELL_DELAY) == null) {
                stack.set(PADataComponents.SPELL_DELAY, spellDelay);
                stack.set(PADataComponents.PENDING_SPELL, spells.get(currentSpellIndex));
            }
            //  cast(level, player, spells.get(currentSpellIndex));
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


    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

        if (level.isClientSide || !isSelected || !(entity instanceof Player player)) return;

        Integer delay = stack.get(PADataComponents.SPELL_DELAY.get());
        ResourceLocation pendingSpell = stack.get(PADataComponents.PENDING_SPELL.get());

        if (delay == null || pendingSpell == null) return;

        if (delay > 0) {
            stack.update(PADataComponents.SPELL_DELAY, 0, (d) -> d - 1);
            //stack.set(PADataComponents.SPELL_DELAY.get(), delay - 1);
        } else {
            // delay expired — perform cast
            AbstractSpell spell = SpellRegistry.getSpell(pendingSpell);
            if (spell != null) {
                spell.tryCast(level, player);
            }

            // Clear data so it does not cast again
            stack.remove(PADataComponents.SPELL_DELAY.get());
            stack.remove(PADataComponents.PENDING_SPELL.get());
        }
    }

    @Override
    public void upgrade() {

    }
}
