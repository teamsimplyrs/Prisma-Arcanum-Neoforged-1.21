package com.teamsimplyrs.prismaarcanum.api.casting.interfaces;

import com.teamsimplyrs.prismaarcanum.api.spell.spells.common.AbstractSpell;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface ICastable {
    public void cast(Level world, Player player, ResourceLocation spellID, int chargedTicks);

    public void upgrade();
}
