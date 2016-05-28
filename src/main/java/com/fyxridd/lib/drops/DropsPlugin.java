package com.fyxridd.lib.drops;

import com.fyxridd.lib.core.api.config.ConfigApi;
import com.fyxridd.lib.core.api.plugin.SimplePlugin;
import com.fyxridd.lib.drops.api.DropsApi;
import com.fyxridd.lib.drops.config.DropsConfig;
import com.fyxridd.lib.drops.config.LangConfig;
import com.fyxridd.lib.drops.dropper.*;
import com.fyxridd.lib.drops.manager.DropsManager;
import org.bukkit.Bukkit;

public class DropsPlugin extends SimplePlugin{
    private static final String LIB_ENCHANTS = "LibEnchants";

    public static DropsPlugin instance;
    public static boolean libEnchantsHook;

    private DropsManager dropsManager;

    @Override
    public void onEnable() {
        instance = this;

        libEnchantsHook = Bukkit.getPluginManager().getPlugin(LIB_ENCHANTS) != null;

        //注册配置
        ConfigApi.register(pn, LangConfig.class);
        ConfigApi.register(pn, DropsConfig.class);

        dropsManager = new DropsManager();

        //注册默认掉落器
        DropsApi.registerDropperFactory(MoneyDropperFactory.KEY, new MoneyDropperFactory());
        DropsApi.registerDropperFactory(ExpDropperFactory.KEY, new ExpDropperFactory());
        DropsApi.registerDropperFactory(ItemDropperFactory.KEY, new ItemDropperFactory());
        DropsApi.registerDropperFactory(EntityDropperFactory.KEY, new EntityDropperFactory());
        DropsApi.registerDropperFactory(TipDropperFactory.KEY, new TipDropperFactory());

        super.onEnable();
    }

    public DropsManager getDropsManager() {
        return dropsManager;
    }
}