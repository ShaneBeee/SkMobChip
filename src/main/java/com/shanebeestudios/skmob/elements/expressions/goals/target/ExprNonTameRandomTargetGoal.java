package com.shanebeestudios.skmob.elements.expressions.goals.target;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.entity.EntityData;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import com.shanebeestudios.skmob.api.skript.PathfinderExpression;
import me.gamercoder215.mobchip.ai.goal.Pathfinder;
import me.gamercoder215.mobchip.ai.goal.target.PathfinderWildTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Tameable;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Goal - Non Tame Random Target Goal")
@Description("Create a new goal for when a non-tamed Animal targets another Entity.")
@Examples("set {_goal} to non tame random target goal for {_e} to target cat")
@Since("INSERT VERSION")
public class ExprNonTameRandomTargetGoal extends PathfinderExpression {

    static {
        register(ExprNonTameRandomTargetGoal.class,
                "non( |-)tame random target goal for %livingentity% to target %entitydata%");
    }

    private Expression<EntityData<?>> target;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, ParseResult parseResult) {
        this.target = (Expression<EntityData<?>>) exprs[1];
        return true;
    }

    @Override
    protected Pathfinder get(Event event, Entity entity) {
        EntityData<?> targetData = this.target.getSingle(event);
        if (!(entity instanceof Tameable tameable) || targetData == null) return null;

        if (!Tameable.class.isAssignableFrom(targetData.getType())) return null;
        //noinspection unchecked
        Class<Tameable> type = (Class<Tameable>) targetData.getType();

        return new PathfinderWildTarget<>(tameable, type);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean d) {
        String entity = getEntity(e, d);
        String target = this.target.toString(e, d);
        return "non tame random target goal for " + entity + " to target " + target;
    }

}
