package com.shanebeestudios.skmob.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.shanebeestudios.skmob.util.BrainUtils;
import me.gamercoder215.mobchip.EntityBrain;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Mob Restriction Radius")
@Description({"Represents the radius of the area a mob/brain is restricted to.",
        "This can't be set, use the 'mob restriction area' expression to set."})
@Examples("set {_rad} to restriction radius of target entity")
@Since("INSERT VERSION")
public class ExprRestrictionRadius extends SimplePropertyExpression<Object,Number> {

    static {
        register(ExprRestrictionRadius.class, Number.class,
                "restriction radius", "entities/entitybrains");
    }

    @Override
    public @Nullable Number convert(Object object) {
        EntityBrain brain = BrainUtils.getBrain(object);
        if (brain != null) return brain.getRestrictionRadius();
        return null;
    }

    @Override
    public @NotNull Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "restriction radius";
    }

}
