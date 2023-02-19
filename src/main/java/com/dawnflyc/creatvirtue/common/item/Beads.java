package com.dawnflyc.creatvirtue.common.item;

import com.dawnflyc.creatvirtue.common.capability.CreatVirtueCapabilityProvider;
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

import java.util.UUID;

public class Beads extends Item {

    public static final Beads item = new Beads();
    public Beads() {
        super(new Properties().tab(CreativeModeTab.TAB_MISC));
    }

//    @Override
//    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
//        if(entity instanceof Player player){
//            if(player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == this  || player.getItemInHand(InteractionHand.OFF_HAND).getItem() == this){
//                if (player.getRandom().nextInt(100)==0){
//                    player.getCapability(CreatVirtueCapabilityProvider.CREAT_VIRTUE_CAPABILITY).ifPresent(creatVirtueCapability -> {
//                        creatVirtueCapability.add(0.05F);
//                    });
//                }
//            }
//        }
//    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(level.isClientSide) return InteractionResultHolder.success(player.getItemInHand(hand));
        player.getCapability(CreatVirtueCapabilityProvider.CREAT_VIRTUE_CAPABILITY).ifPresent(creatVirtueCapability -> {
            player.sendMessage(new TranslatableComponent("text.item.capa_text",creatVirtueCapability.getCreatVirtue()), player.getUUID());
        });
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }



}
