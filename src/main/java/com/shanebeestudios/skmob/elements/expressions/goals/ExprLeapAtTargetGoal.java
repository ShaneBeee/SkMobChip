package com.shanebeestudios.skmob.elements.expressions.goals;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import com.shanebeestudios.skmob.api.skript.lang.PathfinderExpression;
import me.gamercoder215.mobchip.ai.goal.Pathfinder;
import me.gamercoder215.mobchip.ai.goal.PathfinderLeapAtTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Goal - Leap At Target Goal")
@Description("Create a new goal for a Mob to leap at its target.")
@Examples("set {_goal} to leat at target goal for {_e} with height 0.4")
@Since("INSERT VERSION")
public class ExprLeapAtTargetGoal extends PathfinderExpression {

    static {
        register(ExprLeapAtTargetGoal.class,
                "leap at target goal for %livingentity% [with height %-number%]");
    }

    private Expression<Number> height;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, ParseResult parseResult) {
        this.height = (Expression<Number>) exprs[1];
        return true;
    }

    @Override
    protected Pathfinder get(Event event, Entity entity) {
        if (!(entity instanceof Mob mob)) return null;

        float height = 0.5f; // Minecraft default?!?!
        if (this.height != null) {
            Number heightNum = this.height.getSingle(event);
            if (heightNum != null) height = heightNum.floatValue();
        }

        return new PathfinderLeapAtTarget(mob, height);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean d) {
        String entity = getEntity(e, d);
        String height = this.height != null ? (" with height " + this.height.toString(e, d)) : "";
        return "leap at target goal for " + entity + height;
    }

}
