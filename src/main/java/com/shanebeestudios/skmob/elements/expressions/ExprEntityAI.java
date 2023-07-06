package com.shanebeestudios.skmob.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import me.gamercoder215.mobchip.EntityBrain;
import me.gamercoder215.mobchip.ai.EntityAI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("EntityAI - Get")
@Description("Represents the goal/target AI of a mob's brain.")
@Examples({"set {_goal} to goal ai of brain of target entity",
        "set {_target} to target ai of brain of target entity"})
@Since("INSERT VERSION")
public class ExprEntityAI extends SimplePropertyExpression<EntityBrain, EntityAI> {

    static {
        register(ExprEntityAI.class, EntityAI.class, "(goal|:target) [entity] ai", "entitybrains");
    }

    private boolean target;

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        this.target = parseResult.hasTag("target");
        return super.init(exprs, matchedPattern, isDelayed, parseResult);
    }

    @Override
    public @Nullable EntityAI convert(EntityBrain brain) {
        if (this.target) return brain.getTargetAI();
        return brain.getGoalAI();
    }

    @Override
    public @NotNull Class<? extends EntityAI> getReturnType() {
        return EntityAI.class;
    }

    @Override
    protected @NotNull String getPropertyName() {
        String type = this.target ? "target" : "goal";
        return type + " entity ai";
    }

}
