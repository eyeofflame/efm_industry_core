package efm.dev.efmcore.common.config;

import efm.dev.efmcore.common.untils.bossesHealthRange.BossesHealthRange;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class JsonConfigEfm {
    private List<String> bossesList = new ArrayList<>();
    private List<String> itemWhiteList = List.of("efm:holy_pearl");
    private int monsterRespawnProp = 10;
    private int bossesGetProtection = 30;
    private int playerGetProtection = 20;
    private List<String> bossesMaxHealthRange = new ArrayList<>();
    private boolean useCustomBrewing = true;
    private String ingredientUpgrade0 = "";
    private String ingredientUpgrade1 = "";

    public JsonConfigEfm() {
    }

    public List<String> getBossesList() {
        return bossesList;
    }

    public List<String> getItemWhiteList() {
        return itemWhiteList;
    }

    public int getMonsterRespawnProp() {
        return monsterRespawnProp;
    }

    public int getBossesGetProtection() {
        return bossesGetProtection;
    }

    public int getPlayerGetProtection() {
        return playerGetProtection;
    }

    public List<BossesHealthRange> getBossesMaxHealthRange() {
        List<BossesHealthRange> list = new ArrayList<>();

        for (String s : bossesMaxHealthRange) {
            var split = s.split(";;");
            var range = new BossesHealthRange(split[0], Integer.parseInt(split[1]), Integer.parseInt(split[2]));
            list.add(range);
        }

        return list;
    }

    public boolean isUseCustomBrewing() {
        return useCustomBrewing;
    }

    public Item getUpgrade0() {
        var item = ForgeRegistries.ITEMS.getValue(ResourceLocation.parse(this.ingredientUpgrade0));
        if (item == null || item.equals(Items.AIR)) {
            return Items.AMETHYST_BLOCK;
        }
        return item;
    }

    public Item getUpgrade1() {
        var item = ForgeRegistries.ITEMS.getValue(ResourceLocation.parse(this.ingredientUpgrade1));
        if (item == null || item.equals(Items.AIR)) {
            return Items.REDSTONE_BLOCK;
        }
        return item;
    }
}
