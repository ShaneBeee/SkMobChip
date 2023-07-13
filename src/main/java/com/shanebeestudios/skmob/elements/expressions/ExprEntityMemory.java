package com.shanebeestudios.skmob.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.shanebeestudios.skmob.api.mobchip.BrainUtils;
import me.gamercoder215.mobchip.EntityBrain;
import me.gamercoder215.mobchip.ai.memories.Memory;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

@Name("EntityMemory - Memory of Mob/Brain")
@Description("Get/Set/Clear the memory of a mob/brain. The timespan will determine how long this memory will last.")
@Examples({"set {_loc} to home memory of target entity",
        "teleport target entity to home memory of target entity",
        "set home memory of target entity to location of player",
        "set home memory of target entity for 10 minutes to location of player",
        "clear home memory of target entity"})
@Since("INSERT VERSION")
public class ExprEntityMemory extends SimpleExpression<Object> {

    static {
        Skript.registerExpression(ExprEntityMemory.class, Object.class, ExpressionType.COMBINED,
                "%entitymemory% memory of %entities/entitybrains% [for %-timespan%]");
    }

    private Expression<Memory<?>> memory;
    private Literal<Memory<?>> memoryLit;
    private Expression<?> sources;
    private Expression<Timespan> duration;

    @SuppressWarnings({"NullableProblems", "unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        this.memory = (Expression<Memory<?>>) exprs[0];
        Expression<?> expr = exprs[0];
        if (expr instanceof Literal<?>) {
            this.memoryLit = (Literal<Memory<?>>) expr;
        }
        this.sources = exprs[1];
        this.duration = (Expression<Timespan>) exprs[2];
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected Object[] get(@NotNull Event event) {
        Memory<?> memory = this.memory != null ? this.memory.getSingle(event) : null;
        if (memory == null) return null;

        List<Object> memories = new ArrayList<>();
        for (Object object : this.sources.getArray(event)) {
            EntityBrain brain = BrainUtils.getBrain(object);
            if (brain != null) memories.add(brain.getMemory(memory));
        }
        return memories.toArray(new Object[0]);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public @Nullable Class<?>[] acceptChange(ChangeMode mode) {
        if (mode == ChangeMode.SET) return CollectionUtils.array(Object.class);
        else if (mode == ChangeMode.DELETE) return CollectionUtils.array();
        return null;
    }

    @SuppressWarnings({"NullableProblems", "ConstantValue", "rawtypes", "unchecked"})
    @Override
    public void change(Event event, @Nullable Object[] delta, ChangeMode mode) {
        Memory memory = this.memory != null ? this.memory.getSingle(event) : null;
        if (memory == null) return;

        if (mode == ChangeMode.DELETE) {
            for (Object source : this.sources.getArray(event)) {
                EntityBrain brain = BrainUtils.getBrain(source);
                if (brain != null) brain.removeMemory(memory);
            }
        } else if (mode == ChangeMode.SET && delta != null) {
            long duration;
            if (this.duration != null) {
                Timespan timespan = this.duration.getSingle(event);
                duration = timespan != null ? timespan.getTicks_i() : -1;
            } else {
                duration = -1;
            }
            for (Object source : this.sources.getArray(event)) {
                EntityBrain brain = BrainUtils.getBrain(source);
                if (brain != null) {
                    // TODO bug in the API ... can't properly test this right now
                    if (delta.length == 1 && delta[0] != null) {
                        Object object = delta[0];
                        if (memory.getBukkitClass().isAssignableFrom(object.getClass())) {
                            if (duration < 0) {
                                brain.setMemory(memory, object);
                            } else {
                                brain.setMemory(memory, object, duration);
                            }
                        }
                    } else {
                        if (memory.getBukkitClass().isAssignableFrom(delta.getClass())) {
                            if (duration < 0) {
                                brain.setMemory(memory, delta);
                            } else {
                                brain.setMemory(memory, delta, duration);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isSingle() {
        return this.sources.isSingle();
    }

    @Override
    public @NotNull Class<?> getReturnType() {
        if (this.memoryLit != null) return this.memoryLit.getSingle().getBukkitClass();
        return Object.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean d) {
        return "null";
    }

}
