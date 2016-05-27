package com.fyxridd.lib.drops.api;

import com.fyxridd.lib.drops.DropsPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class DropsApi {
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

    /**
     * 获取掉落
     * @param plugin 插件
     * @param type 掉落配置
     * @return 掉落信息,可为null
     */
    public static DropInfo getDrops(String plugin, String type) {
        return DropsPlugin.instance.getDropsManager().getDrops(plugin, type);
    }
}
