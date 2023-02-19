package com.dawnflyc.creatvirtue.common.capability;

import com.dawnflyc.creatvirtue.CreatVirtue;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeCapability {

    private static final Logger LOGGER = LogUtils.getLogger();

    /**
     * 摇点
     *
     * @param player
     * @param good
     * @param bad
     * @return
     */
    public static void shake(Player player, Runnable good, Runnable bad) {
        if (player.getRandom() == null) {
            LOGGER.error("没有随机");
            return;
        }
        player.getCapability(CreatVirtueCapabilityProvider.CREAT_VIRTUE_CAPABILITY).ifPresent(creatVirtueCapability -> {
            Float creatVirtue = creatVirtueCapability.getCreatVirtue();
            Random random = player.getRandom();
            if (creatVirtue > 0 && good != null) {
                creatVirtue = creatVirtue > 100 ? 100 : creatVirtue;
                if (creatVirtue == 100) {
                    good.run();
                } else if (random.nextInt((int) (100.0 - creatVirtue)) == 0) {
                    good.run();
                }
            }
            if (creatVirtue < 0 && bad != null) {
                creatVirtue = creatVirtue < -50 ? -50 : creatVirtue;
                if (random.nextInt(51 - (int) (creatVirtue * -1)) == 0) {
                    bad.run();
                }
            }

        });
    }

    /**
     * 发送消息
     *
     * @param player
     * @param component
     */
    public static void message(Player player, Component component) {
        player.sendMessage(component, player.getUUID());
    }


    /**
     * 附能力
     *
     * @param event
     */
    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (!(event.getObject() instanceof Player)) return;
        event.addCapability(new ResourceLocation(CreatVirtue.modId, CreatVirtue.modId), new CreatVirtueCapabilityProvider());
    }

    /**
     * 喂食增加
     *
     * @param event
     */
    @SubscribeEvent
    public static void feed(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof Animal animal) {
            if (animal.isFood(event.getItemStack())) {
                event.getPlayer().getCapability(CreatVirtueCapabilityProvider.CREAT_VIRTUE_CAPABILITY).ifPresent(creatVirtueCapability -> {
                    creatVirtueCapability.add(0.5F);
                });
            }
        }
    }

    /**
     * 杀
     *
     * @param event
     */
    @SubscribeEvent
    public static void kill(LivingDeathEvent event) {
        if (event.getSource() instanceof EntityDamageSource entityDamageSource) {
            if (entityDamageSource.getEntity() instanceof Player player) {
                if(!(event.getEntity() instanceof Player player1)){
                if (event.getEntity() instanceof Monster && !(event.getEntity() instanceof NeutralMob)) {
                    //怪物
                    player.getCapability(CreatVirtueCapabilityProvider.CREAT_VIRTUE_CAPABILITY).ifPresent(creatVirtueCapability -> {
                        creatVirtueCapability.add(0.5F);
                    });
                }
                if (event.getEntity() instanceof NeutralMob || event.getEntity() instanceof AgeableMob) {
                    //中立或友善
                    player.getCapability(CreatVirtueCapabilityProvider.CREAT_VIRTUE_CAPABILITY).ifPresent(creatVirtueCapability -> {
                        creatVirtueCapability.sub(1F);
                    });
                }
                } else{
                    player.getCapability(CreatVirtueCapabilityProvider.CREAT_VIRTUE_CAPABILITY).ifPresent(creatVirtueCapability -> {
                        player1.getCapability(CreatVirtueCapabilityProvider.CREAT_VIRTUE_CAPABILITY).ifPresent(creatVirtueCapability1 -> {
                            if(creatVirtueCapability.getCreatVirtue()>0 && creatVirtueCapability1.getCreatVirtue()<0){
                                float max = (creatVirtueCapability1.getCreatVirtue()*-1 > 100 ? 100 : creatVirtueCapability1.getCreatVirtue()*-1);
                                creatVirtueCapability.add(max/2);
                            }
                            if(creatVirtueCapability.getCreatVirtue()<0 && creatVirtueCapability1.getCreatVirtue()>0){
                                float max = Math.max(creatVirtueCapability1.getCreatVirtue(), 100);
                                creatVirtueCapability.add(max*-1/2);
                            }
                            if(creatVirtueCapability.getCreatVirtue()>0 && creatVirtueCapability1.getCreatVirtue()>0){
                                float max = Math.max(creatVirtueCapability1.getCreatVirtue(), 100);
                                creatVirtueCapability.sub(max/2);
                            }
                            if(creatVirtueCapability.getCreatVirtue()<0 && creatVirtueCapability1.getCreatVirtue()<0){
                                float max = Math.max(creatVirtueCapability1.getCreatVirtue()*-1, 100);
                                creatVirtueCapability.add(max*-1/2);
                            }
                        });
                    });
                }
            }
        }
    }

    /**
     * 钓鱼
     *
     * @param event
     */
    @SubscribeEvent
    public static void fishd(ItemFishedEvent event) {
        NonNullList<ItemStack> drops = event.getDrops();
        boolean isFish = false;
        for (ItemStack drop : drops) {
            if (drop.is(ItemTags.FISHES)) {
                isFish = true;
            }
        }
        if (isFish) {
            event.getPlayer().getCapability(CreatVirtueCapabilityProvider.CREAT_VIRTUE_CAPABILITY).ifPresent(creatVirtueCapability -> {
                creatVirtueCapability.sub(1F);
            });
        }
    }

    /**
     * 放生
     *
     * @param event
     */
    @SubscribeEvent
    public static void throwItem(ItemTossEvent event) {
//        if(!event.getPlayer().isInWater()) return;
        if (event.getEntityItem().getItem().is(ItemTags.FISHES)) {
            ItemStack item = event.getEntityItem().getItem();
//            event.getEntityItem().level.addFreshEntity();
            for (int i = 0; i < item.getCount(); i++) {
                if (item.getItem() == Items.COD) {
                    Cod cod = new Cod(EntityType.COD, event.getEntityItem().level);
                    cod.setPos(event.getPlayer().getX(), event.getPlayer().getY(), event.getPlayer().getZ());
                    event.getEntityItem().level.addFreshEntity(cod);
                } else if (item.getItem() == Items.SALMON) {
                    Salmon salmon = new Salmon(EntityType.SALMON, event.getEntityItem().level);
                    salmon.setPos(event.getPlayer().getX(), event.getPlayer().getY(), event.getPlayer().getZ());
                    event.getEntityItem().level.addFreshEntity(salmon);

                } else if (item.getItem() == Items.TROPICAL_FISH) {
                    TropicalFish tropicalFish = new TropicalFish(EntityType.TROPICAL_FISH, event.getEntityItem().level);
                    tropicalFish.setPos(event.getPlayer().getX(), event.getPlayer().getY(), event.getPlayer().getZ());
                    event.getEntityItem().level.addFreshEntity(tropicalFish);

                } else if (item.getItem() == Items.PUFFERFISH) {
                    Pufferfish pufferfish = new Pufferfish(EntityType.PUFFERFISH, event.getEntityItem().level);
                    pufferfish.setPos(event.getPlayer().getX(), event.getPlayer().getY(), event.getPlayer().getZ());
                    event.getEntityItem().level.addFreshEntity(pufferfish);
                } else {
                    return;
                }
                event.getPlayer().getCapability(CreatVirtueCapabilityProvider.CREAT_VIRTUE_CAPABILITY).ifPresent(creatVirtueCapability -> {
                    if (event.getPlayer().isInWater()) {
                        creatVirtueCapability.add(0.5F);
                    } else {
                        creatVirtueCapability.add(-1F);
                    }
                });
            }
            event.getEntityItem().remove(Entity.RemovalReason.DISCARDED);
        }
    }

    /**
     * 铁砧
     *
     * @param event
     */
    @SubscribeEvent
    public static void AnvilRepair(AnvilRepairEvent event) {
        event.getPlayer().getCapability(CreatVirtueCapabilityProvider.CREAT_VIRTUE_CAPABILITY).ifPresent(creatVirtueCapability -> {
            shake(event.getPlayer(), () -> {
                message(event.getPlayer(), new TranslatableComponent("text.anvil_good"));
                event.setBreakChance(0);
            }, () -> {
                message(event.getPlayer(), new TranslatableComponent("text.anvil_bad"));
                event.setBreakChance(1);
            });
        });
    }

