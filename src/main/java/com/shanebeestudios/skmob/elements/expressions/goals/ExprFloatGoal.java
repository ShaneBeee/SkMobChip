package com.shanebeestudios.skmob.elements.expressions.goals;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import com.shanebeestudios.skmob.api.skript.lang.PathfinderExpression;
import me.gamercoder215.mobchip.ai.goal.Pathfinder;
import me.gamercoder215.mobchip.ai.goal.PathfinderFloat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Goal - Float Goal")
@Description("Create a new goal that Mobs need to float on water.")
@Examples("set {_goal} to float goal for target entity")
@Since("INSERT VERSION")
public class ExprFloatGoal extends PathfinderExpression {

    static {
        register(ExprFloatGoal.class, "float goal for %livingentity%");
    }

    private Expression<Entity> entity;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, SkriptParser.ParseResult parseResult) {
        this.entity = (Expression<Entity>) exprs[0];
        return true;
    }

    @Override
    protected Pathfinder convert(Event event) {
        Entity entity = this.entity.getSingle(event);
        if (!(entity instanceof Mob mob)) return null;

        return new PathfinderFloat(mob);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean d) {
        return "float goal for " + this.entity.toString(e,d);
    }

}
