package com.teamsimplyrs.prismaarcanum.registry;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.item.SpellPrismItem;
import com.teamsimplyrs.prismaarcanum.item.debug.DebugWand;
import com.teamsimplyrs.prismaarcanum.item.wands.IgnisWand;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class PAItemRegistry {
    public static final DeferredRegister.Items ITEM_REGISTER = DeferredRegister.createItems(PrismaArcanum.MOD_ID);

    /// Register items here
    public static final DeferredItem<Item> DEBUG_WAND = ITEM_REGISTER.register(DebugWand.name, DebugWand::new);
    public static final DeferredItem<Item> SPELL_PRISM_ITEM = ITEM_REGISTER.register(SpellPrismItem.NAME, SpellPrismItem::new);
    public static final DeferredItem<Item> IGNIS_WAND = ITEM_REGISTER.register(IgnisWand.NAME, IgnisWand::new);

    /// ===== MOB SPAWN EGGS =====
    public static final DeferredItem<Item> RIPPLE_SEEKER_SPAWN_EGG = ITEM_REGISTER.register("ripple_seeker_spawn_egg",
            () -> new DeferredSpawnEggItem(PAEntityRegistry.RIPPLE_SEEKER, 0x18558d, 0xcba36a,
                    new Item.Properties()));

    public static final DeferredItem<Item> IGNIUM_LEGIONNAIRE_SPAWN_EGG = ITEM_REGISTER.register("ignium_legionnaire_spawn_egg",
            () -> new DeferredSpawnEggItem(PAEntityRegistry.IGNIUM_LEGIONNAIRE, 0x8f4f29, 0xffaf38,
                    new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEM_REGISTER.register(eventBus);
    }
}