//    @SubscribeEvent
//    public static void sleep(PlayerSleepInBedEvent event) {
//        if (event.getPlayer().getLevel().isDay()) {
//            event.setResult(Event.Result.DEFAULT);
//            return;
//        }
//        event.getPlayer().getCapability(CreatVirtueCapabilityProvider.CREAT_VIRTUE_CAPABILITY).ifPresent(creatVirtueCapability -> {
//            shake(event.getPlayer(), null, () -> {
//                message(event.getPlayer(), new TranslatableComponent("text.sleep_bad"));
//                event.setResult(Event.Result.DENY);
//            });
//        });
//    }

    private static double agele = 0;
    private static final Map<UUID,Long> time = new HashMap<>();
    //这里增加服务端准确放

    @SubscribeEvent
    public static void tick(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof Player player && !player.isLocalPlayer()) {
            player.getCapability(CreatVirtueCapabilityProvider.CREAT_VIRTUE_CAPABILITY).ifPresent(creatVirtueCapability -> {
                if (!time.containsKey(player.getUUID())){
                    time.put(player.getUUID(),0L);
                }
                if(System.currentTimeMillis() - time.get(player.getUUID()) >500) {
                    if (creatVirtueCapability.getCreatVirtue() > 0) {
                        player.addEffect(new MobEffectInstance(MobEffects.LUCK, 1000/30,0,false,false,false));
                        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 1000/30,(int) Math.floor((creatVirtueCapability.getCreatVirtue()>100 ? 100 : creatVirtueCapability.getCreatVirtue())/10),false,false,false));
                    }
                    if (creatVirtueCapability.getCreatVirtue() < 0) {
                        player.addEffect(new MobEffectInstance(MobEffects.UNLUCK, 1000 / 30,0,false,false,false));
                        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1000/30,(int) Math.floor((creatVirtueCapability.getCreatVirtue()*-1 > 100 ? 100 : creatVirtueCapability.getCreatVirtue()*-1)/10),false,false,false));
                    }
                    time.put(player.getUUID(),System.currentTimeMillis());
                }
            });
        }
        if (event.getEntityLiving() instanceof Player player && player.isLocalPlayer()) {
            if (player.hasEffect(MobEffects.LUCK) && player.hasEffect(MobEffects.UNLUCK)) return;

            //半径 半径越大 速度越快
            double r = 1;
            double x = Math.sin(agele) * r;
            double z = Math.cos(agele) * r;
            if (player.hasEffect(MobEffects.LUCK)) {
                player.getLevel().addParticle(ParticleTypes.WAX_OFF, true, player.getX() + x, player.getY()+0.3, player.getZ() + z, 0, 0, 0);
            }
            if (player.hasEffect(MobEffects.UNLUCK)) {
                player.getLevel().addParticle(ParticleTypes.SQUID_INK, true, player.getX() + x, player.getY()+0.3, player.getZ() + z, 0, 0, 0);
            }
            agele = agele==360 ? 0 : agele+0.3;
        }
    }
}
