package com.hosigus.imageloader.CacheStrategies;

import android.graphics.Bitmap;

import com.hosigus.imageloader.interfaces.CacheStrategy;

/**
 * Created by 某只机智 on 2018/4/6.
 * 空缓存方案
 */

public class NoCacheStrategy implements CacheStrategy {
    private static final NoCacheStrategy ourInstance = new NoCacheStrategy();

    public static NoCacheStrategy getInstance() {
        return ourInstance;
    }

    private NoCacheStrategy() {
    }

    @Override
    public void put(String name, Bitmap bitmap) {

    }

    @Override
    public Bitmap get(String name) {
        return null;
    }
}
