package com.teamsimplyrs.prismaarcanum.registry;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.component.PADataComponents;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.data.model.SpellDataModel;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.registry.SpellRegistry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.spongepowered.asm.mixin.injection.struct.InjectorGroupInfo;

import java.util.Map;
import java.util.function.Supplier;

public class PACreativeTabsRegistry {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TAB_REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PrismaArcanum.MOD_ID);

    public static final Supplier<CreativeModeTab> PA_PRISMATIC_TOOLS_TAB = CREATIVE_TAB_REGISTRY.register("prismatic_tools_tab", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(PAItemRegistry.DEBUG_WAND.get()))
            .title(Component.translatable("creativetab.prismaarcanum.prismatic_tools"))
            .displayItems((itemDisplayParameters, output) -> {
                output.accept(PAItemRegistry.DEBUG_WAND);
                output.accept(PAItemRegistry.SPELL_PRISM_ITEM);

                for (SpellDataModel spellData : SpellRegistry.getAllSpellData()) {
                    ItemStack spellPrismItemInstance = new ItemStack(PAItemRegistry.SPELL_PRISM_ITEM.get());
                    // Set bound spell ID resource location
                    spellPrismItemInstance.set(PADataComponents.SPELL_ID, spellData.id);
                    // Set display name
                    spellPrismItemInstance.set(
                            DataComponents.ITEM_NAME,
                            Component.literal(spellData.spell_display_name)
                    );
                    output.accept(spellPrismItemInstance);
                }
            }).build());
    public static void register(IEventBus eventBus) {
        CREATIVE_TAB_REGISTRY.register(eventBus);
    }
}
