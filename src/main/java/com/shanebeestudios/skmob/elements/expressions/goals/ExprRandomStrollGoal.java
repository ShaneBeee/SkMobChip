package com.shanebeestudios.skmob.elements.expressions.goals;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.util.Timespan;
import com.shanebeestudios.skmob.api.skript.lang.PathfinderExpression;
import me.gamercoder215.mobchip.ai.goal.Pathfinder;
import me.gamercoder215.mobchip.ai.goal.PathfinderRandomStroll;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Goal - Random Stroll Goal")
@Description("Create a new goal for Creatures to randomly stroll.")
@Examples("set {_goal} to random stroll goal for {_e} with speed mod 1.5")
@Since("INSERT VERSION")
public class ExprRandomStrollGoal extends PathfinderExpression {

    static {
        Skript.registerExpression(ExprRandomStrollGoal.class, Pathfinder.class, ExpressionType.COMBINED,
                "random stroll goal for %livingentity% [with speed mod %-number%] [with interval %-timespan%]");
    }

    private Expression<Entity> entity;
    private Expression<Number> speedMod;
    private Expression<Timespan> interval;

    @SuppressWarnings({"unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, ParseResult parseResult) {
        this.entity = (Expression<Entity>) exprs[0];
        this.speedMod = (Expression<Number>) exprs[1];
        this.interval = (Expression<Timespan>) exprs[2];
        return true;
    }

    @Override
    protected Pathfinder convert(Event event) {
        Entity entity = this.entity.getSingle(event);
        if (!(entity instanceof Creature creature)) return null;

        double speedMod = 1.0; // MobChip default
        int interval = 120; // MobChip default

        if (this.speedMod != null) {
            Number speedModNum = this.speedMod.getSingle(event);
            if (speedModNum != null) speedMod = speedModNum.doubleValue();
        }

        if (this.interval != null) {
            Timespan intervalTS = this.interval.getSingle(event);
            if (intervalTS != null) interval = (int) intervalTS.getTicks_i();
        }

        return new PathfinderRandomStroll(creature, speedMod, interval);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean d) {
        String entity = this.entity.toString(e, d);
        String speed = this.speedMod != null ? (" with speed mod " + this.speedMod.toString(e, d)) : "";
        String interval = this.interval != null ? (" with interval " + this.interval.toString(e, d)) : "";
        return String.format("random stroll goal for %s %s%s", entity, speed, interval);
    }

}
