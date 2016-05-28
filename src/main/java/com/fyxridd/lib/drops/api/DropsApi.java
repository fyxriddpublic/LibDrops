package com.fyxridd.lib.drops.api;

import com.fyxridd.lib.drops.DropsPlugin;
import com.fyxridd.lib.drops.api.model.DropperFactory;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class DropsApi {
    /**
     * 注册掉落器工厂
     * @param key 注册的键(相当于配置文件中的键)
     */
    public static void registerDropperFactory(String key, DropperFactory dropperFactory) {
        DropsPlugin.instance.getDropsManager().registerDropperFactory(key, dropperFactory);
    }

    /**
     * 重新读取掉落配置
     * 会读取'插件名/drops.yml'文件
     */
    public static void reloadDrops(String plugin) {
        DropsPlugin.instance.getDropsManager().reloadDrops(plugin);
    }

    /**
     * 掉落
     * 玩家为null时:
     *   1. 掉落金币无效
     *   2. 直接添加到玩家上的经验无效
     *   3. 直接提示玩家的信息无效
     * @param plugin 插件
     * @param type 掉落配置
     * @param loc 掉落的位置
     * @param p 玩家,可为null
     */
    public static void drop(String plugin, String type, Location loc, Player p) {
        DropsPlugin.instance.getDropsManager().drop(plugin, type, loc, p);
    }
}
