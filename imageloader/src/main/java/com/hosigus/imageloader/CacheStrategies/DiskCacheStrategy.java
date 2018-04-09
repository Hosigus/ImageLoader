package com.hosigus.imageloader.CacheStrategies;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.hosigus.imageloader.interfaces.CacheStrategy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by 某只机智 on 2018/4/6.
 * 本地缓存
 */

public class DiskCacheStrategy implements CacheStrategy {
    private static final DiskCacheStrategy instance = new DiskCacheStrategy();
    private String mCacheDir;

    public static DiskCacheStrategy getInstance() {
        return instance;
    }

    @Override
    public void put(String name, Bitmap bitmap) {
        if (mCacheDir == null)
            return;
        FileOutputStream fos = null;
        try {
            File file=new File(mCacheDir,name);
            File parentFile = file.getParentFile();
            if (!parentFile.exists()&&!parentFile.mkdirs()){
                return;
            }
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

    }

    @Override
    public Bitmap get(String name) {
        if (mCacheDir == null)
            return null;
        return BitmapFactory.decodeFile(mCacheDir + name);
    }

    public void setCacheDir(String path) {
        if (mCacheDir==null)
            this.mCacheDir = path.endsWith("/") ? path : path + "/";
    }

    public String getCacheDir(){
        return mCacheDir;
    }
}
