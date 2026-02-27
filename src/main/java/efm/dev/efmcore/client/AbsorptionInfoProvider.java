package efm.dev.efmcore.client;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class AbsorptionInfoProvider implements IEntityComponentProvider {
    @Override
    public ResourceLocation getUid() {
        return JadePlugin.ID;
    }

    @Override
    public void appendTooltip(ITooltip iTooltip, EntityAccessor entityAccessor, IPluginConfig iPluginConfig) {
        iTooltip.add(Component.literal("护盾值：" + Math.max(entityAccessor.getServerData().getInt("efm_core"), 0)).withStyle(ChatFormatting.YELLOW).withStyle(ChatFormatting.BOLD));
    }
}
