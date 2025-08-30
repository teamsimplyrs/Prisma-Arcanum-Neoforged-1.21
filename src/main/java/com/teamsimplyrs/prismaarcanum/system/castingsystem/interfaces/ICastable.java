package com.teamsimplyrs.prismaarcanum.system.castingsystem.interfaces;

import com.teamsimplyrs.prismaarcanum.system.spellsystem.spells.common.AbstractSpell;
import com.teamsimplyrs.prismaarcanum.system.spellsystem_deprecated.data.model.SpellDataModel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public interface ICastable {
    public void cast(Level world, Player player, AbstractSpell spell);

    public void upgrade();
}
