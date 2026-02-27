package efm.dev.efmcore.common.untils;

import efm.dev.efmcore.Efmcore;
import efm.dev.efmcore.common.untils.bossesHealthRange.BossesHealthRange;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public interface EfmHelper {
    static void deleteItems(Player player) {
        player.getInventory().compartments.forEach(list -> {
            float amount = 0;
            List<Integer> intList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                ItemStack stack = list.get(i);
                if (!stack.isEmpty() && !Efmcore.configEfm.getItemWhiteList().contains(ForgeRegistries.ITEMS.getKey(stack.getItem()).toString())) {
                    amount++;
                    intList.add(i);
                }
            }

            int a = Math.round(amount / 4);
            List<Integer> list1 = efmRandomNoRepeat(intList.size(), a);
            if (!list1.isEmpty()) list1.forEach(integer -> {
                list.set(integer, ItemStack.EMPTY);
            });
        });
    }

    static boolean checkIsBoss(Entity entity) {
        String id = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString();
        return Efmcore.configEfm.getBossesList().contains(id);
    }


    static List<Integer> efmRandomNoRepeat(int max, int bound) {
        if (bound > 0) {
            List<Integer> list0 = new ArrayList<>();
            for (int i = 0; i < max; i++) {
                list0.add(i);
            }
            Collections.shuffle(list0);
            return list0.subList(0, bound);
        }
        return new ArrayList<>();
    }

    static ResourceLocation buildRes(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }

    static boolean random(int bound) {
        return efmRandomNoRepeat(100, bound).contains(new Random().nextInt(100));
    }

    static void reducePlayersDuriation(List<Player> players, LivingEntity boss) {
        for (Player player : players) {
            player.getInventory().armor.forEach(stack -> {
                if (!stack.isEmpty()) {
                    stack.setDamageValue((int) Math.round(stack.getMaxDamage() * 0.03));
                }
            });

            player.hurt(boss.level().damageSources().mobAttack(boss), player.getMaxHealth() * 0.05f);
        }
    }

    static boolean checkIsMaxHealthBoss(Entity entity) {
        String id = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString();
        boolean check = false;
        for (BossesHealthRange range : Efmcore.configEfm.getBossesMaxHealthRange()) {
            if (id.equals(range.id)) {
                check = true;
            }
        }
        return check;
    }
}
