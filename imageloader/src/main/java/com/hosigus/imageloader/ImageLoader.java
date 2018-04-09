package com.hosigus.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.hosigus.imageloader.CacheStrategies.DiskCacheStrategy;
import com.hosigus.imageloader.CacheStrategies.MemoryCacheStrategy;
import com.hosigus.imageloader.CompressStrategies.ScaleCompress;
import com.hosigus.imageloader.Encryptors.MD5Encryptor;
import com.hosigus.imageloader.Exceptions.LackParamException;
import com.hosigus.imageloader.Options.CompressOptions;
import com.hosigus.imageloader.interfaces.CacheStrategy;
import com.hosigus.imageloader.interfaces.CompressStrategy;
import com.hosigus.imageloader.interfaces.Encryptor;
import com.hosigus.imageloader.interfaces.LoadImageCallback;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * Created by 某只机智 on 2018/4/5.
 * 使用只需关注本类
 */

public class ImageLoader {
    private static final Handler UI_HANDLER = new Handler();
    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();

    private static ExecutorService sFixedThreadPool = new ThreadPoolExecutor(THREAD_COUNT,
            THREAD_COUNT, 0L, TimeUnit.MILLISECONDS,
            new StackLinkedBlockingDeque<Runnable>());

    private ImageLoaderBuilder builder;
    private String mAddress;
    private ImageView mImageView;

    private ImageLoader(@NonNull ImageLoaderBuilder builder) {
        this.builder = builder;
    }

    public static ImageLoaderBuilder with(Context context){
        return new ImageLoaderBuilder(context);
    }

    public ImageLoader load(@NonNull String address) {
        if (address.isEmpty()) {
            return null;
        }
        mAddress = address;
        return this;
    }

    public void into(@NonNull ImageView imageView) {
        this.mImageView = imageView;
        load2ImageView(true);
    }
    public void into(@NonNull ImageView imageView,boolean autoCompress) {
        this.mImageView = imageView;
        load2ImageView(autoCompress);
    }

    public void getImage(LoadImageCallback callback) {
        loadBitmap(callback,getEncodedAddress());
    }

    public void intoCache(final String label) {
        loadBitmap(new LoadImageCallback() {
            @Override
            public void success(Bitmap bitmap) {
                bitmap = compress(bitmap);
                boolean temp = builder.skipMemoryCache;
                builder.skipMemoryCache = true;
                cache(bitmap, builder.mEncryptor.encode(mAddress + label));
                builder.skipMemoryCache = temp;
            }

            @Override
            public void fail(Exception e) {
                e.printStackTrace();
            }
        },builder.mEncryptor.encode(mAddress));
    }

    public void intoCache() {
        intoCache(builder.mCompressOptions.toString());
    }

    private void load2ImageView(boolean autoCompress) {

        if (autoCompress){
            if (builder.mCompressOptions == null) {
                builder.mCompressOptions = new CompressOptions();
            }
            builder.mCompressOptions.scale(mImageView.getWidth(), mImageView.getHeight());
            boolean hadScale = false;
            for (CompressStrategy compressStrategy:builder.compressList) {
                if (compressStrategy instanceof ScaleCompress) {
                    hadScale = true;
                }
            }
            if (!hadScale){
                builder.compressList.add(0, ScaleCompress.getInstance());
            }
        }

        mImageView.setTag(getEncodedAddress());

        if (builder.mPlaceHolder != null && mImageView.getTag().equals(getEncodedAddress())) {
            mImageView.setImageDrawable(builder.mPlaceHolder);
        }

        loadBitmap(new LoadImageCallback() {
            @Override
            public void success(Bitmap bitmap) {
                if (mImageView.getTag().equals(getEncodedAddress())){
                    mImageView.setImageBitmap(bitmap);
                }
            }

            @Override
            public void fail(Exception e) {
                if (builder.mErrorHolder != null && mImageView.getTag().equals(getEncodedAddress())) {
                    mImageView.setImageDrawable(builder.mErrorHolder);
                }
                e.printStackTrace();
            }
        },getEncodedAddress());
    }

    private void loadBitmap(final LoadImageCallback callback, final String url) {
        sFixedThreadPool.submit(new Runnable() {
            @Override
            public void run() {
            Bitmap bitmap = null;
            if (builder.skipMemoryCache){
                bitmap = builder.mCacheStrategy.get(url);
            }else {
                bitmap = MemoryCacheStrategy.getInstance().get(url);
                if (bitmap == null) {
                    bitmap = builder.mCacheStrategy.get(url);
                }
            }
            if (bitmap == null) {
                bitmap = mAddress.startsWith("http") ? readFromHttp():readFromFile();
                if (bitmap == null) {
                    runOnUIThread(callback,null,new Exception("Error address,can't get img from it"));
                }
                if (builder.withOriginal) {
                    cache(bitmap,builder.mEncryptor.encode(mAddress));
                }
                bitmap = compress(bitmap);
                cache(bitmap,url);
            }
            runOnUIThread(callback, bitmap, null);
            }
        });
    }

