package com.mystic.muid.helper.event;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.File;
import java.io.IOException;

@Mod.EventBusSubscriber
public class DebugInfoEvent{

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void RenderGameOverlayEvent(RenderGameOverlayEvent.Text event) {
        if (Minecraft.getInstance().gameSettings.showDebugInfo) {
            event.getRight().add("Number of Biome IDs Registered: " + ForgeRegistries.BIOMES.getKeys().size());
            event.getRight().add("Number of Block IDs Registered: " + ForgeRegistries.BLOCKS.getKeys().size());
            event.getRight().add("Number of Item IDs Registered: " + ForgeRegistries.ITEMS.getKeys().size());
            event.getRight().add("Number of Potion IDs Registered: " + ForgeRegistries.POTIONS.getKeys().size());
            event.getRight().add("Number of Enchantment IDs Registered: " + ForgeRegistries.ENCHANTMENTS.getKeys().size());
            event.getRight().add("Number of Fluid IDs Registered: " + ForgeRegistries.FLUIDS.getKeys().size());
            event.getRight().add("Number of Tile Entities IDs Registered: " + ForgeRegistries.TILE_ENTITIES.getKeys().size());
            event.getRight().add("Number of Entity IDs Registered: " + ForgeRegistries.ENTITIES.getKeys().size());
        }
    }

    public static void createFile(){
        File myObj = new File("MUIDoutput.txt");
        try {
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
