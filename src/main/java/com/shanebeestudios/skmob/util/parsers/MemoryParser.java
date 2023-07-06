package com.shanebeestudios.skmob.util.parsers;

import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.util.StringUtils;
import me.gamercoder215.mobchip.ai.memories.EntityMemory;
import me.gamercoder215.mobchip.ai.memories.Memory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MemoryParser {

    private final Map<String, Memory<?>> MEMORIES = new HashMap<>();

    public MemoryParser() {
        for (EntityMemory<?> value : EntityMemory.values()) {
            String key = value.getKey().getKey();
            MEMORIES.put(key, value);
        }
    }

    private Memory<?> parse(String toParse) {
        String key = toParse.replace(" ", "_").toLowerCase(Locale.ROOT);
        return MEMORIES.get(key);
    }

    public String getNames() {
        List<String> names = new ArrayList<>(MEMORIES.keySet());
        Collections.sort(names);
        return StringUtils.join(names, ", ");
    }

    public Parser<Memory<?>> getParser() {
        return new Parser<>() {

            @Override
            public @Nullable Memory<?> parse(@NotNull String s, @NotNull ParseContext context) {
                return MemoryParser.this.parse(s);
            }

            @Override
            public @NotNull String toString(Memory<?> memory, int flags) {
                return "EntityMemory[" + memory.getKey() + "]";
            }

            @Override
            public @NotNull String toVariableNameString(Memory<?> memory) {
                return toString(memory, 0);
            }
        };
    }

}