    private Bitmap compress(Bitmap bitmap) {
        for (CompressStrategy compress : builder.compressList) {
            bitmap = compress.compress(bitmap, builder.mCompressOptions);
        }
        return bitmap;
    }

    private void cache(Bitmap bitmap,final String url) {
        if (!builder.skipMemoryCache) {
            MemoryCacheStrategy.getInstance().put(url, bitmap);
        }
        builder.mCacheStrategy.put(url, bitmap);
    }

    private Bitmap readFromHttp(){
        Bitmap bitmap = null;
        try {
            URL url = new URL(mAddress);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            bitmap = BitmapFactory.decodeStream(url.openStream());
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private Bitmap readFromFile() {
        return BitmapFactory.decodeFile(mAddress);
    }

    private String getEncodedAddress() {
        if (builder.compressList.size() < 1) {
            return builder.mEncryptor.encode(mAddress);
        }else {
            return builder.mEncryptor.encode(mAddress+builder.mCompressOptions);
        }

    }

    private void runOnUIThread(final LoadImageCallback callback, final Bitmap bitmap, final Exception e) {
        UI_HANDLER.post(new Runnable() {
            @Override
            public void run() {
                if (e == null)
                    callback.success(bitmap);
                else
                    callback.fail(e);
            }
        });
    };

    private static class StackLinkedBlockingDeque<T> extends LinkedBlockingDeque<T> {
        @Override
        public T take() throws InterruptedException {
            return takeLast();
        }

        @Override
        public T poll() {
            return pollLast();
        }
    }

    /**
     * Created by 某只机智 on 2018/4/9.
     * 可重复利用的Builder
     */

    public static class ImageLoaderBuilder {
        private Context mContext;

        private Encryptor mEncryptor;
        private CacheStrategy mCacheStrategy;
        private List<CompressStrategy> compressList = new ArrayList<>();
        private CompressOptions mCompressOptions;

        private Drawable mPlaceHolder;
        private Drawable mErrorHolder;

        private boolean skipMemoryCache;
        private boolean withOriginal;

        private ImageLoaderBuilder(Context context) {
            this.mContext = context;
        }

        public ImageLoaderBuilder encryptor(Encryptor encryptor) {
            mEncryptor = encryptor;
            return this;
        }

        public ImageLoaderBuilder skipMemoryCache(Boolean skipMemoryCache) {
            this.skipMemoryCache = skipMemoryCache;
            return this;
        }

        public ImageLoaderBuilder withOriginal(Boolean withOriginal) {
            this.withOriginal = withOriginal;
            return this;
        }

        public ImageLoaderBuilder cacheStrategy(CacheStrategy cacheStrategy) {
            mCacheStrategy = cacheStrategy;
            if (mCacheStrategy instanceof DiskCacheStrategy && ((DiskCacheStrategy) mCacheStrategy).getCacheDir() == null) {
                ((DiskCacheStrategy) mCacheStrategy).setCacheDir(mContext.getExternalCacheDir().getPath());
            }
            return this;
        }

        public ImageLoaderBuilder addCompress(CompressStrategy compressStrategy) {
            compressList.add(compressStrategy);
            return this;
        }

        public ImageLoaderBuilder compressOptions(CompressOptions options) {
            mCompressOptions = options;
            return this;
        }

        public ImageLoaderBuilder place(Drawable place) {
            mPlaceHolder = place;
            return this;
        }

        public ImageLoaderBuilder place(int id) {
            mPlaceHolder = mContext.getResources().getDrawable(id);
            return this;
        }

        public ImageLoaderBuilder error(Drawable error) {
            mErrorHolder = error;
            return this;
        }

        public ImageLoaderBuilder error(int id) {
            mErrorHolder = mContext.getResources().getDrawable(id);
            return this;
        }

        public ImageLoader build() {
            check();
            return new ImageLoader(this);
        }

        public ImageLoader load(String address) {
            return build().load(address);
        }

        private void check() {
            if (mContext==null)
                throw new LackParamException("Lack of context");
            if (compressList.size() > 0 && mCompressOptions == null)
                compressOptions(new CompressOptions());
            if (mEncryptor==null)
                encryptor(MD5Encryptor.getInstance());
            if (mCacheStrategy==null)
                cacheStrategy(DiskCacheStrategy.getInstance());
        }

    }
}
