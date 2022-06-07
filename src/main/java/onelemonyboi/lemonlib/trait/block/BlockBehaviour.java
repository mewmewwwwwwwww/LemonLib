package onelemonyboi.lemonlib.trait.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.state.Property;
import net.minecraft.tileentity.TileEntity;
import onelemonyboi.lemonlib.trait.behaviour.Behaviour;

import java.util.Map;
import java.util.function.Function;

public class BlockBehaviour extends Behaviour {
    public static class Builder extends Behaviour.Builder<BlockBehaviour, Builder> {
        public Builder() {
            super(new BlockBehaviour());
        }

        public Builder showBreakParticles(boolean showBreakParticles) {
            return this.with(new BlockTraits.ParticlesTrait(showBreakParticles));
        }

        public Builder staticModel() {
            return this.with(new BlockTraits.BlockRenderTypeTrait(BlockRenderType.MODEL));
        }

        public Builder animatedModel() {
            return this.with(new BlockTraits.BlockRenderTypeTrait(BlockRenderType.ENTITYBLOCK_ANIMATED));
        }

        public <T extends TileEntity> Builder tileEntity(Function<Block, T> function) {
            return this.with(new BlockTraits.TileEntityTrait<T>(function));
        }

        public Builder rotation(BlockTraits.RotationType rotation) {
            return this.with(new BlockTraits.BlockRotationTrait(rotation));
        }
    }
}
