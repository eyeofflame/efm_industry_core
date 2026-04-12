package efm.dev.efmcore.common.registry.mob_effect;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import org.jetbrains.annotations.NotNull;

public class UndeaderEffect extends MobEffect {
    public UndeaderEffect() {
        super(MobEffectCategory.HARMFUL, 0x15270e);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("efm.effect.undeader");
    }
}
