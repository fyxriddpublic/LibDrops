package com.fyxridd.lib.drops.model;

import com.fyxridd.lib.core.api.hashList.ChanceHashList;

/**
 * 掉落配置
 */
public class Drop {
    private String name;
    private ChanceHashList<String> infos;

    public Drop(String name, ChanceHashList<String> infos) {
        this.name = name;
        this.infos = infos;
    }

    public String getName() {
        return name;
    }

    public ChanceHashList<String> getInfos() {
        return infos;
    }
}
