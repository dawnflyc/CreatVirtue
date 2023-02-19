package com.dawnflyc.creatvirtue.common.item;

import com.dawnflyc.creatvirtue.CreatVirtue;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CreatVirtue.modId);
    public static final RegistryObject<Item> beads = ITEMS.register("beads", () -> Beads.item);
    public static final RegistryObject<Item> woodenFish = ITEMS.register("wooden_fish", () -> WoodenFish.item);


}
