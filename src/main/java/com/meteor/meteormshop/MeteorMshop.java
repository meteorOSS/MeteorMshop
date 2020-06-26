package com.meteor.meteormshop;

import com.meteor.meteormshop.commands.MshopCommand;
import com.meteor.meteormshop.data.MshopData;
import com.meteor.meteormshop.events.InventoryClick;
import com.meteor.meteormshop.events.PlayerJoinQuit;
import com.meteor.meteormshop.mysql.MySqlManager;
import com.meteor.meteormshop.utils.MessageManager;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
/**
 * @author meteor
 *
 * ......................不要看了不要看了，代码比你还难看......................
 * ......................不要看了不要看了，代码比你还难看......................
 * ......................不要看了不要看了，代码比你还难看......................
 * ......................不要看了不要看了，代码比你还难看......................
 * ......................不要看了不要看了，代码比你还难看......................
 * ......................不要看了不要看了，代码比你还难看......................
 * ......................不要看了不要看了，代码比你还难看......................
 * ......................不要看了不要看了，代码比你还难看......................
 *                       _oo0oo_
 *                      o8888888o
 *                      88" . "88
 *                      (| -_- |)
 *                      0\  =  /0
 *                    ___/`---'\___
 *                  .' \\|     |// '.
 *                 / \\|||  :  |||// \
 *                / _||||| -卍-|||||- \
 *               |   | \\\  -  /// |   |
 *               | \_|  ''\---/''  |_/ |
 *               \  .-\__  '-'  ___/-. /
 *             ___'. .'  /--.--\  `. .'___
 *          ."" '<  `.___\_<|>_/___.' >' "".
 *         | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *         \  \ `_.   \_ __\ /__ _/   .-` /  /
 *     =====`-.____`.___ \_____/___.-`___.-'=====
 *                       `=---='
 *
 *..................没有BUG，游泳滴鸭腿饭...................
 *
 */
public final class MeteorMshop extends JavaPlugin {
    static MeteorMshop plugin;
    MshopData mshopData;
    MySqlManager mySqlManager;
    Economy economy = null;
    PlayerPoints points = null;
    MessageManager messageManager = null;
    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("神秘商店插件已载入,当前版本v2.0,定制插件联系qq2260483272 ");
        plugin = this;
        saveDefaultConfig();
        registerEvents();
        mySqlManager = new MySqlManager(this);
        mshopData = new MshopData(this);
        getServer().getPluginCommand("mmshop").setExecutor(new MshopCommand(this));
        if(Bukkit.getPluginManager().isPluginEnabled("Vault")){
            RegisteredServiceProvider<Economy> ecoapi = getServer().getServicesManager().getRegistration(Economy.class);
            economy = ecoapi.getProvider();
            getLogger().info("已关联到Vault插件");
        }
        if(Bukkit.getPluginManager().isPluginEnabled("PlayerPoints")){
            points = (PlayerPoints)PlayerPoints.getPlugin(PlayerPoints.class);
            getLogger().info("已关联到PlayerPoints");
        }
        messageManager = new MessageManager(YamlConfiguration.loadConfiguration(new File(getDataFolder()+"/lang.yml")));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        mySqlManager.closeMysql();
        getLogger().info("已卸载神秘商店,感谢使用");
    }
    public static MeteorMshop getInstance(){
        return plugin;
    }

    @Override
    public void saveDefaultConfig() {
        String[] files = {"config.yml","lang.yml"};
        for(String string : files){
            File file = new File(getDataFolder()+"/"+string);
            if(!file.exists()){
                saveResource(string,false);
            }
        }
    }

    private void registerEvents(){
        getServer().getPluginManager().registerEvents(new PlayerJoinQuit(this),this);
        getServer().getPluginManager().registerEvents(new InventoryClick(this),this);
    }

    public Economy getEconomy() {
        return economy;
    }

    public PlayerPoints getPoints() {
        return points;
    }

    public MySqlManager getMySqlManager() {
        return mySqlManager;
    }
    public MshopData getMshopData() {
        return mshopData;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }
}
