package com.shanebeestudios.skmob.elements.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import com.shanebeestudios.skmob.util.parsers.MemoryParser;
import me.gamercoder215.mobchip.EntityBrain;
import me.gamercoder215.mobchip.ai.EntityAI;
import me.gamercoder215.mobchip.ai.controller.EntityController;
import me.gamercoder215.mobchip.ai.memories.Memory;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class Types {

    static {
        Classes.registerClass(new ClassInfo<>(EntityBrain.class, "entitybrain")
                .user("entity ?brains?")
                .name("EntityBrain")
                .description("Represents the brain of a mob.")
                .since("INSERT VERSION")
                .parser(new Parser<>() {

                    @Override
                    public boolean canParse(@NotNull ParseContext context) {
                        return false;
                    }

                    @Override
                    public @NotNull String toString(EntityBrain brain, int flags) {
                        return "EntityBrain[mobType:\"" + brain.getEntity().getType().key() + "\"]";
                    }

                    @Override
                    public @NotNull String toVariableNameString(EntityBrain brain) {
                        return "entitybrain:" + brain.getEntity().getType();
                    }
                }));

        Classes.registerClass(new ClassInfo<>(EntityAI.class, "entityart") // Silly Skript plural thing
                .user("entity ?ais?")
                .name("EntityAI")
                .description("Represents the AI of a mob's brain.")
                .since("INSERT VERSION")
                .parser(new Parser<>() {

                    @Override
                    public boolean canParse(@NotNull ParseContext context) {
                        return false;
                    }

                    @Override
                    public @NotNull String toString(EntityAI entityAI, int flags) {
                        return "EntityAI[mobType:\"" + entityAI.getEntity().getType().key() + "\"]";
                    }

                    @Override
                    public @NotNull String toVariableNameString(EntityAI entityAI) {
                        return "entityai:" + entityAI.getEntity().getType();
                    }
                }));

        Classes.registerClass(new ClassInfo<>(EntityController.class, "entitycontroller")
                .user("entity ?controllers?")
                .name("EntityController")
                .description("Represents the controller of a mob's brain.")
                .since("INSERT VERSION")
                .parser(new Parser<>() {

                    @Override
                    public boolean canParse(@NotNull ParseContext context) {
                        return false;
                    }

                    @Override
                    public @NotNull String toString(EntityController controller, int flags) {
                        return "EntityController";
                    }

                    @Override
                    public @NotNull String toVariableNameString(EntityController controller) {
                        return "entitycontroller";
                    }
                }));

        MemoryParser MEMORY_PARSER = new MemoryParser();
        Classes.registerClass(new ClassInfo<>(Memory.class, "entitymemory")
                .user("entity ?memor(y|ies)")
                .name("EntityMemory")
                .description("Representes the memory of a mob.")
                .usage(MEMORY_PARSER.getNames())
                .since("INSERT VERSION")
                .parser(MEMORY_PARSER.getParser()));
    }

}
