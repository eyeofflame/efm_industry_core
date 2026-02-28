package efm.dev.efmcore;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import efm.dev.efmcore.common.config.JsonConfigEfm;
import efm.dev.efmcore.integration.tinkering.Catagory;
import efm.dev.efmcore.integration.tinkering.init.EfmItemsTinker;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Mod(Efmcore.MODID)
public class Efmcore {
    public static final String MODID = "efm_core";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static JsonConfigEfm configEfm = new JsonConfigEfm();

    public Efmcore(FMLJavaModLoadingContext context) {
        IEventBus ibus = context.getModEventBus();
        IEventBus fbus = MinecraftForge.EVENT_BUS;

        ibus.addListener(this::onCommonSetup);
        fbus.addListener(this::registerCommands);
        fbus.addListener(this::onLivingJump);

        Catagory.EFM_TABS.register(ibus);
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
        loadConfig();
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

    public void onClientSetup(final FMLClientSetupEvent event) {
    }

    public void onLivingJump(LivingEvent.LivingTickEvent event) {
        if (event.getEntity() instanceof Player player) {
            Minecraft mc = Minecraft.getInstance();

            if (mc.options.keyJump.consumeClick() && !player.onGround()) {
                player.jumpFromGround();
            }
        }
    }
}
