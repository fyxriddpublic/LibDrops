package com.fyxridd.lib.drops.dropper;

import com.fyxridd.lib.core.api.getter.MultiRandomInt;
import com.fyxridd.lib.drops.api.model.DropperFactory;
import org.bukkit.configuration.ConfigurationSection;

public class ExpDropperFactory implements DropperFactory<ExpDropper>{
    public static final String KEY = "exp";

    @Override
    public ExpDropper produce(ConfigurationSection cs) throws Exception {
        boolean instant = cs.getBoolean("instant");
        MultiRandomInt value = new MultiRandomInt(cs.getString("value"));
        return new ExpDropper(instant, value);
    }
}
