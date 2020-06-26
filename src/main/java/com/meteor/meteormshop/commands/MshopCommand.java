package com.meteor.meteormshop.commands;

import com.meteor.meteormshop.MeteorMshop;
import com.meteor.meteormshop.data.ItemType;
import com.meteor.meteormshop.data.Mshop;
import com.meteor.meteormshop.data.MshopItemData;
import com.meteor.meteormshop.data.ShopType;
import com.meteor.meteormshop.utils.InventoryTools;
import com.meteor.meteormshop.utils.LotteryTools;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.meteor.meteormshop.utils.LotteryTools.saveMshop;

public class MshopCommand implements CommandExecutor {
    MeteorMshop plugin;
    List<String> acmd = Arrays.asList("create","additem","reload");
    public MshopCommand(MeteorMshop plugin){
        this.plugin  = plugin;
    }
    private void addItem(String mshop,YamlConfiguration yaml,ItemStack item,int vaul,ItemType itemType,int limit,int pro){
        ConfigurationSection c = yaml.getConfigurationSection("item-list");
        int id = c.getKeys(false).size();
        yaml.set("item-list."+id+".item",item);
        yaml.set("item-list."+id+".vaul",vaul);
        yaml.set("item-list."+id+".type",itemType.toString());
        yaml.set("item-list."+id+".limit",limit);
        yaml.set("item-list."+id+".pro",pro);
        plugin.getMshopData().getMshops().get(mshop).getItems().put(String.valueOf(id),
                new MshopItemData(id,item,vaul,itemType,limit,pro));
        if(plugin.getConfig().getBoolean("Setting.debug")){
            plugin.getLogger().info("已添加物品...");
        }
    }
    private void saveYaml(File file,YamlConfiguration yamlConfiguration){
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            plugin.getLogger().info("保存神秘商店配置出错...");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length==0||args[0].equalsIgnoreCase("help")){
            sender.sendMessage("§f[ §fMeteorMshop | §6神秘商店 §f]");
            sender.sendMessage("§7/mmshop open §e[shop] §3打开指定的神秘商店.");
            sender.sendMessage("§7/mmshop create §e[shop] §3创建一个神秘商店.");
            sender.sendMessage("§7/mmshop ref §e[player] [mshop] §3刷新指定玩家的神秘商店.");
            sender.sendMessage("§7/mmshop additem §e[shop] [price] [limit] [pro] §3将手中物品上架至神秘商店.");
            sender.sendMessage("§7/mmshop reload §c重载配置文件");
            return true;
        }
        if(args.length==1&&args[0].equalsIgnoreCase("reload")&&sender.isOp()){
            sender.sendMessage("§3§lMshop §7 - > §c已重载配置文件^^");
            plugin.getMshopData().loadMshop();
            plugin.reloadConfig();
            plugin.saveDefaultConfig();
            return true;
        }
        if(args.length==3&&args[0].equalsIgnoreCase("ref")&&sender.isOp()){
            if(!plugin.getMshopData().getMshops().containsKey(args[2])){
                sender.sendMessage("§3§lMshop §7 - > §c该神秘商店不存在,请检查指令");
                return true;
            }
            LotteryTools.saveMshop(plugin.getMshopData().getPlayerDataHashMap().get(args[1]).getYml(),args[2],args[1]);
            sender.sendMessage("§3§lMshop §7 - > §c操作成功,已刷新对应神秘商店");
            return true;
        }
        if(args.length==2&&args[0].equalsIgnoreCase("create")&&sender.isOp()){
            if(plugin.getMshopData().getMshops().containsKey(args[1])){
                sender.sendMessage("§3§lMshop §7 - > §c已经有名为 §e" + args[1] + " §c的神秘商店存在...");
                return true;
            }
            YamlConfiguration mshop = new YamlConfiguration();
            mshop.set("title",args[1]);
            mshop.set("ref-time",3);
            mshop.set("ref-amount",1);
            mshop.set("shop-type", ShopType.BUY.toString());
            mshop.set("item-list.0.item",new ItemStack(Material.STONE));
            mshop.set("item-list.0.vaul",100);
            mshop.set("item-list.0.type", ItemType.MONEY.toString());
            mshop.set("item-list.0.limit",3);
            mshop.set("item-list.0.pro",10);
            saveYaml(new File(plugin.getDataFolder()+"/mshop/"+args[1]+".yml"),mshop);
            plugin.getMshopData().loadMshop();
            sender.sendMessage("§3§lMshop §7 - > §c已创建神秘商店 §e" + args[1]);
            return true;
        }
        if(args.length==5&&args[0].equalsIgnoreCase("additem")&&sender.isOp()&&sender instanceof Player){
            Player player = (Player)sender;
            if(!plugin.getMshopData().getMshops().containsKey(args[1])){
                player.sendMessage("§3§lMshop §7 - > §c该神秘商店不存在,请检查指令");
                return true;
            }
            if(player.getItemInHand()==null&&player.getItemInHand().getType()==Material.AIR){
                player.sendMessage("§3§lMshop §7 - > §c你想添加什么？你脑袋里的空气吗");
                return true;
            }
            File file = new File(plugin.getDataFolder()+"/mshop/"+args[1]+".yml");
            YamlConfiguration mshop = YamlConfiguration.loadConfiguration(file);
            addItem(args[1],mshop,player.getItemInHand(),Integer.valueOf(args[2]),ItemType.MONEY,Integer.valueOf(args[3]),Integer.valueOf(args[4]));
            saveYaml(file,mshop);
            player.sendMessage("§3§lMshop §7 - > §c已成功添加物品,建议前往配置文件进行参数调整...");
            return true;
        }
        if(args.length==2&&args[0].equalsIgnoreCase("open")&&sender instanceof Player){
            Player player = (Player)sender;
            if(!plugin.getMshopData().getMshops().containsKey(args[1])){
                player.sendMessage("§3§lMshop §7 - > §c该神秘商店不存在,请检查指令");
                return true;
            }
            Bukkit.getScheduler().runTaskAsynchronously(plugin,()->{
                YamlConfiguration data = plugin.getMshopData().getPlayerDataHashMap().get(player.getName()).getYml();
                InventoryTools.openMshop(player.getName(),args[1],data);
                if(plugin.getConfig().getBoolean("Setting.debug")){
                    plugin.getLogger().info("已保存数据");
                }
            });
        }
        return false;
    }
}
