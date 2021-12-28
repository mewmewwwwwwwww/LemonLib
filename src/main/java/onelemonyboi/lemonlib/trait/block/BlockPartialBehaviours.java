package onelemonyboi.lemonlib.trait.block;

import onelemonyboi.lemonlib.trait.behaviour.PartialBehaviour;

public class BlockPartialBehaviours {
    public static PartialBehaviour partialMaterial = new BlockBehaviour.Builder()
            .partial();

    public static PartialBehaviour partialBaseBlock = new BlockBehaviour.Builder().composeFrom(partialMaterial)
            .requiredTraits(BlockTraits.ParticlesTrait.class)
            .requiredTraits(BlockTraits.MaterialTrait.class)
            .requiredTraits(BlockTraits.BlockRenderTypeTrait.class)
            .staticModel()
            .showBreakParticles(true)
            .partial();

    public static PartialBehaviour partialTileBlock = new BlockBehaviour.Builder().composeFrom(partialBaseBlock)
            .requiredTraits(BlockTraits.TileEntityTrait.class)
            .partial();
}
