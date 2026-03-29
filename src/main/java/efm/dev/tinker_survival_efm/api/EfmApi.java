package efm.dev.tinker_survival_efm.api;

import efm.dev.tinker_survival_efm.config.EfmConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.tconstruct.library.tools.nbt.MaterialIdNBT;

import java.util.Objects;

public class EfmApi {
    public static boolean checkerWeapon(ItemStack stack) {
        Item item = stack.getItem();
        ResourceLocation name = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item));
        return (
                checkCanAttack(stack)
                        &&
                        EfmConfig.enable
                        &&
                        !EfmConfig.modlist.contains(name.getNamespace())
                        &&
                        !EfmConfig.attackblelist.contains(name.toString())
                        &&
                        !isTinkersTool(stack)
        );
    }

    private static boolean isTinkersTool(ItemStack stack) {
        MaterialIdNBT nbt = MaterialIdNBT.from(stack);
        return !nbt.getMaterials().isEmpty();
    }

    public static boolean checkWeaponUse(ItemStack stack) {
        Item item = stack.getItem();
        ResourceLocation id = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item));

        return (EfmConfig.enable && !EfmConfig.moduselist.contains(id.getNamespace()) && !EfmConfig.itemuselist.contains(id.toString()) && !isTinkersTool(stack));
    }

    public static boolean checkAll(ItemStack stack) {
        Item item = stack.getItem();
        ResourceLocation id = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item));
        return (
                (checkCanAttack(stack)
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
                                &&
                                !isTinkersTool(stack)
                )
        );
    }

    private static boolean checkCanAttack(ItemStack stack) {
        Item item = stack.getItem();
        return (item instanceof TieredItem || item instanceof TridentItem ||
                ForgeRegistries.ITEMS.getHolder(item).map(itemHolder -> itemHolder.is(ItemTags.TOOLS)).orElse(false) && !isTinkersTool(stack)
        );
    }
}
