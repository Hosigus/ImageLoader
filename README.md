# ImageLoader

一个图片加载库，支持异步加载图片，缓存，压缩等功能

## 使用

- 直接使用

  + 加载到ImageView上
    ```Java
        ImageLoader.with(context)
            .load(address)//网络or本地文件地址
            .into(imageView);//into(imageView,false);可关闭根据IamgeView放缩图片
    ```

  + 加载图片，自定义处理
    ```Java
    ImageLoader.with(context)
    .load(address)//网络or本地文件地址
    .getImage(new LoadImageCallback() {
    	@Override
    	public void success(Bitmap bitmap) {

    	}

    	@Override
    	public void fail(Exception e) {

    	}
    });
    ```

  + 加载图片，仅缓存
    ```Java
    ImageLoader.with(context)
    	.load(address)//网络or本地文件地址
    	.intoCache();//intoCache(label)可设置标签
    ```

- 设置缓存、压缩、占位图
  ```Java
  ImageLoader.with(context)
  	.cacheStrategy(DiskCacheStrategy.getInstance())//设置缓存方案
  	.addCompress(ScaleCompress.getInstance())//可添加多种压缩策略，按添加顺序执行
  	.compressOptions(options)//设置压缩所需的参数
  	.error(R.drawable.ic_error_black_24dp)  //设置加载失败时的占位图
  	.place(R.drawable.ic_android_black_24dp);  //设置加载未完成前的占位图
  ```

- 提供的缓存策略、压缩策略以及文件名加密策略

  > 缓存策略： ```DiskCacheStrategy```  ```MemoryCacheStrategy```  ```NoCacheStrategy```
  >
  > 压缩策略： ```InSampleSizeCompress```  ```QualityCompress```  ```ScaleCompress```
  >
  > 文件名加密策略： ```MD5Encryptor```   ```SHAEncryptor```

- 加载大量图片时建议使用：

  1.相同的参数构建成一个builder对象
  ```Java
  ImageLoaderBuilder builder = ImageLoader.with(context)
  	.cacheStrategy(DiskCacheStrategy.getInstance())//设置缓存方案
  	.addCompress(ScaleCompress.getInstance())//可添加多种压缩策略，按添加顺序执行
  	.compressOptions(options)//设置压缩所需的参数
  	.error(R.drawable.ic_error_black_24dp)  //设置加载失败时的占位图
  	.place(R.drawable.ic_android_black_24dp);  //设置加载未完成前的占位图
  ```
  2.用builder对象加载图片
  ```Java
  builder.load(address).into(imageView);
  ```



## 自定义

本图片加载库提供接口，支持自定义缓存策略、压缩策略、文件名加密策略。实现对应的接口即可

> 缓存策略： ```CacheStrategy```
>
> 压缩策略： ```CompressStrategy``` 
>
> 文件名加密策略： ```Encryptor``` 

同时你可以继承 ```CompressOptions``` 来你的压缩策略提供更多参数



## 关于默认

   	1. 默认使用内存缓存+磁盘缓存
    2. 默认使用MD5加密文件名
    3. 默认不缓存原图
    4. 默认使用 ```ScaleCompress``` 适应ImageView压缩图片



## 开发者
  Hosigus [hosigus9733@gmail.com](mailto:hosigus9733@gmail.com)
