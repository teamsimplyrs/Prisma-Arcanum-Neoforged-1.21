package com.teamsimplyrs.prismaarcanum.system.spellsystem.components;

import com.teamsimplyrs.prismaarcanum.system.spellsystem.data.model.SpellDataModel;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.interfaces.ISpellComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class InflictionComponent implements ISpellComponent {
    public float duration;
    public ISpellComponent component;

    public InflictionComponent(float duration, ISpellComponent hit_component) {
        this.duration = duration;
        this.component = hit_component;
    }

    @Override
    public void execute(Player player, Level level, SpellDataModel spellData) {

    }
}
