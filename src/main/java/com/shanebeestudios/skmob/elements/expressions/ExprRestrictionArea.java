package com.shanebeestudios.skmob.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.shanebeestudios.skmob.util.BrainUtils;
import me.gamercoder215.mobchip.EntityBrain;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Name("Mob Restriction Area")
@Description({"Represents an area a mob/brain is restricted to.",
        "\nNOTE: This appears to only work with wandering goals, meaning targeting goals wont be restricted,",
        "such as zombies targeting another mob/player, or sheep following a player with wheat."})
@Examples({"set restriction area with radius 5 of target entity to location of target entity",
        "clear restriction area of all entities",
        "set {_loc} to restriction area of target entity"})
@Since("INSERT VERSION")
public class ExprRestrictionArea extends SimpleExpression<Location> {

    static {
        Skript.registerExpression(ExprRestrictionArea.class, Location.class, ExpressionType.COMBINED,
                "restriction area of %entities/entitybrains%",
                "restriction area with radius %-number% of %entities/entitybrains%");
    }

    private Expression<?> sources;
    private Expression<Number> radius;
    private boolean hasRadius;

    @SuppressWarnings({"NullableProblems", "unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        this.sources = exprs[matchedPattern];
        this.hasRadius = matchedPattern == 1;
        this.radius = this.hasRadius ? (Expression<Number>) exprs[0] : null;
        return true;
    }

    @Override
    protected Location @NotNull [] get(@NotNull Event event) {
        List<Location> locations = new ArrayList<>();
        for (Object object : this.sources.getArray(event)) {
            EntityBrain brain = BrainUtils.getBrain(object);
            if (brain != null) locations.add(brain.getRestrictionArea());
        }
        return locations.toArray(new Location[0]);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Class<?>[] acceptChange(@NotNull ChangeMode mode) {
        if (mode == ChangeMode.SET) {
            if (!hasRadius) {
                Skript.error("Setting a restriction area requires a radius.");
                return null;
            }
            return CollectionUtils.array(Location.class);
        } else if (mode == ChangeMode.RESET || mode == ChangeMode.DELETE) return CollectionUtils.array();
        return null;
    }

    @SuppressWarnings({"NullableProblems", "ConstantValue"})
    @Override
    public void change(Event event, Object[] delta, ChangeMode mode) {
        Location location = null;
        if (delta != null && delta[0] instanceof Location loc) location = loc;

        Number radius = this.radius != null ? this.radius.getSingle(event) : null;

        for (Object object : this.sources.getArray(event)) {
            EntityBrain brain = BrainUtils.getBrain(object);
            if (brain == null) continue;

            if (mode == ChangeMode.RESET || mode == ChangeMode.DELETE) brain.clearRestrictionArea();

            else if (mode == ChangeMode.SET && location != null && radius != null) {
                int rad = radius.intValue();
                brain.setRestrictionArea(location, rad);
            }
        }

    }

    @Override
    public boolean isSingle() {
        return this.sources.isSingle();
    }

    @Override
    public @NotNull Class<Location> getReturnType() {
        return Location.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean d) {
        String radius = this.radius != null ? (" with radius " + this.radius.toString(e,d)) : "";
        return "restriction area" + radius + " of " + this.sources.toString(e,d);
    }

}
