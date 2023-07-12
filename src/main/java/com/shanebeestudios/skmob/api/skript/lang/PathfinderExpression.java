package com.shanebeestudios.skmob.api.skript.lang;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.gamercoder215.mobchip.ai.goal.Pathfinder;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PathfinderExpression extends SimpleExpression<Pathfinder> {

    public static void register(Class<? extends SimpleExpression<Pathfinder>> c, String patterns) {
        Skript.registerExpression(c, Pathfinder.class, ExpressionType.COMBINED, patterns);
    }

    public boolean init(Expression<?>[] exprs, ParseResult parseResult) {
        return true;
    }

    private Expression<Entity> entity;

    @SuppressWarnings({"NullableProblems", "unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        this.entity = (Expression<Entity>) exprs[0];
        return init(exprs, parseResult);
    }

    protected abstract Pathfinder get(Event event, Entity entity);

    @SuppressWarnings("NullableProblems")
    @Override
    protected @Nullable Pathfinder[] get(Event event) {
        Entity entity = this.entity.getSingle(event);
        if (entity == null) return null;
        return new Pathfinder[]{get(event, entity)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Pathfinder> getReturnType() {
        return Pathfinder.class;
    }

    protected String getEntity(Event event, boolean debug) {
        return this.entity.toString(event, debug);
    }

}
