package com.shanebeestudios.skmob.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import me.gamercoder215.mobchip.ai.goal.Pathfinder;
import me.gamercoder215.mobchip.ai.goal.WrappedPathfinder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("WrappedGoal - Goal")
@Description("Represents the goal of a wrapped goal.")
@Examples({"loop wrapped goals of target entity:",
        "\tset {_goal} to goal of loop-value"})
@Since("INSERT VERSION")
public class ExprWrappedGoalGoal extends SimplePropertyExpression<WrappedPathfinder, Pathfinder> {

    static {
        register(ExprWrappedGoalGoal.class, Pathfinder.class, "goal", "wrappedgoals");
    }
    @Override
    public @Nullable Pathfinder convert(WrappedPathfinder wrappedPathfinder) {
        return wrappedPathfinder.getPathfinder();
    }

    @Override
    public @NotNull Class<? extends Pathfinder> getReturnType() {
        return Pathfinder.class;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "goal";
    }

}
