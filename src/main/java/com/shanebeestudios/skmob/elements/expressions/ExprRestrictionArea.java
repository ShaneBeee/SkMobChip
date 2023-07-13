package com.shanebeestudios.skmob.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.shanebeestudios.skmob.api.mobchip.BrainUtils;
import me.gamercoder215.mobchip.EntityBrain;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Mob Restriction Location")
@Description("Represents the center location a mob/brain is restricted to.")
@Examples("set {_loc} to mob restriction center of target entity")
@Since("INSERT VERSION")
public class ExprRestrictionArea extends SimplePropertyExpression<Object, Location> {

    static {
        register(ExprRestrictionArea.class, Location.class,
                "restriction (area|center|location)", "livingentities/entitybrains");
    }

    @Override
    public @Nullable Location convert(Object object) {
        EntityBrain brain = BrainUtils.getBrain(object);
        if (brain != null) return brain.getRestrictionArea();
        return null;
    }

    @Override
    public @NotNull Class<? extends Location> getReturnType() {
        return Location.class;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "restriction location";
    }

}
