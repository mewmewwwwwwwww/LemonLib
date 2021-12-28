package onelemonyboi.lemonlib.blocks.tile;

import lombok.SneakyThrows;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import onelemonyboi.lemonlib.annotations.SaveInNBT;
import onelemonyboi.lemonlib.trait.behaviour.Behaviour;
import onelemonyboi.lemonlib.trait.behaviour.IHasBehaviour;
import onelemonyboi.lemonlib.trait.tile.TileBehaviour;
import onelemonyboi.lemonlib.trait.tile.TileTraits;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.stream.Stream;

// Based On: TileEntityImpl by Ellpeck - https://github.com/Ellpeck/NaturesAura/blob/main/src/main/java/de/ellpeck/naturesaura/blocks/tiles/TileEntityImpl.java

public class TileBase extends TileEntity implements IHasBehaviour {
    TileBehaviour behaviour;

    public TileBase(TileEntityType<?> tileEntityTypeIn, TileBehaviour behaviour) {
        super(tileEntityTypeIn);
        this.behaviour = behaviour.copy();

        behaviour.tweak(this);
    }

    @SneakyThrows
    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        if (behaviour.has(TileTraits.PowerTrait.class)) {
            behaviour.getRequired(TileTraits.PowerTrait.class).getEnergyStorage().write(nbt);
        }
        if (behaviour.has(TileTraits.ItemTrait.class)) {
            nbt.put("itemSH", behaviour.getRequired(TileTraits.ItemTrait.class).getItemStackHandler().serializeNBT());
        }

        for (Field f : this.getClass().getDeclaredFields()) {
            if (!f.isAnnotationPresent(SaveInNBT.class)) continue;
            f.setAccessible(true);
            Object obj = f.get(this);
            String name = f.getAnnotation(SaveInNBT.class).key();
            String className = obj.getClass().getSimpleName();
            if (className.equals("String")) nbt.putString(name, (String) obj);
            else if (className.equals("int") || className.equals("Integer")) nbt.putInt(name, (Integer) obj);
            else if (className.equals("double") || className.equals("Double")) nbt.putDouble(name, (Double) obj);
            else if (className.equals("float") || className.equals("Float")) nbt.putFloat(name, (Float) obj);
            f.setAccessible(false);
        }

        return super.write(nbt);
    }

    @SneakyThrows
    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        if (behaviour.has(TileTraits.PowerTrait.class)) {
            behaviour.getRequired(TileTraits.PowerTrait.class).getEnergyStorage().read(nbt);
        }
        if (behaviour.has(TileTraits.ItemTrait.class)) {
            behaviour.getRequired(TileTraits.ItemTrait.class).getItemStackHandler().deserializeNBT(nbt.getCompound("itemSH"));
        }

        for (Field f : this.getClass().getDeclaredFields()) {
            if (!f.isAnnotationPresent(SaveInNBT.class)) continue;
            f.setAccessible(true);
            String name = f.getAnnotation(SaveInNBT.class).key();
            String className = f.get(this).getClass().getSimpleName();
            if (className.equals("String")) f.set(this, nbt.getString(name));
            else if (className.equals("int") || className.equals("Integer")) f.set(this, nbt.getInt(name));
            else if (className.equals("double") || className.equals("Double")) f.set(this, nbt.getDouble(name));
            else if (className.equals("float") || className.equals("Float")) f.set(this, nbt.getFloat(name));
            f.setAccessible(false);
        }

        super.read(state, nbt);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY && behaviour.has(TileTraits.PowerTrait.class))
            return behaviour.getRequired(TileTraits.PowerTrait.class).getLazyEnergyStorage().cast();

        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && behaviour.has(TileTraits.ItemTrait.class)) {
            return behaviour.getRequired(TileTraits.ItemTrait.class).getLazyItemStackHandler().cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void remove() {
        super.remove();
        if (behaviour.has(TileTraits.PowerTrait.class))
            behaviour.getRequired(TileTraits.PowerTrait.class).getLazyEnergyStorage().invalidate();
        if (behaviour.has(TileTraits.ItemTrait.class))
            behaviour.getRequired(TileTraits.ItemTrait.class).getLazyItemStackHandler().invalidate();
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.getPos(), 514, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.read(world.getBlockState(pkt.getPos()), pkt.getNbtCompound());
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        this.read(state, tag);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    public void sendToClients() {
        if (!world.isRemote) {
            ServerWorld world = (ServerWorld) this.getWorld();
            Stream<ServerPlayerEntity> entities = world.getChunkProvider().chunkManager.getTrackingPlayers(new ChunkPos(this.getPos()), false);
            SUpdateTileEntityPacket packet = this.getUpdatePacket();
            entities.forEach(e -> e.connection.sendPacket(packet));
        }
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}