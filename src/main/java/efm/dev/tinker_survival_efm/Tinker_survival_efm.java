package efm.dev.tinker_survival_efm;

import com.mojang.logging.LogUtils;
import efm.dev.tinker_survival_efm.config.EfmConfig;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Tinker_survival_efm.MODID)
public class Tinker_survival_efm {
    public static final String MODID = "tinker_survival_efm";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Tinker_survival_efm(FMLJavaModLoadingContext context) {
        IEventBus ibus = context.getModEventBus();

        context.registerConfig(ModConfig.Type.COMMON,EfmConfig.SPEC);
    }
}
