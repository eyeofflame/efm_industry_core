package efm.dev.efmcore.mixin.minecraft;

import efm.dev.efmcore.common.registry.EfmModRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(value = LivingEntity.class, remap = true)
public abstract class LivingEntityMixin extends Entity {
    @Shadow
    public abstract float getHealth();

    @Shadow
    @Nullable
    public abstract MobEffectInstance getEffect(MobEffect pEffect);

    public LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "setHealth", at = @At("HEAD"), cancellable = true,remap = true)
    private void setHealthMixin(float pHealth, CallbackInfo ci) {
        if (pHealth >= this.getHealth() && this.getEffect(EfmModRegistry.CURSE_EFM.get()) != null) {
            ci.cancel();
        }
    }
}
