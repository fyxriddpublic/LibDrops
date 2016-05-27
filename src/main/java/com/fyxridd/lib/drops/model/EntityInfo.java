package com.fyxridd.lib.drops.model;

import org.bukkit.entity.EntityType;

/**
 * 实体配置信息
 */
public class EntityInfo {
    private EntityType type;
    private int amount;
    //可为null
    private String strength;

    public EntityInfo(EntityType type, int amount, String strength) {
        this.type = type;
        this.amount = amount;
        this.strength = strength;
    }

    public EntityType getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public String getStrength() {
        return strength;
    }
}
