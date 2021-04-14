package onelemonyboi.lemonlib.items;

import net.minecraft.block.Block;
import net.minecraft.item.BlockNamedItem;

public class ItemSeeds extends BlockNamedItem {
    public ItemSeeds(Block crop)
    {
        super(crop, new Properties().maxStackSize(64));
    }
}
