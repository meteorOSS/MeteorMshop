package com.meteor.meteormshop.invholder;

import com.meteor.meteormshop.MeteorMshop;
import com.meteor.meteormshop.data.Mshop;
import com.meteor.meteormshop.data.MshopItemData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MshopHolder implements InventoryHolder {
    List<MshopItemData> itemDataList = new ArrayList<>();
    String shopname;
    public MshopHolder(String player, String mshopname){
        YamlConfiguration data = MeteorMshop.getInstance().getMshopData().getPlayerDataHashMap().get(player).getYml();
        Mshop mshop = MeteorMshop.getInstance().getMshopData().getMshops().get(mshopname);
        ConfigurationSection items = data.getConfigurationSection(mshopname+".item-list");
        for(String key : items.getKeys(false)){
            itemDataList.add(mshop.getItems().get(key));
        }
        if(MeteorMshop.getInstance().getConfig().getBoolean("Setting.debug")){
            MeteorMshop.getInstance().getLogger().info("put items" + itemDataList.size());
        }
    }
    @Override
    public Inventory getInventory() {
        return null;
    }

    public List<MshopItemData> getItemDataList() {
        return itemDataList;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public void setItemDataList(List<MshopItemData> itemDataList) {
        this.itemDataList = itemDataList;
    }
}
