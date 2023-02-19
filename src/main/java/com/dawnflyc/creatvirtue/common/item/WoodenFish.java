package com.dawnflyc.creatvirtue.common.item;

import com.dawnflyc.creatvirtue.common.capability.CreatVirtueCapabilityProvider;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.IForgeRegistry;
import org.w3c.dom.Text;

import java.util.Random;
import java.util.UUID;

public class WoodenFish extends Item {

    public static final WoodenFish item = new WoodenFish();


    public WoodenFish() {
        super(new Properties().tab(CreativeModeTab.TAB_MISC));
    }



    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(level.isClientSide) return InteractionResultHolder.success(player.getItemInHand(hand));
        player.getCapability(CreatVirtueCapabilityProvider.CREAT_VIRTUE_CAPABILITY).ifPresent(creatVirtueCapability -> {
            creatVirtueCapability.add(0.05F);
        });
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}
