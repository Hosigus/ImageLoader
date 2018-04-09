package com.hosigus.imageloader.CompressStrategies;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.hosigus.imageloader.Options.CompressOptions;
import com.hosigus.imageloader.interfaces.CompressStrategy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Hosigus on 2018/4/6.
 * 质量压缩
 * 注:此方法压缩文件大小有限，建议 quality=85
 */

public class QualityCompress implements CompressStrategy {
    private static final QualityCompress ourInstance = new QualityCompress();

    public static QualityCompress getInstance() {
        return ourInstance;
    }

    private QualityCompress() {}

    @Override
    public Bitmap compress(Bitmap bitmap, CompressOptions options) {
        int quality,maxSize;
        if (options.quality == CompressOptions.ANY) {
            if (options.maxSize == CompressOptions.ANY) {
                return bitmap;
            }
            quality = 100;
            maxSize = options.maxSize;
        }else {
            quality = options.quality;
            maxSize = bitmap.getRowBytes() * bitmap.getHeight();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, quality, baos);
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, null);
        while (baos.toByteArray().length > maxSize && quality < 10) {
            quality -= 5;
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, baos);
        }
        try {
            baos.close();
            isBm.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
