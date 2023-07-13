package com.shanebeestudios.skmob.elements.expressions.goals;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import com.shanebeestudios.skmob.api.skript.PathfinderExpression;
import me.gamercoder215.mobchip.ai.goal.Pathfinder;
import me.gamercoder215.mobchip.ai.goal.PathfinderPanic;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Goal - Panic Goal")
@Description("Create a goal for a Creature to panic when damaged.")
public class ExprPanicGoal extends PathfinderExpression {

    static {
        register(ExprPanicGoal.class, "panic goal for %livingentity% [with speed mod %-number%]");
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

        double speedMod = 1.5; // MobChip default
        if (this.speedMod != null) {
            Number speedModNumber = this.speedMod.getSingle(event);
            if (speedModNumber != null) speedMod = speedModNumber.doubleValue();
        }

        return new PathfinderPanic(creature, speedMod);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean d) {
        String speed = this.speedMod != null ? (" with speed mod " + this.speedMod.toString(e, d)) : "";
        return "panic goal for " + getEntity(e, d) + speed;
    }

}
