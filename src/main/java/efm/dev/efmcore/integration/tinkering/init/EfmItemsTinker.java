package efm.dev.efmcore.integration.tinkering.init;

import net.minecraft.world.item.Item;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.common.registration.ItemDeferredRegisterExtension;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;
import slimeknights.tconstruct.tools.stats.HeadMaterialStats;

public class EfmItemsTinker {
    public static final String TINKER_ID = "efmstinker";

    public static final ItemDeferredRegisterExtension ITEMS = new ItemDeferredRegisterExtension(TINKER_ID);

    public static final Item.Properties ITEM_PROPS = new Item.Properties();

    public static final ItemObject<ToolPartItem> EFM_TOOL_PART = ITEMS.register("efm_tool_part", () -> new ToolPartItem(ITEM_PROPS, HeadMaterialStats.ID));
}
