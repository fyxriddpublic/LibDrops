package com.fyxridd.lib.drops.manager;

import com.fyxridd.lib.core.api.CoreApi;
import com.fyxridd.lib.core.api.EcoApi;
import com.fyxridd.lib.core.api.UtilApi;
import com.fyxridd.lib.core.api.fancymessage.FancyMessage;
import com.fyxridd.lib.core.api.getter.MultiRandomInt;
import com.fyxridd.lib.core.api.hashList.ChanceHashList;
import com.fyxridd.lib.core.api.hashList.ChanceHashListImpl;
import com.fyxridd.lib.drops.DropsPlugin;
import com.fyxridd.lib.drops.api.DropInfo;
import com.fyxridd.lib.drops.model.Drop;
import com.fyxridd.lib.drops.model.EntityInfo;
import com.fyxridd.lib.drops.model.Info;
import com.fyxridd.lib.enchants.api.EnchantsApi;
import com.fyxridd.lib.items.api.ItemsApi;
import org.bukkit.Location;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DropsManager implements Listener {
    //插件名 掉落配置名 掉落配置
    private Map<String, Map<String, Drop>> drops = new HashMap<>();
    //插件名 掉落信息名 掉落信息
    private Map<String, Map<String, Info>> infos = new HashMap<>();

	public DropsManager() {
    }

    /**
     * @see com.fyxridd.lib.drops.api.DropsApi#reloadDrops(String)
     */
    public void reloadDrops(String plugin) {
        YamlConfiguration config = UtilApi.loadConfigByUTF8(new File(CoreApi.pluginPath, plugin+File.separator+"drops.yml"));
        //重置缓存
        Map<String, Drop> dropHash = new HashMap<>();
        drops.put(plugin, dropHash);
        Map<String, Info> infoHash = new HashMap<>();
        infos.put(plugin, infoHash);

        //drops
        MemorySection dropsMs = (MemorySection) config.get("drops");
        if (dropsMs != null) {
            for (String dropName:dropsMs.getValues(false).keySet()) {
                ChanceHashList<String> infos = new ChanceHashListImpl<>();
                for (String s: dropsMs.getStringList(dropName)) infos.addChance(s.split(" ")[0], Integer.parseInt(s.split(" ")[1]));
                dropHash.put(dropName, new Drop(dropName, infos));
            }
        }

        //infos
        MemorySection infosMs = (MemorySection) config.get("infos");
        if (infosMs != null) {
            for (String infoName:infosMs.getValues(false).keySet()) {
                MemorySection infoMs = (MemorySection) infosMs.get(infoName);

                //money
                MultiRandomInt money = new MultiRandomInt(infoMs.getString("money"));

                //exp
                boolean expInstant = infoMs.getBoolean("exp.instant");
                MultiRandomInt exp = new MultiRandomInt(infoMs.getString("exp"));

                //item
                String itemType = infoMs.getString("item.type");
                String itemEnchants = infoMs.getString("item.enchants");

                //entity
                ChanceHashList<EntityInfo> entity = new ChanceHashListImpl<>();
                for (String s:infoMs.getStringList("entity.types")) {
                    String[] args = s.split(" ");
                    EntityType type = CoreApi.getEntityType(args[0]);
                    int amount = Integer.parseInt(args[1]);
                    if (amount < 0) {
                        amount = 0;
                        ConfigApi.log(DropsPlugin.pn, infoName+"'s some amount < 0");
                    }
                    int chance = Integer.parseInt(args[2]);
                    if (chance < 0) {
                        chance = 0;
                        ConfigApi.log(DropsPlugin.pn, infoName+"'s some chance < 0");
                    }
                    String strength = args.length == 4?args[3]:null;
                    entity.addChance(new EntityInfo(type, amount, strength), chance);
                }

                //tip
                FancyMessage tipMsg = infoMs.contains("tip.lang")?FormatApi.get(plugin, infoMs.getInt("tip.lang")):null;
                boolean tipRange = infoMs.getBoolean("tip.range");

                //添加
                infoHash.put(infoName, new Info(plugin, money, expInstant, exp, itemType, itemEnchants, entity, tipMsg, tipRange));
            }
        }
    }

    /**
     * @see com.fyxridd.lib.drops.api.DropsApi#drop(String, String, Location, Player)
     */
    public void drop(String plugin, String type, Location loc, Player p) {
        //获取掉落
        DropInfo dropInfo = getDrops(plugin, type);
        //掉落
        if (p != null && dropInfo.getMoney() > 0) {
            EcoApi.add(p.getName(), dropInfo.getMoney());
            ShowApi.tip(p, get(20, dropInfo.getMoney()), true);
        }
        if (dropInfo.getExp() > 0) {
            if (dropInfo.isExpInstant()) {
                if (p != null) {
                    p.giveExp(dropInfo.getExp());
                    ShowApi.tip(p, get(30, dropInfo.getExp()), true);
                }
            }else {
                ExperienceOrb orb = (ExperienceOrb) loc.getWorld().spawnEntity(loc, EntityType.EXPERIENCE_ORB);
                orb.setExperience(dropInfo.getExp());
            }
        }
        if (dropInfo.getItem() != null) {
            for (ItemStack is:dropInfo.getItem()) {
                loc.getWorld().dropItem(loc, is);
            }
        }
        if (dropInfo.getEntityType() != null) {
            for (int index=0;index<dropInfo.getEntityAmount();index++) {
                Entity entity = loc.getWorld().spawnEntity(loc, dropInfo.getEntityType());
                if (DropsPlugin.strengthHook && dropInfo.getStrengthType() != null && entity instanceof LivingEntity) {
                    LivingEntity le = (LivingEntity) entity;
                    StrengthApi.strength(le, dropInfo.getStrengthType(), true);
                }
            }
        }
        if (dropInfo.getTip() != null) {
            if (dropInfo.isTipRange()) CoreApi.sendMsg(loc, DropsConfig.tipRange, false, dropInfo.getTip(), false);
            else if (p != null) ShowApi.tip(p, dropInfo.getTip(), true);
        }
    }

    public DropInfo getDrops(String plugin, String type) {
        Map<String, Drop> dropHash = drops.get(plugin);
        if (dropHash != null) {
            Drop drop = dropHash.get(type);
            if (drop != null && drop.getInfos() != null && !drop.getInfos().isEmpty()) {
                String infoName = drop.getInfos().getRandom();
                Map<String, Info> infoHash = infos.get(plugin);
                if (infoHash != null) {
                    Info info = infoHash.get(infoName);
                    if (info != null) {
                        //解析
                        int money = CoreApi.Random.nextInt(info.getMoneyMax()-info.getMoneyMin()+1)+info.getMoneyMin();
                        int exp = CoreApi.Random.nextInt(info.getExpMax()-info.getExpMin()+1)+info.getExpMin();
                        List<ItemStack> item;
                        if (info.getItemType() != null) {
                            item = ItemsApi.getItems(info.getPlugin(), info.getItemType());
                            if (!item.isEmpty() && info.getItemEnchants() != null) {
                                for (ItemStack is:item) EnchantsApi.addEnchant(info.getPlugin(), info.getItemEnchants(), is);
                            }
                        }else item = null;
                        EntityInfo entityInfo;
                        if (info.getEntity() != null && !info.getEntity().isEmpty()) entityInfo = info.getEntity().getRandom();
                        else entityInfo = null;
                        return new DropInfo(money, info.isExpInstant(), exp, item, entityInfo!=null?entityInfo.getType():null, entityInfo!=null?entityInfo.getStrength():null, entityInfo!=null?entityInfo.getAmount():0, info.getTipMsg(), info.isTipRange());
                    }
                }
            }
        }
        return null;
    }

    private static FancyMessage get(String player, int id, Object... args) {
        return FormatApi.get(DropsPlugin.pn, id, args);
    }
}
