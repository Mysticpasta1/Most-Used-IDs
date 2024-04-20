package com.mystic.muid.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

public class TagsCommand {
    private static Path tags = Path.of("tags");

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        var pairs = BuiltInRegistries.REGISTRY.holders().collect(Collectors.toMap(Holder.Reference::key, Holder::get));

        var command = Commands.literal("tags").requires(cs -> cs.hasPermission(3));


        pairs.forEach((resourceKey, registry) -> {
            if(!resourceKey.location().getPath().contains("worldgen")) {
                command.then(Commands.literal(resourceKey.location().toString()).executes(ctx -> printTags(ctx, resourceKey.location(), registry)));
            }
        });

        return command;
    }


    public static <T> int printTags(CommandContext<CommandSourceStack> context, ResourceLocation name, Registry<T> registry) {
        try {
            if (Files.notExists(tags)) Files.createDirectory(tags);

            var file = tags.resolve("%s-%s.txt".formatted(name.getNamespace(), name.getPath())).toFile();
            PrintStream out = new PrintStream(new FileOutputStream(file));
            printTagsExt(out, registry);
            out.close();

            context.getSource().sendSystemMessage(Component.literal("Tagged %s File Written :)".formatted(name)));

        } catch (IOException e) {
            System.out.println("Error during reading/writing");
            e.printStackTrace();
        }
        return 0;
    }

    public static <T> void printTagsExt(PrintStream out, Registry<T> registry) {
        var map1 = registry.getTagNames().map(registry::getTag)
                .filter(Optional::isPresent).map(Optional::get).reduce(new HashMap<>(), (stringMapMap, holders) -> {
                    stringMapMap.computeIfAbsent(holders.key().location().getNamespace(), a -> new HashMap<>()).put(holders.key().location().getPath(),
                            holders.stream().map(Holder::unwrapKey)
                                    .filter(Optional::isPresent).map(Optional::get).map(ResourceKey::location).collect(Collectors.toSet()));
                    return stringMapMap;
                }, (BinaryOperator<Map<String, Map<String, Set<ResourceLocation>>>>) (stringMapMap, stringMapMap2) -> {
                    stringMapMap.putAll(stringMapMap2);
                    return stringMapMap;
                });


        map1.forEach((s, stringSetMap) -> {
            out.println(s);

            stringSetMap.forEach((s1, resourceLocations) -> {
                out.println("\t" + s1);

                resourceLocations.forEach(location -> out.println("\t\t" + location));
            });
        });
    }
}