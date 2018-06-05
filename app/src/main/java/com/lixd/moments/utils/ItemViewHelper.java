package com.lixd.moments.utils;

import java.util.HashMap;

public class ItemViewHelper {
    private static ItemViewHelper sHelper = null;
    /*
     * 保存每个ItemView的高度
     * 主要解决获取linearLayoutManager.findViewByPosition() 为null
     * 得不到高度就无法计算需要滚动的偏移量了!
     */
    private HashMap<Integer, Integer> map;

    private ItemViewHelper() {
        map = new HashMap<>();
    }

    public static final ItemViewHelper getInstance() {
        if (sHelper == null) {
            sHelper = new ItemViewHelper();
        }
        return sHelper;
    }

    public HashMap<Integer, Integer> getMap() {
        return map;
    }

    public void put(int key, int value) {
        map.put(key, value);
    }

    public int findItemViewHeight(int position) {
        if (map.containsKey(position)) {
            return map.get(position);
        }
        return 0;
    }

}
