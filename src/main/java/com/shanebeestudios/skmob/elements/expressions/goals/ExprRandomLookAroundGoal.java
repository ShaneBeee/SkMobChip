package com.shanebeestudios.skmob.elements.expressions.goals;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import com.shanebeestudios.skmob.api.skript.lang.PathfinderExpression;
import me.gamercoder215.mobchip.ai.goal.Pathfinder;
import me.gamercoder215.mobchip.ai.goal.PathfinderRandomLook;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Goal - Random Look Around Goal")
@Description("Create a goal for a Mob to randomly look around.")
@Examples({"set {_goal} to random look around goal for {_e}",
        "add {_goal} to goals of {_e}"})
@Since("INSERT VERSION")
public class ExprRandomLookAroundGoal extends PathfinderExpression {

    static {
        register(ExprRandomLookAroundGoal.class, "random look around goal for %livingentity%");
    }

    private Expression<Entity> entity;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, ParseResult parseResult) {
        this.entity = (Expression<Entity>) exprs[0];
        return true;
    }

    @Override
    protected @Nullable Pathfinder convert(Event event) {
        Entity entity = this.entity.getSingle(event);
        if (!(entity instanceof Mob mob)) return null;

        return new PathfinderRandomLook(mob);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean d) {
        return "random look around goal for " + this.entity.toString(e, d);
    }

}
