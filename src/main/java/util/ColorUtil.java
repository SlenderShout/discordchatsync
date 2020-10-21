package util;

import org.bukkit.ChatColor;

public class ColorUtil {

    private ColorUtil() { }

    public static String colorize(String message){
        return message =  ChatColor.translateAlternateColorCodes('&', message);
    }
}
