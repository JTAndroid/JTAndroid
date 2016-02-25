package com.utils.image;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tr.R;

public class LoadImage {
	
	public final static int TYPE_IMAGE_null=0;
	public final static int TYPE_IMAGE_ICON=1;
	public final static int TYPE_IMAGE_connection80=2;
	public final static int TYPE_IMAGE_connection60=4;
	public final static int TYPE_IMAGE_companyfriend80=3;
	public static DisplayImageOptions mDefaultHead = initDisplayOptions(true, R.drawable.ic_default_avatar,R.drawable.ic_default_avatar, R.drawable.ic_default_avatar);
	public static DisplayImageOptions mDefaultFlowHead = initDisplayOptions(true, R.drawable.ic_default_avatar,R.drawable.ic_default_avatar, R.drawable.ic_default_avatar);
	public static DisplayImageOptions mOrganizationDefaultHead = initDisplayOptions(true, R.drawable.org_default_orgnization,R.drawable.org_default_orgnization, R.drawable.org_default_orgnization);
	public static DisplayImageOptions mHomeDefaultHead = initDisplayOptions(true, R.drawable.hy_chat_right_pic,R.drawable.hy_chat_right_pic, R.drawable.hy_chat_right_pic);
	public static DisplayImageOptions mHyDefaultHead = initDisplayOptions(true, R.drawable.chat_im_img_user,R.drawable.chat_im_img_user, R.drawable.chat_im_img_user);
	public static DisplayImageOptions mKnowledgeDefaultImage = initDisplayOptions(true, R.drawable.hy_chat_share_img,R.drawable.hy_chat_share_img, R.drawable.hy_chat_share_img); 
	public static DisplayImageOptions mMeetingDefaultImage = initDisplayOptions(true, R.drawable.meeting_logo_a,R.drawable.meeting_logo_a, R.drawable.meeting_logo_a);
	public static ImageLoader initImageLoader(Context context){

		// 获取本地缓存的目录，该目录在SDCard的根目录下
         File cacheDir = StorageUtils.getOwnCacheDirectory(context, "jt/image/Cache");

         ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
        		 context);
         // 设置线程数量
         builder.threadPoolSize(3);
         // 设定线程等级比普通低一点
         builder.threadPriority(Thread.NORM_PRIORITY - 1);
         // 设定内存缓存为弱缓存
         builder.memoryCache(new WeakMemoryCache());
         // 设定内存图片缓存大小限制，不设置默认为屏幕的宽高
         builder.memoryCacheExtraOptions(480, 800);
         // 缓存到内存的最大数据
         builder.memoryCacheSize(8 * 1024 * 1024);
         // 缓存到文件的最大数据
         builder.discCacheSize(50 * 1024 * 1024);
         // 文件数量
         builder.discCacheFileCount(1000);
         // 设定只保存同一尺寸的图片在内存
         builder.denyCacheImageMultipleSizesInMemory();
         // 设定缓存的SDcard目录，UnlimitDiscCache速度最快
         builder.discCache(new UnlimitedDiscCache(cacheDir));
         // 设定缓存到SDCard目录的文件命名
         builder.discCacheFileNameGenerator(new HashCodeFileNameGenerator());
         // 设定网络连接超时 timeout: 10s 读取网络连接超时read timeout: 60s
         builder.imageDownloader(new BaseImageDownloader(context, 10000, 60000));
         // 设置ImageLoader的配置参数
         //builder.defaultDisplayImageOptions(getDisplayOptions(true));

         // 初始化ImageLoader
         ImageLoader imageLoader = ImageLoader.getInstance();
         imageLoader.init(builder.build());
         
