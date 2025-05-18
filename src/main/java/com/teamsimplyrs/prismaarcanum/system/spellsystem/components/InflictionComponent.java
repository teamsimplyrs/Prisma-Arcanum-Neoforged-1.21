package com.teamsimplyrs.prismaarcanum.system.spellsystem.components;

import com.teamsimplyrs.prismaarcanum.system.spellsystem.data.SpellDataModel;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.interfaces.ISpellComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class InflictionComponent implements ISpellComponent {
    public float duration;
    public ISpellComponent[] components;

    public InflictionComponent(float duration, ISpellComponent[] hit_components) {
        this.duration = duration;
        this.components = hit_components;
    }

    @Override
    public void execute(Player player, Level level, SpellDataModel spellData) {

    }
}
