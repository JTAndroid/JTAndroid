package com.tr.ui.im;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import uk.co.senab.photoview.PhotoViewAttacher;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.tr.App;
import com.tr.R;
import com.tr.db.ChatLocalFileDBManager;
import com.tr.db.ChatRecordDBManager;
import com.tr.model.obj.IMBaseMessage;
import com.tr.navigate.ENavConsts;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.MainActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.common.EUtil;

/**
 * 图片浏览页面
 * @author leon
 */
public class ChatImageBrowserActivity extends JBaseFragmentActivity {

	private final String TAG = getClass().getSimpleName();
	
	private ViewPager imageVp; // 滑动控件
	private ImageBrowserAdapter adapter; // 适配器
	private DisplayImageOptions displayOptions; // 图片显示配置
	
	private String msgId; // 消息ID
	private String chatId; // 畅聊ID
	private ChatRecordDBManager chatRecordManager; // 消息记录数据库管理器
	private ChatLocalFileDBManager localFileManager; // 本地文件管理器
	
	protected ArrayList<IMBaseMessage> listMessage;//聊天数据
	
	private ImageLoader imageLoader;
	
	@Override
	public void initJabActionBar() {
//		jabGetActionBar().setTitle("图片浏览");
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "图片浏览", false, null,true, true);
//		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_browser);
		initParams();
		initVars();
		initControls();
		initImageLoaderConfiguration();
		doInit();
	}
	
	private void initParams(){
		msgId = getIntent().getStringExtra(ENavConsts.EMessageID);
		chatId = getIntent().getStringExtra(ENavConsts.EMucID);
		
		listMessage = (ArrayList<IMBaseMessage>) getIntent().getSerializableExtra("listMessage");
	}

	private void initVars(){
		// 图片显示配置
		displayOptions = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
		.build();
		// 
		chatRecordManager = new ChatRecordDBManager(this);
		localFileManager = new ChatLocalFileDBManager(this);
		adapter = new ImageBrowserAdapter(this);
	}
	
	private void initControls(){
		imageVp = (ViewPager) findViewById(R.id.imageVp);
		imageVp.setAdapter(adapter);
	}
	
	// 初始化ImageLoader参数配置
	private void initImageLoaderConfiguration() {
		File cacheDir = EUtil.getChatImageCacheDir(this, chatId);
		if (cacheDir != null) {
			ImageLoader.getInstance().destroy();
			ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(
					getApplicationContext())
					.discCache(new UnlimitedDiscCache(cacheDir))
					.discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75, null)
					.defaultDisplayImageOptions(displayOptions)
					.discCacheSize(50 * 1024 * 1024)//
					.discCacheFileCount(100)// 缓存一百张图片
					.writeDebugLogs()//
					.build();
			imageLoader = ImageLoader.getInstance();
			imageLoader.init(configuration);
		}
	}
	
	private void doInit(){
//		Pair<Integer, List<IMBaseMessage>> pair= chatRecordManager.queryImageItem(App.getUserID(), chatId, msgId, 10);
//		if(pair != null){
//			adapter.updateAdapter(pair.second);
//			imageVp.setCurrentItem(pair.first);
//		}
		if(listMessage != null){
			adapter.updateAdapter(listMessage);
			imageVp.setCurrentItem(getIndex(listMessage, msgId));
		}
	}

	class ImageBrowserAdapter extends PagerAdapter{

		private Context context;
		private List<View> listView;
		private List<IMBaseMessage> listMsg;
		
		public ImageBrowserAdapter(Context context){
			this.context = context;
			this.listView = new ArrayList<View>();
			this.listMsg = new ArrayList<IMBaseMessage>();
		}
		
		public void updateAdapter(List<IMBaseMessage> listMsg){
			if(listMsg != null && listMsg.size() > 0){
				this.listMsg = listMsg;
				for(int i = 0; i < this.listMsg.size(); i++){
					listView.add(LayoutInflater.from(context).inflate(R.layout.view_pager_item_image, null));
				}
				notifyDataSetChanged();
			}
		}
		
		@Override
		public int getCount() {
			return listView.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1; 
		}
		
		@Override  
        public void destroyItem(ViewGroup container, int position,  
                Object object) {  
            container.removeView(listView.get(position)); 
            object = null;
        }  

        @Override  
        public int getItemPosition(Object object) {  
            return super.getItemPosition(object);  
        }  
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			try {
			container.addView(listView.get(position));
			ImageView imgIv = (ImageView) listView.get(position).findViewById(R.id.imgIv);
			String originalFilePath = localFileManager.query(chatId, msgId);
			File originalFile = null;
			if(!TextUtils.isEmpty(originalFilePath)){
				originalFile = new File(originalFilePath);
			}
			if(originalFile != null && originalFile.exists()){
				imageLoader.displayImage(Uri.fromFile(originalFile).toString(), imgIv, displayOptions);
			}
			else{
				imageLoader.displayImage(listMsg.get(position).getJtFile().mUrl, imgIv, displayOptions);

			}
			new PhotoViewAttacher(imgIv);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return listView.get(position);
		}
	}
	
	private int getIndex(ArrayList<IMBaseMessage> listMessage, String msgId){
		int index = 0;
		if(listMessage!=null){
			for(IMBaseMessage imbm : listMessage){
				if(imbm.getMessageID().equals(msgId)){
					break;
				}
				index++;
			}
		}
		return index;
	}
}
