package com.cxcl.property.customer;

import android.graphics.Bitmap;

import com.baidu.mapapi.SDKInitializer;
import com.example.tjwx_person.http.URLConstant;
import com.example.tjwx_person.utils.CrashHandler;
import com.example.tjwx_person.utils.Theme;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

public class Application extends android.app.Application {

	private static DisplayImageOptions mOptions;
	public String token;

	// 分享

	private static Application instance;

	public static Application getInstance() {
		return instance;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	private boolean isDownload;



	public boolean isDownload() {
		return isDownload;
	}

	public void setDownload(boolean isDownload) {
		this.isDownload = isDownload;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// 初始化百度地图类服务
		SDKInitializer.initialize(getApplicationContext());
		// 初始化界面适配等组件
		Theme.init(this);
		// 初始化 消息
		URLConstant.initMessage();
		// 开启意外崩溃 处理（日志记录）
		initImageLoader();
		CrashHandler crash = CrashHandler.getInstance();
		crash.init(getApplicationContext());
	}

	private void initImageLoader() {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this)
				// .memoryCacheExtraOptions(480, 800)
				// // default = device screen dimensions 内存缓存文件的最大长宽
				// .diskCacheExtraOptions(480, 800, null)
				// 本地缓存的详细信息(缓存的最大长宽)，最好不要设置这个

				.threadPoolSize(3)
				// default 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2)
				// default 设置当前线程的优先级
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				// default
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024)) // 可以通过自己的内存缓存实现
				.memoryCacheSize(2 * 1024 * 1024) // 内存缓存的最大值
				.memoryCacheSizePercentage(13) // default

				.diskCacheSize(50 * 1024 * 1024) // 50 Mb sd卡(本地)缓存的最大值
				.diskCacheFileCount(100) // 可以缓存的文件数量
				// default为使用HASHCODE对UIL进行加密命名， 还可以用MD5(new
				// Md5FileNameGenerator())加密
				.diskCacheFileNameGenerator(new HashCodeFileNameGenerator())

				.defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
				.writeDebugLogs() // 打印debug log
				.build(); // 开始构建
		ImageLoader.getInstance().init(config);

		mOptions = new DisplayImageOptions.Builder()

		.imageScaleType(ImageScaleType.EXACTLY).resetViewBeforeLoading(true)
				.cacheInMemory(true).cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
				.displayer(new SimpleBitmapDisplayer())
				.considerExifParams(true).build();
	}

	public static DisplayImageOptions getImgOptions() {
		return mOptions;
	}
}
