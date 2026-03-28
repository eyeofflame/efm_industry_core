package efm.dev.tinker_survival_efm.api;

import efm.dev.tinker_survival_efm.config.EfmConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class EfmApi {
    public static boolean checkerWeapon(ItemStack stack) {
        Item item = stack.getItem();
        ResourceLocation name = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item));
        return (
                checkCanAttack(item)
                        &&
                        EfmConfig.enable
                        &&
                        !EfmConfig.modlist.contains(name.getNamespace())
                        &&
                        !EfmConfig.attackblelist.contains(name.toString())
        );
    }

    public static boolean checkWeaponUse(ItemStack stack) {
        Item item = stack.getItem();
        ResourceLocation id = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item));

        return (EfmConfig.enable && !EfmConfig.moduselist.contains(id.getNamespace()) && !EfmConfig.itemuselist.contains(id.toString()));
    }

    public static boolean checkAll(Item item) {
        ResourceLocation id = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item));
        return (
                (checkCanAttack(item)
                        ||
                        item instanceof ArmorItem
                        ||
                        item instanceof ProjectileWeaponItem)

                        && (
                        EfmConfig.enable
                                &&
                                !EfmConfig.armorlist.contains(id.toString())
                                &&
                                !EfmConfig.attackblelist.contains(id.toString())
                                &&
                                !EfmConfig.modlist.contains(id.getNamespace())
                )
        );
    }

    private static boolean checkCanAttack(Item item) {
        return (item instanceof TieredItem || item instanceof TridentItem ||
                ForgeRegistries.ITEMS.getHolder(item).map(itemHolder -> itemHolder.is(ItemTags.TOOLS)).orElse(false)
        );
    }
}