         iconDisplayImageOptions=initDisplayOptions(true,R.drawable.face0,R.drawable.face0, R.drawable.face0);
         nullDisplayImageOptions=initDisplayOptions(true,R.drawable.face0,R.drawable.face0, R.drawable.face0);
         TYPE_IMAGE_connection80obj=initDisplayOptions(true,R.drawable.ic_default_avatar,R.drawable.ic_default_avatar, 
        		 R.drawable.ic_default_avatar);
         TYPE_IMAGE_connection60obj=initDisplayOptions(true,R.drawable.ic_share_people,R.drawable.ic_share_people, 
        		 R.drawable.ic_share_people);
         TYPE_IMAGE_companyfriend80Obj=initDisplayOptions(true,R.drawable.companyfriend,R.drawable.companyfriend, 
        		 R.drawable.companyfriend);
         return imageLoader;
         
	}
	
	
	
	public static DisplayImageOptions iconDisplayImageOptions=null;
	public static DisplayImageOptions nullDisplayImageOptions=null;
	public static DisplayImageOptions TYPE_IMAGE_connection80obj=null;
	public static DisplayImageOptions TYPE_IMAGE_connection60obj=null;
	public static DisplayImageOptions TYPE_IMAGE_companyfriend80Obj=null;
	
	public static DisplayImageOptions getDisplayOptions() {
		return getDisplayOptions(TYPE_IMAGE_null);
	}
	
	public static DisplayImageOptions getDisplayOptions(int type) {
		if(type==TYPE_IMAGE_ICON){
			return iconDisplayImageOptions;
		}else if(type==TYPE_IMAGE_null){
			return nullDisplayImageOptions;
		}else if(type==TYPE_IMAGE_connection80){
			return TYPE_IMAGE_connection80obj;
		}else if(type==TYPE_IMAGE_companyfriend80){
			return TYPE_IMAGE_companyfriend80Obj;
		}else if(type==TYPE_IMAGE_connection60){
			return TYPE_IMAGE_connection60obj;
		}
		else{
			return null;
		}
	}
	
	
	
	public static DisplayImageOptions initDisplayOptions(boolean isShowDefault,int loadingRes,int nullRes,int failRes) {
        DisplayImageOptions.Builder displayImageOptionsBuilder = new DisplayImageOptions.Builder();
        // 设置图片缩放方式
        // EXACTLY: 图像将完全按比例缩小的目标大小
        // EXACTLY_STRETCHED: 图片会缩放到目标大小
        // IN_SAMPLE_INT: 图像将被二次采样的整数倍
        // IN_SAMPLE_POWER_OF_2: 图片将降低2倍，直到下一减少步骤，使图像更小的目标大小
        // NONE: 图片不会调整
        displayImageOptionsBuilder.imageScaleType(ImageScaleType.EXACTLY);
        if (isShowDefault) {
                // 默认显示的图片
                displayImageOptionsBuilder.showImageOnLoading(loadingRes);
                // 地址为空的默认显示图片
                displayImageOptionsBuilder
                                .showImageForEmptyUri(nullRes);
                // 加载失败的显示图片
                displayImageOptionsBuilder.showImageOnFail(failRes);
        }
        // 开启内存缓存
        displayImageOptionsBuilder.cacheInMemory(true);
        // 开启SDCard缓存
        displayImageOptionsBuilder.cacheOnDisc(true);
        // 设置图片的编码格式为RGB_565，此格式比ARGB_8888快
        displayImageOptionsBuilder.bitmapConfig(Bitmap.Config.RGB_565);
        displayImageOptionsBuilder.considerExifParams(false);
        // 设置圆角，不需要请删除
        // displayImageOptionsBuilder.displayer(new RoundedBitmapDisplayer(5));

        return displayImageOptionsBuilder.build();
	}
	
	public static void displayImage(Context context ,String uri, ImageView imageView, DisplayImageOptions options) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration  
                .Builder(context)  
                .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽  
                .threadPoolSize(3)//线程池内加载的数量  
                .threadPriority(Thread.NORM_PRIORITY - 2)  
                .denyCacheImageMultipleSizesInMemory()  
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现  
                .memoryCacheSize(2 * 1024 * 1024)    
                .discCacheSize(50 * 1024 * 1024)    
                .discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密  
                .tasksProcessingOrder(QueueProcessingType.LIFO)  
                .discCacheFileCount(100) //缓存的文件数量  
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())  
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间  
                .writeDebugLogs() // Remove for release app  
                .build();//开始构建 
        
        ImageLoader.getInstance().init(config);
        ImageLoader.getInstance().displayImage(uri, imageView,options);
	}
}
