package com.tr.ui.conference.im;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import uk.co.senab.photoview.PhotoViewAttacher;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.tr.App;
import com.tr.R;
import com.tr.db.ChatLocalFileDBManager;
import com.tr.db.MeetingRecordDBManager;
import com.tr.model.obj.IMBaseMessage;
import com.tr.model.obj.MeetingMessage;
import com.tr.navigate.ENavConsts;
import com.tr.ui.base.JBaseActivity;

/**
 * 会议畅聊图片文件浏览
 * @author leon
 */
public class MChatImageBrowserActivity extends JBaseActivity implements OnClickListener{

	private final String TAG = getClass().getSimpleName();
	
	// 控件
	private LinearLayout backLl;
	private TextView titleTv;
	private ViewPager imageVp;
	
	// 变量
	private List<MeetingMessage> listMsg; // 包含图片的聊天记录
	private ImageBrowserAdapter adapter; // 图片适配器
	private DisplayImageOptions displayOptions;
	private int imgIndex;
	private long meetingId;
	private long topicId;
	private String messageId;
	private int span; // 前后图片数
	private MeetingRecordDBManager dbManager; // 会议畅聊记录
	private ChatLocalFileDBManager localFileDBManager;
	
	@Override
	public void initJabActionBar() {
		jabGetActionBar().hide();
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hy_activity_image_browser);
		initVars();
		initControls();
	}

	private void initVars(){
		
		imgIndex = 0;
		listMsg = new ArrayList<MeetingMessage>();
		span = 5;
		
		// 图片显示配置
		displayOptions = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
		.build();

		Intent intent = getIntent();
		messageId = intent.getStringExtra(ENavConsts.EMessageID);
		meetingId = intent.getLongExtra(ENavConsts.EMeetingId, 0);
		topicId = intent.getLongExtra(ENavConsts.ETopicId, 0);
		
		// 本地文件数据库
		localFileDBManager = new ChatLocalFileDBManager(this);
		// 聊天记录数据库
		dbManager = new MeetingRecordDBManager(this);
		Pair<Integer, List<MeetingMessage>> pair = dbManager.queryImageItem(App.getUserID(), meetingId + "", topicId + "", messageId, span);
		if(pair != null){
			listMsg = pair.second;
			imgIndex = pair.first;
		}
		else{
			finish();
		}
		// 适配器
		adapter = new ImageBrowserAdapter(this);
	}
	
	// 初始化控件
	private void initControls(){
		
		backLl = (LinearLayout) findViewById(R.id.hy_layoutTitle_backBtn);
		backLl.setOnClickListener(this);
		titleTv = (TextView) findViewById(R.id.hy_layoutTitle_title);
		titleTv.setText("图片预览");
		imageVp = (ViewPager) findViewById(R.id.imageVp);
		imageVp.setAdapter(adapter);
		imageVp.setCurrentItem(imgIndex);
	}

	class ImageBrowserAdapter extends PagerAdapter{

		private List<View> listView = new ArrayList<View>();
		
		public ImageBrowserAdapter(Context context){
			for(int i = 0; i < listMsg.size(); i++){
				View view = LayoutInflater.from(context).inflate(R.layout.view_pager_item_image, null);
				listView.add(view);
			}
		}
		
		@Override
		public int getCount() {
			return listMsg.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1; 
		}
		
		@Override  
        public void destroyItem(ViewGroup container, int position,  
                Object object) {  
			object = null;
            container.removeView(listView.get(position)); 
        }  

        @Override  
        public int getItemPosition(Object object) {  
            return super.getItemPosition(object);  
        }  
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(listView.get(position));
			ImageView imgIv = (ImageView) listView.get(position).findViewById(R.id.imgIv);
			MeetingMessage msg = listMsg.get(position);
			if(msg.getSenderType() == IMBaseMessage.MSG_MY_SEND){ // 我发送的
				String originalPath = localFileDBManager.query(App.getUserID(), msg.getMessageID());
				if(!TextUtils.isEmpty(originalPath)){
					File originalFile = new File(originalPath);
					if(originalFile.exists()){
						ImageLoader.getInstance().displayImage(Uri.fromFile(originalFile).toString(), imgIv, displayOptions);
					}
					else{
						Log.d(TAG, "原文件不存在");
					}
				}
				else{
					ImageLoader.getInstance().displayImage(msg.getJtFile().mUrl, imgIv, displayOptions);
				}
			}
			else if(msg.getSenderType() == IMBaseMessage.MSG_OTHER_SEND){ // 其他人发送的
				ImageLoader.getInstance().displayImage(msg.getJtFile().mUrl, imgIv, displayOptions);
			}
			new PhotoViewAttacher(imgIv);
			return listView.get(position);
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.hy_layoutTitle_backBtn: // 返回
			finish();
			break;
		}
	}
}
