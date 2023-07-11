package com.shanebeestudios.skmob.api.goal;

import ch.njol.skript.entity.EntityData;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.util.Timespan;
import ch.njol.util.StringUtils;
import me.gamercoder215.mobchip.ai.goal.*;
import me.gamercoder215.mobchip.ai.goal.target.PathfinderNearestAttackableTarget;
import org.bukkit.Difficulty;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@SuppressWarnings({"rawtypes", "unused"})
public abstract class GoalWrapper<P extends Pathfinder> {

    private static final Map<Class<? extends Pathfinder>, GoalWrapper> MAP_BY_CLASS = new HashMap<>();
    private static final Map<NamespacedKey, GoalWrapper> MAP_BY_KEY = new HashMap<>();

    // GOALS
    public static GoalWrapper<PathfinderAvoidEntity> AVOID_ENTITY_GOAL = new GoalWrapper<>(PathfinderAvoidEntity.class, "avoid_entity_goal") {
        @Override
        public String toSkriptString(PathfinderAvoidEntity pathfinder) {
            String entity = EntityData.toString(pathfinder.getEntity());
            //noinspection unchecked
            String filter = EntityData.toString(pathfinder.getFilter());
            return String.format("AvoidEntityGoal[mobType=%s,avoid=%s,walkMod=%s,sprintMod=%s,maxDistance=%s]",
                    entity, filter, pathfinder.getSpeedModifier(), pathfinder.getSprintModifier(),
                    pathfinder.getMaxDistance());
        }
    };

    public static GoalWrapper<PathfinderBreakDoor> BREAK_DOOR_GOAL = new GoalWrapper<>(PathfinderBreakDoor.class, "break_door_goal") {
        @Override
        public String toSkriptString(PathfinderBreakDoor pathfinder) {
            String entity = EntityData.toString(pathfinder.getEntity());
            String breaktime = new Timespan(pathfinder.getBreakTime()).toString();
            Predicate<Difficulty> condition = pathfinder.getCondition();
            List<String> difficulties = new ArrayList<>();
            for (Difficulty difficulty : Difficulty.values()) {
                if (condition.test(difficulty)) difficulties.add(Classes.toString(difficulty));
            }
            String diff = StringUtils.join(difficulties, ",");
            return String.format("BreakDoorGoal[mobType=%s,breakTime='%s',difficulties=[%s]]",
                    entity, breaktime, diff);
        }
    };

    public static GoalWrapper<PathfinderFleeSun> FLEE_SUN_GOAL = new GoalWrapper<>(PathfinderFleeSun.class, "flee_sun_goal") {
        @Override
        public String toSkriptString(PathfinderFleeSun pathfinder) {
            String entity = EntityData.toString(pathfinder.getEntity());
            return "FleeSunGoal[mobType=" + entity + "]";
        }
    };

    public static GoalWrapper<PathfinderFloat> FLOAT_GOAL = new GoalWrapper<>(PathfinderFloat.class, "float_goal") {
        @Override
        public String toSkriptString(PathfinderFloat pathfinder) {
            String entity = EntityData.toString(pathfinder.getEntity());
            return "FloatGoal[mobType=" + entity + "]";
        }
    };

    public static GoalWrapper<PathfinderPanic> PANIC_GOAL = new GoalWrapper<>(PathfinderPanic.class, "panic_goal") {
        @Override
        public String toSkriptString(PathfinderPanic pathfinder) {
            String entity = EntityData.toString(pathfinder.getEntity());
            double speed = pathfinder.getSpeedModifier();
            return String.format("PanicGoal[mobType=%s,speedMod=%s]", entity, speed);
        }
    };

    public static GoalWrapper<PathfinderRandomLook> RANDOM_LOOK_AROUND_GOAL = new GoalWrapper<>(PathfinderRandomLook.class, "random_look_around_goal") {
        @Override
        public String toSkriptString(PathfinderRandomLook pathfinder) {
            String entity = EntityData.toString(pathfinder.getEntity());
            return "RandomLookAroundGoal[mobType=" + entity + "]";
        }
    };

    public static GoalWrapper<PathfinderRandomStroll> RANDOM_STROLL_GOAL = new GoalWrapper<>(PathfinderRandomStroll.class, "random_stroll_goal") {
        @Override
        public String toSkriptString(PathfinderRandomStroll pathfinder) {
            String entity = EntityData.toString(pathfinder.getEntity());
            double speed = pathfinder.getSpeedModifier();
            String interval = new Timespan(pathfinder.getInterval()).toString();
            return String.format("RandomStrollGoal[mobType=%s,speedMod=%s,interval='%s']",
                    entity, speed, interval);
        }
    };

    public static GoalWrapper<PathfinderRestrictSun> RESTRICT_SUN_GOAL = new GoalWrapper<>(PathfinderRestrictSun.class, "restrict_sun_goal") {
        @Override
        public String toSkriptString(PathfinderRestrictSun pathfinder) {
            String entity = EntityData.toString(pathfinder.getEntity());
            return "RestrictSunGoal[mobType=" + entity + "]";
        }
    };

    public static GoalWrapper<PathfinderFindWater> TRY_FIND_WATER_GOAL = new GoalWrapper<>(PathfinderFindWater.class, "try_find_water_goal") {
        @Override
        public String toSkriptString(PathfinderFindWater pathfinder) {
            String entity = EntityData.toString(pathfinder.getEntity());
            return "TryFindWaterGoal[mobType=" + entity + "]";
        }
    };

    // TARGETS
    public static GoalWrapper<PathfinderNearestAttackableTarget> NEAREST_ATTACKABLE_TARGET_GOAL = new GoalWrapper<>(PathfinderNearestAttackableTarget.class, "nearest_attackable_target_goal") {
        @Override
        public String toSkriptString(PathfinderNearestAttackableTarget pathfinder) {
            String entity = EntityData.toString(pathfinder.getEntity());
            //noinspection unchecked
            String target = EntityData.toString(pathfinder.getFilter());
            String interval = new Timespan(pathfinder.getInterval()).toString();
            return String.format("NearestAttackableTargetGoal[mobType=%s,target=%s,interval='%s']",
                    entity, target, interval);
        }
    };

    @Nullable
    public static GoalWrapper getByClass(Class<? extends Pathfinder> pathClass) {
        return MAP_BY_CLASS.get(pathClass);
    }

    @Nullable
    public static GoalWrapper getByKey(NamespacedKey key) {
        return MAP_BY_KEY.get(key);
    }

    private final NamespacedKey namespacedKey;

    public GoalWrapper(Class<P> pathClass, String key) {
        this.namespacedKey = NamespacedKey.minecraft(key);
        MAP_BY_CLASS.put(pathClass, this);
        MAP_BY_KEY.put(this.namespacedKey, this);
    }

    public abstract String toSkriptString(P pathfinder);

    public NamespacedKey getKey() {
        return this.namespacedKey;
    }

}