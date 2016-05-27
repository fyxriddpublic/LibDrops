package com.fyxridd.lib.drops;

import com.fyxridd.lib.core.api.plugin.SimplePlugin;
import com.fyxridd.lib.drops.manager.DropsManager;

public class DropsPlugin extends SimplePlugin{
    public static DropsPlugin instance;

    private DropsManager dropsManager;

    @Override
    public void onEnable() {
        instance = this;

        dropsManager = new DropsManager();

        super.onEnable();
    }

    public DropsManager getDropsManager() {
        return dropsManager;
    }
}