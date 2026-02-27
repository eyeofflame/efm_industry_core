package efm.dev.efmcore.client;

import efm.dev.efmcore.Efmcore;
import efm.dev.efmcore.common.untils.EfmHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin(Efmcore.MODID)
public class JadePlugin implements IWailaPlugin {
    public static final ResourceLocation ID = EfmHelper.buildRes(Efmcore.MODID, "info");

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerEntityDataProvider(
                new AbsorptionInfoServerDataProvider(),
                LivingEntity.class
        );
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerEntityComponent(
                new AbsorptionInfoProvider(),
                LivingEntity.class
        );
    }
}
