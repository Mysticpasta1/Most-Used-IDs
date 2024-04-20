package com.mystic.muid.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class DebugInfoCommand implements Command<CommandSourceStack> {

    private static final DebugInfoCommand CMD = new DebugInfoCommand();

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("idsizes")
                .requires(cs -> cs.hasPermission(3))
                .executes(CMD);
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        try {

            String lineFromInput1 = " ";

            boolean append = false;
            createFile();
            PrintStream out = new PrintStream(new FileOutputStream("MUID_Output.txt", append));
            System.setOut(out);

            ModList.get().getMods().forEach((modContainer -> {
                int a = 0;
                for (ResourceLocation resourceLocation : context.getSource().getLevel().registryAccess().registry(ForgeRegistries.BIOMES.getRegistryKey()).get().keySet()) {
                    if (resourceLocation.toString().contains(modContainer.getModId())) {
                        a++;
                    }
                }
                int b = 0;
                for (ResourceLocation resourceLocation : ForgeRegistries.BLOCKS.getKeys()) {
                    if (resourceLocation.toString().contains(modContainer.getModId())) {
                        b++;
                    }
                }
                int c = 0;
                for (ResourceLocation resourceLocation : ForgeRegistries.ITEMS.getKeys()) {
                    if (resourceLocation.toString().contains(modContainer.getModId())) {
                        c++;
                    }
                }
                int d = 0;
                for (ResourceLocation resourceLocation : ForgeRegistries.POTIONS.getKeys()) {
                    if (resourceLocation.toString().contains(modContainer.getModId())) {
                        d++;
                    }
                }
                int e = 0;
                for (ResourceLocation resourceLocation : ForgeRegistries.ENCHANTMENTS.getKeys()) {
                    if (resourceLocation.toString().contains(modContainer.getModId())) {
                        e++;
                    }
                }
                int f = 0;
                for (ResourceLocation resourceLocation : ForgeRegistries.BLOCK_ENTITY_TYPES.getKeys()) {
                    if (resourceLocation.toString().contains(modContainer.getModId())) {
                        f++;
                    }
                }
                int g = 0;
                for (ResourceLocation resourceLocation : ForgeRegistries.ENTITY_TYPES.getKeys()) {
                    if (resourceLocation.toString().contains(modContainer.getModId())) {
                        g++;
                    }
                }

                out.println(lineFromInput1);
                out.println(modContainer.getModId());
                if(a > 0){
                    out.println("Number of Biome IDs Registered: " + a);
                }
                if(b > 0){
                    out.println("Number of Block IDs Registered: " + b);
                }
                if(c > 0){
                    out.println("Number of Item IDs Registered: " + c);
                }
                if(d > 0){
                    out.println("Number of Potion IDs Registered: " + d);
                }
                if(e > 0){
                    out.println("Number of Enchantment IDs Registered: " + e);
                }
                if(f > 0){
                    out.println("Number of Tile Entity IDs Registered: " + f);
                }
                if(g > 0){
                    out.println("Number of Entity IDs Registered: " + g);
                }
            }));

            out.close();

            context.getSource().sendSystemMessage(Component.literal("Mod Debug File Written :)"));

        } catch (IOException e) {
            System.out.println("Error during reading/writing");
            e.printStackTrace();
        }
        return 0;
    }

    public static void createFile(){
        File myObj = new File("MUID_Output.txt");
        try {
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("WTF Exploded");
        }
    }
}
