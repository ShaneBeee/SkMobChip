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
import com.shanebeestudios.skmob.api.mobchip.BrainUtils;
import me.gamercoder215.mobchip.ai.EntityAI;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Goal - Clear Goals")
@Description("Clear the goals of a mob.")
@Examples({"clear goals of target entity",
        "clear goals of last spawned entity",
        "clear target goals of all monsters"})
@Since("INSERT VERSION")
public class EffClearGoals extends Effect {

    static {
        Skript.registerEffect(EffClearGoals.class, "clear [:target] goals of %livingentity/entitybrain/entityart%");
    }

    private Expression<?> object;
    private boolean target;

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        this.object = exprs[0];
        this.target = parseResult.hasTag("target");
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected void execute(Event event) {
        Object object = this.object.getSingle(event);

        EntityAI goalAI = BrainUtils.getGoalAI(object);
        EntityAI targetAI = BrainUtils.getTargetAI(object);

        if (goalAI != null && !this.target) goalAI.clear();
        else if (targetAI != null && this.target) targetAI.clear();
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean d) {
        String goal = this.target ? "target" : "goal";
        return "clear " + goal + " goals of " + this.object.toString(e, d);
    }

}
