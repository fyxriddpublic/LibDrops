package com.fyxridd.lib.drops.dropper;

import com.fyxridd.lib.drops.api.model.DropperFactory;
import org.bukkit.configuration.ConfigurationSection;

public class ItemDropperFactory implements DropperFactory<ItemDropper>{
    public static final String KEY = "item";

    @Override
    public ItemDropper produce(ConfigurationSection cs) throws Exception {
        String getType = cs.getString("getType");
        String enchantType = cs.getString("enchantType");
        return new ItemDropper(getType, enchantType);
    }
}
