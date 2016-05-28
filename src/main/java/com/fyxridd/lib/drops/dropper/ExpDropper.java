package com.fyxridd.lib.drops.dropper;

import com.fyxridd.lib.core.api.MessageApi;
import com.fyxridd.lib.core.api.getter.MultiRandomInt;
import com.fyxridd.lib.drops.DropsPlugin;
import com.fyxridd.lib.drops.api.model.Dropper;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;

public class ExpDropper implements Dropper{
    private boolean instant;
    private MultiRandomInt exp;

    public ExpDropper(boolean instant, MultiRandomInt exp) {
        this.instant = instant;
        this.exp = exp;
    }

    @Override
    public void drop(String plugin, Player p, Location loc) {
        int drop = exp.get(0);
        if (drop > 0) {
            if (instant) {
                if (p != null) {
                    p.giveExp(drop);
                    MessageApi.send(p, DropsPlugin.instance.getDropsManager().get(p.getName(), 30, drop), true);
                }
            }else {
                ExperienceOrb orb = (ExperienceOrb) loc.getWorld().spawnEntity(loc, EntityType.EXPERIENCE_ORB);
                orb.setExperience(drop);
            }
        }
    }
}
