package com.teamsimplyrs.prismaarcanum.system.spellsystem.components;

import com.teamsimplyrs.prismaarcanum.system.spellsystem.data.SpellDataModel;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.interfaces.ISpellComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ChainComponent implements ISpellComponent {
    public int hops;
    public float radius_x;
    public float radius_y;
    public float radius_z;
    public float effect_dropoff;

    @Override
    public void execute(Player player, Level level, SpellDataModel spellData) {

    }
}
