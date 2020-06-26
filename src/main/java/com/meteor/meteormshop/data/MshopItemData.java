package com.meteor.meteormshop.data;

import org.bukkit.inventory.ItemStack;

public class MshopItemData {
    int id;
    ItemStack itemStack;
    //价格(点卷,金币)
    int vaul;
    ItemType itemType;
    int limit;
    //刷新几率,权重
    int pro;

    public MshopItemData(int id, ItemStack itemStack, int vaul, ItemType itemType, int limit, int pro) {
        this.id = id;
        this.itemStack = itemStack;
        this.vaul = vaul;
        this.itemType = itemType;
        this.limit = limit;
        this.pro = pro;
    }
    
    public int getPro() {
        return pro;
    }

    public void setPro(int pro) {
        this.pro = pro;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public int getVaul() {
        return vaul;
    }

    public void setVaul(int vaul) {
        this.vaul = vaul;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
