package com.shanebeestudios.skmob.elements.expressions.goals;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import com.shanebeestudios.skmob.api.skript.lang.PathfinderExpression;
import me.gamercoder215.mobchip.ai.goal.Pathfinder;
import me.gamercoder215.mobchip.ai.goal.PathfinderRemoveBlock;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Goal - Remove Block Goal")
@Description({"Create a goal for a Creature to remove a block. Vertical range must be a positive integer."})
@Examples({"set {_goal} to remove block goal for {_e} to remove diamond ore",
        "set {_goal} to remove block goal for {_e} to remove diamond ore with speed mod 1.7",
        "set {_goal} to remove block goal for {_e} to remove diamond ore with vertical range 3"})
@Since("INSERT VERSION")
public class ExprRemoveBlockGoal extends PathfinderExpression {

    static {
        register(ExprRemoveBlockGoal.class,
                "remove block goal for %livingentity% to remove %itemtype/blockdata%" +
                        " [with speed mod %-number%] [[and ]with vertical search range %-number%]");
    }

    private Expression<?> material;
    private Expression<Number> speedMod;
    private Expression<Number> verticalRange;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, SkriptParser.ParseResult parseResult) {
        this.material = exprs[1];
        this.speedMod = (Expression<Number>) exprs[2];
        this.verticalRange = (Expression<Number>) exprs[3];
        return true;
    }

    @Override
    protected Pathfinder get(Event event, Entity entity) {
        if (!(entity instanceof Creature creature)) return null;

        Material material = null;
        Object object = this.material.getSingle(event);
        if (object instanceof ItemType itemType) material = itemType.getMaterial();
        else if (object instanceof BlockData blockData) material = blockData.getMaterial();
        if (material == null || material.isAir() || !material.isBlock()) return null;

        double speedMod = 1.0; // MobChip default
        if (this.speedMod != null) {
            Number speedModNum = this.speedMod.getSingle(event);
            if (speedModNum != null) speedMod = speedModNum.doubleValue();
        }

        int verticalRange = 1;
        if (this.verticalRange != null) {
            Number verticalRangeNum = this.verticalRange.getSingle(event);
            if (verticalRangeNum != null) verticalRange = verticalRangeNum.intValue();
        }
        if (verticalRange < 1) verticalRange = 1;

        return new PathfinderRemoveBlock(creature, material, speedMod, verticalRange);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean d) {
        String entity = getEntity(e, d);
        String remove = this.material.toString(e, d);
        String speed = this.speedMod != null ? (" with speed mod " + this.speedMod.toString(e, d)) : "";
        String vert = this.verticalRange != null ? (" with vertical search range " + this.verticalRange.toString(e, d)) : "";
        return String.format("remove block goal for %s to remove %s %s%s",
                entity, remove, speed, vert);
    }

}
