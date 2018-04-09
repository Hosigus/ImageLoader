package com.hosigus.imageloader.interfaces;

import android.graphics.Bitmap;

/**
 * Created by 某只机智 on 2018/4/7.
 * 加载图片回调接口
 */

public interface LoadImageCallback {
    /**
     * 图片加载成功后回调此方法
     *
     * @param bitmap 加载出的图片
     */
    void success(Bitmap bitmap);

    /**
     * 图片加载失败后回调此方法
     *
     * @param e 错误原因
     */
    void fail(Exception e);
}
