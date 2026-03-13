package efm.dev.efmcore.mixin.minecraft;

import efm.dev.efmcore.common.efm_events.ItemBurnEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemEntity.class,remap = true)
public abstract class ItemEntityMixin {
    @Inject(method = "hurt", at = @At(value = "HEAD"), remap = true, cancellable = true)
    private void noFireItem(DamageSource pSource, float pAmount, CallbackInfoReturnable<Boolean> cir) {
        ItemEntity entity = (ItemEntity) (Object) this;

        if (entity.level().isClientSide) {
            return;
        }

        if (pSource.equals(entity.level().damageSources().onFire()) || pSource.equals(entity.level().damageSources().inFire())) {
            ItemBurnEvent event = new ItemBurnEvent(entity, pSource, pAmount);
            boolean check = MinecraftForge.EVENT_BUS.post(event);

            if (check) {
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }
}
