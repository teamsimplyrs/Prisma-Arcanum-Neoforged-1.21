package com.teamsimplyrs.prismaarcanum.system.spellsystem.registry;

import com.teamsimplyrs.prismaarcanum.system.spellsystem.data.SpellDataModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SpellRegistry {
    public static Map<String, SpellDataModel> SPELL_MAP = new HashMap<String, SpellDataModel>();
    public static ArrayList<String> PATHS = new ArrayList<String>();

    public static void initalize() {

    }

    public static void initializePaths() {
        PATHS.add("");
    }
}
