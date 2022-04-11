package onelemonyboi.lemonlib.trait.block;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import onelemonyboi.lemonlib.trait.IHasProperty;
import onelemonyboi.lemonlib.trait.Trait;

import java.util.function.Function;

@UtilityClass
public class BlockTraits {
    public abstract static class MaterialTrait extends Trait {
        protected MaterialTrait() {
            this.addTweaker(AbstractBlock.Properties.class, this::tweakProperties);
        }
        protected abstract void tweakProperties(AbstractBlock.Properties properties);
    }

    public static class TileEntityTrait<T extends TileEntity> extends Trait {
        private final Function<Block, T> function;

        public TileEntityTrait(Function<Block, T> function) {
            this.function = function;
        }

        @SneakyThrows
        public T createTileEntity(Block block) {
            return function.apply(block);
        }
    }

    @Data
    public static class ParticlesTrait extends Trait {
        private final boolean showBreakParticles;
    }

    @Data
    public static class BlockRenderTypeTrait extends Trait {
        private final BlockRenderType blockRenderType;
    }

    @Data
    public static class KeepNBTOnBreakTrait extends Trait {
        private final boolean storeNBTData;
    }
    
    public static class BlockRotationTrait extends Trait implements IHasProperty {

    	protected final RotationType rotationType;

    	public BlockRotationTrait(RotationType rotationType) {
    		this.rotationType = rotationType;
    	}

        public BlockState getStateForPlacement(Block block, BlockItemUseContext context) {
        	Direction dir;
        	switch (rotationType) {
                case XZ:
                    dir = context.getPlacementHorizontalFacing().getOpposite();
                    break;
                case XYZ:
                default:
                    dir = context.getPlayer().isSneaking() ? context.getNearestLookingDirection().getOpposite() : context.getNearestLookingDirection();
            }

        	return block.getDefaultState().with(rotationType.direction, dir);
        }

        public void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
            builder.add(rotationType.direction);
        }

        public BlockState modifyDefaultState(BlockState state) {
    	    return state.with(rotationType.direction, rotationType.defaultDir);
        }

        public DirectionProperty getDirectionProperty() {
        	return rotationType.direction;
        }
    }

	public enum RotationType {
        XZ(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH),
		XYZ(BlockStateProperties.FACING, Direction.NORTH);

		DirectionProperty direction;
        Direction defaultDir;

		RotationType(DirectionProperty dir, Direction def) {
			direction = dir;
			defaultDir = def;
		}
	}
}
