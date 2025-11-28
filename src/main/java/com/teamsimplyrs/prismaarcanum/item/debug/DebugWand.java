package com.teamsimplyrs.prismaarcanum.item.debug;

import com.teamsimplyrs.prismaarcanum.api.casting.AbstractCastable;
import net.minecraft.resources.ResourceLocation;
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

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        return super.use(level, player, usedHand);
    }

    @Override
    public void cast(Level world, Player player, ResourceLocation spellID) {
        super.cast(world, player, spellID);
    }

    @Override
    public void upgrade() {

    }
}
