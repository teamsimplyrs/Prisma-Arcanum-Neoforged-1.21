package com.teamsimplyrs.prismaarcanum.registry;

import com.teamsimplyrs.prismaarcanum.api.casting.PlayerSpellCooldowns;
import com.teamsimplyrs.prismaarcanum.api.mana.PlayerChromana;
import com.teamsimplyrs.prismaarcanum.api.spell.states.EntitySpellControlStateComponent;
import com.teamsimplyrs.prismaarcanum.api.spell.states.EntitySpellControlStateInstance;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

import static com.teamsimplyrs.prismaarcanum.PrismaArcanum.MOD_ID;

public class PADataAttachmentsRegistry {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MOD_ID);

    public static final Supplier<AttachmentType<PlayerChromana>> CHROMANA = ATTACHMENT_TYPES.register(
            "chromana", () -> AttachmentType.builder(PlayerChromana::new)
                    .serialize(PlayerChromana.CODEC)
                    .copyOnDeath()
                    .build()
    );

    public static final Supplier<AttachmentType<PlayerSpellCooldowns>> SPELL_COOLDOWNS = ATTACHMENT_TYPES.register(
            "spell_cooldowns", () -> AttachmentType.builder(() -> new PlayerSpellCooldowns())
                    .serialize(PlayerSpellCooldowns.CODEC)
                    .copyOnDeath()
                    .build()
    );

    public static final Supplier<AttachmentType<EntitySpellControlStateComponent>> SPELL_CONTROL_STATE = ATTACHMENT_TYPES.register(
            "spell_control_state", () -> AttachmentType.builder(EntitySpellControlStateComponent::new).build()
    );

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
