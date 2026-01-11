package com.teamsimplyrs.prismaarcanum.api.spell.spells.terra;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.spell.spells.common.AbstractSpell;
import com.teamsimplyrs.prismaarcanum.api.spell.states.EntitySpellControlState;
import com.teamsimplyrs.prismaarcanum.api.spell.states.EntitySpellControlStateComponent;
import com.teamsimplyrs.prismaarcanum.api.spell.states.EntitySpellControlStateInstance;
import com.teamsimplyrs.prismaarcanum.api.spell.states.data.HoverStateData;
import com.teamsimplyrs.prismaarcanum.api.spell.states.data.RisingStateData;
import com.teamsimplyrs.prismaarcanum.api.utils.Element;
import com.teamsimplyrs.prismaarcanum.api.utils.School;
import com.teamsimplyrs.prismaarcanum.registry.PADataAttachmentsRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class GroundPound extends AbstractSpell {
    public static final String SPELL_ID = "ground_pound";
    protected static final Element ELEMENT = Element.Terra;
    protected static final School SCHOOL = School.Mortality;
    protected static final int TIER = 1;

    protected static int basicManaCost = 20;
    protected static int basicCooldown = 70;

    protected static boolean hasEvolution = true;

    public GroundPound() {
        super(SPELL_ID, ELEMENT, SCHOOL, TIER, basicManaCost, basicCooldown, hasEvolution);
    }

    @Override
    public void cast(ServerPlayer player, Level world) {
        super.cast(player, world);
        EntitySpellControlStateComponent spellControlState = player.getData(PADataAttachmentsRegistry.SPELL_CONTROL_STATE.get());
        EntitySpellControlStateInstance hoveringState = new EntitySpellControlStateInstance(
                EntitySpellControlState.HOVERING,
                new HoverStateData(),
                40,
                0,
                null,
                this.getResourceLocation()
        );
        EntitySpellControlStateInstance risingToHoverState = new EntitySpellControlStateInstance(
                EntitySpellControlState.RISING,
                new RisingStateData(new Vec3(0f, 20f, 0f)),
                20,
                0,
                hoveringState,
                null
        );
        spellControlState.addState(risingToHoverState);
    }

    @Override
    public ResourceLocation getFXid() {
        return ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "ground_pound_fx");
    }
}
