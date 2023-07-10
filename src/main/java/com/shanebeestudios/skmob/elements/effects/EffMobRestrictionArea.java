package com.shanebeestudios.skmob.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.shanebeestudios.skmob.util.BrainUtils;
import me.gamercoder215.mobchip.EntityBrain;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Mob Restrict Area")
@Description("Restrict a mob to a certain area.")
@Examples({"set {_e} to last spawned entity",
        "restrict {_e} to location of {_e} with radius 10"})
public class EffMobRestrictionArea extends Effect {

    static {
        Skript.registerEffect(EffMobRestrictionArea.class,
                "restrict %livingentities/entitybrains% to %location% with radius %number%");
    }

    private Expression<?> objects;
    private Expression<Location> location;
    private Expression<Number> radius;

    @SuppressWarnings({"NullableProblems", "unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        this.objects = exprs[0];
        this.location = (Expression<Location>) exprs[1];
        this.radius = (Expression<Number>) exprs[2];
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected void execute(Event event) {
        Location location = this.location.getSingle(event);
        Number radiusNum = this.radius.getSingle(event);
        if (location == null || radiusNum == null) return;

        int radius = radiusNum.intValue();

        for (Object object : this.objects.getArray(event)) {
            EntityBrain brain = BrainUtils.getBrain(object);
            if (brain != null) brain.setRestrictionArea(location, radius);
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean d) {
        String object = this.objects.toString(e,d);
        String loc = this.location.toString(e,d);
        String rad = this.radius.toString(e,d);
        return String.format("restrict %s to %s with radius %s", object, loc, rad);
    }

}
