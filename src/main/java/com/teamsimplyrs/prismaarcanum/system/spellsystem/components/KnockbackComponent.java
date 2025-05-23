package com.teamsimplyrs.prismaarcanum.system.spellsystem.components;

import com.teamsimplyrs.prismaarcanum.system.spellsystem.data.model.SpellDataModel;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.interfaces.ISpellComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class KnockbackComponent implements ISpellComponent {
    public float strength;

    public KnockbackComponent(float strength) {
        this.strength = strength;
    }

    @Override
    public void execute(Player player, Level level, SpellDataModel spellData) {

    }
}
