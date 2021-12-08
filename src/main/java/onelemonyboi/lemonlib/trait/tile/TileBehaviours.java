package onelemonyboi.lemonlib.trait.tile;


public class TileBehaviours {
    public static TileBehaviour base = new TileBehaviour.Builder()
            .composeFrom(TilePartialBehaviours.partialBaseTile)
            .build();
}
