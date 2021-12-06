package onelemonyboi.lemonlib.blocks.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import onelemonyboi.lemonlib.trait.behaviour.Behaviour;
import onelemonyboi.lemonlib.trait.behaviour.IHasBehaviour;
import onelemonyboi.lemonlib.trait.block.BlockBehaviour;
import onelemonyboi.lemonlib.trait.block.BlockTraits.*;

import javax.annotation.Nullable;

public class BlockBase extends Block implements IHasBehaviour {
    BlockBehaviour behaviour;

    public BlockBase(AbstractBlock.Properties properties, BlockBehaviour behaviour) {
        super(properties);
        this.behaviour = behaviour;

        behaviour.tweak(this);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        if (behaviour.has(BlockRenderTypeTrait.class))
            return behaviour.getRequired(BlockRenderTypeTrait.class).getBlockRenderType();
        return super.getRenderType(state);
    }

    @Override
    public boolean addDestroyEffects(BlockState state, World world, BlockPos pos, ParticleManager manager) {
        return !behaviour.getRequired(ParticlesTrait.class).isShowBreakParticles();
    }

    @Override
    public boolean addHitEffects(BlockState state, World world, RayTraceResult target, ParticleManager manager) {
        return !behaviour.getRequired(ParticlesTrait.class).isShowBreakParticles();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return behaviour.has(TileEntityTrait.class);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return behaviour.getRequired(TileEntityTrait.class).createTileEntity();
    }

    @Override
    @Deprecated
    public BlockState rotate(BlockState state, Rotation rotation) {
        if (behaviour.has(BlockRotationTrait.class)) {
            BlockRotationTrait trait = behaviour.getRequired(BlockRotationTrait.class);
            return state.with(trait.getDirectionProperty(), rotation.rotate(state.get(trait.getDirectionProperty())));
        }
        return super.rotate(state, rotation);
    }

    @Override
    @Deprecated
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        if (behaviour.has(BlockRotationTrait.class)) {
            BlockRotationTrait trait = behaviour.getRequired(BlockRotationTrait.class);
            return state.rotate(mirrorIn.toRotation(state.get(trait.getDirectionProperty())));
        }
        return super.mirror(state, mirrorIn);
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        if (behaviour.has(BlockRotationTrait.class)) {
            BlockRotationTrait trait = behaviour.getRequired(BlockRotationTrait.class);
            builder.add(trait.getDirectionProperty());
        }
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        if (behaviour.has(BlockRotationTrait.class)) {
            BlockRotationTrait trait = behaviour.getRequired(BlockRotationTrait.class);
            return trait.getStateForPlacement(this, context);
        }
        return super.getStateForPlacement(context);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (worldIn.isRemote()) {
            return;
        }

        if (behaviour.has(KeepNBTOnBreakTrait.class) && behaviour.getRequired(KeepNBTOnBreakTrait.class).isStoreNBTData()) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            ItemStack itemStack = new ItemStack(this);
            CompoundNBT compoundNBT = tileEntity.write(new CompoundNBT());
            compoundNBT.remove("x");
            compoundNBT.remove("y");
            compoundNBT.remove("z");
            itemStack.setTagInfo("BlockEntityTag", compoundNBT);
            InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), itemStack);
        }

        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}
