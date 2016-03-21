package com.tr.ui.flow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tr.R;
import com.tr.image.FileUtils;
import com.tr.image.ImageLoader;
import com.tr.model.obj.DynamicPicturePath;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.utils.HomeCommonUtils;

/**
 * 图片浏览页面
 * @author leon
 */
public class ImageBrowserActivity extends JBaseFragmentActivity {

	private final String TAG = getClass().getSimpleName();
	
	private ViewPager imageVp; // 滑动控件
	private ImageBrowserAdapter adapter; // 适配器
	
	private ImageLoader imageLoader;
	private List<DynamicPicturePath> listMsg;
	private int index;
	
	@Override
	public void initJabActionBar() {
//		jabGetActionBar().setTitle("图片浏览");

		listMsg = (List<DynamicPicturePath>) getIntent().getSerializableExtra("DynamicPicturePaths");
		index = getIntent().getIntExtra("index", 0);
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), (index+1)+"/"+listMsg.size(), false, null,true, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_browser);
		initVars();
		initControls();
		initParams();
	}
	
	private void initParams(){
		adapter.updateAdapter(listMsg);
		imageVp.setCurrentItem(index);
	}

	private void initVars(){
		// 图片显示配置
	}
	
	private void initControls(){
		adapter = new ImageBrowserAdapter(this);
		imageVp = (ViewPager) findViewById(R.id.imageVp);
		imageVp.setAdapter(adapter);
		imageVp.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				HomeCommonUtils.initLeftCustomActionBar(ImageBrowserActivity.this, getActionBar(), (arg0+1)+"/"+listMsg.size(), false, null,true, true);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}
	
	class ImageBrowserAdapter extends PagerAdapter{

		private Context context;
		private List<View> listView;
		private List<DynamicPicturePath> listMsg;
		
		public ImageBrowserAdapter(Context context){
			this.context = context;
			this.listView = new ArrayList<View>();
			this.listMsg = new ArrayList<DynamicPicturePath>();
		}
		
		public void updateAdapter(List<DynamicPicturePath> listMsg){
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
				imageLoader.load(imgIv, listMsg.get(position).getSourcePath());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return listView.get(position);
		}
	}
}
