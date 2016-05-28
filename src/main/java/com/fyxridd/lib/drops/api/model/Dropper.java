package com.fyxridd.lib.drops.api.model;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * 掉落器
 */
public interface Dropper {
    /**
     * 掉落
     * @param p 可为null
     * @param loc 掉落位置
     */
    void drop(String plugin, Player p, Location loc);
}
