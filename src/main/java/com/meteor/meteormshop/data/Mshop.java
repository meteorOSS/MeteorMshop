package com.meteor.meteormshop.data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Mshop {
    String title;
    Map<String,MshopItemData> items = new HashMap<>();
    //刷新时间,单位:小时
    int time;
    ShopType shopType;
    int amount;

    public Mshop(ShopType shopType,String title, Map<String, MshopItemData> items, int time, int amount) {
        this.shopType = shopType;
        this.title = title;
        this.items = items;
        this.time = time;
        this.amount = amount;
    }

    public Map<String, MshopItemData> getItems() {
        return items;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setItems(Map<String, MshopItemData> items) {
        this.items = items;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public ShopType getShopType() {
        return shopType;
    }

    public void setShopType(ShopType shopType) {
        this.shopType = shopType;
    }
}
