package com.teamsimplyrs.prismaarcanum.spells.natura;

import com.lowdragmc.photon.client.fx.EntityEffectExecutor;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXHelper;
import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.utils.Element;
import com.teamsimplyrs.prismaarcanum.api.utils.School;
import com.teamsimplyrs.prismaarcanum.spells.common.AbstractSpell;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class HealSpell extends AbstractSpell {
    public static final String SPELL_ID = "heal_spell";
    protected static final Element ELEMENT = Element.Natura;
    protected static final School SCHOOL = School.Continuity;
    protected static final int TIER = 1;

    protected static int basicManaCost = 20;
    protected static int basicCooldown = 70;

    protected static final boolean hasEvolution = true;

    public HealSpell() {
        super(ELEMENT, SCHOOL, TIER, basicManaCost, basicCooldown, 0, hasEvolution);
    }

    public ResourceLocation getResourceLocation() {
        return ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, SPELL_ID);
    }

    @Override
    public void onCastingStarted(Player player, Level world) {
        super.onCastingStarted(player, world);
        if (world.isClientSide) {
            FX fx = FXHelper.getFX(getFXid());
            EntityEffectExecutor fxExec = new EntityEffectExecutor(fx, world, player, EntityEffectExecutor.AutoRotate.NONE);
            fxExec.setOffset(0, -1.5f, 0);
            fxExec.start();
        }
    }

    @Override
    public void cast(ServerPlayer player, Level world) {
        super.cast(player, world);
        player.heal(6);
    }

    @Override
    public ResourceLocation getFXid() {
        return ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "heal_fx");
    }
}
