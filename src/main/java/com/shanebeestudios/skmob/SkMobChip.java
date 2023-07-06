package com.shanebeestudios.skmob;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import com.shanebeestudios.skmob.config.Config;
import com.shanebeestudios.skmob.util.UpdateChecker;
import com.shanebeestudios.skmob.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

@SuppressWarnings({"deprecation", "unused"})
public class SkMobChip extends JavaPlugin {

    private static SkMobChip INSTANCE;
    private Config pluginConfig;

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        Utils.log("Starting up ...");

        // Load plugin config
        this.pluginConfig = new Config(this);

        // Check if instance is already set
        if (INSTANCE == null) {
            INSTANCE = this;
        } else {
            Utils.error("Cannot create another instance of SkMobChip!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // Disable plugin if can't load Skript addon
        if (!loadSkriptAddon()) Bukkit.getPluginManager().disablePlugin(this);

        // Check for update
        checkUpdate();

        // Finish up
        String version = getDescription().getVersion();
        Utils.log("&aSuccessfully enabled v%s&7 in &b%.2f seconds", version, (float) (System.currentTimeMillis() - start) / 1000);
    }

    private boolean loadSkriptAddon() {
        Plugin skriptPlugin = Bukkit.getPluginManager().getPlugin("Skript");

        // Check if can load
        if (skriptPlugin == null) {
            Utils.log("&cDependency Skript was not found, plugin disabling.");
            return false;
        }

        if (!skriptPlugin.isEnabled()) {
            Utils.log("&cDependency Skript is not enabled, plugin disabling.");
            Utils.log("&cThis could mean SkMobChip is being forced to load before Skript.");
            return false;
        }

        if (!Skript.isAcceptRegistrations()) {
            // SkMobChip should be loading right after Skript, during Skript's registration period
            // If a plugin is delaying SkMobChip's loading, this causes issues with registrations and no longer works
            // We need to find the route of this issue, so far the only plugin I know that does this is PlugMan
            Utils.log("&cSkript is no longer accepting registrations, addons can no longer be loaded!");
            if (Bukkit.getPluginManager().getPlugin("PlugMan") != null) {
                Utils.log("&cIt appears you're running PlugMan.");
                Utils.log("&cIf you're trying to reload/enable SkMobChip with PlugMan.... you can't.");
                Utils.log("&ePlease restart your server!");
            } else {
                Utils.log("&cNo clue how this could happen.");
                Utils.log("&cSeems a plugin is delaying SkMobChip loading, which is after Skript stops accepting registrations.");
            }
            return false;
        }

        // Start loading addon
        int[] elementCountBefore = Utils.getSkriptElementCount();
        SkriptAddon skriptAddon = Skript.registerAddon(this);
        try {
            skriptAddon.setLanguageFileDirectory("lang");
            skriptAddon.loadClasses("com.shanebeestudios.skmob.elements");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int[] elementCountAfter = Utils.getSkriptElementCount();
        int[] finish = new int[elementCountBefore.length];
        for (int i = 0; i < elementCountBefore.length; i++) {
            finish[i] = elementCountAfter[i] - elementCountBefore[i];
        }
        String[] elementNames = new String[]{"event", "effect", "expression", "condition", "section"};

        Utils.log("Loaded elements:");
        for (int i = 0; i < finish.length; i++) {
            Utils.log(" - %s %s%s", finish[i], elementNames[i], finish[i] == 1 ? "" : "s");
        }
        return true;
    }

    private void checkUpdate() {
        if (pluginConfig.SETTINGS_UPDATE_CHECKER) {
            Bukkit.getPluginManager().registerEvents(new UpdateChecker(this), this);
        } else {
            Utils.log("Update checker disabled... will not check for update!");
        }
    }

    @Override
    public void onDisable() {
        INSTANCE = null;
    }

    public static SkMobChip getPlugin() {
        return INSTANCE;
    }

    public Config getPluginConfig() {
        return pluginConfig;
    }

}
