package efm.dev.tinker_survival_efm.event;

import efm.dev.tinker_survival_efm.Tinker_survival_efm;
import efm.dev.tinker_survival_efm.api.EfmApi;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = Tinker_survival_efm.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WeaponDisable {
    @SubscribeEvent
    public static void onAttack(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof Player player && !player.level().isClientSide) {
            LivingEntity entity = event.getEntity();

            ItemStack itemInMain = player.getMainHandItem();

            if (
                    event.getSource().type().equals(entity.level().damageSources().playerAttack(player).type())
                            &&
                            EfmApi.checkerWeapon(itemInMain)
            ) {
                player.displayClientMessage(Component.literal("该物品不能造成伤害！").withStyle(ChatFormatting.RED), true);
                event.setAmount(0f);
            }
        }
    }


    @SubscribeEvent
    public static void onDig(BlockEvent.BreakEvent event) {
        if (event.getLevel().isClientSide()) return;

        Player player = event.getPlayer();

        if (EfmApi.checkerWeapon(player.getMainHandItem())) {
            player.displayClientMessage(Component.literal("该物品不能挖掘！").withStyle(ChatFormatting.RED), true);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onUse(LivingEntityUseItemEvent.Start event) {
        if (/*event.getEntity().level().isClientSide || */!(event.getEntity() instanceof Player player)) return;

        if (EfmApi.checkAll(event.getItem().getItem()) && EfmApi.checkWeaponUse(event.getItem())) {
            player.displayClientMessage(Component.literal("该物品不能使用！").withStyle(ChatFormatting.RED), true);
            event.setCanceled(true);
        }
    }

    private static final UUID uid = UUID.fromString("83339827-f7fb-4600-bc3f-05918c0a6924");
    private static final AttributeModifier modifier = new AttributeModifier(uid, "armor efm modifier", -1d, AttributeModifier.Operation.MULTIPLY_BASE);

    @SubscribeEvent
    public static void onAttributeAttach(ItemAttributeModifierEvent event) {
        if (EfmApi.checkAll(event.getItemStack().getItem()) && event.getItemStack().getItem() instanceof ArmorItem) {
            if (!event.getModifiers().containsValue(modifier)) event.addModifier(Attributes.ARMOR, modifier);
        }
    }

    @SubscribeEvent
    public static void itemTooltip(ItemTooltipEvent event) {
        if (EfmApi.checkAll(event.getItemStack().getItem())) {
            event.getToolTip().add(Component.translatable("efm.disable.message").withStyle(ChatFormatting.RED));

            if (!EfmApi.checkWeaponUse(event.getItemStack())) {
                event.getToolTip().add(Component.literal("所幸的是，该物品仍具备使用能力！(右键)").withStyle(ChatFormatting.AQUA));
            }
        }
    }
}
