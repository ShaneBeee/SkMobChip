package com.shanebeestudios.skmob.elements.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import com.shanebeestudios.skmob.api.goal.GoalWrapper;
import com.shanebeestudios.skmob.util.parsers.MemoryParser;
import me.gamercoder215.mobchip.EntityBrain;
import me.gamercoder215.mobchip.ai.EntityAI;
import me.gamercoder215.mobchip.ai.controller.EntityController;
import me.gamercoder215.mobchip.ai.goal.Pathfinder;
import me.gamercoder215.mobchip.ai.goal.WrappedPathfinder;
import me.gamercoder215.mobchip.ai.memories.Memory;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class Types {

    static {
        Classes.registerClass(new ClassInfo<>(EntityBrain.class, "entitybrain")
                .user("entity ?brains?")
                .name("EntityBrain")
                .description("Represents the brain of a mob.")
                .since("INSERT VERSION")
                .parser(new Parser<>() {

                    @Override
                    public boolean canParse(@NotNull ParseContext context) {
                        return false;
                    }

                    @Override
                    public @NotNull String toString(EntityBrain brain, int flags) {
                        return "EntityBrain[mobType:\"" + brain.getEntity().getType().key() + "\"]";
                    }

                    @Override
                    public @NotNull String toVariableNameString(EntityBrain brain) {
                        return "entitybrain:" + brain.getEntity().getType();
                    }
                }));

        Classes.registerClass(new ClassInfo<>(EntityAI.class, "entityart") // Silly Skript plural thing
                .user("entity ?ais?")
                .name("EntityAI")
                .description("Represents the AI of a mob's brain.",
                        "Mobs have 2 sets of ai, Goal AI and Target AI.")
                .since("INSERT VERSION")
                .parser(new Parser<>() {

                    @Override
                    public boolean canParse(@NotNull ParseContext context) {
                        return false;
                    }

                    @Override
                    public @NotNull String toString(EntityAI entityAI, int flags) {
                        return "EntityAI[mobType:\"" + entityAI.getEntity().getType().key() + "\"]";
                    }

                    @Override
                    public @NotNull String toVariableNameString(EntityAI entityAI) {
                        return "entityai:" + entityAI.getEntity().getType();
                    }
                }));

        Classes.registerClass(new ClassInfo<>(EntityController.class, "entitycontroller")
                .user("entity ?controllers?")
                .name("EntityController")
                .description("Represents the controller of a mob's brain.")
                .since("INSERT VERSION")
                .parser(new Parser<>() {

                    @Override
                    public boolean canParse(@NotNull ParseContext context) {
                        return false;
                    }

                    @Override
                    public @NotNull String toString(EntityController controller, int flags) {
                        return "EntityController";
                    }

                    @Override
                    public @NotNull String toVariableNameString(EntityController controller) {
                        return "entitycontroller";
                    }
                }));

        MemoryParser MEMORY_PARSER = new MemoryParser();
        Classes.registerClass(new ClassInfo<>(Memory.class, "entitymemory")
                .user("entity ?memor(y|ies)")
                .name("EntityMemory")
                .description("Representes the memory of a mob.")
                .usage(MEMORY_PARSER.getNames())
                .since("INSERT VERSION")
                .parser(MEMORY_PARSER.getParser()));

        Classes.registerClass(new ClassInfo<>(Pathfinder.class, "goal")
                .user("goals?")
                .name("Goal")
                .description("Represents a pathfinding goal of a mob.")
                .since("INSERT VERSION")
                .parser(new Parser<>() {

                    @SuppressWarnings("NullableProblems")
                    @Override
                    public boolean canParse(ParseContext context) {
                        return false;
                    }

                    @SuppressWarnings({"NullableProblems", "rawtypes", "unchecked"})
                    @Override
                    public String toString(Pathfinder pathfinder, int flags) {
                        Class<? extends Pathfinder> aClass = pathfinder.getClass();
                        GoalWrapper byClass = GoalWrapper.getByClass(aClass);
                        if (byClass == null) return "UnknownGoal[goalClass=" + pathfinder.getName() + "]";
                        return byClass.toSkriptString(pathfinder);
                    }

                    @SuppressWarnings("NullableProblems")
                    @Override
                    public String toVariableNameString(Pathfinder pathfinder) {
                        return "";
                    }
                }));

        Classes.registerClass(new ClassInfo<>(WrappedPathfinder.class, "wrappedgoal")
                .user("wrapped ?goals?")
                .name("WrappedGoal")
                .description("Represents a goal and priority wrapped.")
                .parser(new Parser<>() {
                    @SuppressWarnings("NullableProblems")
                    @Override
                    public boolean canParse(ParseContext context) {
                        return false;
                    }

                    @SuppressWarnings("NullableProblems")
                    @Override
                    public String toString(WrappedPathfinder wrappedPathfinder, int flags) {
                        Pathfinder pathfinder = wrappedPathfinder.getPathfinder();
                        int priority = wrappedPathfinder.getPriority();
                        if (pathfinder == null) {
                            return String.format("WrappedGoal[priority=%s,missingGoal]", priority);
                        }

                        GoalWrapper<?> goalWrapper = GoalWrapper.getByClass(pathfinder.getClass());
                        if (goalWrapper == null) {
                            return String.format("WrappedGoal[priority=%s,unknownGoal=%s]",
                                    priority, pathfinder.getName());
                        }
                        return String.format("WrappedGoal[priority=%s,goal=\"%s\"]",
                                priority, goalWrapper.getKey().getKey());
                    }

                    @SuppressWarnings("NullableProblems")
                    @Override
                    public String toVariableNameString(WrappedPathfinder wrappedPathfinder) {
                        return toString(wrappedPathfinder, 0);
                    }
                }));
    }

}
