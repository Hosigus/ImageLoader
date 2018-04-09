package com.hosigus.imageloader.CompressStrategies;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;

import com.hosigus.imageloader.Options.CompressOptions;
import com.hosigus.imageloader.interfaces.CompressStrategy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Hosigus on 2018/4/6.
 * 采样压缩，能有效压缩文件大小
 * 但是只能按2的倍数压缩图片
 */

public class InSampleSizeCompress implements CompressStrategy {
    private static final InSampleSizeCompress instance = new InSampleSizeCompress();

    public static InSampleSizeCompress getInstance() {
        return instance;
    }

    private InSampleSizeCompress() {}

    @Override
    public Bitmap compress(Bitmap bitmap, CompressOptions options) {
        final int inSampleSize = calculateInSampleSize(bitmap, options);
        if (inSampleSize != 1) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            ByteArrayInputStream isBm = new ByteArrayInputStream(out.toByteArray());
            BitmapFactory.Options bfo = new BitmapFactory.Options();
            bfo.inJustDecodeBounds = true;
            bfo.inSampleSize = inSampleSize;
            bitmap = BitmapFactory.decodeStream(isBm, null, bfo);
            try {
                out.close();
                isBm.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    private int calculateInSampleSize(Bitmap bitmap, CompressOptions options) {
        if (options.inSampleSize != -1) {
            return options.inSampleSize;
        }
        final int maxSize = options.maxSize, size = bitmap.getRowBytes() * bitmap.getHeight();
        int inSampleSize = 1;
        while (size / inSampleSize > maxSize) {
            inSampleSize *= 2;
        }
        return inSampleSize;
    }

}
