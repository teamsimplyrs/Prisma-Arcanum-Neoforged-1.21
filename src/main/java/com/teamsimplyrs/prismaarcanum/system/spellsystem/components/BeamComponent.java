package com.teamsimplyrs.prismaarcanum.system.spellsystem.components;

import com.teamsimplyrs.prismaarcanum.system.spellsystem.data.model.SpellDataModel;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.interfaces.ISpellComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class BeamComponent implements ISpellComponent {
    public float width;
    public float height;
    public float range;
    public ISpellComponent hit_components;

    public BeamComponent(float width, float height, float range, ISpellComponent hit_component) {
        this.width = width;
        this.height = height;
        this.range = range;
        this.hit_components = hit_component;
    }

    @Override
    public void execute(Player player, Level level, SpellDataModel spellData) {

    }
}
