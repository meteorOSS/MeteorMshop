package com.meteor.meteormshop.data;

import com.meteor.meteormshop.MeteorMshop;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MshopData {
    HashMap<String,Mshop> mshops = new HashMap<>();
    HashMap<String,PlayerData> playerDataHashMap = new HashMap<>();
    MeteorMshop plugin;

    public HashMap<String, Mshop> getMshops() {
        return mshops;
    }

    public HashMap<String, PlayerData> getPlayerDataHashMap() {
        return playerDataHashMap;
    }

    public void setPlayerDataHashMap(HashMap<String, PlayerData> playerDataHashMap) {
        this.playerDataHashMap = playerDataHashMap;
    }

    public void setMshops(HashMap<String, Mshop> mshops) {
        this.mshops = mshops;
    }
    public MshopData(MeteorMshop plugin){
        this.plugin = plugin;
        loadMshop();
    }
    public void loadMshop(){
        mshops.clear();
        File file = new File(plugin.getDataFolder()+"/mshop");
        if(!file.exists()){
            file.mkdirs();
            return;
        }
        File[] files = file.listFiles();
        for (File yml: files){
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(yml);
            ConfigurationSection items = yamlConfiguration.getConfigurationSection("item-list");
            Map<String,MshopItemData> itemDataMap = new HashMap<>();
            items.getKeys(false).forEach((a)->{
                itemDataMap.put(a,new MshopItemData(Integer.valueOf(a),items.getItemStack(a+".item"),items.getInt(a+".vaul")
                        ,ItemType.valueOf(items.getString(a+".type")),items.getInt(a+".limit"),items.getInt(a+".pro")));
            });
            String name = yml.getName().substring(0,yml.getName().indexOf("."));
            this.mshops.put(name,new Mshop(ShopType.valueOf(yamlConfiguration.getString("shop-type")),yamlConfiguration.getString("title"),itemDataMap,yamlConfiguration.getInt("ref-time"),yamlConfiguration.getInt("ref-amount")));
        }
        MeteorMshop.getInstance().getLogger().info("本次载入了" + mshops.size() + "个神秘商店");
    }
}
