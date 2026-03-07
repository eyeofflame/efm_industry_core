package efm.dev.efmcore.common.registry.mob_effect;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import org.jetbrains.annotations.NotNull;

public class CursedEfm extends MobEffect {
    public CursedEfm() {
        super(MobEffectCategory.HARMFUL, 0x5d4a62);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("efm.effect.curse");
    }
}
