package efm.dev.efmcore.common.registry.mob_effect;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import org.jetbrains.annotations.NotNull;

public class DoubleJumpMobEffect extends MobEffect {
    public DoubleJumpMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x6beafd);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("effect.efm.double.jump");
    }
}
