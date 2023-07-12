package com.shanebeestudios.skmob.elements.expressions.goals;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.Timespan;
import com.shanebeestudios.skmob.api.skript.lang.PathfinderExpression;
import me.gamercoder215.mobchip.ai.goal.Pathfinder;
import me.gamercoder215.mobchip.ai.goal.PathfinderBreakDoor;
import org.bukkit.Difficulty;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Goal - Break Door Goal")
@Description({"Create a new goal for a Mob to break a door.",
        "\nbreak time = the time it will take the mob to break the door. (default = 240 ticks)",
        "\ndifficulties = the difficulties this will work on."})
@Examples({"set {_goal} to break door goal for last spawned entity with break time 1 minute with difficulty normal",
        "add {_goal} with priority 1 to goals of last spawned entity"})
@Since("INSERT VERSION")
public class ExprBreakDoorGoal extends PathfinderExpression {

    static {
        Skript.registerExpression(ExprBreakDoorGoal.class, Pathfinder.class, ExpressionType.COMBINED,
                "break door goal for %livingentity% [with break time %-timespan%] [with difficult(y|ies) %-difficulties%]");
    }

    private Expression<Timespan> breakTime;
    private Expression<Difficulty> difficulties;

    @SuppressWarnings({"unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, SkriptParser.ParseResult parseResult) {
        this.breakTime = (Expression<Timespan>) exprs[1];
        this.difficulties = (Expression<Difficulty>) exprs[2];
        return true;
    }

    @Override
    protected @Nullable Pathfinder get(Event event, Entity entity) {
        if (!(entity instanceof Mob mob)) return null;

        int breakTime = PathfinderBreakDoor.DEFAULT_DOOR_BREAK_TIME;
        if (this.breakTime != null) {
            Timespan timeSpan = this.breakTime.getSingle(event);
            if (timeSpan != null) breakTime = (int) timeSpan.getTicks_i();
        }

        Difficulty[] difficulties = new Difficulty[]{Difficulty.NORMAL};
        if (this.difficulties != null) difficulties = this.difficulties.getArray(event);

        return new PathfinderBreakDoor(mob, breakTime, difficulties);
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean d) {
        String entity = getEntity(e,d);
        String breakTime = this.breakTime != null ? (" with break time " + this.breakTime.toString(e, d)) : "";
        String diff = this.difficulties != null ? (" with difficulties " + this.difficulties.toString(e, d)) : "";
        return String.format("break door goal for %s %s%s", entity, breakTime, diff);
    }
}
