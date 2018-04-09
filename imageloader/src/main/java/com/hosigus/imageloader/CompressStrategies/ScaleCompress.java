package com.hosigus.imageloader.CompressStrategies;

import android.graphics.Bitmap;
import android.util.Log;

import com.hosigus.imageloader.Options.CompressOptions;
import com.hosigus.imageloader.interfaces.CompressStrategy;

/**
 * Created by 某只机智 on 2018/4/6.
 * 放缩图片
 */

public class ScaleCompress implements CompressStrategy {
    private static final ScaleCompress instance = new ScaleCompress();

    public static ScaleCompress getInstance() {
        return instance;
    }

    private ScaleCompress() {}

    @Override
    public Bitmap compress(Bitmap bitmap, CompressOptions options) {
        int srcWidth = bitmap.getWidth(),
                srcHeight = bitmap.getHeight(),
                outWidth = options.width,
                outHeight = options.height;
        float srcRatio = 1f * srcWidth / srcHeight;

        Log.d("compress test", "src: " + srcWidth + "/" + srcHeight
                +"\n option:"+outWidth + "/" + outHeight);
        if (outHeight <= 0 && outWidth <= 0) {
            return bitmap;
        } else if (outHeight <= 0) {
            outHeight = (int) (outWidth / srcRatio);
        } else if (outWidth <= 0) {
            outWidth = (int) (outHeight * srcRatio);
        }else {
            float outRatio = 1f * outWidth / outHeight;
            if (outRatio < srcRatio) {
                outHeight = (int) (outWidth / srcRatio);
            } else if (outRatio > srcRatio) {
                outWidth = (int) (outHeight * srcRatio);
            }
        }
        Log.d("compress test", "out:" + outWidth + "/" + outHeight);
        return Bitmap.createScaledBitmap(bitmap, outWidth, outHeight, true);
    }

}
