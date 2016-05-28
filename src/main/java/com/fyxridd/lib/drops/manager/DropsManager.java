package com.fyxridd.lib.drops.manager;

import com.fyxridd.lib.core.api.CoreApi;
import com.fyxridd.lib.core.api.UtilApi;
import com.fyxridd.lib.core.api.config.ConfigApi;
import com.fyxridd.lib.core.api.config.Setter;
import com.fyxridd.lib.core.api.fancymessage.FancyMessage;
import com.fyxridd.lib.core.api.hashList.ChanceHashList;
import com.fyxridd.lib.core.api.hashList.ChanceHashListImpl;
import com.fyxridd.lib.drops.DropsPlugin;
import com.fyxridd.lib.drops.api.model.Dropper;
import com.fyxridd.lib.drops.api.model.DropperFactory;
import com.fyxridd.lib.drops.config.DropsConfig;
import com.fyxridd.lib.drops.config.LangConfig;
import com.fyxridd.lib.items.api.ItemsApi;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DropsManager implements Listener {
    /**
     * 掉落配置
     */
    private class Drop {
        private ChanceHashList<String> infos;

        Drop(ChanceHashList<String> infos) {
            this.infos = infos;
        }

        ChanceHashList<String> getInfos() {
            return infos;
        }
    }

    /**
     * 掉落信息
     */
    private class Info {
        //可为空列表不为null
        private List<Dropper> droppers;

        Info(List<Dropper> droppers) {
            this.droppers = droppers;
        }

        List<Dropper> getDroppers() {
            return droppers;
        }
    }

    private LangConfig langConfig;
    private DropsConfig dropsConfig;

    //插件名 掉落配置名 掉落配置
    private Map<String, Map<String, Drop>> drops = new HashMap<>();
    //插件名 掉落信息名 掉落信息
    private Map<String, Map<String, Info>> infos = new HashMap<>();

    //key(相当于配置文件中使用的key) 掉落器工厂
    private Map<String, DropperFactory> dropperFactoryMap = new HashMap<>();

	public DropsManager() {
        //添加配置监听
        ConfigApi.addListener(DropsPlugin.instance.pn, LangConfig.class, new Setter<LangConfig>() {
            @Override
            public void set(LangConfig value) {
                langConfig = value;
            }
        });
        ConfigApi.addListener(DropsPlugin.instance.pn, DropsConfig.class, new Setter<DropsConfig>() {
            @Override
            public void set(DropsConfig value) {
                dropsConfig = value;
                ItemsApi.reloadItems(DropsPlugin.instance.pn, value.getGetItemsCs());
            }
        });
    }

    /**
     * @see com.fyxridd.lib.drops.api.DropsApi#registerDropperFactory(String, DropperFactory)
     */
    public void registerDropperFactory(String key, DropperFactory dropperFactory) {
        dropperFactoryMap.put(key, dropperFactory);
    }

    /**
     * @see com.fyxridd.lib.drops.api.DropsApi#reloadDrops(String)
     */
    public void reloadDrops(String plugin) {
        try {
            YamlConfiguration config = UtilApi.loadConfigByUTF8(new File(CoreApi.pluginPath, plugin+File.separator+"drops.yml"));
            //重置缓存
            Map<String, Drop> dropHash = new HashMap<>();
            drops.put(plugin, dropHash);
            Map<String, Info> infoHash = new HashMap<>();
            infos.put(plugin, infoHash);

            //drops
            ConfigurationSection dropsCs = (MemorySection) config.get("drops");
            if (dropsCs != null) {
                for (String dropName:dropsCs.getValues(false).keySet()) {
                    try {
                        ChanceHashList<String> infos = new ChanceHashListImpl<>();
                        for (String s: dropsCs.getStringList(dropName)) infos.addChance(s.split(" ")[0], Integer.parseInt(s.split(" ")[1]));
                        dropHash.put(dropName, new Drop(infos));
                    } catch (Exception e) {
                        throw new Exception("load drop '"+dropName+"' error: "+e.getMessage(), e);
                    }
                }
            }

            //infos
            ConfigurationSection infosCs = (MemorySection) config.get("infos");
            if (infosCs != null) {
                for (String infoName:infosCs.getValues(false).keySet()) {
                    try {
                        ConfigurationSection infoCs = (ConfigurationSection) infosCs.get(infoName);

                        List<Dropper> droppers = new ArrayList<>();
                        infoHash.put(infoName, new Info(droppers));
                        for (String key:infoCs.getValues(false).keySet()) {
                            try {
                                DropperFactory dropperFactory = dropperFactoryMap.get(key);
                                if (dropperFactory != null) {
                                    try {
                                        droppers.add(dropperFactory.produce(infoCs.getConfigurationSection(key)));
                                    } catch (Exception e) {
                                        throw new Exception("produce Dropper error: "+e.getMessage(), e);
                                    }
                                }else CoreApi.debug("DropperFactory for '"+key+"' not found!");//指定的配置没有被处理,提示
                            } catch (Exception e) {
                                throw new Exception("load key '"+key+"' error: "+e.getMessage(), e);
                            }
                        }
                    } catch (Exception e) {
                        throw new Exception("load info '"+infoName+"' error: "+e.getMessage(), e);
                    }
                }
            }
        } catch (Exception e) {
            //todo
        }
    }

    /**
     * @see com.fyxridd.lib.drops.api.DropsApi#drop(String, String, Location, Player)
     */
    public void drop(String plugin, String type, Location loc, Player p) {
        Map<String, Drop> dropsMap = drops.get(plugin);
        if (dropsMap != null) {
            Drop drop = dropsMap.get(type);
            if (drop != null) {
                Map<String, Info> infosMap = infos.get(plugin);
                if (infosMap != null) {
                    Info info = infosMap.get(drop.getInfos().getRandom());
                    if (info != null) {
                        for (Dropper dropper:info.getDroppers()) dropper.drop(plugin, p, loc);
                    }
                }
            }
        }
    }

    public DropsConfig getDropsConfig() {
        return dropsConfig;
    }

    public FancyMessage get(String player, int id, Object... args) {
        return langConfig.getLang().get(player, id, args);
    }
}
