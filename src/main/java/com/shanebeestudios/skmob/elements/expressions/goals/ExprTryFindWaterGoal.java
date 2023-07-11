package com.shanebeestudios.skmob.elements.expressions.goals;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import com.shanebeestudios.skmob.api.skript.lang.PathfinderExpression;
import me.gamercoder215.mobchip.ai.goal.Pathfinder;
import me.gamercoder215.mobchip.ai.goal.PathfinderFindWater;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Goal - Try Find Water Goal")
@Description("Create a new goal for a Creature to try and find water.")
@Examples("set {_goal} to find water goal for target entity")
@Since("INSERT VERSION")
public class ExprTryFindWaterGoal extends PathfinderExpression {

    static {
        register(ExprTryFindWaterGoal.class, "try find water goal for %livingentity%");
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

        if (!(entity instanceof Creature creature)) return null;

        return new PathfinderFindWater(creature);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean d) {
        return "try find water goal for " + this.entity.toString(e,d);
    }

}
