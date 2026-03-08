package efm.dev.tinker_survival_efm.config;

import efm.dev.tinker_survival_efm.Tinker_survival_efm;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Tinker_survival_efm.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EfmConfig {
    private static ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

    private static ForgeConfigSpec.BooleanValue modEnable = builder.define("modEnable", true);

    private static ForgeConfigSpec.ConfigValue<List<? extends String>> whiteListOfMod = builder.defineListAllowEmpty("whiteListMod", new ArrayList<>(), EfmConfig::check);

    private static ForgeConfigSpec.ConfigValue<List<? extends String>> whiteListOfAttackableOrMaterial = builder.defineListAllowEmpty("whiteListAttackable", new ArrayList<>(), EfmConfig::check);

    private static ForgeConfigSpec.ConfigValue<List<? extends String>> whiteListOfArmorOrMaterial = builder.defineListAllowEmpty("whiteListArmor", List.of("minecraft:wooden_axe", "minecraft:wooden_pickaxe"), EfmConfig::check);

    private static ForgeConfigSpec.ConfigValue<List<? extends String>> whiteListOfModAllowUse = builder.defineListAllowEmpty("whiteListModUse", new ArrayList<>(), EfmConfig::check);
    private static ForgeConfigSpec.ConfigValue<List<? extends String>> whiteListOfItemAllowUse = builder.defineListAllowEmpty("whiteListItemUse", new ArrayList<>(), EfmConfig::check);

    private static boolean check(Object o) {
        return o instanceof String;
    }

    public static ForgeConfigSpec SPEC = builder.build();

    public static boolean enable;
    public static List<? extends String> modlist, attackblelist, armorlist, moduselist, itemuselist;

    private static void loadConfig() {
        enable = modEnable.get();
        modlist = whiteListOfMod.get();
        attackblelist = whiteListOfAttackableOrMaterial.get();
        armorlist = whiteListOfArmorOrMaterial.get();
        moduselist = whiteListOfModAllowUse.get();
        itemuselist = whiteListOfItemAllowUse.get();
    }

    @SubscribeEvent
    public static void onSetup(final FMLCommonSetupEvent event) {
        loadConfig();
    }

    @SubscribeEvent
    public static void onLoading(final ModConfigEvent.Reloading event) {
        loadConfig();
    }
}
