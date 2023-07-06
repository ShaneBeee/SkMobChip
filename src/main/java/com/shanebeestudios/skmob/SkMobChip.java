package com.shanebeestudios.skmob;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class SkMobChip extends JavaPlugin {

    private static SkMobChip INSTANCE;

    @Override
    public void onEnable() {
        if (INSTANCE == null) {
            INSTANCE = this;
        } else {
            //error
        }

        SkriptAddon skriptAddon = Skript.registerAddon(this);
        try {
            skriptAddon.setLanguageFileDirectory("lang");
            skriptAddon.loadClasses("com.shanebeestudios.skmob.elements");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        INSTANCE = null;
    }

    public static SkMobChip getPluginInstance() {
        return INSTANCE;
    }

}
