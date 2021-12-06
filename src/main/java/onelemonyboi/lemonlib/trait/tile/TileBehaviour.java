package onelemonyboi.lemonlib.trait.tile;

import onelemonyboi.lemonlib.handlers.MUItemStackHandler;
import onelemonyboi.lemonlib.trait.Trait;
import onelemonyboi.lemonlib.trait.behaviour.Behaviour;

public class TileBehaviour extends Behaviour {
    public static class Builder extends Behaviour.Builder<TileBehaviour, Builder> {
        public Builder() {
            super(new TileBehaviour());
        }

        public Builder powerTrait(int capacity) {
            return this.with(new TileTraits.PowerTrait(capacity));
        }

        public Builder powerTrait(int capacity, int maxTransfer) {
            return this.with(new TileTraits.PowerTrait(capacity, maxTransfer));
        }

        public Builder powerTrait(int capacity, int maxReceive, int maxExtract) {
            return this.with(new TileTraits.PowerTrait(capacity, maxReceive, maxExtract));
        }

        public Builder powerTrait(int capacity, int maxReceive, int maxExtract, int energy) {
            return this.with(new TileTraits.PowerTrait(capacity, maxReceive, maxExtract, energy));
        }

        public Builder itemTrait(int slots) {
            return this.with(new TileTraits.ItemTrait(slots));
        }

        public Builder itemTrait(MUItemStackHandler handler) {
            return this.with(new TileTraits.ItemTrait(handler));
        }
    }

    public TileBehaviour copy() {
        Builder builder = new Builder();
        for (Trait trait : this.traits.values()) {
            try {
                builder = builder.with((Trait) trait.clone());
            } catch (CloneNotSupportedException e) {
                builder = builder.with(trait);
            }
        }
        return builder.build();
    }
}
