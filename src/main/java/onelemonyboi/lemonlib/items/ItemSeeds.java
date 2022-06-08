package onelemonyboi.lemonlib.items;

import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.CreativeModeTab;

import net.minecraft.world.item.Item.Properties;

public class ItemSeeds extends ItemNameBlockItem {
    public ItemSeeds(Block crop, CreativeModeTab group)
    {
        super(crop, new Properties().stacksTo(64).tab(group));
    }
}