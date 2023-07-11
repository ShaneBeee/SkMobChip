package com.shanebeestudios.skmob.api.skript.lang;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.gamercoder215.mobchip.ai.goal.Pathfinder;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PathfinderExpression extends SimpleExpression<Pathfinder> {

    public static void register(Class<? extends SimpleExpression<Pathfinder>> c, String patterns) {
        Skript.registerExpression(c, Pathfinder.class, ExpressionType.COMBINED, patterns);
    }

    public abstract boolean init(Expression<?>[] exprs, ParseResult parseResult);

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        return init(exprs, parseResult);
    }

    protected abstract Pathfinder convert(Event event);

    @SuppressWarnings("NullableProblems")
    @Override
    protected @Nullable Pathfinder[] get(Event event) {
        return new Pathfinder[]{convert(event)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Pathfinder> getReturnType() {
        return Pathfinder.class;
    }

}
