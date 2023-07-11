package com.shanebeestudios.skmob.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.shanebeestudios.skmob.util.BrainUtils;
import me.gamercoder215.mobchip.ai.EntityAI;
import me.gamercoder215.mobchip.ai.goal.Pathfinder;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Goal - Add to Entity")
@Description("Add a goal to an entity with a priority. Lower priority numbers are handled by the game first.")
@Examples({"set {_goal} to avoid entity goal for {_e} to avoid player with walk speed mod 2",
        "add {_goal} with priority 0 to goals of {_e}"})
@Since("INSERT VERSION")
public class EffAddGoal extends Effect {

    static {
        Skript.registerEffect(EffAddGoal.class,
                "add [goal] %goal% with priority %number% to [[:target] goals of] %livingentity/entitybrain/entityart%");
    }

    private Expression<Pathfinder> goal;
    private Expression<Number> priority;
    private Expression<?> object;
    private boolean target;

    @SuppressWarnings({"NullableProblems", "unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        this.goal = (Expression<Pathfinder>) exprs[0];
        this.priority = (Expression<Number>) exprs[1];
        this.object = exprs[2];
        this.target = parseResult.hasTag("target");
        return true;
    }

    @Override
    protected void execute(@NotNull Event event) {
        Pathfinder goal = this.goal.getSingle(event);
        Number priorityNum = this.priority.getSingle(event);
        Object object = this.object.getSingle(event);
        if (goal == null || priorityNum == null) return;

        int priority = priorityNum.intValue();
        EntityAI goalAI = BrainUtils.getGoalAI(object);
        EntityAI targetAI = BrainUtils.getTargetAI(object);
        if (!this.target && goalAI != null) {
            goalAI.put(goal, priority);
        } else if (this.target && targetAI != null) {
            targetAI.put(goal, priority);
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean d) {
        String goals = this.target ? " target goals of " : " goals of ";
        return "add goal " + this.goal.toString(e,d) + " with priority " + this.priority.toString(e,d) +
                " to " + goals + this.object.toString(e,d);
    }

}
