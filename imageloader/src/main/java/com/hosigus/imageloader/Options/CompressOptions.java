package com.hosigus.imageloader.Options;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.hosigus.imageloader.interfaces.CompressStrategy;

/**
 * Created by Hosigus on 2018/4/6.
 * 压缩参数
 * 若需要更多参数，请继承本类
 */

public class CompressOptions {
    /**
     * 任意值
     */
    public static final int ANY = -1;
    /**
     * 图片宽度
     * 单位: px
     */
    public int width = ANY;
    /**
     * 图片高度
     * 单位: px
     */
    public int height = ANY;
    /**
     * 质量，建议为85
     */
    public int quality = 85;
    /**
     * 图片最大尺寸
     * 单位: byte
     */
    public int maxSize = ANY;
    /**
     * 取样率
     */
    public int inSampleSize = 1;

    public CompressOptions scale(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public CompressOptions quality(int quality) {
        this.quality = quality;
        return this;
    }

    public CompressOptions inSampleSize(int inSampleSize) {
        this.inSampleSize = inSampleSize;
        return this;
    }

    public CompressOptions maxSize(int maxSize) {
        this.maxSize = maxSize;
        return this;
    }

    public static int calculateInSampleSize(Bitmap bitmap,int reqWidth, int reqHeight) {
        if (reqWidth <= 0 || reqHeight <= 0) {
            return 1;
        }
        int w = bitmap.getWidth(), h = bitmap.getHeight(), inSampleSize = 1;
        if (h > reqHeight || w > reqWidth) {
            final int halfW = w / 2, halfH = h / 2;
            while (halfH / inSampleSize >= reqHeight && halfW / inSampleSize >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    @Override
    public String toString() {
        return "width=" + width + "&height=" + height + "&quality=" + quality + "&maxSize" + maxSize + "&inSampleSize=" + inSampleSize;
    }
}