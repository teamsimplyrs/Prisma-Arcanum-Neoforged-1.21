package com.teamsimplyrs.prismaarcanum.system.spellsystem.components;

import com.teamsimplyrs.prismaarcanum.system.spellsystem.data.model.SpellDataModel;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.interfaces.ISpellComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class AOEComponent implements ISpellComponent {
    public float radius_x;
    public float radius_y;
    public float radius_z;
    public ISpellComponent hit_component;

    public AOEComponent(float radius_x, float radius_y, float radius_z, ISpellComponent hit_component) {
        this.radius_x = radius_x;
        this.radius_y = radius_y;
        this.radius_z = radius_z;
        this.hit_component = hit_component;
    }

    @Override
    public void execute(Player player, Level level, SpellDataModel spellData) {

    }
}
