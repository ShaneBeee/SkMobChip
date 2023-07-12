package com.shanebeestudios.skmob.elements.expressions.goals;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import com.shanebeestudios.skmob.api.skript.lang.PathfinderExpression;
import me.gamercoder215.mobchip.ai.goal.Pathfinder;
import me.gamercoder215.mobchip.ai.goal.PathfinderRandomStrollFlying;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Goal - Water Avoiding Random Flying Goal")
@Description("Create a new goal for Creatures to randomly fly and avoid water.")
@Examples("set {_goal} to water avoiding random stroll goal for {_e} with speed mod 1.2")
@Since("INSERT VERSION")
public class ExprWaterAvoidingRandomFlyingGoal extends PathfinderExpression {

    static {
        register(ExprWaterAvoidingRandomFlyingGoal.class,
                "water avoiding random flying goal for %livingentity% [with speed mod %-number%]");
    }

    private Expression<Entity> entity;
    private Expression<Number> speedMod;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, SkriptParser.ParseResult parseResult) {
        this.entity = (Expression<Entity>) exprs[0];
        this.speedMod = (Expression<Number>) exprs[1];
        return true;
    }

    @Override
    protected Pathfinder convert(Event event) {
        if (!(this.entity.getSingle(event) instanceof Creature creature)) return null;

        double speedMod = 1.0; // MobChip default
        if (this.speedMod != null) {
            Number speedModNum = this.speedMod.getSingle(event);
            if (speedModNum != null) speedMod = speedModNum.doubleValue();
        }

        return new PathfinderRandomStrollFlying(creature, speedMod);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean d) {
        String entity = this.entity.toString(e, d);
        String speedMod = this.speedMod != null ? (" with speed mod " + this.speedMod.toString(e, d)) : "";
        return String.format("water avoiding random stroll goal for %s %s", entity, speedMod);
    }

}
