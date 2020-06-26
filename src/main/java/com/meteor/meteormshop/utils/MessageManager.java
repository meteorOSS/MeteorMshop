package com.meteor.meteormshop.utils;

import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageManager {
    YamlConfiguration lang;
    public MessageManager(YamlConfiguration lang){
        this.lang = lang;
    }
    public String getString(String path){
        if(lang.getString(path)==null){
            return "未寻找至语言文件,请检查....";
        }
        return lang.getString(path).replace("@prefix@",lang.getString("prefix")).replace("&","§");
    }
    public List<String> getStringList(String path){
        if(lang.getStringList(path)==null){
            return Arrays.asList("未寻找到语言文件,请检查...");
        }
        List<String> rn = new ArrayList<>();
        lang.getStringList(path).forEach(a->rn.add(a.replace("@prefix@",lang.getString("prefix")).replace("&","§")));
        return rn;
    }
}
