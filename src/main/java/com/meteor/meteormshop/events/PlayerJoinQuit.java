package com.meteor.meteormshop.events;

import com.meteor.meteormshop.MeteorMshop;
import com.meteor.meteormshop.data.PlayerData;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class PlayerJoinQuit implements Listener {
    MeteorMshop plugin;
    public PlayerJoinQuit(MeteorMshop plugin){
        this.plugin = plugin;
    }
    @EventHandler
    void joinGame(PlayerJoinEvent joinEvent){
        String name = joinEvent.getPlayer().getName();
        if(plugin.getMySqlManager().getData(name)==null){
            plugin.getMySqlManager().chanageData(name, new ByteArrayInputStream(new YamlConfiguration().saveToString().getBytes(StandardCharsets.UTF_8)));
            if(plugin.getConfig().getBoolean("Setting.debug")){
                plugin.getLogger().info("已添加新记录");
            }
        }
        plugin.getMshopData().getPlayerDataHashMap().put(name,new PlayerData(name,plugin.getMySqlManager().getData(name)));
    }
    @EventHandler
    void quitGame(PlayerQuitEvent quitEvent){
        String name = quitEvent.getPlayer().getName();
        if(plugin.getMshopData().getPlayerDataHashMap().containsKey(name)){
            plugin.getMySqlManager().chanageData(name,new ByteArrayInputStream(plugin.getMshopData().getPlayerDataHashMap().get(name).getYml().saveToString().getBytes(StandardCharsets.UTF_8)));
            plugin.getMshopData().getPlayerDataHashMap().remove(name);
            if(plugin.getConfig().getBoolean("Setting.debug")){
                plugin.getLogger().info("已清空缓存");
            }
        }
    }
}
