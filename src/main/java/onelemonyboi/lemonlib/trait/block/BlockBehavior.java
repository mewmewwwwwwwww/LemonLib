package onelemonyboi.lemonlib.trait.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import onelemonyboi.lemonlib.trait.behaviour.Behaviour;
import org.apache.commons.lang3.function.TriFunction;

import java.util.function.Function;

public class BlockBehavior extends Behaviour {
    public static class Builder extends Behaviour.Builder<BlockBehavior, Builder> {
        public Builder() {
            super(new BlockBehavior());
        }

        public Builder showBreakParticles(boolean showBreakParticles) {
            return this.with(new BlockTraits.ParticlesTrait(showBreakParticles));
        }

        public Builder staticModel() {
            return this.with(new BlockTraits.BlockRenderTypeTrait(RenderShape.MODEL));
        }

        public Builder animatedModel() {
            return this.with(new BlockTraits.BlockRenderTypeTrait(RenderShape.ENTITYBLOCK_ANIMATED));
        }

        public <T extends BlockEntity> Builder tileEntity(TriFunction<Block, BlockPos, BlockState, T> function) {
            return this.with(new BlockTraits.TileEntityTrait<T>(function));
        }

        public Builder rotation(BlockTraits.RotationType rotation) {
            return this.with(new BlockTraits.BlockRotationTrait(rotation));
        }
    }
}
