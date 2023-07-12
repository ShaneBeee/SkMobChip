package com.shanebeestudios.skmob.elements.expressions.goals.target;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.entity.EntityData;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.Timespan;
import com.shanebeestudios.skmob.api.skript.lang.PathfinderExpression;
import me.gamercoder215.mobchip.ai.goal.Pathfinder;
import me.gamercoder215.mobchip.ai.goal.target.PathfinderNearestAttackableTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Goal - Nearest Attackable Target Goal")
@Description("Create a new goal for a mob to target a specific type of living entity.")
@Examples("")
@Since("INSERT VERSION")
public class ExprNearestAttackableTargetGoal extends PathfinderExpression {

    static {
        register(ExprNearestAttackableTargetGoal.class, "nearest attackable target goal for " +
                "%livingentity% to (target|attack) %entitydata% [with interval %-timespan%]");
    }

    private Expression<EntityData<?>> target;
    private Expression<Timespan> interval;

    @SuppressWarnings({"unchecked", "NullableProblems"})
    @Override
    public boolean init(Expression<?>[] exprs, SkriptParser.ParseResult parseResult) {
        this.target = (Expression<EntityData<?>>) exprs[1];
        this.interval = (Expression<Timespan>) exprs[2];
        return true;
    }

    @SuppressWarnings({"NullableProblems", "unchecked"})
    @Override
    protected @Nullable Pathfinder get(Event event, Entity entity) {
        EntityData<?> targetData = this.target.getSingle(event);

        if (!(entity instanceof Mob mob) || targetData == null) return null;

        if (!LivingEntity.class.isAssignableFrom(targetData.getType())) return null;
        Class<? extends LivingEntity> type = (Class<? extends LivingEntity>) targetData.getType();

        int interval = 10; // MobChip default
        if (this.interval != null) {
            Timespan timeSpan = this.interval.getSingle(event);
            if (timeSpan != null) interval = (int) timeSpan.getTicks_i();
        }

        return new PathfinderNearestAttackableTarget<>(mob, type, interval);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean d) {
        String entity = getEntity(e,d);
        String target = this.target.toString(e, d);
        return String.format("nearest attackable target goal for %s to target %s", entity, target);
    }

}
