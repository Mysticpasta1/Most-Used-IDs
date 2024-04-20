package com.mystic.muid.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ModCommand
{
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("muid").then(DebugInfoCommand.register()));
        dispatcher.register(Commands.literal("muid").then(TagsCommand.register()));
        dispatcher.register(Commands.literal("muid").then(IdCommand.register()));
    }
}
