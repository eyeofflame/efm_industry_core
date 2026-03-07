package efm.dev.efmcore.mixin.doubleJump;

import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.extensions.IForgeLivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = LivingEntity.class, remap = true)
public interface LivingEntityAccessor extends Attackable, IForgeLivingEntity {
    @Accessor(value = "noJumpDelay", remap = true)
    int getNoJumpDelay();

    @Accessor(value = "noJumpDelay", remap = true)
    void setNoJumpDelay(int delay);
}
