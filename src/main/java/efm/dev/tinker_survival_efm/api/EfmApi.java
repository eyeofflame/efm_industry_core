package efm.dev.tinker_survival_efm.api;

import efm.dev.tinker_survival_efm.Tinker_survival_efm;
import efm.dev.tinker_survival_efm.config.EfmConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Method;
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
        try {
            Method method = item.getClass().getMethod(
                    "hurtEnemy", ItemStack.class, LivingEntity.class, LivingEntity.class
            );
            return !method.getDeclaringClass().equals(Item.class);
        } catch (NoSuchMethodException e) {
            Tinker_survival_efm.LOGGER.error(e.getMessage());
            return false;
        }
    }
}
