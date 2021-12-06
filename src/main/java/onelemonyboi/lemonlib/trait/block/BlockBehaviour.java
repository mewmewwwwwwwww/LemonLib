package onelemonyboi.lemonlib.trait.block;

import net.minecraft.block.BlockRenderType;
import net.minecraft.state.Property;
import net.minecraft.tileentity.TileEntity;
import onelemonyboi.lemonlib.trait.behaviour.Behaviour;

import java.util.Map;

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

        public Builder tileEntity(Class<? extends TileEntity> tileEntityType, Object... args) {
            return this.with(new BlockTraits.TileEntityTrait<>(tileEntityType, args));
        }

        public Builder rotation(BlockTraits.RotationType rotation) {
            return this.with(new BlockTraits.BlockRotationTrait(rotation));
        }

        public Builder property(Map<Property<?>, Object> propertyMap) {
            return this.with(new BlockTraits.PropertyTrait(propertyMap));
        }

        public Builder keepNBTOnBreak() {
            return this.with(new BlockTraits.KeepNBTOnBreakTrait(true));
        }
    }
}
