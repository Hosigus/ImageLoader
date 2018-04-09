package com.hosigus.imageloader.interfaces;

import android.graphics.Bitmap;

import com.hosigus.imageloader.Options.CompressOptions;

/**
 * Created by Hosigus on 2018/4/5.
 * 压缩接口
 */

public interface CompressStrategy {
    Bitmap compress(Bitmap bitmap, CompressOptions options);
}
