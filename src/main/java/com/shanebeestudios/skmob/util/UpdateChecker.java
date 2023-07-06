package com.shanebeestudios.skmob.util;

import ch.njol.skript.util.Version;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.shanebeestudios.skmob.SkMobChip;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class UpdateChecker implements Listener {

    private static Version UPDATE_VERSION;

    public static void checkForUpdate(String pluginVersion) {
        Utils.log("Checking for update...");
        getLatestReleaseVersion(version -> {
            Version plugVer = new Version(pluginVersion);
            Version curVer = new Version(version);
            if (curVer.compareTo(plugVer) <= 0) {
                Utils.log("&aPlugin is up to date!");
            } else {
                Utils.log("&cPlugin is not up to date!");
                Utils.log(" - Current version: &cv%s", pluginVersion);
                Utils.log(" - Available update: &av%s", version);
                Utils.log(" - Download available at: https://github.com/ShaneBeee/SkMobChip/releases");
                UPDATE_VERSION = curVer;
            }
        });
    }

    private static void getLatestReleaseVersion(final Consumer<String> consumer) {
        try {
            URL url = new URL("https://api.github.com/repos/ShaneBeee/SkMobChip/releases/latest");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            JsonObject jsonObject = new Gson().fromJson(reader, JsonObject.class);
            String tag_name = jsonObject.get("tag_name").getAsString();
            consumer.accept(tag_name);
        } catch (IOException e) {
            if (SkMobChip.getPlugin().getPluginConfig().SETTINGS_DEBUG) {
                e.printStackTrace();
            } else {
                Utils.log("&cChecking for update failed!");
            }
        }
    }

    private final SkMobChip plugin;

    @SuppressWarnings("deprecation")
    public UpdateChecker(SkMobChip plugin) {
        this.plugin = plugin;
        checkForUpdate(plugin.getDescription().getVersion());
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("skbee.update.check")) return;

        String currentVersion = this.plugin.getDescription().getVersion();
        CompletableFuture<Version> updateVersion = getUpdateVersion(currentVersion);

        Bukkit.getScheduler().runTaskLater(this.plugin, () -> updateVersion.thenApply(version -> {
            Utils.sendColMsg(player, "&7[&bSk&3MobChip&7] update available: &a" + version);
            Utils.sendColMsg(player, "&7[&bSk&3MobChip&7] download at &bhttps://github.com/ShaneBeee/SkMobChip/releases");
            return true;
        }), 30);
    }

    private CompletableFuture<Version> getUpdateVersion(String currentVersion) {
        CompletableFuture<Version> future = new CompletableFuture<>();
        if (UPDATE_VERSION != null) {
            future.complete(UPDATE_VERSION);
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> getLatestReleaseVersion(version -> {
                Version plugVer = new Version(currentVersion);
                Version curVer = new Version(version);
                if (curVer.compareTo(plugVer) <= 0) {
                    future.cancel(true);
                } else {
                    UPDATE_VERSION = curVer;
                    future.complete(UPDATE_VERSION);
                }
            }));
        }
        return future;
    }

}
