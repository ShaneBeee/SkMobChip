package com.shanebeestudios.skmob.elements.expressions.goals;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.entity.EntityData;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import com.shanebeestudios.skmob.api.skript.PathfinderExpression;
import me.gamercoder215.mobchip.ai.goal.Pathfinder;
import me.gamercoder215.mobchip.ai.goal.PathfinderLookAtEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Goal - Look At Player Goal")
@Description({"Create a goal for a Mob to look at another Entity.",
        "Although the name implies Player, I believe it can look at any entity.",
        "\nNote: Probability (0.0 - 1.0) to look at something. Called every tick, recommended to be a low number.",
        "Default probability = 0.02"})
@Examples({"set {_goal} to look at player goal for {_e} to look at sheep",
        "set {_goal} to look at player goal for {_e} to look at player with look range 10"})
@Since("INSERT VERSION")
public class ExprLookAtPlayerGoal extends PathfinderExpression {

    static {
        register(ExprLookAtPlayerGoal.class,
                "look at player goal for %livingentity% to look at %entitydata%" +
                        " [with look range %-number%] [with probability %-number%]");
    }

    private Expression<EntityData<?>> target;
    private Expression<Number> range;
    private Expression<Number> probability;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, ParseResult parseResult) {
        this.target = (Expression<EntityData<?>>) exprs[1];
        this.range = (Expression<Number>) exprs[2];
        this.probability = (Expression<Number>) exprs[3];
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Pathfinder get(Event event, Entity entity) {
        EntityData<?> targetData = this.target.getSingle(event);
        if (!(entity instanceof Mob mob) || targetData == null) return null;

        if (!LivingEntity.class.isAssignableFrom(targetData.getType())) return null;
        Class<? extends LivingEntity> type = (Class<? extends LivingEntity>) targetData.getType();

        float range = PathfinderLookAtEntity.DEFAULT_LOOK_RANGE;
        if (this.range != null) {
            Number rangeNum = this.range.getSingle(event);
            if (rangeNum != null) range = rangeNum.floatValue();
        }

        float probability = PathfinderLookAtEntity.DEFAULT_PROBABILITY;
        if (this.probability != null) {
            Number probNum = this.probability.getSingle(event);
            if (probNum != null) probability = probNum.floatValue();
        }

        return new PathfinderLookAtEntity<>(mob, type, range, probability);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean d) {
        String entity = getEntity(e, d);
        String target = this.target.toString(e, d);
        String range = this.range != null ? (" with look range " + this.range.toString(e, d)) : "";
        String prob = this.probability != null ? (" with probability " + this.probability.toString(e, d)) : "";
        return String.format("look at player goal for %s to look at %s %s%s", entity, target, range, prob);
    }

}
