package com.fyxridd.lib.drops.config;

import com.fyxridd.lib.core.api.config.basic.Path;
import com.fyxridd.lib.core.api.config.convert.ConfigConvert;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;

public class DropsConfig implements Listener {
    private class GetItemsCsConverter implements ConfigConvert.ConfigConverter<ConfigurationSection>{
        @Override
        public ConfigurationSection convert(String plugin, ConfigurationSection config) throws Exception {
            return config;
        }
    }

    @Path("tipRange")
    private double tipRange;

    @Path("items")
    @ConfigConvert(GetItemsCsConverter.class)
    private ConfigurationSection getItemsCs;

    public double getTipRange() {
        return tipRange;
    }

    public ConfigurationSection getGetItemsCs() {
        return getItemsCs;
    }
}
