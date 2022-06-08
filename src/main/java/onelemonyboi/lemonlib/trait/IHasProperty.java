package onelemonyboi.lemonlib.trait;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public interface IHasProperty {
    void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder);
    BlockState modifyDefaultState(BlockState state);
}
