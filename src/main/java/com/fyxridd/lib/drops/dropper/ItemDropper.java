package com.fyxridd.lib.drops.dropper;

import com.fyxridd.lib.drops.DropsPlugin;
import com.fyxridd.lib.drops.api.model.Dropper;
import com.fyxridd.lib.enchants.api.EnchantsApi;
import com.fyxridd.lib.items.api.ItemsApi;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ItemDropper implements Dropper{
    //不为null
    private String getType;
    //可为null
    private String enchantType;

    public ItemDropper(String getType, String enchantType) {
        this.getType = getType;
        this.enchantType = enchantType;
    }

    @Override
    public void drop(String plugin, Player p, Location loc) {
        //获取物品
        List<ItemStack> items = ItemsApi.getItems(plugin, getType);
        //添加附魔
        if (DropsPlugin.libEnchantsHook && enchantType != null) {
            for (ItemStack is:items) EnchantsApi.addEnchant(plugin, enchantType, is);
        }
        //掉落物品
        for (ItemStack is:items) loc.getWorld().dropItem(loc, is);
    }
}
