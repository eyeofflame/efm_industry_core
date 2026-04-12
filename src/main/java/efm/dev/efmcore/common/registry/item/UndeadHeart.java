package efm.dev.efmcore.common.registry.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;

public class UndeadHeart extends Item {
    public UndeadHeart() {
        super(new Properties().stacksTo(16).rarity(Rarity.RARE).setNoRepair().fireResistant());
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack pStack) {
        return Component.translatable("item.efm.undead_heart");
    }
}
