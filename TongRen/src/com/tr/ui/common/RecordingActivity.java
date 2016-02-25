package com.tr.ui.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.tr.R;
import com.tr.model.AudioRecorder;
import com.tr.ui.base.JBaseFragmentActivity;
import com.utils.common.EConsts;

/**
 * @ClassName:     RecordingActivity.java
 * @Description:   录音功能模块
 * @Author         leon
 * @Version        v 1.0  
 * @Create         2014-04-03
 * @Update         2014-04-15
 */
public class RecordingActivity extends JBaseFragmentActivity {

	private final String TAG = getClass().getSimpleName();
	
	// 常量
	private final int STATE_BASE = 100;
	private final int STATE_ON = STATE_BASE + 1;
	private final int STATE_OFF = STATE_BASE + 2;
	
	private final int MENU_ITEM_ID_BASE = 200;
	private final int MENU_ITEM_ID_FINISH = MENU_ITEM_ID_BASE +1;

	private final String RECORDING_FILE_SUFFIX = ".amr";
	private final String RECORDING_FILE_PREFIX = "录音文件_";
	private final String CUSTOM_DATE_FORMAT = "yyyy-MM-dd kk:mm:ss";
	private final String TIP_NO_SD_CARD = "没有存储卡，本功能无法使用";
	private final String TIP_FAILED = "录音失败，请重试";
	private final String TIP_READY = "点击按钮开始录音";
	
	// 控件
	private TextView statusTv; // 状态
	private ImageView switchIv; // 切换按钮
	private ListView fileLv; // 录音文件列表
	private MenuItem finishItem; // 
	
	// 适配器
	private int mStatus; // 当前状态
	private RecordingFileAdpater mAdapter; // 列表适配器
	private AudioRecorder mRecorder; // 录音器
	private List<RecordingFile> mListRecFile; // 录音文件
	private RecordingFile mCurRecFile; // 当前文件
	private String mFilePath;  // 文件存储路径
	private Timer mTimer; // 计时任务
	
