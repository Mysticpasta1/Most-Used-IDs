package com.mystic.muid.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

public class IdCommand {
    private static final Path ids = Path.of("ids");

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        var pairs = BuiltInRegistries.REGISTRY.holders().collect(Collectors.toMap(Holder.Reference::key, Holder::get));

        var command = Commands.literal("ids").requires(cs -> cs.hasPermission(3));

        pairs.forEach((resourceKey, registry) -> {
            if(!resourceKey.location().getPath().contains("worldgen")) {
                command.then(Commands.literal(resourceKey.location().toString()).executes(ctx -> printIds(ctx, resourceKey.location(), registry)));
            }
        });

        return command;
    }


    public static <T> int printIds(CommandContext<CommandSourceStack> context, ResourceLocation name, Registry<T> registry) {
        try {
            if (Files.notExists(ids)) Files.createDirectory(ids);


            var file = ids.resolve("%s-%s.txt".formatted(name.getNamespace(), name.getPath())).toFile();
            PrintStream out = new PrintStream(new FileOutputStream(file));
            printIdsExt(out, registry);
            out.close();

            context.getSource().sendSystemMessage(Component.literal("%s File Written :)".formatted(name)));

        } catch (IOException e) {
            System.out.println("Error during reading/writing");
            e.printStackTrace();
        }
        return 0;
    }

    public static <T> void printIdsExt(PrintStream out, Registry<T> registry) {
        var map1 = registry.registryKeySet().stream().map(registry::getHolder)
                .filter(Optional::isPresent).map(Optional::get).reduce(new HashMap<>(), (stringMapMap, holders) -> {
                    stringMapMap.computeIfAbsent(holders.key().location().getNamespace(), a -> new HashSet<>()).add(holders.key().location());
                    return stringMapMap;
                }, (BinaryOperator<Map<String, Set<ResourceLocation>>>) (stringMapMap, stringMapMap2) -> {
                    stringMapMap.putAll(stringMapMap2);
                    return stringMapMap;
                });


        map1.forEach((s, stringSetMap) -> {
            out.println(s);

            stringSetMap.forEach((s1) -> out.println("\t" + s1));
        });
    }
}