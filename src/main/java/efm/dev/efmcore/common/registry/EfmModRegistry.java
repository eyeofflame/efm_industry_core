package efm.dev.efmcore.common.registry;

import efm.dev.efmcore.Efmcore;
import efm.dev.efmcore.common.registry.item.UndeadHeart;
import efm.dev.efmcore.common.registry.mob_effect.CursedEfm;
import efm.dev.efmcore.common.registry.mob_effect.DoubleJumpMobEffect;
import efm.dev.efmcore.common.registry.mob_effect.UndeaderEffect;
import efm.dev.efmcore.common.registry.potion.DoubleJumpPotion;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

public class EfmModRegistry {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Efmcore.MODID);
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, Efmcore.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,Efmcore.MODID);

    public static final RegistryObject<MobEffect> DOUBLEJUMP_EFFECT = EFFECTS.register("double_jump", DoubleJumpMobEffect::new);
    public static final RegistryObject<MobEffect> CURSE_EFM = EFFECTS.register("curse_efm", CursedEfm::new);
    public static final RegistryObject<MobEffect> UNDEADER =  EFFECTS.register("undeader", UndeaderEffect::new);
    public static final RegistryObject<Item> UNDEAD_HEART = ITEMS.register("undead_heart", UndeadHeart::new);

    public static Map<Integer, RegistryObject<Potion>> map_level1 = new HashMap<>();
    public static Map<Integer, RegistryObject<Potion>> map_level2 = new HashMap<>();

    public static void register(IEventBus bus) {
        for (int i = 0; i < 2; i++) {
            int finalI = i;

            RegistryObject<Potion> potionRegistryObject1 = POTIONS.register("efm_doublejump_potion1_" + i, () -> new DoubleJumpPotion(20 * 60, finalI));
            RegistryObject<Potion> potionRegistryObject2 = POTIONS.register("efm_doublejump_potion2_" + i, () -> new DoubleJumpPotion(20 * 60 * 5, finalI));

            map_level1.put(i, potionRegistryObject1);
            map_level2.put(i, potionRegistryObject2);
        }

        EFFECTS.register(bus);
        POTIONS.register(bus);
        ITEMS.register(bus);
    }
}