	@Override
	public void initJabActionBar() {
		ActionBar actionBar = jabGetActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common_recording);
		initVars();
		initControls();
	}
	
	private void initVars() {
		
		// 状态
		mStatus = STATE_OFF; 
		// 初始化文件存储路径
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			File storageDir = getExternalFilesDir(Environment.DIRECTORY_MUSIC);
			if(!storageDir.exists()){
				storageDir.mkdirs();
			}
			mFilePath = storageDir.getAbsolutePath();
		}
		else{
			mFilePath = "";
		}
		// 文件列表
		mListRecFile = new ArrayList<RecordingFile>();
		// 录音器
		mRecorder = new AudioRecorder();
		// 适配器
		mAdapter= new RecordingFileAdpater();
		// 计时器
		mTimer = new Timer();
	}
	
	// 初始化控件
	private void initControls(){
		
		// 状态
		statusTv = (TextView) findViewById(R.id.statusTv);
		// 开关按钮
		switchIv = (ImageView) findViewById(R.id.switchIv);
		switchIv.setOnClickListener(mClickListener);
		// 录音文件列表
		fileLv = (ListView) findViewById(R.id.fileLv);
		fileLv.setAdapter(mAdapter);
	}
	
	// 用户离开页面时中断录音
	@Override
	public void onPause(){
		super.onPause();
		stopRecording();
	}
	
	// 开始录音
	private void startRecording(){
		
		// 页面状态
		mStatus = STATE_ON;
		// 改变按钮状态
		statusTv.setText("00:00:00.00");
		// 图标
		switchIv.setImageResource(R.drawable.ic_rec_on);
		// 初始化录音文件信息
		RecordingFile tempRecFile = new RecordingFile();
		tempRecFile.mCreateTime = System.currentTimeMillis();
		tempRecFile.mName = RECORDING_FILE_PREFIX + (mListRecFile.size() + 1)
				+ RECORDING_FILE_SUFFIX;
		tempRecFile.mIsChecked = false;
		File file = new File(mFilePath + File.separator + tempRecFile.mName);
		if(file.exists()){
			file.delete();
		}
		mCurRecFile = tempRecFile;
		// 开始录音
		mRecorder.start(mFilePath, mCurRecFile.mName);
		// 开始计时
		if(mTimer !=null){
			mTimer.cancel();
			mTimer =null;
		}
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mCurRecFile.mDuration += 1;
				mHandler.sendEmptyMessage(0);
			}
		}, 10, 10);
	}
	
	// 停止录音
	private void stopRecording(){
		
		// 页面状态
		mStatus = STATE_OFF;
		// 改变按钮状态
		statusTv.setText(TIP_READY);
		// 图标
		switchIv.setImageResource(R.drawable.ic_rec_off);
		// 停止录音
		mRecorder.stop();
		// 停止计时器
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
	}
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){
		
		@Override
		public void handleMessage(Message msg){
			
			if (mStatus == STATE_ON) {
				statusTv.setText(toDoubleDigitFormat(mCurRecFile.mDuration / 100 / 3600)
						+ ":"
						+ toDoubleDigitFormat(mCurRecFile.mDuration / 100 / 60)
						+ ":"
						+ toDoubleDigitFormat(mCurRecFile.mDuration / 100)
						+ "."
						+ toDoubleDigitFormat(mCurRecFile.mDuration % 100));
			}
		}
	};
	
	// 转换为双数字模式
	private String toDoubleDigitFormat(long number) {
		String result = "";
		if (number < 10) {
			result = "0" + number;
		}
		else{
			result = number + "";
		}
		return result;
	}
	
	// 点击事件监听器
	private OnClickListener mClickListener=new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v == switchIv){
				if(mStatus == STATE_OFF){ 
					if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
							|| mFilePath.length() <= 0){
						Toast.makeText(RecordingActivity.this, TIP_NO_SD_CARD,
								Toast.LENGTH_SHORT).show();
						return;
					}
					// 开始录音
					startRecording();
				}
				else{ 
					// 停止录音
					stopRecording();
					// 将文件添加到列表
					File tempRecFile = new File(mFilePath + File.separator
							+ mCurRecFile.mName);
					if(tempRecFile.exists()){ 
						// 始终将最新的音频文件置顶
						mListRecFile.add(0, mCurRecFile);
						// 更新列表
						mAdapter.notifyDataSetChanged();
					}
					else{ 
						// 文件生成失败
						Toast.makeText(RecordingActivity.this, TIP_FAILED,
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		}
	};
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: // 返回
			finish();
			break;
		case MENU_ITEM_ID_FINISH: // 确定
			if (mStatus == STATE_ON) {
				stopRecording();
			}
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			ArrayList<String> filePaths = new ArrayList<String>();
			for (int i = 0; i < mListRecFile.size(); i++) {
				if (mListRecFile.get(i).mIsChecked) {
					filePaths.add(mFilePath + File.separator
							+ mListRecFile.get(i).mName);
				}
			}
			bundle.putStringArrayList(EConsts.Key.RECORDING, filePaths);
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		finishItem = menu.add(0, MENU_ITEM_ID_FINISH, 0, "确定");
		finishItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}
	
	// 录音文件信息类（只是为了列表显示用）
	private class RecordingFile{
		
		public long mCreateTime; // 创建时间
		public String mName;  // 文件名
		public boolean mIsChecked; // 是否被选中
		public long mDuration; // 时长
	}

	// 录音文件适配器
	private class RecordingFileAdpater extends BaseAdapter{
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mListRecFile.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mListRecFile.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if(convertView==null){
				convertView = getLayoutInflater().inflate(R.layout.list_item_recording_file, parent, false);
				holder=new ViewHolder();
				holder.nameTv=(TextView) convertView.findViewById(R.id.nameTv);
				holder.statusCb =(CheckBox) convertView.findViewById(R.id.statusCb);
				holder.createTimeTv =(TextView) convertView.findViewById(R.id.createTimeTv);
				convertView.setTag(holder);
			}
			else{
				holder= (ViewHolder) convertView.getTag();
			}
			initControls(holder, position);
			return convertView;
		}
		
		// 初始化控件的数据
		private void initControls(ViewHolder holder,final int position){
			holder.nameTv.setText(mListRecFile.get(position).mName);
			holder.createTimeTv.setText(DateFormat.format(CUSTOM_DATE_FORMAT, mListRecFile.get(position).mCreateTime)+"");
			holder.statusCb.setChecked(mListRecFile.get(position).mIsChecked);
			holder.statusCb.setOnCheckedChangeListener(new OnCheckedChangeListener(){
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					// TODO Auto-generated method stub
					mListRecFile.get(position).mIsChecked = isChecked;
					int count = 0;
					for (int i = 0; i < mListRecFile.size(); i++) {
						if (mListRecFile.get(i).mIsChecked) {
							count++;
						}
					}
					if(count == 0){
						finishItem.setTitle(String.format("确定", count));
					}
					else{
						finishItem.setTitle(String.format("确定(%d)", count));
					}
				}
			});
		}
		
		private class ViewHolder {
			TextView nameTv; // 文件名
			CheckBox statusCb; // 状态
			TextView createTimeTv; // 创建时间
		}
	}
}
