package com.hosigus.imageloader.CacheStrategies;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.hosigus.imageloader.interfaces.CacheStrategy;

/**
 * Created by 某只机智 on 2018/4/6.
 * 内存缓存
 */

public class MemoryCacheStrategy implements CacheStrategy {
    private static final MemoryCacheStrategy instance = new MemoryCacheStrategy();

    private LruCache<String, Bitmap> mMemoryCache;

    public static MemoryCacheStrategy getInstance() {
        return instance;
    }

    private MemoryCacheStrategy() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024),
                cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };
    }

    @Override
    public void put(String name, Bitmap bitmap) {
        if (mMemoryCache.get(name) != null) {
            mMemoryCache.put(name,bitmap);
        }
    }

    @Override
    public Bitmap get(String name) {
        return mMemoryCache.get(name);
    }
}
