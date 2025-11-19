package com.teamsimplyrs.prismaarcanum.registry;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.component.PADataComponents;
import com.teamsimplyrs.prismaarcanum.item.SpellPrismItem;
import com.teamsimplyrs.prismaarcanum.item.debug.DebugWand;
import com.teamsimplyrs.prismaarcanum.api.spell.registry.SpellRegistry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class PACreativeTabsRegistry {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TAB_REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PrismaArcanum.MOD_ID);

    public static final Supplier<CreativeModeTab> PA_PRISMATIC_TOOLS_TAB = CREATIVE_TAB_REGISTRY.register("prismatic_tools_tab", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(PAItemRegistry.DEBUG_WAND.get()))
            .title(Component.translatable("creativetab.prismaarcanum.prismatic_tools"))
            .displayItems((itemDisplayParameters, output) -> {
                ItemStack debugWandInstance = new ItemStack((DebugWand)PAItemRegistry.DEBUG_WAND.get());
                debugWandInstance.set(PADataComponents.SPELLS_BOUND, SpellRegistry.getAllSpellIDs());
                debugWandInstance.set(PADataComponents.CURRENT_SPELL_INDEX, 0);
                output.accept(debugWandInstance);

                output.accept(PAItemRegistry.IGNIS_WAND);

                for (ResourceLocation spellID : SpellRegistry.getAllSpellIDs()) {
                    ItemStack spellPrismItemInstance = new ItemStack(PAItemRegistry.SPELL_PRISM_ITEM.get());
                    // Set bound spell ID resource location
                    spellPrismItemInstance.set(PADataComponents.SPELL_ID, spellID);
                    // Set display name; in the format: Spell Prism (Spell Name)
                    spellPrismItemInstance.set(
                            DataComponents.ITEM_NAME,
                            Component.literal(String.format("%s (%s)",
                                    Component.translatable(String.format("item.%s.%s", PrismaArcanum.MOD_ID, SpellPrismItem.NAME)).getString(),
                                    SpellRegistry.getSpell(spellID).getDisplayName()
                            ))
                    );
                    output.accept(spellPrismItemInstance);
                }
            })
            .build());


    public static final Supplier<CreativeModeTab> PA_PRISMATIC_BLOCKS_TAB = CREATIVE_TAB_REGISTRY.register("prismatic_blocks_tab", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(PABlockRegistry.PRISM_FOCUS_BENCH))
            .withTabsBefore(ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "prismatic_tools_tab"))
            .title(Component.translatable("creativetab.prismaarcanum.prismatic_blocks"))
            .displayItems(((itemDisplayParameters, output) -> {
                output.accept(PABlockRegistry.PRISM_FOCUS_BENCH.get());
            }))
            .build());

    public static final Supplier<CreativeModeTab> PA_MONSTERS_TAB = CREATIVE_TAB_REGISTRY.register("pristmatic_mobs_tab", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(PAItemRegistry.RIPPLE_SEEKER_SPAWN_EGG.get()))
            .withTabsBefore(ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "prismatic_mobs_tab"))
            .title(Component.translatable("creativetab.prismaarcanum.prismatic_mobs"))
            .displayItems(((itemDisplayParameters, output) -> {
                output.accept(PAItemRegistry.RIPPLE_SEEKER_SPAWN_EGG.get());
            } ))
            .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_TAB_REGISTRY.register(eventBus);
    }
}
