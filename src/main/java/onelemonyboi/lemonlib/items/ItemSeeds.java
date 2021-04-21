package onelemonyboi.lemonlib.items;

import net.minecraft.block.Block;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item.Properties;

public class ItemSeeds extends BlockNamedItem {
    public ItemSeeds(Block crop)
    {
        super(crop, new Properties().maxStackSize(64));
    }
}