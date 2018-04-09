package com.hosigus.imageloader.interfaces;

import android.graphics.Bitmap;

/**
 * Created by Hosigus on 2018/4/5.
 * 缓存接口
 */

public interface CacheStrategy {
    void put(String name, Bitmap bitmap);
    Bitmap get(String name);
}
