package com.meteor.meteormshop.mysql;

import com.meteor.meteormshop.MeteorMshop;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public class MySqlManager {
    MeteorMshop plugin;
    Connection connection;
    public MySqlManager(MeteorMshop plugin){
        this.plugin = plugin;
        try {
            connection = DriverManager.getConnection(plugin.getConfig().getString("mysql.url"),plugin.getConfig().getString("mysql.user"),
                    plugin.getConfig().getString("mysql.password"));
            PreparedStatement ps = connection.prepareStatement(MySqlCommand.CREATE_TABLE.getCommand());
            doCommand(ps);
            plugin.getLogger().info("链接数据库成功...!");
        } catch (SQLException throwables) {
            plugin.getLogger().info("数据库链接失败...插件已卸载");
            plugin.onDisable();
        }
    }
    private void doCommand(PreparedStatement ps){
        try {
            ps.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                ps.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
    public YamlConfiguration getData(String player){
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        try {
            PreparedStatement ps = connection.prepareStatement(MySqlCommand.CHECK_DATA.getCommand());
            ps.setString(1,player);
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()){
                ByteArrayInputStream inputStream = (ByteArrayInputStream)resultSet.getBinaryStream("data");
                byte[] bytes = new byte[inputStream.available()];
                inputStream.read(bytes);
                yamlConfiguration.load(new StringReader(new String(bytes, StandardCharsets.UTF_8)));
                inputStream.close();
                return yamlConfiguration;
            }
        } catch (SQLException | IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void chanageData(String player, ByteArrayInputStream data){
        PreparedStatement ps = null;
        try {
            ps = getData(player)==null?connection.prepareStatement(MySqlCommand.ADD_PLAYER.getCommand()):connection.prepareStatement(MySqlCommand.UPDATE_DATA.getCommand());
            ps.setBinaryStream(1,data);
            ps.setString(2,player);
            doCommand(ps);
            if(plugin.getConfig().getBoolean("Setting.debug")){
                plugin.getLogger().info("添加数据库记录成功...");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void closeMysql(){
        try {
            connection.close();
            plugin.getLogger().info("已断开数据库连接");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
