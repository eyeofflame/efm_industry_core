package efm.dev.efmcore.client;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IServerDataProvider;

public class AbsorptionInfoServerDataProvider implements IServerDataProvider<EntityAccessor> {
    @Override
    public ResourceLocation getUid() {
        return JadePlugin.ID;
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, EntityAccessor entityAccessor) {
        Entity entity = entityAccessor.getEntity();
        compoundTag.putInt("efm_core", (int) ((LivingEntity) entity).getAbsorptionAmount());
    }
}
