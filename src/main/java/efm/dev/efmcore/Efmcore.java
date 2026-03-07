package efm.dev.efmcore;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import efm.dev.efmcore.common.config.JsonConfigEfm;
import efm.dev.efmcore.common.registry.EfmModRegistry;
import efm.dev.efmcore.integration.tinkering.Catagory;
import efm.dev.efmcore.integration.tinkering.init.EfmItemsTinker;
import efm.dev.efmcore.network.NetworkInstance;
import efm.dev.efmcore.network.toServer.ServerPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.crafting.StrictNBTIngredient;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Mod(Efmcore.MODID)
public class Efmcore {
    public static final String MODID = "efm_core";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static JsonConfigEfm configEfm = new JsonConfigEfm();

    private static final Boolean debug = true;

    public Efmcore(FMLJavaModLoadingContext context) {
        IEventBus ibus = context.getModEventBus();
        IEventBus fbus = MinecraftForge.EVENT_BUS;

        loadConfig();

        ibus.addListener(this::onCommonSetup);
        ibus.addListener(this::onClientSetup);
        fbus.addListener(this::registerCommands);
        fbus.addListener(this::onLivingJump);

        if (debug) {
            fbus.addListener(this::onChat);
        }

        Catagory.EFM_TABS.register(ibus);

        NetworkInstance.register();
        EfmModRegistry.register(ibus);
        EfmItemsTinker.ITEMS.register(ibus);
    }

    private static Path getConfigPath() {
        return FMLPaths.CONFIGDIR.get().resolve("efmcore-config.json");
    }

    public static void loadConfig() {
        Path configPath = getConfigPath();
        if (Files.exists(configPath)) {
            try {
                String content = Files.readString(configPath);
                configEfm = GSON.fromJson(content, JsonConfigEfm.class);
            } catch (IOException | JsonSyntaxException e) {
                System.err.println("Failed to load conig");
                configEfm = new JsonConfigEfm();
                saveConfig();
            }
        } else {
            configEfm = new JsonConfigEfm();
            saveConfig();
        }
    }

    public static void saveConfig() {
        Path configPath = getConfigPath();
        try {
            String json = GSON.toJson(configEfm);
            Files.writeString(configPath, json);
        } catch (IOException e) {
            System.err.println("Failed to save config");
            ;
        }
    }

    public void onCommonSetup(FMLCommonSetupEvent event) {

        event.enqueueWork(() -> {

            ItemStack potionNormal = Items.POTION.getDefaultInstance();
            PotionUtils.setPotion(potionNormal, Potions.MUNDANE);
            ItemStack stack = PotionUtils.setPotion(new ItemStack(Items.POTION), EfmModRegistry.map_level1.get(0).get());

            ItemStack potion = new ItemStack(Items.POTION);


            if (configEfm.isUseCustomBrewing()) {

                BrewingRecipeRegistry.addRecipe(
                        Ingredient.of(potionNormal),
                        Ingredient.of(Items.RABBIT_FOOT),
                        stack
                );

                for (int i = 0; i < 9; i++) {
                    ItemStack item0 = PotionUtils.setPotion(potion.copy(), EfmModRegistry.map_level1.get(i).get());
                    ItemStack item1 = PotionUtils.setPotion(potion.copy(), EfmModRegistry.map_level1.get(i + 1).get());

                    BrewingRecipeRegistry.addRecipe(
                            StrictNBTIngredient.of(item0),
                            Ingredient.of(configEfm.getUpgrade0()),
                            item1
                    );

                    ItemStack itemStack = PotionUtils.setPotion(potion.copy(), EfmModRegistry.map_level2.get(i).get());

                    BrewingRecipeRegistry.addRecipe(
                            StrictNBTIngredient.of(item0),
                            Ingredient.of(configEfm.getUpgrade1()),
                            itemStack
                    );

                }

            }
        });
    }

    public void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("reloadefmconfig")
                        .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                        .executes(context -> {
                            loadConfig();
                            context.getSource().sendSuccess(() -> Component.literal("efmcore的配置重载成功！").withStyle(ChatFormatting.YELLOW), true);
                            return 1;
                        })
        );
    }

    public void onClientSetup(FMLClientSetupEvent event) {
    }


    public void onLivingJump(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && mc.level != null && mc.level.isClientSide()) {
            MobEffectInstance effect = mc.player.getEffect(EfmModRegistry.DOUBLEJUMP_EFFECT.get());
            if (event.getKey() == mc.options.keyJump.getKey().getValue() && event.getAction() == GLFW.GLFW_PRESS && effect != null) {

                NetworkInstance.INSTANCE.sendToServer(new ServerPacket(mc.player.getUUID(), "jump"));
            }
        }
    }

    public void onChat(ServerChatEvent event) {
        if (event.getRawText().equals("test")) {
            event.setMessage(Component.literal(configEfm.getUpgrade0().getDescriptionId() + '\n' + configEfm.getUpgrade1().getDescriptionId()));
        }
    }
}
