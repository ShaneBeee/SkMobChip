package com.shanebeestudios.skmob.util;

import me.gamercoder215.mobchip.EntityBrain;
import me.gamercoder215.mobchip.bukkit.BukkitBrain;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.Nullable;

public class BrainUtils {

    @Nullable
    public static EntityBrain getBrain(Entity entity) {
        if (entity instanceof EnderDragon enderDragon) return BukkitBrain.getBrain(enderDragon);
        else if (entity instanceof Mob mob) return BukkitBrain.getBrain(mob);
        return null;
    }

    public static EntityBrain getBrain(Object object) {
        if (object instanceof EntityBrain entityBrain) return entityBrain;
        else if (object instanceof Entity entity) return getBrain(entity);
        return null;
    }

}
