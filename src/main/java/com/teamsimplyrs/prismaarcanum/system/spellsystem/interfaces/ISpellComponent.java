package com.teamsimplyrs.prismaarcanum.system.spellsystem.interfaces;

import com.teamsimplyrs.prismaarcanum.system.spellsystem.data.model.SpellDataModel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface ISpellComponent {
    void execute(Player player, Level level, SpellDataModel spellData);
}
