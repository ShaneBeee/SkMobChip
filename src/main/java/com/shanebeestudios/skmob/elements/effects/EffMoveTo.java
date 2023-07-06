package com.shanebeestudios.skmob.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import me.gamercoder215.mobchip.EntityBrain;
import me.gamercoder215.mobchip.ai.controller.EntityController;
import me.gamercoder215.mobchip.bukkit.BukkitBrain;
import org.bukkit.Location;
import org.bukkit.entity.Mob;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Entity Move To")
@Description("Move a mob/enityController to a location with an optional speed.")
@Examples("move {_e} to location of player")
@Since("INSERT VERSION")
public class EffMoveTo extends Effect {

    static {
        Skript.registerEffect(EffMoveTo.class,
                "move %entities/entitycontrollers% to %location% [with speed %-number%]");
    }

    private Expression<?> objects;
    private Expression<Location> location;
    private Expression<Number> speed;

    @SuppressWarnings({"NullableProblems", "unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        this.objects = exprs[0];
        this.location = (Expression<Location>) exprs[1];
        this.speed = (Expression<Number>) exprs[2];
        return true;
    }

    @Override
    protected void execute(@NotNull Event event) {
        Location location = this.location.getSingle(event);
        if (location == null) return;

        double speed = 1.0; // default value
        if (this.speed != null) {
            Number number = this.speed.getSingle(event);
            if (number != null) speed = number.doubleValue();
        }

        EntityController entityController = null;
        for (Object object : this.objects.getArray(event)) {
            if (object instanceof Mob mob) {
                EntityBrain brain = BukkitBrain.getBrain(mob);
                entityController = brain.getController();
            } else if (object instanceof EntityController controller) {
                entityController = controller;
            }
        }

        if (entityController != null) entityController.moveTo(location, speed);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean d) {
        String speed = this.speed != null ? (" with speed " + this.speed.toString(e,d)) : "";
        String object = this.objects.toString(e,d);
        String location = this.location.toString(e,d);
        return "move " + object + " to " + location + speed;
    }

}
