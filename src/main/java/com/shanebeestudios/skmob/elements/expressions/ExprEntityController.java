package com.shanebeestudios.skmob.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import me.gamercoder215.mobchip.EntityBrain;
import me.gamercoder215.mobchip.ai.controller.EntityController;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("EntityController - Get")
@Description("Get the entity controller from a mob's brain.")
@Examples("set {_controller} to entity controller of brain of target entity")
@Since("INSERT VERSION")
public class ExprEntityController extends SimplePropertyExpression<EntityBrain, EntityController> {

    static {
        register(ExprEntityController.class, EntityController.class, "entity controller", "entitybrains");
    }

    @Override
    public @Nullable EntityController convert(EntityBrain brain) {
        return brain.getController();
    }

    @Override
    public @NotNull Class<? extends EntityController> getReturnType() {
        return EntityController.class;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "entity controller";
    }

}
