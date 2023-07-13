package com.shanebeestudios.skmob.api.util;

import ch.njol.skript.Skript;
import ch.njol.skript.log.ErrorQuality;
import com.shanebeestudios.skmob.SkMobChip;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"UsagesOfObsoleteApi", "deprecation"})
public class Utils {

    private static final String PREFIX = "&7[&bSk&3MobChip&7] ";
    private static final String PREFIX_ERROR = "&7[&bSk&3MobChip &cERROR&7] ";
    private static final Pattern HEX_PATTERN = Pattern.compile("<#([A-Fa-f\\d]){6}>");
    private static final boolean SKRIPT_IS_THERE = Bukkit.getPluginManager().getPlugin("Skript") != null;

    public static String getColString(String string) {
        Matcher matcher = HEX_PATTERN.matcher(string);
        if (SKRIPT_IS_THERE) {
            while (matcher.find()) {
                final ChatColor hexColor = ChatColor.of(matcher.group().substring(1, matcher.group().length() - 1));
                final String before = string.substring(0, matcher.start());
                final String after = string.substring(matcher.end());
                string = before + hexColor + after;
                matcher = HEX_PATTERN.matcher(string);
            }
        } else {
            string = HEX_PATTERN.matcher(string).replaceAll("");
        }
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static void sendColMsg(CommandSender receiver, String format, Object... objects) {
        receiver.sendMessage(getColString(String.format(format, objects)));
    }

    public static void log(String format, Object... objects) {
        String log = String.format(format, objects);
        Bukkit.getConsoleSender().sendMessage(getColString(PREFIX + log));
    }

    public static void error(String format, Object... objects) {
        String log = String.format(format, objects);
        Bukkit.getConsoleSender().sendMessage(getColString(PREFIX_ERROR + log));
    }

    public static void skriptError(String format, Object... objects) {
        String error = String.format(format, objects);
        Skript.error(getColString(PREFIX_ERROR + error), ErrorQuality.SEMANTIC_ERROR);
    }

    public static void debug(String format, Object... objects) {
        if (SkMobChip.getPlugin().getPluginConfig().SETTINGS_DEBUG) {
            String debug = String.format(format, objects);
            Bukkit.getConsoleSender().sendMessage(getColString(PREFIX_ERROR + debug));
        }
    }

    /**
     * Get counts of loaded Skript elements
     * <br>
     * In order events, effects, expressions, conditions, sections
     *
     * @return Counts of loaded Skript elements
     */
    public static int[] getSkriptElementCount() {
        int[] i = new int[5];

        i[0] = Skript.getEvents().size();
        i[1] = Skript.getEffects().size();
        AtomicInteger exprs = new AtomicInteger();
        Skript.getExpressions().forEachRemaining(e -> exprs.getAndIncrement());
        i[2] = exprs.get();
        i[3] = Skript.getConditions().size();
        i[4] = Skript.getSections().size();

        return i;
    }

}
