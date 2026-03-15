package com.teamsimplyrs.prismaarcanum.spells.mentis;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.spells.common.AbstractSpell;
import com.teamsimplyrs.prismaarcanum.api.utils.Element;
import com.teamsimplyrs.prismaarcanum.api.utils.School;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class MagicBullet extends AbstractSpell {
    public static final String SPELL_ID = "magic_bullet";
    public static final Element element = Element.Mentis;
    public static final School school = School.Occult;

    public static final int tier = 2;
    public static final int basicManaCost = 8;
    public static final int basicCooldown = 20;
    public static final int spellDelay = 0;

    public static final boolean hasEvolution = true;

    private static final float baseDamage = 3f;

    public MagicBullet() {
        super(element, school, tier, basicManaCost, basicCooldown, spellDelay, hasEvolution);
    }

    @Override
    public void cast(ServerPlayer player, Level world) {

    }

    @Override
    public ResourceLocation getResourceLocation() {
        return ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, SPELL_ID);
    }
}
