package com.teamsimplyrs.prismaarcanum.api.combo;

public enum ComboAttemptResult {
    INACTIVE,
    SUCCESS,
    INTERNAL_COOLDOWN,
    MAX_COMBO_CHAIN_REACHED,
    WINDOW_EXPIRED,
}
