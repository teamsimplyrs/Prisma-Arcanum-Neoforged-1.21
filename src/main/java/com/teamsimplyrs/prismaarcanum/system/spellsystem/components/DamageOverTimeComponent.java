package com.teamsimplyrs.prismaarcanum.system.spellsystem.components;

import com.teamsimplyrs.prismaarcanum.system.spellsystem.data.model.SpellDataModel;
import com.teamsimplyrs.prismaarcanum.system.spellsystem.interfaces.ISpellComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class DamageOverTimeComponent implements ISpellComponent {
    public float amount;
    public float duration;
    public float interval;

    public DamageOverTimeComponent(float amount, float duration, float interval) {
        this.amount = amount;
        this.duration = duration;
        this.interval = interval;
    }

    @Override
    public void execute(Player player, Level level, SpellDataModel spellData) {

    }
}
