package com.tr.ui.user;

import java.io.File;
import java.util.List;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import com.tr.App;
import com.tr.R;
import com.tr.db.DownloadFileDBManager;
import com.tr.model.obj.JTFile;
import com.tr.ui.adapter.FileAdapter;
import com.tr.ui.base.JBaseFragmentActivity;
import com.utils.common.EUtil;
import com.utils.common.OpenFiles;

/**
 * @ClassName:     FileManagementActivity.java
 * @Description:   文件管理
 * @Author         leon
 * @Version        v 1.0  
 * @Create         2014-04-11
 * @Update         2014-06-11
 */
public class FileManagementActivity extends JBaseFragmentActivity {

	private final String TAG = getClass().getSimpleName();
	
	// 控件
	private ListView fileLv;
	
	// 变量
	private FileAdapter mAdapter;
	private List<JTFile> mListJTFile; 
	
	@Override
	public void initJabActionBar() {
		jabGetActionBar().setTitle("文件管理");
		jabGetActionBar().setDisplayShowTitleEnabled(true);
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_act_file_management);
		initVars();
		initControls();
	}
	
	private void initVars(){
		mAdapter = new FileAdapter(this);
	}
	
	private void initControls(){
		fileLv = (ListView) findViewById(R.id.fileLv);
		fileLv.setAdapter(mAdapter);
		fileLv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				JTFile jtFile = (JTFile) mAdapter.getItem(arg2);
				OpenFiles.open(FileManagementActivity.this,EUtil.getAppFileDir(FileManagementActivity.this)
						+ File.separator + jtFile.mFileName);
			}
		});
		fileLv.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
				// TODO Auto-generated method stub
				EUtil.showFileDeleteDialog(FileManagementActivity.this,new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 显示加载框
						showLoadingDialog();
						// 删除本地文件
						JTFile jtfile = (JTFile) mAdapter.getItem(arg2);
						// 删除本地文件
						File file = new File(EUtil.getAppFileDir(FileManagementActivity.this), jtfile.mFileName);
						file.delete();
						// 从列表中删除
						mListJTFile = EUtil.getUserListJTFile(FileManagementActivity.this);
						mAdapter.updateAdapter(mListJTFile);
						// 隐藏加载框
						dismissLoadingDialog();
					}
				});
				return false;
			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		// 显示加载框
		showLoadingDialog();
		// 用户已下载的文件
		mListJTFile = EUtil.getUserListJTFile(this);
		mAdapter.updateAdapter(mListJTFile);
		// 隐藏加载框
		dismissLoadingDialog();
	}
	
}
