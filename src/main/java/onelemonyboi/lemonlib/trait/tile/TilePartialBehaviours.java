package onelemonyboi.lemonlib.trait.tile;

import onelemonyboi.lemonlib.trait.behaviour.PartialBehaviour;

public class TilePartialBehaviours {
    public static PartialBehaviour partialBaseTile = new TileBehaviour.Builder()
            .partial();

    public static PartialBehaviour partialEnergyTile = new TileBehaviour.Builder().composeFrom(partialBaseTile)
            .requiredTraits(TileTraits.PowerTrait.class)
            .partial();
}
