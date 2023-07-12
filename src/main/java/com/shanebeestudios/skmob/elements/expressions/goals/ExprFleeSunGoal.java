package com.shanebeestudios.skmob.elements.expressions.goals;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import com.shanebeestudios.skmob.api.skript.lang.PathfinderExpression;
import me.gamercoder215.mobchip.ai.goal.Pathfinder;
import me.gamercoder215.mobchip.ai.goal.PathfinderFleeSun;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Goal - Flee Sun Goal")
@Description({"Create a new goal for a Creature to avoid the sun or to extinguish a fire.",
        "This is an advanced version of restrict sun goal.",
        "This will include if the Creature is on fire and if they do not have a helmet,",
        "and will also pathfind the entity to the nearest extinguish source.",
        "The other goal will only have the Creature avoid the sun, with no speed modifier included."})
@Examples("set {_goal} to flee sun goal for last spawned entity with speed mod 1.9")
@Since("INSERT VERSION")
public class ExprFleeSunGoal extends PathfinderExpression {

    static {
        register(ExprFleeSunGoal.class, "flee sun goal for %livingentity% [with speed mod %-number%]");
    }

    private Expression<Number> speedMod;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, SkriptParser.ParseResult parseResult) {
        this.speedMod = (Expression<Number>) exprs[1];
        return true;
    }

    @Override
    protected Pathfinder get(Event event, Entity entity) {
        if (!(entity instanceof Creature creature)) return null;

        double speedMod = 1.5; // ModChip default
        if (this.speedMod != null) {
            Number speedModNumber = this.speedMod.getSingle(event);
            if (speedModNumber != null) speedMod = speedModNumber.doubleValue();
        }

        return new PathfinderFleeSun(creature, speedMod);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean d) {
        return "flee sun goal for " + getEntity(e, d);
    }

}
