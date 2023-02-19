package com.dawnflyc.creatvirtue.common.capability;

import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

public class CreatVirtueCapability implements INBTSerializable<FloatTag> {

    private Float creatVirtue = 0F;

    public Float getCreatVirtue() {
        return creatVirtue;
    }

    public void setCreatVirtue(Float creatVirtue) {
        this.creatVirtue = creatVirtue;
    }
    public void add(float value){
        this.creatVirtue +=value;
    }
    public void sub(float value){
        this.creatVirtue -=value;
    }

    @Override
    public FloatTag serializeNBT() {
        return FloatTag.valueOf(creatVirtue);
    }

    @Override
    public void deserializeNBT(FloatTag nbt) {
        this.creatVirtue = nbt.getAsFloat();
    }
}
