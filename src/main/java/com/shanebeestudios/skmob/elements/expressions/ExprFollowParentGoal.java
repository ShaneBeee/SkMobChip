package com.shanebeestudios.skmob.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import com.shanebeestudios.skmob.api.skript.lang.PathfinderExpression;
import me.gamercoder215.mobchip.ai.goal.Pathfinder;
import me.gamercoder215.mobchip.ai.goal.PathfinderFollowParent;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Goal - Follow Parent Goal")
@Description("Create a new goal for a baby version of a Mob to follow an adult version of a Mob.")
@Examples("set {_goal} to follow parent goal for last spawned sheep with speed mod 0.5")
@Since("INSERT VERSION")
public class ExprFollowParentGoal extends PathfinderExpression {

    static {
        register(ExprFollowParentGoal.class, "follow parent goal for %livingentity% [with speed mod %-number%]");
    }

    private Expression<Number> speedMod;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, ParseResult parseResult) {
        this.speedMod = (Expression<Number>) exprs[1];
        return true;
    }

    @Override
    protected Pathfinder get(Event event, Entity entity) {
        if (!(entity instanceof Animals animal)) return null;

        double speedMod = 1.0;
        if (this.speedMod != null) {
            Number speedModNum = this.speedMod.getSingle(event);
            if (speedModNum != null) speedMod = speedModNum.doubleValue();
        }

        return new PathfinderFollowParent(animal, speedMod);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean d) {
        String speed = this.speedMod != null ? (" with speed mod " + this.speedMod.toString(e, d)) : "";
        return "follow parent goal for " + getEntity(e, d) + speed;
    }

}
