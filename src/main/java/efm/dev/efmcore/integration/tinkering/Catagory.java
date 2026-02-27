package efm.dev.efmcore.integration.tinkering;

import efm.dev.efmcore.Efmcore;
import efm.dev.efmcore.integration.tinkering.init.EfmItemsTinker;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Efmcore.MODID)
public class Catagory {
    public static final DeferredRegister<CreativeModeTab> EFM_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Efmcore.MODID);

    public static final RegistryObject<CreativeModeTab> Tab = EFM_TABS.register(Efmcore.MODID,
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("efm.title.zero"))
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                    .displayItems(((pParameters, pOutput) -> {
                        pOutput.accept(EfmItemsTinker.EFM_TOOL_PART.get());
                    }))
                    .icon(() -> new ItemStack(Items.STONE)).build()
    );
}
