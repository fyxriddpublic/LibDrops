package com.fyxridd.lib.drops.dropper;

import com.fyxridd.lib.drops.api.model.DropperFactory;
import org.bukkit.configuration.ConfigurationSection;

public class TipDropperFactory implements DropperFactory<TipDropper>{
    public static final String KEY = "tip";

    @Override
    public TipDropper produce(ConfigurationSection cs) throws Exception {
        int lang = cs.getInt("lang");
        boolean range = cs.getBoolean("range");
        return new TipDropper(lang, range);
    }
}
