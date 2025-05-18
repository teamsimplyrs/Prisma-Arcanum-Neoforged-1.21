package com.teamsimplyrs.prismaarcanum.system.spellsystem.components;

import com.teamsimplyrs.prismaarcanum.system.spellsystem.data.SpellDataModel;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.interfaces.ISpellComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class DashComponent implements ISpellComponent {
    public float distance;
    public float speed;

    public DashComponent(float distance, float speed) {
        this.distance = distance;
        this.speed = speed;
    }

    @Override
    public void execute(Player player, Level level, SpellDataModel spellData) {

    }
}
