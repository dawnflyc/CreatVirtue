package com.dawnflyc.creatvirtue.common.capability;

import com.dawnflyc.creatvirtue.CreatVirtue;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCapability {

    @SubscribeEvent
    public static void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(CreatVirtueCapabilityProvider.class);
    }

//    @SubscribeEvent
//    public void playerJoin(PlayerEvent.PlayerLoggedInEvent event){
//        event.getEntityLiving()
//    }
}
