package onelemonyboi.lemonlib;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class CustomEnergyStorage extends EnergyStorage {
    Boolean canReceive = true;
    Boolean canExtract = true;

    public CustomEnergyStorage(int capacity) {
        super(capacity, capacity, capacity, 0);
    }

    public CustomEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer, maxTransfer, 0);
    }

    public CustomEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract, 0);
    }

    public CustomEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    public CustomEnergyStorage(int capacity, int maxReceive, int maxExtract, Boolean canReceive, Boolean canExtract) {
        super(capacity, maxReceive, maxExtract, 0);
        this.canReceive = canReceive;
        this.canExtract = canExtract;
    }

    public CompoundNBT write(CompoundNBT nbt) {
        nbt.putInt("energy", energy);
        return nbt;
    }

    public void read(CompoundNBT nbt) {
        setEnergy(nbt.getInt("energy"));
    }

    @Override
    public boolean canExtract() {
        return this.canExtract;
    }

    @Override
    public boolean canReceive() {
        return this.canReceive;
    }

    public void setCanExtract(Boolean bool) {
        this.canExtract = bool;
    }

    public void setCanReceive(Boolean bool) {
        this.canReceive = bool;
    }

    public void setMaxExtract(Integer num) {
        this.maxExtract = num;
    }

    public void setMaxRecieve(Integer num) {
        this.maxReceive = num;
    }

    public int internalProduceEnergy(int produce) {
        int energyRecieved = Math.min(capacity - energy, Math.min(maxReceive, produce));
        energy += energyRecieved;
        return energyRecieved;
    }

    public int internalConsumeEnergy(int consume) {
        int energyRecieved = Math.min(maxExtract, Math.min(energy, consume));
        energy -= energyRecieved;
        return energyRecieved;
    }

    public int simulateInternalProduceEnergy(int produce) {
        return Math.min(capacity - energy, produce);
    }

    public int simulateInternalConsumeEnergy(int consume) {
        return Math.min(maxExtract, Math.min(energy, consume));
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void outputToSide(World world, BlockPos pos, Direction side, int max) {
        TileEntity te = world.getTileEntity(pos.offset(side));
        if(te == null) {return;}
        LazyOptional<IEnergyStorage> opt = te.getCapability(CapabilityEnergy.ENERGY, side.getOpposite());
        IEnergyStorage ies = opt.orElse(null);
        if(ies == null) {return;}
        int ext = this.internalConsumeEnergy(max);
        int putBack = Math.max(0, ies.receiveEnergy(ext, false));
        this.internalProduceEnergy(ext - putBack);
    }

    @Deprecated
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate)
    {
        if (simulate) {return simulateInternalProduceEnergy(maxReceive);}
        return internalProduceEnergy(maxReceive);
    }

    @Deprecated
    @Override
    public int extractEnergy(int maxExtract, boolean simulate)
    {
        if (simulate) {return simulateInternalConsumeEnergy(maxReceive);}
        return internalConsumeEnergy(maxReceive);
    }

    @Override
    public String toString() {
        return getEnergyStored() + "/" + getMaxEnergyStored();
    }
}
