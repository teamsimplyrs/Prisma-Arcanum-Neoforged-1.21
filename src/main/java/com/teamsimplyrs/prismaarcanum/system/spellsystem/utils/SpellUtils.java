package com.teamsimplyrs.prismaarcanum.system.spellsystem.utils;

public class SpellUtils {
    public static class SpellPropertyConstants {
        public static final String ID = "id";
        public static final String TIER = "tier";
        public static final String BASE_MANA_COST = "base_mana_cost";
        public static final String COOLDOWN = "cooldown";

        public static final String SPELL_DISPLAY_NAME = "spell_display_name";
        public static final String SPELL_DESCRIPTION = "spell_description";

        public static final String ELEMENT = "element";
        public static final String SCHOOL = "school";

        public static final String HAS_EVOLUTION = "has_evolution";
        public static final String SPELL_ACTIONS = "spell_actions";

        public static final String SPELL_VFX = "vfx";
        public static final String SPELL_PARTICLES = "particles";
    }

    public static class SpellActionNames {
        public static final String AREA_OF_EFFECT = "aoe";
        public static final String BEAM = "beam";
        public static final String CHAIN_EFFECT = "chain";
        public static final String DAMAGE_INSTANT = "damage_instant";
        public static final String DAMAGE_OVER_TIME = "damage_over_time";
        public static final String DASH = "dash";
        public static final String EMPTY = "empty";
        public static final String HEAL_INSTANT = "heal_instant";
        public static  final String HEAL_OVER_TIME = "heal_over_time";
        public static final String INFLICTION = "infliction";
        public static final String KNOCKBACK = "knockback";
        public static final String PARTICLE_EFFECT = "particles";
    }

    public static class SpellActionProperties {
        public static final String TYPE = "type";
        public static final String DISTANCE = "distance";
        public static final String SPEED = "speed";
        public static final String RADIUS_X = "radius_x";
        public static final String RADIUS_Y = "radius_y";
        public static final String RADIUS_Z = "radius_z";
        public static final String WIDTH = "width";
        public static final String HEIGHT = "height";
        public static final String RANGE = "range";
        public static final String AMOUNT = "amount";
        public static final String DURATION = "duration";
        public static final String INTERVAL = "interval";
        public static final String HOPS = "hops";
        public static final String EFFECT_DROPOFF = "effect_dropoff";
        public static final String STRENGTH = "strength";
        public static final String HIT_ACTION = "hit_action";
        public static final String COUNT = "count";
        public static final String IS_BLOCKING = "is_blocking";
    }
}