package com.fyxridd.lib.drops.model;

import com.fyxridd.lib.core.api.fancymessage.FancyMessage;
import com.fyxridd.lib.core.api.getter.MultiRandomInt;
import com.fyxridd.lib.core.api.hashList.ChanceHashList;

/**
 * 掉落信息
 */
public class Info {
    private String plugin;
    private MultiRandomInt money;

    private boolean expInstant;
    private MultiRandomInt exp;

    private String itemType;
    private String itemEnchants;

    private ChanceHashList<EntityInfo> entity;

    private FancyMessage tipMsg;
    private boolean tipRange;

    public Info(String plugin, MultiRandomInt money, boolean expInstant, MultiRandomInt exp, String itemType, String itemEnchants, ChanceHashList<EntityInfo> entity, FancyMessage tipMsg, boolean tipRange) {
        this.plugin = plugin;
        this.money = money;
        this.expInstant = expInstant;
        this.exp = exp;
        this.itemType = itemType;
        this.itemEnchants = itemEnchants;
        this.entity = entity;
        this.tipMsg = tipMsg;
        this.tipRange = tipRange;
    }

    public String getPlugin() {
        return plugin;
    }

    public MultiRandomInt getMoney() {
        return money;
    }

    public MultiRandomInt getExp() {
        return exp;
    }

    public boolean isExpInstant() {
        return expInstant;
    }

    public String getItemType() {
        return itemType;
    }

    public String getItemEnchants() {
        return itemEnchants;
    }

    public ChanceHashList<EntityInfo> getEntity() {
        return entity;
    }

    public FancyMessage getTipMsg() {
        return tipMsg;
    }

    public boolean isTipRange() {
        return tipRange;
    }
}
