package com.teamsimplyrs.prismaarcanum.api.spell.spells.mentis;

import com.teamsimplyrs.prismaarcanum.api.spell.spells.common.AbstractSpell;
import com.teamsimplyrs.prismaarcanum.api.utils.Element;
import com.teamsimplyrs.prismaarcanum.api.utils.School;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class MagicBullet extends AbstractSpell {
    public static final String spellID = "magic_bullet";
    public static final Element element = Element.Mentis;
    public static final School school = School.Occult;

    public static final int tier = 2;
    public static final float basicManaCost = 3f;
    public static final float basicCooldown = 1f;

    public static final boolean hasEvolution = true;
    public static final AbstractSpell prevolutionSpell = null;
    public static final AbstractSpell evolutionSpell = null;

    private static final float baseDamage = 3f;

    public MagicBullet() {
        super();
    }

    @Override
    public void cast(ServerPlayer player, Level world) {

    }
}
