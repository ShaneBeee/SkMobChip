package com.shanebeestudios.skmob.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import me.gamercoder215.mobchip.EntityBrain;
import me.gamercoder215.mobchip.bukkit.BukkitBrain;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Brain - Get")
@Description("Get the brain of a mob.")
@Examples("set {_brain} to brain of target entity")
@Since("INSERT VERSION")
public class ExprBrain extends SimplePropertyExpression<Entity, EntityBrain> {

    static {
        register(ExprBrain.class, EntityBrain.class, "brain", "entities");
    }

    @Override
    public @Nullable EntityBrain convert(Entity entity) {
        // TODO Not sure the diff, we may need to revisit this later
        if (entity instanceof EnderDragon enderDragon) return BukkitBrain.getBrain(enderDragon);
        if (entity instanceof Mob mob) return BukkitBrain.getBrain(mob);
        return null;
    }

    @Override
    public @NotNull Class<? extends EntityBrain> getReturnType() {
        return EntityBrain.class;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "brain";
    }

}
