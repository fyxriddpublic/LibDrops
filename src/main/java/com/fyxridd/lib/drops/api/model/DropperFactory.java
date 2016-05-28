package com.fyxridd.lib.drops.api.model;

import org.bukkit.configuration.ConfigurationSection;

/**
 * 掉落器工厂
 */
public interface DropperFactory<T extends Dropper> {
    /**
     * 生产掉落器
     * @param cs 配置
     * @return 掉落器,不为null
     */
    T produce(ConfigurationSection cs) throws Exception;
}
