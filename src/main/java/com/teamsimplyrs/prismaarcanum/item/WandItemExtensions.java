package com.teamsimplyrs.prismaarcanum.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public class WandItemExtensions implements IClientItemExtensions {
    WandRenderer renderer = new WandRenderer();

    @Override
    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return renderer;
    }
}
