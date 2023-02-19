package com.dawnflyc.creatvirtue.common.capability;

import com.dawnflyc.creatvirtue.CreatVirtue;
import net.minecraft.core.Direction;
import net.minecraft.nbt.FloatTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CreatVirtueCapabilityProvider implements ICapabilitySerializable<FloatTag> {

    public static final Capability<CreatVirtueCapability> CREAT_VIRTUE_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {

    });

    private CreatVirtueCapability capability;

    private CreatVirtueCapability createCapability() {
        if (capability == null) {
            this.capability = new CreatVirtueCapability();
        }
        return capability;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return getCapability(cap);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap == CREAT_VIRTUE_CAPABILITY) {
            return LazyOptional.of(this::createCapability).cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public FloatTag serializeNBT() {
        return createCapability().serializeNBT();
    }

    @Override
    public void deserializeNBT(FloatTag nbt) {
        createCapability().deserializeNBT(nbt);
    }
}
