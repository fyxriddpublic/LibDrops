package com.fyxridd.lib.drops.dropper;

import com.fyxridd.lib.core.api.getter.MultiRandomInt;
import com.fyxridd.lib.drops.api.model.Dropper;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Map;

public class EntityDropper implements Dropper{
    private Map<EntityType, MultiRandomInt> entities;

    public EntityDropper(Map<EntityType, MultiRandomInt> entities) {
        this.entities = entities;
    }

    @Override
    public void drop(String plugin, Player p, Location loc) {
        for (Map.Entry<EntityType, MultiRandomInt> entry:entities.entrySet()) {
            int amount = entry.getValue().get(0);
            for (int index=0;index<amount;index++) loc.getWorld().spawnEntity(loc, entry.getKey());
        }
    }
}
