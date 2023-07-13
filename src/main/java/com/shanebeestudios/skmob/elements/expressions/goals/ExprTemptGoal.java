package com.shanebeestudios.skmob.elements.expressions.goals;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import com.shanebeestudios.skmob.api.skript.lang.PathfinderExpression;
import me.gamercoder215.mobchip.ai.goal.Pathfinder;
import me.gamercoder215.mobchip.ai.goal.PathfinderTempt;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Name("Goal - Tempt Goal")
@Description({"Create a new goal for the logic of this Creature getting tempted to move to another entity",
        "for when they hold a specific item."})
@Examples("set {_goal} to tempt goal for {_e} using diamond and emerald")
@Since("INSERT VERSION")
public class ExprTemptGoal extends PathfinderExpression {

    static {
        register(ExprTemptGoal.class, "temp goal for %livingentity% using %itemtypes%" +
                " [with speed mod %-number%]");
    }

    private Expression<ItemType> itemTypes;
    private Expression<Number> speedMod;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, ParseResult parseResult) {
        this.itemTypes = (Expression<ItemType>) exprs[1];
        this.speedMod = (Expression<Number>) exprs[2];
        return true;
    }

    @Override
    protected Pathfinder get(Event event, Entity entity) {
        if (!(entity instanceof Creature creature)) return null;

        double speedMod = 1.5; // MobChip default
        if (this.speedMod != null) {
            Number speedModNumber = this.speedMod.getSingle(event);
            if (speedModNumber != null) speedMod = speedModNumber.doubleValue();
        }

        List<ItemStack> itemStacks = new ArrayList<>();
        for (ItemType itemData : this.itemTypes.getArray(event)) {
            itemStacks.add(itemData.getRandom());
        }

        return new PathfinderTempt(creature, speedMod, itemStacks);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean d) {
        String entity = getEntity(e, d);
        String items = this.itemTypes.toString(e, d);
        String speed = this.speedMod != null ? (" with speed mod " + this.speedMod.toString(e, d)) : "";
        return String.format("tempt goal for %s using %s %s", entity, items, speed);
    }

}
