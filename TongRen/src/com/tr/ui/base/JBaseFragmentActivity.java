package com.tr.ui.base;

import java.io.File;
import java.util.List;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.tr.App;
import com.tr.R;
import com.tr.model.obj.JTFile;
import com.tr.ui.widgets.EProgressDialog;
import com.umeng.analytics.MobclickAgent;
import com.utils.common.EConsts;
import com.utils.common.EUtil;
import com.utils.common.FileDownloader.OnFileDownloadListener;
import com.utils.log.KeelLog;

/**
 * @Filename JBaseActivity.java
 * @Date 2014-3-13
 * @description
 */
public abstract class JBaseFragmentActivity extends FragmentActivity {

	private JBaseFragmentActivity mParentActivity; // 父Activity

	public final static int STATE_NORMAL = 0;
	public final static int STATE_GETMORE = 1;
	public final static int STATE_REFRESH = 2;

	public static final String TAG = "EBaseActivity";
	protected EProgressDialog mProgressDialog;
	public LayoutInflater mInflater = null;

	private OnFileDownloadListener mDownloadListener;
	protected OnFileSelectedListener mFileSelectedListener; // 文件选择监听器
	public File mPicFile; // 图片文件
	public File mVideoFile; // 视频文件

	private boolean isDestroy = false;

	private IntentFilter mIntentFilter; // 广播过滤器

