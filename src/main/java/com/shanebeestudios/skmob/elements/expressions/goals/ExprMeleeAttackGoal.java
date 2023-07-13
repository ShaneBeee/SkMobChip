package com.shanebeestudios.skmob.elements.expressions.goals;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import com.shanebeestudios.skmob.api.skript.PathfinderExpression;
import me.gamercoder215.mobchip.ai.goal.Pathfinder;
import me.gamercoder215.mobchip.ai.goal.PathfinderMeleeAttack;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Goal - Melee Attack Goal")
@Description({"Create a new goal for a Creature to attack.",
        "This goal does not look for entities to attack, but only attacks them.",
        "To look for entities use 'nearest attackable target goal'.",
        "Any entities that do not normally attack (i.e. animals) most commonly do not have attack attributes.",
        "An entity that attacks without an Attack Attribute will crash the server."})
@Examples("set {_goal} to melee attack goal for {_e} with speed mod 2.5")
@Since("INSERT VERSION")
public class ExprMeleeAttackGoal extends PathfinderExpression {

    static {
        register(ExprMeleeAttackGoal.class,
                "melee attack goal for %livingentity% [with speed mod %-number%]");
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
        if (!(entity instanceof Creature creature)) return null;

        double speedMod = 1.5; // ModChip default
        if (this.speedMod != null) {
            Number speedModNum = this.speedMod.getSingle(event);
            if (speedModNum != null) speedMod = speedModNum.doubleValue();
        }
        return new PathfinderMeleeAttack(creature, speedMod);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean d) {
        String entity = getEntity(e, d);
        String speed = this.speedMod != null ? (" with speed mod " + this.speedMod.toString(e, d)) : "";
        return "melee attack goal for " + entity + speed;
    }

}
