package com.mystic.muid;

import com.mystic.muid.command.ModCommand;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod("muid")
@Mod.EventBusSubscriber(modid = "muid", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MUID
{
    public MUID() {}

    @SubscribeEvent
    public static void serverLoad(ServerStartingEvent event) {
        ModCommand.register(event.getServer().getCommands().getDispatcher());
    }
}
