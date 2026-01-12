package com.teamsimplyrs.prismaarcanum.api.spell.states;

public enum EntitySpellControlState {
    // Default common states
    NONE,
    CHARGING,

    // Movement based states
    IMMOBILE,
    RISING,
    HOVERING,
    SLAMMING,
    DASHING,

    // Physics based states
    CUSTOM_GRAVITY,
    NO_GRAVITY,
    SWINGING,
    TIME_CONTROL,
}
