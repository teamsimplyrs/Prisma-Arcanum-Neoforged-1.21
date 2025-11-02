package com.teamsimplyrs.prismaarcanum.api.utils;

import java.util.HashMap;
import java.util.Map;

import static java.util.Map.entry;

public class GuiUtils {
    public static int getTextColorForElement(Element element) {
        return switch (element) {
            case Aqua -> 0x2e55d9;
            case Ignis -> 0xde6b23;
            case Terra -> 0x522316;
            case Gelum -> 0x90f0e8;
            case Fulgur -> 0x8843de;
            case Ventus -> 0x3cab71;
            case Natura -> 0x1ec20c;
            case Mentis -> 0xe841cc;
            case Lux -> 0xe7f06e;
            case Nox -> 0x311b61;
        };
    }
}
