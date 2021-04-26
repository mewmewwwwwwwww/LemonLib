package onelemonyboi.lemonlib.items;

import net.minecraft.block.Block;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.ItemGroup;

public class ItemSeeds extends BlockNamedItem {
    public ItemSeeds(Block crop, ItemGroup group)
    {
        super(crop, new Properties().maxStackSize(64).group(group));
    }
}