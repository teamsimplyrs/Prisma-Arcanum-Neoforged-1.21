package com.teamsimplyrs.prismaarcanum.item.debug;

import com.mojang.logging.LogUtils;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

public class DebugWand extends Item {
    public static final String name = "debug_wand";
    public static final Item.Properties properties = new Item.Properties();

    private static final Logger LOGGER = LogUtils.getLogger();

    public DebugWand() {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (level.isClientSide()) {
            LOGGER.info("Debug Wand used");
        }

        return super.use(level, player, usedHand);
    }
}
