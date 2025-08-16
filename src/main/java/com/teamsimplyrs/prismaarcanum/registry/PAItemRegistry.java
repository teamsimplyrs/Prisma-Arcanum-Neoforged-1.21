package com.teamsimplyrs.prismaarcanum.registry;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.item.SpellPrismItem;
import com.teamsimplyrs.prismaarcanum.item.debug.DebugWand;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Debug;

public class PAItemRegistry {
    public static final DeferredRegister.Items ITEM_REGISTER = DeferredRegister.createItems(PrismaArcanum.MOD_ID);

    // Register items here
    public static final DeferredItem<Item> DEBUG_WAND = ITEM_REGISTER.register(DebugWand.name, DebugWand::new);
    public static final DeferredItem<Item> SPELL_PRISM_ITEM = ITEM_REGISTER.register(SpellPrismItem.NAME, SpellPrismItem::new);

    public static void register(IEventBus eventBus) {
        ITEM_REGISTER.register(eventBus);
    }
}
