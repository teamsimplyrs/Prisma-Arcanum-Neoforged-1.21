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

    public ChainComponent(int hops, float radius_x, float radius_y, float radius_z, float effect_dropoff) {
        this.hops = hops;
        this.radius_x = radius_x;
        this.radius_y = radius_y;
        this.radius_z = radius_z;
        this.effect_dropoff = effect_dropoff;
    }

    @Override
    public void execute(Player player, Level level, SpellDataModel spellData) {

    }
}
