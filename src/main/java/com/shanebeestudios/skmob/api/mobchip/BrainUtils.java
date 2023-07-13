package com.shanebeestudios.skmob.api.mobchip;

import me.gamercoder215.mobchip.EntityBrain;
import me.gamercoder215.mobchip.ai.EntityAI;
import me.gamercoder215.mobchip.bukkit.BukkitBrain;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.Nullable;

public class BrainUtils {

    @Nullable
    public static EntityBrain getBrain(Object object) {
        if (object instanceof EntityBrain entityBrain) return entityBrain;
        if (object instanceof EnderDragon enderDragon) return BukkitBrain.getBrain(enderDragon);
        else if (object instanceof Mob mob) return BukkitBrain.getBrain(mob);
        return null;
    }

    @Nullable
    public static EntityAI getGoalAI(Object object) {
        if (object instanceof EntityAI entityAI) return entityAI;
        EntityBrain brain = getBrain(object);
        if (brain != null) return brain.getGoalAI();
        return null;
    }

    @Nullable
    public static EntityAI getTargetAI(Object object) {
        if (object instanceof EntityAI entityAI) return entityAI;
        EntityBrain brain = getBrain(object);
        if (brain != null) return brain.getTargetAI();
        return null;
    }

}
