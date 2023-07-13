package com.shanebeestudios.skmob.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.shanebeestudios.skmob.util.BrainUtils;
import me.gamercoder215.mobchip.EntityBrain;
import me.gamercoder215.mobchip.ai.EntityAI;
import me.gamercoder215.mobchip.ai.goal.WrappedPathfinder;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Name("EntityBrain - Wrapped Goals")
@Description({"Represents the goals of a Mob's brain. These are wrapped to include the goal and priority.",
        "Mobs have 2 types of goals, regular goals and target goals.",
        "Running goals are the goals that are currently running at the given moment in time."})
@Examples({"set {_goals::*} to wrapped goals of target entity",
        "set {_goals::*} to running wrapped goals of target entity",
        "set {_goals::*} to target wrapped goals of target entity",
        "set {_goals::*} to running wrapped target goals of target entity"})
@Since("INSERT VERSION")
public class ExprEntityBrainGoalsWrapped extends SimpleExpression<WrappedPathfinder> {

    static {
        Skript.registerExpression(ExprEntityBrainGoalsWrapped.class, WrappedPathfinder.class, ExpressionType.PROPERTY,
                "[:running] [:target] wrapped goals of %livingentities/entitybrains%",
                "%livingentities/entitybrains%'[s] [:running] [:target] wrapped goals");
    }

    private Expression<?> entity;
    private boolean target;
    private boolean running;

    @SuppressWarnings({"NullableProblems"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.entity = exprs[0];
        this.target = parseResult.hasTag("target");
        this.running = parseResult.hasTag("running");
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected WrappedPathfinder[] get(Event event) {
        List<WrappedPathfinder> goals = new ArrayList<>();

        for (Object object : this.entity.getArray(event)) {
            EntityBrain brain = BrainUtils.getBrain(object);
            if (brain == null) continue;

            EntityAI entityAI = brain.getGoalAI();
            if (this.target) entityAI = brain.getTargetAI();

            if (this.running) {
                goals.addAll(entityAI.getRunningGoals());
            } else {
                goals.addAll(entityAI);
            }
        }
        // Sort by priority
        goals = goals.stream().sorted(Comparator.comparing(WrappedPathfinder::getPriority)).collect(Collectors.toList());
        return goals.toArray(new WrappedPathfinder[0]);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public @NotNull Class<? extends WrappedPathfinder> getReturnType() {
        return WrappedPathfinder.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean d) {
        String running = this.running ? " running" : "";
        String target = this.target ? " target" : "";
        return String.format("%s%s wrapped goals of %s", running, target, this.entity.toString(e, d));
    }

}
