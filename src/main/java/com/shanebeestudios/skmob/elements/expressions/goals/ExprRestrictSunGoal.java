package com.shanebeestudios.skmob.elements.expressions.goals;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import com.shanebeestudios.skmob.api.skript.lang.PathfinderExpression;
import me.gamercoder215.mobchip.ai.goal.Pathfinder;
import me.gamercoder215.mobchip.ai.goal.PathfinderRestrictSun;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Goal - Restrict Sun Goal")
@Description({"Create a goal for a Creature to avoid the Sun.",
        "This Goal is a more basic version of flee sun goal.",
        "There is no speed modifier included, and it will only avoid the sun.",
        "The other goal will include if the Creature is on fire and if they do not have a helmet,",
        "and will also pathfind the entity to the nearest extinguish source."})
@Examples("set {_goal} to restrcit sun goal for {_entity}")
@Since("INSERT VERSION")
public class ExprRestrictSunGoal extends PathfinderExpression {

    static {
        register(ExprRestrictSunGoal.class, "restrict sun goal for %livingentity%");
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

        return new PathfinderRestrictSun(creature);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean d) {
        return "restrict sun goal for " + this.entity.toString(e, d);
    }

}
