package onelemonyboi.lemonlib.items;

import net.minecraft.block.Block;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.ItemGroup;

import net.minecraft.item.Item.Properties;

public class ItemSeeds extends BlockNamedItem {
    public ItemSeeds(Block crop, ItemGroup group)
    {
        super(crop, new Properties().stacksTo(64).tab(group));
    }
}