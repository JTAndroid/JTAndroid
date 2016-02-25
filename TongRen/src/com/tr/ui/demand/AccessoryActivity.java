package com.tr.ui.demand;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.R;
import com.tr.model.obj.JTFile;
import com.tr.navigate.ENavConsts;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.demand.fragment.AccessoryFragment;
import com.tr.ui.demand.fragment.AccessoryFragment.TypeDocument;
import com.tr.ui.demand.fragment.MyFragmentPagerAdapter;
import com.tr.ui.home.utils.HomeCommonUtils;

/**
 * @ClassName: AccessoryActivity.java
 * @author zcs
 * @Date 2015年3月18日 下午5:40:11
 * @Description: 添加附件
 */
public class AccessoryActivity extends JBaseFragmentActivity {
	private ViewPager mPager;
	private ArrayList<Fragment> fragmentList;
	private TextView view1;
	private TextView view2;
	private TextView view3;
	private View barText;
	public int currIndex;
	public static Set<String> fileAllList;
	public static HashSet<String> fileSelectList;
	public static long countSize;
	private Fragment allFragment1;
	private Fragment allFragment2;
	private Fragment allFragment3;
	private TextView fileSizeTv;
	
	private String fuJianTmp = "";
	private String fuJian = "doc|docx|ppt|pptx|pdf|txt|xls|xlsx|rar|zip|7z";
	private String zongHefuJian = "doc|docx|ppt|pptx|pdf|txt|xls|xlsx|rar|zip|7z|jpg|png|bmp|jpeg|gif|avi|mpeg|mpg|qt|ram|viv|avi|mp4|wmv|rmvb|mkv|vob|3gp|doc|wps|word|docx|aif|svx|snd|mid|voc|wav|mp3";
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demand_accessory);
		//读取系统文件
		fileAllList = new HashSet<String>();
		fileSelectList = new HashSet<String>();
		boolean isFromUpload = getIntent().getBooleanExtra(ENavConsts.ISFROMUPLOAD, false);
		if (isFromUpload) {
			fuJianTmp = zongHefuJian;
		} else {
			fuJianTmp = fuJian;
		}
		ArrayList<JTFile> fileListSD = (ArrayList<JTFile>) getIntent().getSerializableExtra(ENavConsts.DEMAND_INTENT_SELECTED_ACCESSORY);
		if (isFromUpload) {
			fileListSD.clear();
		}
		for (int i = 0; fileListSD!=null&&i < fileListSD.size(); i++) {
			fileSelectList.add(fileListSD.get(i).mLocalFilePath);
		}
		fileSizeTv = (TextView) findViewById(R.id.fileSizeTv);
		countSize=0;
		
		SharedPreferences upLoadParmar = getSharedPreferences("upLoad",Activity.MODE_PRIVATE);
		if (!isFromUpload) {
			if (upLoadParmar.getBoolean("isComp", false) && HomeCommonUtils.fileAllList.size() != 0) {
				fileAllList = HomeCommonUtils.fileAllList;
				initData();
			} else {
				readFile();
			}
		} else {
			if (upLoadParmar.getBoolean("isCompZonghe", false) && HomeCommonUtils.fileAllListZonghe.size() != 0) {
				fileAllList = HomeCommonUtils.fileAllListZonghe;
				initData();
			} else {
				readFile();
			}
		}
		
		
	}
	private void initData(){
		dismissLoadingDialog();
		InitTextView();
		InitTextBar();
		InitViewPager();//显示数据
	}

	private void readFile() {
		//Environment.getExternalStorageState();
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			showLoadingDialog();
			new AsyncTask<Void, Void, Void>(){

				@Override
				protected Void doInBackground(Void... params) {
					readerAllFiles(Environment.getExternalStorageDirectory()); 
					return null;
				}
				
				@Override
				protected void onPostExecute(Void result) {
					initData();
					
					super.onPostExecute(result);
				}
				
			}.execute();
		} else {
			Toast.makeText(getApplicationContext(), "读取文件失败", 0).show();
			//uploadFileDir = this.getCacheDir();
		}
		
	}
	private void readerAllFiles(File dir)  {
		File[] fs = dir.listFiles();
		if(fs!=null){
			for (int i = 0; i < fs.length; i++) {
				String path = fs[i].getAbsolutePath();
				if(!path.startsWith(".")&&path.matches("^.*?\\.(" + fuJianTmp + ")$")&&fs[i].length()>0){//doc|docx|ppt|pptx|pdf|txt|xls|xlsx
					fileAllList.add(path);
					//System.out.println(path);
				}
				if (!fs[i].getAbsolutePath().startsWith(".")&&!fs[i].isHidden()&&fs[i].isDirectory()) {
					try {
						readerAllFiles(fs[i]);
						//System.out.println();
					} catch (Exception e) {
						Toast.makeText(getApplicationContext(), "读取文件失败", 0).show();
						e.printStackTrace();
					}
				}
			}
		}
	}

	/*
	 * 初始化标签名
	 */
	public void InitTextView() {
		view1 = (TextView) findViewById(R.id.accessoryAllTv);
		view2 = (TextView) findViewById(R.id.documentTv);
		view3 = (TextView) findViewById(R.id.compressFileTv);
		view1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPager.setCurrentItem(0);

			}
		});
		view2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPager.setCurrentItem(1);

			}
		});
		view3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPager.setCurrentItem(2);

			}
		});
	}

	/*
	 * 初始化图片的位移像素
	 */
	public void InitTextBar() {
		barText = findViewById(R.id.cursor);
		Display display = getWindow().getWindowManager().getDefaultDisplay();
		// 得到显示屏宽度
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		// 1/3屏幕宽度
		int tabLineLength = metrics.widthPixels / 3;
		LayoutParams lp = (LayoutParams) barText.getLayoutParams();
		lp.width = tabLineLength;
		barText.setLayoutParams(lp);

	}

	/*
	 * 初始化ViewPager
	 */
	public void InitViewPager() {
		mPager = (ViewPager) findViewById(R.id.contentVp);
		// mPager.requestDisallowInterceptTouchEvent(false);
		fragmentList = new ArrayList<Fragment>();
		allFragment1 = new AccessoryFragment(AccessoryActivity.this,TypeDocument.all,fileSizeTv);
		allFragment2 = new AccessoryFragment(AccessoryActivity.this,TypeDocument.document,fileSizeTv);
		allFragment3 = new AccessoryFragment(AccessoryActivity.this,TypeDocument.accessory,fileSizeTv);
		fragmentList.add(allFragment1);
		fragmentList.add(allFragment2);
		fragmentList.add(allFragment3);
		
		// 给ViewPager设置适配器
		mPager.setAdapter(new MyFragmentPagerAdapter(
				getSupportFragmentManager(), fragmentList));
		mPager.setCurrentItem(0);// 设置当前显示标签页为第一页
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());// 页面变化时的监听器
	}
	/**
	 * 页面变化时的监听器
	 * @author Administrator
	 *
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// 取得该控件的实例
			LinearLayout.LayoutParams ll = (android.widget.LinearLayout.LayoutParams) barText
					.getLayoutParams();

			if (currIndex == arg0) {
				ll.leftMargin = (int) (currIndex * barText.getWidth() + arg1
						* barText.getWidth());
			} else if (currIndex > arg0) {
				ll.leftMargin = (int) (currIndex * barText.getWidth() - (1 - arg1)
						* barText.getWidth());
			}
			barText.setLayoutParams(ll);
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageSelected(int arg0) {
			currIndex = arg0;
			int i = currIndex + 1;
			if(arg0==0){
				((AccessoryFragment) allFragment1).refreshData();
			}else if(arg0==1){
				((AccessoryFragment) allFragment2).refreshData();
				
			}else{
				((AccessoryFragment) allFragment3).refreshData();
			}
			/*Toast.makeText(NeedDetailsActivity.this, "您选择了第" + i + "个页卡",
					Toast.LENGTH_SHORT).show();*/
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.demand_create_label_ok, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.save_ok:
			Intent intent = new Intent();
			intent.putExtra("select_list", fileSelectList);
			setResult(Activity.RESULT_OK, intent);
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void initJabActionBar() {
		ActionBar mActionBar = jabGetActionBar();
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(true);
		View mCustomView = getLayoutInflater().inflate(
				R.layout.demand_actionbar_title, null);
		mActionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView
				.getLayoutParams();
		mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK
				| Gravity.CENTER_HORIZONTAL;
		mActionBar.setCustomView(mCustomView, mP);
		mActionBar.setTitle(" ");
		TextView myTitle = (TextView) mCustomView.findViewById(R.id.titleTv);
		myTitle.setText("附件");
		mCustomView.findViewById(R.id.titleIv).setVisibility(View.GONE);
		

	}

}
