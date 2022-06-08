package onelemonyboi.lemonlib.trait.block;

import onelemonyboi.lemonlib.trait.behaviour.PartialBehaviour;

public class BlockPartialBehaviours {
    public static PartialBehaviour partialMaterial = new BlockBehavior.Builder()
            .partial();

    public static PartialBehaviour partialBaseBlock = new BlockBehavior.Builder().composeFrom(partialMaterial)
            .requiredTraits(BlockTraits.ParticlesTrait.class)
            .requiredTraits(BlockTraits.MaterialTrait.class)
            .requiredTraits(BlockTraits.BlockRenderTypeTrait.class)
            .staticModel()
            .showBreakParticles(true)
            .partial();

    public static PartialBehaviour partialTileBlock = new BlockBehavior.Builder().composeFrom(partialBaseBlock)
            .requiredTraits(BlockTraits.TileEntityTrait.class)
            .partial();
}
