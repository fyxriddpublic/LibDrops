package com.fyxridd.lib.drops.dropper;

import com.fyxridd.lib.core.api.CoreApi;
import com.fyxridd.lib.core.api.getter.MultiRandomInt;
import com.fyxridd.lib.drops.api.model.DropperFactory;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

public class EntityDropperFactory implements DropperFactory<EntityDropper>{
    public static final String KEY = "entity";

    @Override
    public EntityDropper produce(ConfigurationSection cs) throws Exception {
        Map<EntityType, MultiRandomInt> map = new HashMap<>();
        for (String s:cs.getStringList("values")) {
            String[] ss = s.split(" ", 2);
            map.put(CoreApi.getEntityType(ss[0]), new MultiRandomInt(ss[1]));
        }
        return new EntityDropper(map);
    }
}
