package com.teamsimplyrs.prismaarcanum.system.spellsystem.components;

import com.teamsimplyrs.prismaarcanum.system.spellsystem.data.SpellDataModel;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.interfaces.ISpellComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ParticlesComponent implements ISpellComponent {
    public String type;
    public int count;

    public ParticlesComponent(String type, int count) {
        this.type = type;
        this.count = count;
    }

    @Override
    public void execute(Player player, Level level, SpellDataModel spellData) {

    }
}
