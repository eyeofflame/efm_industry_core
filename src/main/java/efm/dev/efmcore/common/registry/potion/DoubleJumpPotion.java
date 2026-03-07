package efm.dev.efmcore.common.registry.potion;

import efm.dev.efmcore.common.registry.EfmModRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import org.jetbrains.annotations.NotNull;

public class DoubleJumpPotion extends Potion {
    public DoubleJumpPotion(int duration, int ampl) {
        super(new MobEffectInstance(EfmModRegistry.DOUBLEJUMP_EFFECT.get(), duration, ampl));
    }

    @Override
    public @NotNull String getName(@NotNull String pPrefix) {
        return pPrefix + "efm_doublejump_potion";
    }
}
