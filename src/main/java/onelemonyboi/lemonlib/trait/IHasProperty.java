package onelemonyboi.lemonlib.trait;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;

public interface IHasProperty {
    void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder);
    BlockState modifyDefaultState(BlockState state);
}
