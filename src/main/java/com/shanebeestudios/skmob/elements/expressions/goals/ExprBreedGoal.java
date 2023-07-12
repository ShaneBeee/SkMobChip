package com.shanebeestudios.skmob.elements.expressions.goals;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import com.shanebeestudios.skmob.api.skript.lang.PathfinderExpression;
import me.gamercoder215.mobchip.ai.goal.Pathfinder;
import me.gamercoder215.mobchip.ai.goal.PathfinderBreed;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Goal - Breed Goal")
@Description("Create a new goal that causes Animals to breed.")
@Examples("set {_goal} to breed goal for {_sheep}")
@Since("INSERT VERSION")
public class ExprBreedGoal extends PathfinderExpression {

    static {
        register(ExprBreedGoal.class, "breed goal for %livingentity% [with speed mod %-number%]");
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
        return new PathfinderBreed(animal);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean d) {
        String speed = this.speedMod != null ? (" with speed mod " + this.speedMod.toString(e, d)) : "";
        return "breed goal for " + getEntity(e, d) + speed;
    }

}
