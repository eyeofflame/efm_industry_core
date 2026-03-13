package efm.dev.efmcore.mixin.minecraft;

import efm.dev.efmcore.common.registry.EfmModRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = WitherBoss.class, remap = true)
public abstract class WitherMixin extends Monster {
    protected WitherMixin(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "addEffect*", at = @At("TAIL"), remap = true, cancellable = true)
    private void canApplyCurse(MobEffectInstance pEffectInstance, Entity pEntity, CallbackInfoReturnable<Boolean> cir) {
        if (pEffectInstance.getEffect().equals(EfmModRegistry.CURSE_EFM.get())) {
            super.forceAddEffect(pEffectInstance, this);
            cir.setReturnValue(true);
        }
    }
}
