package com.shanebeestudios.skmob.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.shanebeestudios.skmob.api.mobchip.BrainUtils;
import me.gamercoder215.mobchip.EntityBrain;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("EntityBrain - Get")
@Description("Get the brain of a mob.")
@Examples("set {_brain} to brain of target entity")
@Since("INSERT VERSION")
public class ExprEntityBrain extends SimplePropertyExpression<Entity, EntityBrain> {

    static {
        register(ExprEntityBrain.class, EntityBrain.class, "[entity] brain", "entities");
    }

    @Override
    public @Nullable EntityBrain convert(Entity entity) {
        return BrainUtils.getBrain(entity);
    }

    @Override
    public @NotNull Class<? extends EntityBrain> getReturnType() {
        return EntityBrain.class;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "entity brain";
    }

}
