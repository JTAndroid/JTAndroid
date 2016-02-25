package com.tr.ui.base;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.tr.App;
import com.tr.ui.widgets.EProgressDialog;
import com.utils.http.EAPIConsts;
import com.utils.log.KeelLog;
import com.utils.log.ToastUtil;

/**
 * @Filename JFragment.java
 * @Author xuxinjian
 * @Date 2014-3-13
 * @description
 */
public abstract class JBaseFragment extends Fragment implements
		JFragmentListListener {

	public static final String TAG = "EBaseFragment";
	public SharedPreferences mPrefs;
	private boolean hasPause = false;

	public final static int STATE_NORMAL = 0;
	public final static int STATE_GETMORE = 1;
	public final static int STATE_REFRESH = 2;

	protected FragmentActivity mParentActivity;// 当前activity所属的activity容器
	// protected EProgressDialog mProgressDialog;
	protected LayoutInflater inflater;

	public boolean isHasPause() {
		return hasPause;
	}

	public void setParent(FragmentActivity parent) {
		mParentActivity = parent;
		getActivity();
	}

	/**
	 * q When creating, retrieve this instance's number from its arguments.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);//设置使fragment可以编辑菜单
		mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity()
				.getApplicationContext());
		KeelLog.v(TAG, "onCreate:" + this);
	}

	@Override
	public void onPause() {
		super.onPause();
		KeelLog.v(TAG, "onPause:" + this);
		hasPause = true;
	}

	@Override
	public void onResume() {
		super.onResume();
		KeelLog.v(TAG, "onResume:" + this);
		hasPause = false;
	}

	/**
     */
	public void refresh() {

	}

	/**
     * 
     */
	public void clear() {

	}

	/**
	 * 切换到创建会议,采用事件分发
	 * 
	 * @param message
	 */
	public void changeManager() {

	}

	/**
	 * 回到会议
	 * 
	 * @param message
	 */
	public void backToMeeting() {

	}

	/**
	 * 隐藏底部删除
	 * 
	 * @param message
	 */
	public void hideFooter() {

	}

	public void showUIToast(final String message) {
		Activity activity = getActivity();
		if (null != activity) {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(App.getApp(), message, Toast.LENGTH_SHORT)
							.show();
				}
			});
		}
	}

	public void showUIToast(final int resId) {
		Activity activity = getActivity();
		if (null != activity) {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(App.getApp(), resId, Toast.LENGTH_SHORT)
							.show();
				}
			});
		}
	}

	public void showToast(final String message) {
		ToastUtil.showToast(App.getApp(), message);
	}

	public void showToast(final int resId) {
		Toast.makeText(App.getApp(), resId, Toast.LENGTH_SHORT).show();
	}

	public void showToast(final String message, final int delay) {
		Toast.makeText(App.getApp(), message, delay).show();
	}

	public void showToast(final int resId, final int delay) {
		Toast.makeText(App.getApp(), resId, delay).show();
	}

	public void finishParentActivity() {
		getActivity().finish();
	}

	@Override
	public void onDestroy() {
		ToastUtil.cancelToast();
		super.onDestroy();
	}

	protected Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == EAPIConsts.handler.show_err) {

			}
		}
	};
	private EProgressDialog mProgressDialog;
	
	public void showLoadingDialog() {
        showLoadingDialog("");
    }
	public void showLoadingDialog(String message) {
		if (null == mProgressDialog) {
			mProgressDialog = new EProgressDialog(getActivity());
			mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialogInterface) {
					// onLoadingDialogCancel();//如果取消对话框， 结束当前activity
				}
			});
		}
		mProgressDialog.setMessage(message);
		mProgressDialog.show();
	}
	public void dismissLoadingDialog() {
		try{
			if (mProgressDialog != null && mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
			}
		}catch(Exception e){
			
		}
	}

}
