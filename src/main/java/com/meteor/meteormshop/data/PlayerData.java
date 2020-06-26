package com.meteor.meteormshop.data;

import com.meteor.meteormshop.MeteorMshop;
import com.meteor.meteormshop.invholder.MshopHolder;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;

import java.util.Map;

public class PlayerData {
    String player;
    YamlConfiguration yml;

    public PlayerData(String player, YamlConfiguration yml) {
        this.player = player;
        this.yml = yml;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public YamlConfiguration getYml() {
        return yml;
    }

    public void setYml(YamlConfiguration yml) {
        this.yml = yml;
    }
}
