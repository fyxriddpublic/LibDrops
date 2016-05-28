package com.fyxridd.lib.drops.dropper;

import com.fyxridd.lib.core.api.CoreApi;
import com.fyxridd.lib.core.api.MessageApi;
import com.fyxridd.lib.core.api.fancymessage.FancyMessage;
import com.fyxridd.lib.drops.DropsPlugin;
import com.fyxridd.lib.drops.api.model.Dropper;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TipDropper implements Dropper{
    private int lang;
    private boolean range;

    public TipDropper(int lang, boolean range) {
        this.lang = lang;
        this.range = range;
    }

    @Override
    public void drop(String plugin, Player p, Location loc) {
        String name = p != null?p.getName():null;
        FancyMessage msg = DropsPlugin.instance.getDropsManager().get(name, lang);
        if (range) CoreApi.sendMsg(loc, DropsPlugin.instance.getDropsManager().getDropsConfig().getTipRange(), false, msg, false);
        else if (p != null) MessageApi.send(p, msg, true);
    }
}
