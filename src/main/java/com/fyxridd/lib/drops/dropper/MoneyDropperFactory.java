package com.fyxridd.lib.drops.dropper;

import com.fyxridd.lib.core.api.getter.MultiRandomInt;
import com.fyxridd.lib.drops.api.model.DropperFactory;
import org.bukkit.configuration.ConfigurationSection;

public class MoneyDropperFactory implements DropperFactory<MoneyDropper>{
    public static final String KEY = "money";

    @Override
    public MoneyDropper produce(ConfigurationSection cs) throws Exception {
        return new MoneyDropper(new MultiRandomInt(cs.getString("value")));
    }
}