	private int SCALESIZE = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		initJActionBarImpl();
		initVars();
		// 添加到App堆栈
		App.getApp().addActivity(this);
	}

	private void initVars() {
		// 过滤器（监听下载进度）
		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(EConsts.Action.DOWNLOAD_START);
		mIntentFilter.addAction(EConsts.Action.DOWNLOAD_UPDATE);
		mIntentFilter.addAction(EConsts.Action.DOWNLOAD_FAILED);
		mIntentFilter.addAction(EConsts.Action.DOWNLOAD_CANCELED);
		mIntentFilter.addAction(EConsts.Action.DOWNLOAD_SUCCESS);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
//			InputMethodManager m = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//			if (m.isActive()) {
//				// 如果开启
//				m.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
//						InputMethodManager.HIDE_NOT_ALWAYS);
//				// 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
//			}
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		isDestroy = true;
		dismissLoadingDialog();
		super.onDestroy();
	}

	/**
	 * 设置ActionBar样式
	 */
	private void initJActionBarImpl() {
		ActionBar actionbar = this.getActionBar();
		if (actionbar == null) {
			return;
		}
		actionbar.setDisplayShowTitleEnabled(true);
		actionbar.setHomeButtonEnabled(false);
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setIcon(R.color.none);
		// 设置actionbar的背景图
		Drawable myDrawable = getResources().getDrawable(
				R.drawable.action_bar_bg);
		actionbar.setBackgroundDrawable(myDrawable); // 设置背景图片
		myDrawable.clearColorFilter();
		actionbar.setSplitBackgroundDrawable(getResources().getDrawable(
				R.drawable.action_bar_bg));
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		initJabActionBar();
	}

	// 子类在该回调中实现actionbar相关修改和设置
	public abstract void initJabActionBar();

	public void jabHideActionBar() {
		getActionBar().hide();
	}

	protected ActionBar jabGetActionBar() {
		// return getSupportActionBar();
		return this.getActionBar();
	}

	public void showLoadingDialog() {
		showLoadingDialog("");
	}

	protected void showLoadingDialog(int resId) {
		showLoadingDialog(getString(resId));
	}

	public void showLoadingDialog(String message) {
		if (null == mProgressDialog) {
			mProgressDialog = new EProgressDialog(JBaseFragmentActivity.this);
			mProgressDialog
					.setOnCancelListener(new DialogInterface.OnCancelListener() {
						@Override
						public void onCancel(DialogInterface dialogInterface) {
							KeelLog.d(TAG, "mProgressDialog.onCancel");
						}
					});
		}
		mProgressDialog.setMessage(message);
		mProgressDialog.show();
	}

	/**
	 * 显示一个没有OnCancelListener的 Dialog
	 * 
	 * @param message
	 * @param listener
	 */
	public void showLoadingDialogWithoutOnCancelListener() {
		if (null == mProgressDialog) {
			mProgressDialog = new EProgressDialog(JBaseFragmentActivity.this);
			mProgressDialog.setCanceledOnTouchOutside(false);
		}
		mProgressDialog.show();
	}

	/**
	 * 取消监听器
	 * 
	 * @param message
	 * @param listener
	 */
	public void showLoadingDialog(String message,
			DialogInterface.OnCancelListener listener) {
		if (null == mProgressDialog) {
			mProgressDialog = new EProgressDialog(JBaseFragmentActivity.this);
			mProgressDialog.setOnCancelListener(listener);
		}
		mProgressDialog.setMessage(message);
		mProgressDialog.show();
	}

	public void dismissLoadingDialog() {
		try {
			if (mProgressDialog != null && mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
			}
		} catch (Exception e) {

		}
	}

	/**
	 * 取消loading对话框动作回调，如果想要处理，就继承该函数，默认关闭当前页面
	 */
	public void onLoadingDialogCancel() {
		finish();
	}

	public boolean isLoadingDialogShowing() {
		if (mProgressDialog != null) {
			return mProgressDialog.isShowing();
		} else {
			return false;
		}
	}

	public void showToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	public void showToast(int id) {
		String text = getString(id);
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	public void showLongToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}

	public void showLongToast(int id) {
		String text = getString(id);
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}

	// 设置文件选择监听器
	public void setOnFileSelctedListener(OnFileSelectedListener listener) {
		mFileSelectedListener = listener;
	}

	// 用来处理用户选择的文件
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {

		if (requestCode == EConsts.REQ_CODE_TAKE_PICTURE) { // 拍照
			if (resultCode == RESULT_OK) {
				if (mFileSelectedListener != null) {
					String filePath = mPicFile.getAbsolutePath();
					mFileSelectedListener.onFileSelected(filePath);
				}
			}
		} else if (requestCode == EConsts.REQ_CODE_PICK_PICTURE) { // 选照
			if (resultCode == RESULT_OK) {
				if (mFileSelectedListener != null) {
					String filePath = EUtil.uri2Path(getContentResolver(),
							intent.getData());
					mFileSelectedListener.onFileSelected(filePath);
				}
			}
		} else if (requestCode == EConsts.REQ_CODE_TAKE_VIDEO) { // 拍视频
			if (resultCode == RESULT_OK) {
				if (mFileSelectedListener != null) {
					String filePath = mVideoFile.getAbsolutePath();
					mFileSelectedListener.onFileSelected(filePath);
				}
			}
		} else if (requestCode == EConsts.REQ_CODE_PICK_VIDEO) { // 选视频
			if (resultCode == RESULT_OK) {
				if (mFileSelectedListener != null) {
					String filePath = EUtil.uri2Path(getContentResolver(),
							intent.getData());
					mFileSelectedListener.onFileSelected(filePath);
				}
			}
		} else if (requestCode == EConsts.REQ_CODE_PICK_FILE) { // 选文件
			if (resultCode == RESULT_OK) {
				if (mFileSelectedListener != null) {
					String filePath = EUtil.uri2Path(getContentResolver(),
							intent.getData());
					mFileSelectedListener.onFileSelected(filePath);
				}
			}
		} else if (requestCode == EConsts.REQ_CODE_GET_RECORD) { // 选录音
			if (resultCode == RESULT_OK) {
				if (mFileSelectedListener != null) {
					Bundle bundle = intent.getExtras();
					List<String> urls = bundle
							.getStringArrayList(EConsts.Key.RECORDING);
					mFileSelectedListener.onFilesSelected(urls);
				}
			}
		}

		super.onActivityResult(requestCode, resultCode, intent);

	}

	@Override
	public void onBackPressed() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		} else {
			super.onBackPressed();
		}
	}

	// 选中文件监听器
	public interface OnFileSelectedListener {
		public void onFileSelected(String filePath);

		public void onFilesSelected(List<String> filePaths);
	}

	public boolean hasDestroy() {
		return isDestroy;
	}

	/**
	 * 设置下载监听器
	 * 
	 * @param listener
	 */
	public void setOnFileDownloadListener(OnFileDownloadListener listener) {
		mDownloadListener = listener;
	}

	@Override
	public void onResume() {
		super.onResume();
		registerReceiver(mBroadcastReceiver, mIntentFilter); // 注册广播监听器
		MobclickAgent.onResume(this); // 友盟统计
		ActivityHolder.getInstance().push(this); // 进栈
	}

	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(mBroadcastReceiver); // 取消广播监听器
		MobclickAgent.onPause(this); // 友盟统计
		ActivityHolder.getInstance().pop(this); // 出栈
	}

	// 广播监听器
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(EConsts.Action.DOWNLOAD_START)) { // 开始下载
				if (mDownloadListener != null) {
					mDownloadListener.onStarted(intent
							.getStringExtra(EConsts.Key.WEB_FILE_URL));
				}
			} else if (intent.getAction()
					.equals(EConsts.Action.DOWNLOAD_UPDATE)) { // 下载进度更新
				if (mDownloadListener != null) {
					mDownloadListener.onUpdate(
							intent.getStringExtra(EConsts.Key.WEB_FILE_URL),
							intent.getIntExtra(EConsts.Key.PROGRESS_UPDATE, 0));
				}
			} else if (intent.getAction().equals(
					EConsts.Action.DOWNLOAD_SUCCESS)) { // 下载成功
				if (mDownloadListener != null) {
					mDownloadListener.onSuccess(intent
							.getStringExtra(EConsts.Key.WEB_FILE_URL),
							(JTFile) intent
									.getSerializableExtra(EConsts.Key.JT_FILE));
				}
			} else if (intent.getAction()
					.equals(EConsts.Action.DOWNLOAD_FAILED)) { // 下载失败
				if (mDownloadListener != null) {
					mDownloadListener.onError(
							intent.getStringExtra(EConsts.Key.WEB_FILE_URL), 0,
							intent.getStringExtra(EConsts.Key.ERROR_MESSAGE));
				}
			} else if (intent.getAction().equals(
					EConsts.Action.DOWNLOAD_CANCELED)) { // 取消下载
				if (mDownloadListener != null) {
					mDownloadListener.onCanceled(intent
							.getStringExtra(EConsts.Key.WEB_FILE_URL));
				}
			}
		}
	};

	protected EProgressDialog progressDialog;

	/**
	 * 显示加载框
	 */
	protected void showLoadingDialogHy() {
		if (null == progressDialog) {
			progressDialog = new EProgressDialog(this);
		}
		progressDialog.setMessage("");
		progressDialog.show();
	}

	/**
	 * 隐藏加载框
	 */
	protected void dismissLoadingDialogHy() {
		try {
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
		} catch (Exception e) {
			Log.d(TAG, e.getMessage());
		}
	}
}
