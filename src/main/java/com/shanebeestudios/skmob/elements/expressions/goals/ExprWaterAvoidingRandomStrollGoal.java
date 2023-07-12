package com.shanebeestudios.skmob.elements.expressions.goals;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import com.shanebeestudios.skmob.api.skript.lang.PathfinderExpression;
import me.gamercoder215.mobchip.ai.goal.Pathfinder;
import me.gamercoder215.mobchip.ai.goal.PathfinderRandomStrollLand;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Goal - Water Avoiding Random Stroll Goal")
@Description("Create a new goal for Creatures to randomly stroll, but only on land.")
@Examples("set {_goal} to water avoiding random stroll goal for {_e} with speed mod 1.2")
@Since("INSERT VERSION")
public class ExprWaterAvoidingRandomStrollGoal extends PathfinderExpression {

    static {
        register(ExprWaterAvoidingRandomStrollGoal.class,
                "water avoiding random stroll goal for %livingentity% [with speed mod %-number%]" +
                        " [[and ]with probability %-number%]");
    }

    private Expression<Number> speedMod;
    private Expression<Number> probability;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, SkriptParser.ParseResult parseResult) {
        this.speedMod = (Expression<Number>) exprs[1];
        this.probability = (Expression<Number>) exprs[2];
        return true;
    }

    @Override
    protected Pathfinder get(Event event, Entity entity) {
        if (!(entity instanceof Creature creature)) return null;

        double speedMod = 1.0; // MobChip default
        if (this.speedMod != null) {
            Number speedModNum = this.speedMod.getSingle(event);
            if (speedModNum != null) speedMod = speedModNum.doubleValue();
        }

        float probability = PathfinderRandomStrollLand.DEFAULT_PROBABILITY;
        if (this.probability != null) {
            Number probabilityNum = this.probability.getSingle(event);
            if (probabilityNum != null) probability = probabilityNum.floatValue();
        }

        return new PathfinderRandomStrollLand(creature, speedMod, probability);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean d) {
        String entity = getEntity(e,d);
        String speedMod = this.speedMod != null ? (" with speed mod " + this.speedMod.toString(e,d)) : "";
        String prob = this.probability != null ? (" with probability " + this.probability.toString(e,d)) : "";
        return String.format("water avoiding random stroll goal for %s %s%s", entity, speedMod, prob);
    }

}

