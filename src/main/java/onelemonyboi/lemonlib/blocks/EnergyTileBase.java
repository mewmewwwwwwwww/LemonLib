package onelemonyboi.lemonlib.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import onelemonyboi.lemonlib.handlers.CustomEnergyStorage;

public abstract class EnergyTileBase extends TileBase {
    public final CustomEnergyStorage energy;
    public LazyOptional<IEnergyStorage> lazyEnergy;

    public EnergyTileBase(TileEntityType<?> tileEntityTypeIn, int cap, int receive, int extract) {
        super(tileEntityTypeIn);
        energy = new CustomEnergyStorage(cap, receive, extract, receive != 0, extract != 0);
        lazyEnergy = LazyOptional.of(() -> energy);
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        energy.write(nbt);
        return super.write(nbt);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        energy.read(nbt);
        super.read(state, nbt);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if(cap == CapabilityEnergy.ENERGY && !world.isRemote)
            return lazyEnergy.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void remove() {
        super.remove();
        lazyEnergy.invalidate();
    }
}