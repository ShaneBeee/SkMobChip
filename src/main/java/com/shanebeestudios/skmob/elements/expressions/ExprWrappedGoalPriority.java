package com.shanebeestudios.skmob.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import me.gamercoder215.mobchip.ai.goal.WrappedPathfinder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("WrappedGoal - Priority")
@Description("Represents the priority of a wrapped goal.")
@Examples({"loop wrapped goals of target entity:",
        "\tset {_int} to goal priority of loop-value"})
@Since("INSERT VERSION")
public class ExprWrappedGoalPriority extends SimplePropertyExpression<WrappedPathfinder, Number> {

    static {
        register(ExprWrappedGoalPriority.class, Number.class, "goal priority", "wrappedgoals");
    }

    @Override
    public @Nullable Number convert(WrappedPathfinder wrappedPathfinder) {
        return wrappedPathfinder.getPriority();
    }

    @Override
    public @NotNull Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "goal priority";
    }

}
