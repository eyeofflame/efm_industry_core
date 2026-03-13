package efm.dev.efmcore.common.registry.item;

import efm.dev.efmcore.common.registry.EfmModRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class UndeadHeart extends Item {
    public UndeadHeart() {
        super(new Properties().stacksTo(16).rarity(Rarity.RARE).setNoRepair().fireResistant());
    }

    private final int maxTick = 5 * 20;

    private int tickEfm = 5 * 20;

    @Override
    public void inventoryTick(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (!pLevel.isClientSide && pEntity instanceof Player player) {
            if (tickEfm <= 0) {
                player.forceAddEffect(new MobEffectInstance(EfmModRegistry.CURSE_EFM.get(), 200), player);
                tickEfm = maxTick;
            }

            tickEfm--;
        }
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack pStack) {
        return Component.translatable("item.efm.undead_heart");
    }
}
