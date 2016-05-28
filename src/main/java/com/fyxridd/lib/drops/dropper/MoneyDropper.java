package com.fyxridd.lib.drops.dropper;

import com.fyxridd.lib.core.api.EcoApi;
import com.fyxridd.lib.core.api.MessageApi;
import com.fyxridd.lib.core.api.getter.MultiRandomInt;
import com.fyxridd.lib.drops.DropsPlugin;
import com.fyxridd.lib.drops.api.model.Dropper;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MoneyDropper implements Dropper{
    private MultiRandomInt money;

    public MoneyDropper(MultiRandomInt money) {
        this.money = money;
    }

    @Override
    public void drop(String plugin, Player p, Location loc) {
        if (p != null) {
            int drop = money.get(0);
            if (drop > 0) {
                EcoApi.add(p.getName(), drop);
                MessageApi.send(p, DropsPlugin.instance.getDropsManager().get(p.getName(), 20, drop), true);
            }
        }
    }
}
