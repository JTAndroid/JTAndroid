package com.tr.ui.people.contactsdetails.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.tr.ui.people.contactsdetails.dialog.EProgressDialogs;
import com.tr.ui.people.contactsdetails.listener.ScrollableFragmentListeners;
import com.tr.ui.people.contactsdetails.listener.ScrollableListeners;

public abstract class BaseViewPagerFragments extends Fragment implements ScrollableListeners {

	protected ScrollableFragmentListeners mListener;
	protected static final String BUNDLE_FRAGMENT_INDEX = "BaseFragment.BUNDLE_FRAGMENT_INDEX";
	protected int mFragmentIndex;
	protected EProgressDialogs mProgressDialog;

	protected FragmentActivity mParentActivity;// 当前activity所属的activity容器

	public void setParent(FragmentActivity parent) {
		mParentActivity = parent;
		getActivity();
	}

	public void showLoadingDialog() {
        showLoadingDialog("");
    }
	public void showLoadingDialog(String message) {
		if (null == mProgressDialog) {
			mProgressDialog = new EProgressDialogs(getActivity());
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
			mListener = (ScrollableFragmentListeners) activity;
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
