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
import onelemonyboi.lemonlib.trait.IHasProperty;
import onelemonyboi.lemonlib.trait.behaviour.Behaviour;
import onelemonyboi.lemonlib.trait.behaviour.IHasBehaviour;
import onelemonyboi.lemonlib.trait.block.BlockBehaviour;
import onelemonyboi.lemonlib.trait.block.BlockTraits.*;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock.Properties;

public class BlockBase extends Block implements IHasBehaviour {
    BlockBehaviour behaviour;

    public BlockBase(AbstractBlock.Properties properties, BlockBehaviour behaviour) {
        super(tweak(behaviour, properties));
        this.behaviour = behaviour;

        StateContainer.Builder<Block, BlockState> builder = new StateContainer.Builder<>(this);
        this.createBlockStateDefinition(builder);
        this.stateDefinition = builder.create(Block::defaultBlockState, BlockState::new);
        this.registerDefaultState(defineDefaultState());

        behaviour.tweak(this);
    }

    public static Properties tweak(BlockBehaviour behaviour, Properties properties) {
        behaviour.tweak(properties);
        return properties;
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        if (behaviour == null) return super.getRenderShape(state);

        if (behaviour.has(BlockRenderTypeTrait.class))
            return behaviour.getRequired(BlockRenderTypeTrait.class).getBlockRenderType();
        return super.getRenderShape(state);
    }

    @Override
    public boolean addDestroyEffects(BlockState state, World world, BlockPos pos, ParticleManager manager) {
        return behaviour.has(ParticlesTrait.class) && !behaviour.getRequired(ParticlesTrait.class).isShowBreakParticles();
    }

    @Override
    public boolean addHitEffects(BlockState state, World world, RayTraceResult target, ParticleManager manager) {
        return behaviour.has(ParticlesTrait.class) && !behaviour.getRequired(ParticlesTrait.class).isShowBreakParticles();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return behaviour.has(TileEntityTrait.class);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return behaviour.getRequired(TileEntityTrait.class).createTileEntity(this);
    }

    @Override
    @Deprecated
    public BlockState rotate(BlockState state, Rotation rotation) {
        if (behaviour == null) return super.rotate(state, rotation);

        if (behaviour.has(BlockRotationTrait.class)) {
            BlockRotationTrait trait = behaviour.getRequired(BlockRotationTrait.class);
            return state.setValue(trait.getDirectionProperty(), rotation.rotate(state.getValue(trait.getDirectionProperty())));
        }
        return super.rotate(state, rotation);
    }

    @Override
    @Deprecated
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        if (behaviour == null) return super.mirror(state, mirrorIn);

        if (behaviour.has(BlockRotationTrait.class)) {
            BlockRotationTrait trait = behaviour.getRequired(BlockRotationTrait.class);
            return state.rotate(mirrorIn.getRotation(state.getValue(trait.getDirectionProperty())));
        }
        return super.mirror(state, mirrorIn);
    }

    public BlockState defineDefaultState() {
        final BlockState[] def = {this.stateDefinition.any()};
        behaviour.getRelated(IHasProperty.class).forEach(t -> def[0] = t.modifyDefaultState(def[0]));
        return def[0];
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        if (behaviour == null) return;

        behaviour.getRelated(IHasProperty.class).forEach(t -> t.createBlockStateDefinition(builder));
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        if (behaviour == null) return super.getStateForPlacement(context);

        if (behaviour.has(BlockRotationTrait.class)) {
            BlockRotationTrait trait = behaviour.getRequired(BlockRotationTrait.class);
            return trait.getStateForPlacement(this, context);
        }
        return super.getStateForPlacement(context);
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}
