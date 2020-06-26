package com.meteor.meteormshop.utils;

import com.meteor.meteormshop.MeteorMshop;
import com.meteor.meteormshop.data.Mshop;
import com.meteor.meteormshop.data.MshopItemData;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.*;

public class LotteryTools {
    public static MshopItemData lotteryItem(Map<String,MshopItemData> items){
        MshopItemData mshopItemData = null;
        if (items == null || items.isEmpty()) {
            return mshopItemData;
        }

        // 总概率
        double sumRate = 0d;
        for(String key : items.keySet()) {
            double pro = items.get(key).getPro();
            if(pro>0) {
                sumRate += pro;
            }
        }

        // 总概率下的概率情况
        List<Double> sortOrignalRates = new ArrayList<Double>(items.size());
        Double tempSumRate = 0d;
        for(String key : items.keySet()) {
            double pro = items.get(key).getPro();
            if(pro>0) {
                tempSumRate += pro;
                sortOrignalRates.add(tempSumRate/sumRate);
            }
        }

        // 抽取物品索引
        double nextDouble = Math.random();
        sortOrignalRates.add(nextDouble);
        Collections.sort(sortOrignalRates);
        int numA=sortOrignalRates.indexOf(nextDouble);
        List<String> rn = new ArrayList<String>();
        rn.addAll(items.keySet());
        mshopItemData = items.get(rn.get(numA));
        return mshopItemData;
    }

    public static void saveMshop(YamlConfiguration data, String mshopname, String player){
        Mshop mshop = MeteorMshop.getInstance().getMshopData().getMshops().get(mshopname);
            data.set(mshopname+".time",System.currentTimeMillis());
            data.set(mshopname+".item-list",null);
            for(int i=0;i<mshop.getAmount();i++){
                MshopItemData mshopItemData = LotteryTools.lotteryItem(mshop.getItems());
                data.set(mshopname+".item-list."+mshopItemData.getId()+".limit",0);
            }
    }
}
