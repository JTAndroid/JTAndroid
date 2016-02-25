package com.tr.ui.conference.square;

import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tr.R;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.widgets.EProgressDialog;
import com.utils.time.Util;
//import com.baidu.navisdk.ui.widget.BNNetworkingDialog.OnBackPressedListener;

public class PhotoBrowseFragment extends JBaseFragment {

	private List<String> imgs = new ArrayList<String>();
	private int currPosition;

	@ViewInject(R.id.hy_photo_browse_pager)
	private ViewPager pager;
	@ViewInject(R.id.hy_browse_curr_position)
	private TextView text;

	public PhotoBrowseFragment(List<String> imgs, int currPosition) {
		this.imgs = imgs;
		this.currPosition = currPosition;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		FrameLayout view = (FrameLayout) inflater.inflate(R.layout.hy_fragment_photo_browse, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		PhotoBrowseAdapter adapter = new PhotoBrowseAdapter();
		pager.setAdapter(adapter);
		pager.setCurrentItem(currPosition);
	}

	public interface OnEndListener {
		public void onEndListener();
	}

	private OnEndListener OnEndListener;

	public void setOnEndListener(OnEndListener onEndListener) {
		OnEndListener = onEndListener;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			OnEndListener.onEndListener();
			return true;
		} else
			return false;
	}

	class PhotoBrowseAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			if (imgs != null) {
				return imgs.size();
			}
			return 0;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = View.inflate(getActivity(), R.layout.hy_item_photo_browse, null);
			ImageView imageView = (ImageView) view.findViewById(R.id.hy_pager_img);
			LinearLayout dots = (LinearLayout) view.findViewById(R.id.hy_browse_dots_container);
			// TextView tvPosition = (TextView)
			// view.findViewById(R.id.hy_browse_curr_position);
			for (int i = 0; i < imgs.size(); i++) {
				ImageView dot = new ImageView(getActivity());
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Util.DensityUtil.dip2px(getActivity(), 5), Util.DensityUtil.dip2px(
						getActivity(), 5));
				params.rightMargin = Util.DensityUtil.dip2px(getActivity(), 5);
				params.leftMargin = Util.DensityUtil.dip2px(getActivity(), 5);
				dot.setLayoutParams(params);
				if (i == position) {
					dot.setBackgroundResource(R.drawable.hy_icon_meeting_dot_normal);
					dots.addView(dot);
				} else {
					dot.setBackgroundResource(R.drawable.hy_icon_meeting_dot_current);
					dots.addView(dot);
				}
			}
			ImageLoader.getInstance().displayImage(imgs.get(position), imageView, new ImageLoadingListener() {

				private EProgressDialog prgDialog;

				@Override
				public void onLoadingStarted(String imageUri, View view) {
					prgDialog = new EProgressDialog(getActivity());
					prgDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
						@Override
						public void onCancel(DialogInterface dialog) {

						}
					});
					prgDialog.setMessage("加载中...");
					prgDialog.setCancelable(false);
					prgDialog.setCanceledOnTouchOutside(false);
					prgDialog.show();
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					prgDialog.dismiss();
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					prgDialog.dismiss();
				}

				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					prgDialog.dismiss();
				}
			});
			// tvPosition.setText((position+1) + " / " + imgs.size());
			container.addView(view);
			return view;
		}

	}
}
