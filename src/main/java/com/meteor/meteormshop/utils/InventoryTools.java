package com.meteor.meteormshop.utils;

import com.meteor.meteormshop.MeteorMshop;
import com.meteor.meteormshop.data.ItemType;
import com.meteor.meteormshop.data.Mshop;
import com.meteor.meteormshop.data.ShopType;
import com.meteor.meteormshop.invholder.MshopHolder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InventoryTools {

    private static void addFlag(String time, Inventory inv) {
        MeteorMshop plugin = MeteorMshop.getInstance();
        int flag[] = {53, 52, 51, 50, 48, 47, 46, 45};
        ItemStack itemStack = new ItemStack(Material.valueOf(plugin.getConfig().getString("items.flag.ID")), 1, (short) plugin.getConfig().getInt("items.flag.data"));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(plugin.getConfig().getString("items.flag.name").replace("&", "§"));
        itemStack.setItemMeta(itemMeta);
        for (int i : flag) {
            inv.setItem(i, itemStack);
        }
        ItemStack info = new ItemStack(Material.valueOf(plugin.getConfig().getString("items.info.ID")), 1, (short) plugin.getConfig().getInt("items.info.data"));
        ItemMeta itemMeta1 = info.getItemMeta();
        itemMeta1.setDisplayName(plugin.getConfig().getString("items.info.name").replace("&", "§"));
        List<String> lore = new ArrayList<>();
        plugin.getConfig().getStringList("items.info.lore").forEach(string -> lore.add(string.replace("@time@", time).replace("&", "§")));
        itemMeta1.setLore(lore);
        info.setItemMeta(itemMeta1);
        inv.setItem(49, info);
    }

    static long getRefCd(String mshop, YamlConfiguration data, String player) {
        Mshop shop = MeteorMshop.getInstance().getMshopData().getMshops().get(mshop);
        long ref = shop.getTime() * 60 * 60 * 1000;
        Player p = Bukkit.getPlayer(player);
        ConfigurationSection vip = MeteorMshop.getInstance().getConfig().getConfigurationSection("vip-speed");
        for (PermissionAttachmentInfo permissionAttachmentInfo : p.getEffectivePermissions()
        ) {
            for (String key : vip.getKeys(false)) {
                if (permissionAttachmentInfo.getPermission().startsWith("mmshop.vip")) {
                    String mul = permissionAttachmentInfo.getPermission().substring(11);
                    if (key.equalsIgnoreCase(mul)) {
                        ref = ref - (long) ((long)ref*vip.getDouble(key));
                    }
                }
            }
        }
        return ref - (System.currentTimeMillis() - data.getLong(mshop + ".time"));
    }

    public static void openMshop(String player, String mshop, YamlConfiguration data) {
        if (data.getString(mshop) == null) {
            LotteryTools.saveMshop(data, mshop, player);
        }
        Mshop shop = MeteorMshop.getInstance().getMshopData().getMshops().get(mshop);
        if (getRefCd(mshop, data, player) < 0 || getRefCd(mshop, data, player) == 0) {
            LotteryTools.saveMshop(data, mshop, player);
            Bukkit.getPlayer(player).sendMessage("§3§lMshop §7 - > §e神秘商店已刷新...");
        }
        MshopHolder mshopHolder = new MshopHolder(player, mshop);
        mshopHolder.setShopname(mshop);
        Inventory inventory = Bukkit.createInventory(mshopHolder, 9 * 6, shop.getTitle().replace("&", "§"));
        addFlag(String.valueOf(getRefCd(mshop, data, player)/1000/60), inventory);
        Bukkit.getPlayer(player).openInventory(inventory);
        mshopHolder.getItemDataList().forEach((item) -> {
            ItemStack itemStack = new ItemStack(item.getItemStack());
            String path = shop.getShopType() == ShopType.BUY ? "info.buy.lore" : "info.sell.lore";
            int limi = data.getInt(mshop + ".item-list." + item.getId() + ".limit");
            ItemMeta itemMeta = itemStack.getItemMeta();
            List<String> lore = itemMeta.hasLore()?itemMeta.getLore():new ArrayList<>();
            MeteorMshop.getInstance().getConfig().getStringList(path).forEach((l) -> {
                l = l.replace("@vaul@", "" + item.getVaul()).replace("@type@", item.getItemType() == ItemType.MONEY ? "金币" : "点卷")
                        .replace("@limi@", item.getLimit() + "").replace("&", "§");
                lore.add(l);
            });
            lore.add("");
            lore.add("§8id:" + item.getId());
            itemMeta.setLore(lore);
            if(MeteorMshop.getInstance().getConfig().getBoolean("Setting.limi-barrier")){
                if(limi>=item.getLimit()){
                    itemStack.setType(Material.BARRIER);
                    itemMeta.setDisplayName("§c§n无法再次购买");
                    itemMeta.setLore(Arrays.asList("","§8§l§m §f§l§m        §8§l§m ","§e已到达限购数量","§e请等待神秘商店刷新","","§8id:"+item.getId()));
                }
            }
            itemStack.setItemMeta(itemMeta);
            inventory.addItem(itemStack);
        });
    }
}
