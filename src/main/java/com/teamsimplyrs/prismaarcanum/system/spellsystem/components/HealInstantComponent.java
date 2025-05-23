package com.teamsimplyrs.prismaarcanum.system.spellsystem.components;

import com.teamsimplyrs.prismaarcanum.system.spellsystem.data.model.SpellDataModel;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.interfaces.ISpellComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class HealInstantComponent implements ISpellComponent {
    public float amount;

    public HealInstantComponent(float amount) {
        this.amount = amount;
    }

    @Override
    public void execute(Player player, Level level, SpellDataModel spellData) {

    }
}
