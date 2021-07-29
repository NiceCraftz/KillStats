package me.alexmc.killstats.utils;

import org.bukkit.ChatColor;

public class ColorAPI {
    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
