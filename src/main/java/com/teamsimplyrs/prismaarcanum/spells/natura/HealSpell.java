package com.teamsimplyrs.prismaarcanum.spells.natura;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.spells.common.AbstractSpell;
import net.minecraft.resources.ResourceLocation;

public class HealSpell extends AbstractSpell {
    public static final String SPELL_ID = "heal_spell";
    
    public ResourceLocation getResourceLocation() {
        return ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, SPELL_ID);
    }
}
