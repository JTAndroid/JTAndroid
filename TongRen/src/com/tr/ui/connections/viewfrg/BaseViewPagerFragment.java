package com.tr.ui.connections.viewfrg;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.tr.ui.base.JBaseActivity;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.widgets.EProgressDialog;
import com.tr.ui.widgets.viewpagerheaderscroll.tools.ScrollableFragmentListener;
import com.tr.ui.widgets.viewpagerheaderscroll.tools.ScrollableListener;
import com.utils.log.KeelLog;

public abstract class BaseViewPagerFragment extends Fragment implements ScrollableListener {

	protected ScrollableFragmentListener mListener;
	protected static final String BUNDLE_FRAGMENT_INDEX = "BaseFragment.BUNDLE_FRAGMENT_INDEX";
	protected int mFragmentIndex;
	protected EProgressDialog mProgressDialog;

	protected FragmentActivity mParentActivity;// 当前activity所属的activity容器
	public void setParent(FragmentActivity parent) {
		mParentActivity = parent;
		getActivity();
	}

	public void showLoadingDialog() {
        showLoadingDialog("");
    }
	public void showLoadingDialog(String message) {
		try {
			if ( mProgressDialog==null) {
				mProgressDialog = new EProgressDialog(mParentActivity);
				mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialogInterface) {
						// onLoadingDialogCancel();//如果取消对话框， 结束当前activity
					}
				});
			}
			mProgressDialog.setMessage(message);
			mProgressDialog.show();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	public void dismissLoadingDialog() {
		try{
			if (mProgressDialog != null && mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
			}
		}catch(Exception e){
			
		}
	}
	// 加载框是否可以取消
    protected void showLoadingDialog(String message,boolean cancelable,DialogInterface.OnCancelListener cancelListener) {
        if (null==mProgressDialog) {
            mProgressDialog=new EProgressDialog(mParentActivity);
            mProgressDialog.setCancelable(cancelable);
			if (cancelable) {
				mProgressDialog.setOnCancelListener(cancelListener);
			}
        }
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }
    
    /**
	 * 显示一个没有OnCancelListener的 Dialog
	 * @param message
	 * @param listener
	 */
	public void showLoadingDialogWithoutOnCancelListener() {
		if (null == mProgressDialog) {
			mProgressDialog = new EProgressDialog(mParentActivity);
			mProgressDialog.setCanceledOnTouchOutside(false);
		}
		mProgressDialog.show();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if (bundle != null) {
			mFragmentIndex = bundle.getInt(BUNDLE_FRAGMENT_INDEX, 0);
		}

		if (mListener != null) {
			mListener.onFragmentAttached(this, mFragmentIndex);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (ScrollableFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement ScrollableFragmentListener");
		}
	}

	@Override
	public void onDetach() {
		if (mListener != null) {
			mListener.onFragmentDetached(this, mFragmentIndex);
		}

		super.onDetach();
		mListener = null;
	}
}
