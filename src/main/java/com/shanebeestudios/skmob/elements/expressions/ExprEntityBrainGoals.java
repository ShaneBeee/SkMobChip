package com.shanebeestudios.skmob.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.shanebeestudios.skmob.api.mobchip.BrainUtils;
import me.gamercoder215.mobchip.EntityBrain;
import me.gamercoder215.mobchip.ai.EntityAI;
import me.gamercoder215.mobchip.ai.goal.Pathfinder;
import me.gamercoder215.mobchip.ai.goal.WrappedPathfinder;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Name("EntityBrain - Goals")
@Description({"Represents the goals of a Mob's brain.",
        "Mobs have 2 types of goals, regular goals and target goals.",
        "Running goals are the goals that are currently running at the given moment in time."})
@Examples({"set {_goals::*} to goals of target entity",
        "set {_goals::*} to running goals of target entity",
        "set {_goals::*} to target goals of target entity",
        "set {_goals::*} to running target goals of target entity"})
@Since("INSERT VERSION")
public class ExprEntityBrainGoals extends SimpleExpression<Pathfinder> {

    static {
        Skript.registerExpression(ExprEntityBrainGoals.class, Pathfinder.class, ExpressionType.PROPERTY,
                "[:running] [:target] goals of %livingentities/entitybrains%",
                "%livingentities/entitybrains%'[s] [:running] [:target] goals");
    }

    private Expression<?> entity;
    private boolean target;
    private boolean running;

    @SuppressWarnings({"NullableProblems"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        this.entity = exprs[0];
        this.target = parseResult.hasTag("target");
        this.running = parseResult.hasTag("running");
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected Pathfinder[] get(Event event) {
        List<Pathfinder> goals = new ArrayList<>();
        for (Object object : this.entity.getArray(event)) {
            EntityBrain brain = BrainUtils.getBrain(object);
            if (brain == null) continue;

            EntityAI entityAI = brain.getGoalAI();
            if (this.target) entityAI = brain.getTargetAI();

            if (this.running) {
                for (WrappedPathfinder runningGoal : entityAI.getRunningGoals()) {
                    goals.add(runningGoal.getPathfinder());
                }
            } else {
                for (WrappedPathfinder wrappedPathfinder : entityAI) {
                    goals.add(wrappedPathfinder.getPathfinder());
                }
            }
        }
        return goals.toArray(new Pathfinder[0]);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public @NotNull Class<? extends Pathfinder> getReturnType() {
        return Pathfinder.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean d) {
        String running = this.running ? " running" : "";
        String target = this.target ? " target" : "";
        return String.format("%s%s goals of %s", running, target, this.entity.toString(e,d));
    }

}
