package com.teamsimplyrs.prismaarcanum.api.spell.spells.mentis;

import com.teamsimplyrs.prismaarcanum.api.spell.spells.common.AbstractSpell;
import com.teamsimplyrs.prismaarcanum.api.utils.Element;
import com.teamsimplyrs.prismaarcanum.api.utils.School;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class MagicBullet extends AbstractSpell {
    public static final String spellID = "magic_bullet";
    public static final Element element = Element.Mentis;
    public static final School school = School.Occult;

    public static final int tier = 2;
    public static final int basicManaCost = 8;
    public static final int basicCooldown = 20;

    public static final boolean hasEvolution = true;

    private static final float baseDamage = 3f;

    public MagicBullet() {
        super(spellID, element, school, tier, basicManaCost, basicCooldown, hasEvolution);
    }

    @Override
    public void cast(ServerPlayer player, Level world) {

    }
}
