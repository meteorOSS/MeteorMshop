package com.meteor.meteormshop.events;

import com.meteor.meteormshop.MeteorMshop;
import com.meteor.meteormshop.data.ItemType;
import com.meteor.meteormshop.data.Mshop;
import com.meteor.meteormshop.data.MshopItemData;
import com.meteor.meteormshop.data.ShopType;
import com.meteor.meteormshop.invholder.MshopHolder;
import com.meteor.meteormshop.utils.InventoryTools;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class InventoryClick implements Listener {
    MeteorMshop plugin;
    public InventoryClick(MeteorMshop plugin){
        this.plugin = plugin;
    }
    private MshopItemData getItem(List<MshopItemData> items, ItemStack itemStack) {
        List<String> lore = itemStack.getItemMeta().getLore();
        String string = lore.get(lore.size()-1).substring(5);
        for(MshopItemData itemData : items){
            if(String.valueOf(itemData.getId()).equalsIgnoreCase(string)){
                return itemData;
            }
        }
        return null;
    }
    private boolean sellItem(Player player,MshopItemData mshopItemData){
        ItemStack[] itemStacks;
        for(int a=0;a<(itemStacks=player.getInventory().getContents()).length;){
            if(itemStacks[a]!=null&&itemStacks[a].isSimilar(mshopItemData.getItemStack())
                    &&itemStacks[a].getAmount()>=mshopItemData.getItemStack().getAmount()){
                itemStacks[a].setAmount(itemStacks[a].getAmount()-mshopItemData.getItemStack().getAmount());
                return true;
            }
            a++;
        }
        return false;
    }
    @EventHandler
    void clickInv(InventoryClickEvent clickEvent){
        if(clickEvent.getInventory().getHolder()!=null&&clickEvent.getInventory().getHolder() instanceof MshopHolder){
            if(clickEvent.getCurrentItem()!=null&&clickEvent.getCurrentItem().getType()!= Material.AIR){
                clickEvent.setCancelled(true);
                MshopHolder mshopHolder = (MshopHolder)clickEvent.getInventory().getHolder();
                Mshop mshop = plugin.getMshopData().getMshops().get(mshopHolder.getShopname());
                List<MshopItemData> items = mshopHolder.getItemDataList();
                MshopItemData mshopItemData = getItem(items,clickEvent.getCurrentItem());
                Player player = (Player)clickEvent.getWhoClicked();
                if(mshopItemData!=null) {
                    YamlConfiguration data = plugin.getMshopData().getPlayerDataHashMap().get(player.getName()).getYml();
                    int i = data.getInt(mshopHolder.getShopname() + ".item-list." + mshopItemData.getId() + ".limit");
                    if (i >= mshopItemData.getLimit()) {
                        player.sendMessage(plugin.getMessageManager().getString("limit").replace("@limi@", i + "").replace("@maxlimi@", mshopItemData.getLimit() + ""));
                        return;
                    }
                    if(mshop.getShopType()==ShopType.BUY){
                        if (mshopItemData.getItemType() == ItemType.MONEY && plugin.getEconomy().getBalance(player.getName()) - mshopItemData.getVaul() < 0
                                || mshopItemData.getItemType() == ItemType.POINTS && plugin.getPoints().getAPI().look(player.getName()) - mshopItemData.getVaul() < 0) {
                            player.sendMessage(plugin.getMessageManager().getString("no-vaul"));
                            return;
                        }
                        switch (mshopItemData.getItemType()) {
                            case MONEY:
                                plugin.getEconomy().withdrawPlayer(player.getName(), mshopItemData.getVaul());
                                break;
                            case POINTS:
                                plugin.getPoints().getAPI().take(player.getName(), mshopItemData.getVaul());
                                break;
                            default:
                                return;
                        }
                        player.sendMessage(plugin.getMessageManager().getString("buy-item"));
                        data.set(mshopHolder.getShopname() + ".item-list." + mshopItemData.getId() + ".limit", i + 1);
                        player.getInventory().addItem(mshopItemData.getItemStack());
                    }else{
                        if(sellItem(player,mshopItemData)){
                            player.sendMessage(plugin.getMessageManager().getString("sell-item-sur")
                            .replace("@money@",""+mshopItemData.getVaul()).replace("@type@",
                                            mshopItemData.getItemType()==ItemType.MONEY?"金币":"点卷"));
                            data.set(mshopHolder.getShopname() + ".item-list." + mshopItemData.getId() + ".limit", i + 1);
                            switch (mshopItemData.getItemType()){
                                case POINTS:
                                    plugin.getPoints().getAPI().give(player.getName(),mshopItemData.getVaul());
                                    break;
                                case MONEY:
                                    plugin.getEconomy().withdrawPlayer(player.getName(),mshopItemData.getVaul());
                                    break;
                                default:
                                    return;
                            }
                        }else {
                            player.sendMessage(plugin.getMessageManager().getString("sell-item-no"));
                            return;
                        }
                    }
                    player.closeInventory();
                    InventoryTools.openMshop(player.getName(), mshopHolder.getShopname(), data);
                    return;
                }
            }
        }
    }
}
