package efm.dev.efmcore.common.registry;

import efm.dev.efmcore.Efmcore;
import efm.dev.efmcore.common.registry.mob_effect.DoubleJumpMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EfmModRegistry {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Efmcore.MODID);
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, Efmcore.MODID);

    public static final RegistryObject<MobEffect> DOUBLEJUMP_EFFECT = EFFECTS.register("double_jump", DoubleJumpMobEffect::new);
    public static final RegistryObject<Potion> DOUBLEJUMP_POTION1 = POTIONS.register("double_jump_potion", () -> new Potion(new MobEffectInstance(DOUBLEJUMP_EFFECT.get(), 60 * 20, 0)));
    public static final RegistryObject<Potion> DOUBLEJUMP_POTION2 = POTIONS.register("double_jump_potion_level1", () -> new Potion(new MobEffectInstance(DOUBLEJUMP_EFFECT.get(), 5 * 60 * 20, 0)));

    public static void register(IEventBus bus) {
        EFFECTS.register(bus);
        POTIONS.register(bus);
    }
}
