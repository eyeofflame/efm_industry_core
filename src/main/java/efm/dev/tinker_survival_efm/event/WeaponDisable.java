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
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

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
                event.setCanceled(true);
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
    public static void onUse(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        if (EfmApi.checkAll(event.getItemStack()) && EfmApi.checkWeaponUse(event.getItemStack())) {
            player.displayClientMessage(Component.literal("该物品不能进行操作！").withStyle(ChatFormatting.RED), true);
            if (event.getCancellationResult() != null) event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onUseOnBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        if (EfmApi.checkAll(event.getItemStack()) && EfmApi.checkWeaponUse(event.getItemStack())) {
            player.displayClientMessage(Component.literal("该物品不能进行操作！").withStyle(ChatFormatting.RED), true);
            if (event.getCancellationResult() != null) event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onAttributeAttach(ItemAttributeModifierEvent event) {
        if (EfmApi.checkAll(event.getItemStack()) && event.getItemStack().getItem() instanceof ArmorItem armorItem) {
            List<AttributeModifier> modifiers = new ArrayList<>(event.getModifiers().get(Attributes.ARMOR));

            for (AttributeModifier modifier : modifiers) {
                event.removeModifier(Attributes.ARMOR, modifier);
            }

        }
    }

    @SubscribeEvent
    public static void itemTooltip(ItemTooltipEvent event) {
        if (EfmApi.checkAll(event.getItemStack())) {
            event.getToolTip().add(Component.translatable("efm.disable.message").withStyle(ChatFormatting.RED));

            if (!EfmApi.checkWeaponUse(event.getItemStack())) {
                event.getToolTip().add(Component.literal("所幸的是，该物品仍具备使用能力！(右键)").withStyle(ChatFormatting.AQUA));
            }
        }
    }
}
