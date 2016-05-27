package com.fyxridd.lib.drops.config;

import com.fyxridd.lib.core.api.ConfigApi;
import com.fyxridd.lib.core.api.event.ReloadConfigEvent;
import com.fyxridd.lib.drops.DropsPlugin;
import com.fyxridd.lib.enchants.api.EnchantsApi;
import com.fyxridd.lib.items.api.ItemsApi;
import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class DropsConfig implements Listener {
    public static double tipRange;
    //配置

    public DropsConfig() {
        //读取配置
        loadConfig();
        //注册事件
        Bukkit.getPluginManager().registerEvents(this, DropsPlugin.instance);
    }

    @EventHandler(priority= EventPriority.LOW)
    public void onReloadConfig(ReloadConfigEvent e) {
        if (e.getPlugin().equals(DropsPlugin.pn)) loadConfig();
    }

    private void loadConfig() {
        YamlConfiguration config = ConfigApi.getConfig(DropsPlugin.pn);

        tipRange = config.getDouble("tipRange");
        if (tipRange < 0) {
            tipRange = 0;
            ConfigApi.log(DropsPlugin.pn, "tipRange < 0");
        }

        //重新读取物品
        ItemsApi.reloadItems(DropsPlugin.pn, (MemorySection) config.get("items"));
        //重新读取附魔
        EnchantsApi.reloadEnchants(DropsPlugin.pn);
    }
}
