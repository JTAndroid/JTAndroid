package com.tr.ui.common;

import java.io.File;
import java.util.Map;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.R;
import com.tr.model.obj.JTFile;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.utils.common.EConsts;
import com.utils.common.EUtil;
import com.utils.common.OpenFiles;
import com.utils.file.downloader.DownloadProgressListener;
import com.utils.file.downloader.FileDownloader;
import com.utils.file.downloader.FileService;
import com.utils.time.Util;

/**
 * 文件预览
 * @author leon
 */
public class FilePreviewActivity extends JBaseActivity {

	private final String TAG = getClass().getSimpleName();
	
	// 控件
	private ImageView typeIv;
	private TextView nameTv;
	private TextView sizeTv;
	private TextView controlTv;
	private TextView saveTv;
	private LinearLayout statusLl;
	private TextView progressTv;
	private ImageView cancelIv;
	private ProgressBar progressPb;
	private boolean isShowSaveButton;
	
	private FileDownloader loader;
	
	private JTFile mJTFile;
	
	private boolean cancelDownload = false;
	
	/**
     * 当Handler被创建会关联到创建它的当前线程的消息队列，该类用于往消息队列发送消息
     * 消息队列中的消息由当前线程内部进行处理
     */
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {            
            switch (msg.what) {
            case 1:  
            	if(!cancelDownload){
                	progressPb.setProgress(msg.getData().getInt("size"));
                    float num = (float)progressPb.getProgress()/(float)progressPb.getMax();
                    int result = (int)(num*100);
                    controlTv.setVisibility(View.GONE);
                    statusLl.setVisibility(View.VISIBLE);
                    progressTv.setText(result+ "%");
                    
                    if(result == 100){
                        controlTv.setText("打开");
        				controlTv.setVisibility(View.VISIBLE);
        				statusLl.setVisibility(View.GONE);
        				progressTv.setText("0%");
        				progressPb.setProgress(0);
                    }
            	}
                break;
            case -1:
                Toast.makeText(FilePreviewActivity.this, "下载失败", 1).show();
				controlTv.setText("恢复下载");
				controlTv.setVisibility(View.VISIBLE);
				statusLl.setVisibility(View.GONE);
				progressTv.setText("0%");
				progressPb.setProgress(0);
                break;
            }
        }
    };
    
	@Override
	public void initJabActionBar() {
		jabGetActionBar().setTitle("文件详情");
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_preview);
		initVars();
		initControls();
		doInit();
		initTypeIv();
	}
	
	private void initTypeIv()
	{
		String suffixName = mJTFile.getmSuffixName();
		int fileSourceId = 0;// 资源文件ID
		if (EUtil.PIC_FILE_STR.contains(suffixName)) {// 图片
			fileSourceId = R.drawable.picture_fang;
		} else if (EUtil.VIDEO_FILE_STR.contains(suffixName)) {// 视频
			fileSourceId = R.drawable.video_fang;
		} else if ("PDF".contains(suffixName)) {//pdf文件
			fileSourceId = R.drawable.pdf_fang;
		} else if ("PPT".contains(suffixName)
				|| "pptx".contains(suffixName)) {//ppt文件
			fileSourceId = R.drawable.ppt_fang;
		} else if ("XLS".contains(suffixName)
				|| "XLSX".equals(suffixName)) {//excel文件
			fileSourceId = R.drawable.file_excel_fang;
		} else if (EUtil.DOC_FILE_STR.contains(suffixName)) {//word文档
			fileSourceId = R.drawable.word_fang;
		} else if (EUtil.AUDIO_FILE_STR.contains(suffixName)) {//音频文件
			fileSourceId = R.drawable.file_audio_fang;
		} else if ("ZIP".contains(suffixName)
				|| "RAR".contains(suffixName)) {//压缩文件
			fileSourceId = R.drawable.file_zip;
		} else {
			fileSourceId = R.drawable.file_other;
		}
		typeIv.setBackgroundResource(fileSourceId);
	}
	
	private void initVars(){
		isShowSaveButton = getIntent().getBooleanExtra("isShowSaveButton", true);
		mJTFile = (JTFile) getIntent().getSerializableExtra(EConsts.Key.JT_FILE);
		mJTFile.setmType(JTFile.TYPE_FILE); // 统一按文件类型处理
	}

	private void initControls(){
		typeIv = (ImageView) findViewById(R.id.typeIv);
		nameTv = (TextView) findViewById(R.id.nameTv);
		nameTv.setText(mJTFile.mFileName);
		sizeTv = (TextView) findViewById(R.id.sizeTv);
		sizeTv.setText(EUtil.formatFileSize(mJTFile.mFileSize));
		controlTv = (TextView) findViewById(R.id.controlTv);
		controlTv.setOnClickListener(mClickListener);
		saveTv = (TextView)findViewById(R.id.saveTv);
		if (!isShowSaveButton) {
			saveTv.setVisibility(View.GONE);
		}
		saveTv.setOnClickListener(mClickListener);
		statusLl = (LinearLayout) findViewById(R.id.statusLl);
		progressTv = (TextView) findViewById(R.id.progressTv);
		cancelIv = (ImageView) findViewById(R.id.cancelIv);
		cancelIv.setOnClickListener(mClickListener);
		progressPb = (ProgressBar) findViewById(R.id.progressPb);
	}
	
	private void doInit(){
        
		FileService fileservice = new FileService(this);
		int downlength = fileservice.getDownLength(mJTFile.mUrl);
		if(downlength>0){//未下载完成
			controlTv.setText("恢复下载");
			controlTv.setVisibility(View.VISIBLE);
			statusLl.setVisibility(View.GONE);
		}else if (EUtil.getFileSize(FilePreviewActivity.this, mJTFile) == mJTFile.mFileSize) {
			controlTv.setText("打开");
			controlTv.setVisibility(View.VISIBLE);
			statusLl.setVisibility(View.GONE);
		}else{
			controlTv.setText("开始下载");
			controlTv.setVisibility(View.VISIBLE);
			statusLl.setVisibility(View.GONE);
		}
	}
	
	private OnClickListener mClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.controlTv: // 控制按钮
				if(controlTv.getText().equals("打开")){
					OpenFiles.open(FilePreviewActivity.this, EUtil.getAppCacheFileDir(FilePreviewActivity.this).getAbsolutePath() + File.separator + mJTFile.mFileName);
				}
				else{
					if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
	                    download(mJTFile);
	                }else{
	                    Toast.makeText(FilePreviewActivity.this, "SDCard不可用", 1).show();
	                }
				}
				break;
			case R.id.cancelIv: // 取消按钮
				cancelDownload = true;
				loader.cancleDownLoad();
				controlTv.setText("恢复下载");
				controlTv.setVisibility(View.VISIBLE);
				statusLl.setVisibility(View.GONE);
				progressTv.setText("0%");
				progressPb.setProgress(0);
				break;
			case R.id.saveTv://保存目录
				String fileIds;
				try {
					fileIds = Util.getDownloadIdByUrl(mJTFile.getmUrl());
					ENavigate.startFileManagementActivity(FilePreviewActivity.this,fileIds);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
	};
	
	/**
     * 主线程(UI线程)
     * 对于显示控件的界面更新只是由UI线程负责，如果是在非UI线程更新控件的属性值，更新后的显示界面不会反映到屏幕上
     * @param path
     * @param savedir
     */
  private void download(final JTFile mJTFile) {
	  cancelDownload = false;
      new Thread(new Runnable() {            
          @Override
          public void run() {
              loader = new FileDownloader(FilePreviewActivity.this, mJTFile, 3);
              progressPb.setMax(loader.getFileSize());//设置进度条的最大刻度为文件的长度
              try {
                  loader.download(new DownloadProgressListener() {
                      @Override
                      public void onDownloadSize(int size) {//实时获知文件已经下载的数据长度
                          Message msg = new Message();
                          msg.what = 1;
                          msg.getData().putInt("size", size);
                          handler.sendMessage(msg);//发送消息
                      }
                  });
              } catch (Exception e) {
                  handler.obtainMessage(-1).sendToTarget();
              }
          }
      }).start();
  }
	
	@Override
	public void onResume(){
		super.onResume();
	}
	
	@Override
	public void onPause(){
		super.onPause();
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
	}
	
}
