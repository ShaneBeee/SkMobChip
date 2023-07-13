package com.shanebeestudios.skmob.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.shanebeestudios.skmob.api.mobchip.BrainUtils;
import me.gamercoder215.mobchip.EntityBrain;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Mob Can See")
@Description("Check if a mob/brain can see another mob/brain.")
@Since("INSERT VERSION")
public class CondCanSee extends Condition {

    static {
        Skript.registerCondition(CondCanSee.class,
                "%entities/entitybrains% (can|1:(can't|cannot)) brain see %entities/entitybrains%");
    }

    private Expression<?> sources;
    private Expression<?> targets;

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        this.sources = exprs[0];
        this.targets = exprs[0];
        setNegated(parseResult.hasTag("1"));
        return true;
    }

    @Override
    public boolean check(@NotNull Event event) {
        return this.sources.check(event, source -> this.targets.check(event, target -> {
            EntityBrain sourceBrain = null;
            EntityBrain targetBrain = null;
            if (source instanceof Entity entity) sourceBrain = BrainUtils.getBrain(entity);
            else if (source instanceof EntityBrain entityBrain) sourceBrain = entityBrain;

            if (target instanceof Entity entity) targetBrain = BrainUtils.getBrain(entity);
            else if (source instanceof EntityBrain entityBrain) targetBrain = entityBrain;

            if (sourceBrain != null && targetBrain != null) {
                return sourceBrain.canSee(targetBrain);
            }
            return false;
        }), isNegated());
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean d) {
        String see = isNegated() ? "can't see" : "can see";
        return this.sources.toString(e,d) + see + this.targets.toString(e,d);
    }

}
