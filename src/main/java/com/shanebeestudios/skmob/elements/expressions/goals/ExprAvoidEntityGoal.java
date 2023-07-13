package com.shanebeestudios.skmob.elements.expressions.goals;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.entity.EntityData;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import com.shanebeestudios.skmob.api.skript.PathfinderExpression;
import me.gamercoder215.mobchip.ai.goal.Pathfinder;
import me.gamercoder215.mobchip.ai.goal.PathfinderAvoidEntity;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Goal - Avoid Entity Goal")
@Description({"Create a new goal for a Creature to avoid a LivingEntity.",
        "\n`to Avoid %entitytype%` = the type of entity to avoid.",
        "\n`with walk speed mod %number%` = the modifier for walking speed.",
        "\n`with sprint speed mod %number%` = the modifier for sprinting speed.",
        "\n`with max distance %number%` = max distance away to stop fleeing."})
@Examples({"set {_goal} to avoid entity goal for {_e} to avoid player with walk speed mod 2",
        "add goal {_goal} with priority 0 to {_e}"})
@Since("INSERT VERSION")
public class ExprAvoidEntityGoal extends PathfinderExpression {

    static {
        Skript.registerExpression(ExprAvoidEntityGoal.class, Pathfinder.class, ExpressionType.COMBINED,
                "avoid entity goal for %livingentity% to avoid %entitydata% " +
                        "[with [walk] speed mod %-number%] [[and ]with sprint speed mod %-number%] " +
                        "[[and ]with max distance %-number%]");
    }

    private Expression<EntityData<?>> avoid;
    private Expression<Number> walkSpeedMod;
    private Expression<Number> sprintSpeedMod;
    private Expression<Number> maxDistance;

    @SuppressWarnings({"unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, ParseResult parseResult) {
        this.avoid = (Expression<EntityData<?>>) exprs[1];
        this.walkSpeedMod = (Expression<Number>) exprs[2];
        this.sprintSpeedMod = (Expression<Number>) exprs[3];
        this.maxDistance = (Expression<Number>) exprs[4];
        return true;
    }

    @Override
    protected @Nullable Pathfinder get(Event event, Entity entity) {
        EntityData<?> filter = this.avoid.getSingle(event);

        if (!(entity instanceof Creature creature) || filter == null) return null;

        double walkSpeedMod = 1.5; // Default in MobChip
        if (this.walkSpeedMod != null) {
            Number speedModNum = this.walkSpeedMod.getSingle(event);
            if (speedModNum != null) walkSpeedMod = speedModNum.doubleValue();
        }

        double springSpeedMod = walkSpeedMod; // Defualt in MobChip (if not available, matches speedMod)
        if (this.sprintSpeedMod != null) {
            Number sprintModNum = this.sprintSpeedMod.getSingle(event);
            if (sprintModNum != null) springSpeedMod = sprintModNum.doubleValue();
        }

        float maxDistance = 5.0f; // Default in MobChip
        if (this.maxDistance != null) {
            Number maxDistanceNum = this.maxDistance.getSingle(event);
            if (maxDistanceNum != null) maxDistance = maxDistanceNum.floatValue();
        }

        if (!LivingEntity.class.isAssignableFrom(filter.getType())) return null;
        //noinspection unchecked
        Class<? extends LivingEntity> type = (Class<? extends LivingEntity>) filter.getType();

        return new PathfinderAvoidEntity<>(creature, type, maxDistance, walkSpeedMod, springSpeedMod);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean d) {
        String entity = getEntity(e,d);
        String avoid = this.avoid.toString(e, d);
        String walk = this.walkSpeedMod != null ? (" with walk speed " + this.walkSpeedMod.toString(e, d)) : "";
        String sprint = this.sprintSpeedMod != null ? (" with sprint speed " + this.sprintSpeedMod.toString(e, d)) : "";
        String dist = this.maxDistance != null ? (" with max distance " + this.maxDistance.toString(e, d)) : "";
        return String.format("avoid entity goal for %s to avoid %s %s%s%s",
                entity, avoid, walk, sprint, dist);
    }

}
