package onelemonyboi.lemonlib.trait.block;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.ToolType;
import onelemonyboi.lemonlib.trait.Trait;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@UtilityClass
public class BlockTraits {
    public abstract static class MaterialTrait extends Trait {
        protected MaterialTrait() {
            this.addTweaker(AbstractBlock.Properties.class, this::tweakProperties);
        }
        protected abstract void tweakProperties(AbstractBlock.Properties properties);
    }

    public static class MachineMaterialTrait extends MaterialTrait{
        @Override
        protected void tweakProperties(AbstractBlock.Properties properties) {
            properties.hardnessAndResistance(4f).harvestLevel(2).harvestTool(ToolType.PICKAXE).notSolid().setRequiresTool();
        }
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
    
    public static class BlockRotationTrait extends Trait {

    	protected final RotationType rotationType;

    	public BlockRotationTrait(RotationType rotationType) {
    		this.rotationType = rotationType;
    	}

        public BlockState getStateForPlacement(Block block, BlockItemUseContext context) {
        	Direction dir;
        	if (rotationType == RotationType.XZ)
        		dir = context.getPlacementHorizontalFacing().getOpposite();
        	else
        	   	dir = context.getPlayer().isSneaking() ? context.getNearestLookingDirection().getOpposite() : context.getNearestLookingDirection();

        	return block.getDefaultState().with(rotationType.direction, dir);
        }

        public void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
            builder.add(rotationType.direction);
        }

        public DirectionProperty getDirectionProperty() {
        	return rotationType.direction;
        }
    }

    public static class PropertyTrait extends Trait {

        private final Map<Property<?>, Object> propertyMap;

        public PropertyTrait(Property<?> property, Object defaultValue) {
            this.propertyMap = Collections.singletonMap(property, defaultValue);
        }

        public PropertyTrait(Property<?> p1, Object d1, Property<?> p2, Object d2) {
            this.propertyMap = new HashMap<Property<?>, Object>(){{
                put(p1, d1);
                put(p2, d2);
            }};
        }

        public PropertyTrait(Map<Property<?>, Object> map) {
            this.propertyMap = map;
        }

        public void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
            propertyMap.keySet().forEach(builder::add);
        }
    }

	public enum RotationType {
        XZ(BlockStateProperties.HORIZONTAL_FACING),
		XYZ(BlockStateProperties.FACING);

		DirectionProperty direction;

		RotationType(DirectionProperty dir) {
			direction = dir;
		}
	}
}
