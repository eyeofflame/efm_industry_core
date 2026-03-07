package efm.dev.efmcore.integration.tinkering.init;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.common.registration.ItemDeferredRegisterExtension;
import slimeknights.tconstruct.library.tools.part.IMaterialItem;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;
import slimeknights.tconstruct.tools.stats.HeadMaterialStats;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class EfmItemsTinker {
    public static final String TINKER_ID = "efmstinker";
    public static final Item.Properties ITEM_PROPS = new Item.Properties().stacksTo(1);

    public static final ItemDeferredRegisterExtension ITEMS = new ItemDeferredRegisterExtension(TINKER_ID);
    public static final ItemObject<ToolPartItem> MAGIC_CORE = ITEMS.register("magic_core", () -> new ToolPartItem(ITEM_PROPS, HeadMaterialStats.ID));

    public static void addTabItems(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output tab) {
        Objects.requireNonNull(tab);
        Consumer<ItemStack> output = tab::accept;
        accept(output, MAGIC_CORE);
    }

    private static void accept(Consumer<ItemStack> output, Supplier<? extends IMaterialItem> item) {
        item.get().addVariants(output, "");
    }
}
