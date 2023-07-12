package com.shanebeestudios.skmob.elements.expressions.goals;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import com.shanebeestudios.skmob.api.skript.lang.PathfinderExpression;
import me.gamercoder215.mobchip.ai.goal.Pathfinder;
import me.gamercoder215.mobchip.ai.goal.PathfinderEatTile;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Goal - Eat Block Goal")
@Description({"Create a new goal for a Mob to eat a block.",
        "Because this is made specifially for sheep, the target block will be grass_block."})
@Examples("set {_goal} to eat block goal for last spawned entity")
@Since("INSERT VERSION")
public class ExprEatBlockGoal extends PathfinderExpression {

    static {
        register(ExprEatBlockGoal.class, "eat block goal for %livingentity%");
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
        if (!(this.entity.getSingle(event) instanceof Mob mob)) return null;
        return new PathfinderEatTile(mob);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean d) {
        return "eat block goal for " + this.entity.toString(e, d);
    }

}
