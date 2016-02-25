package com.tr.ui.user.modified;

import java.lang.reflect.Field;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.tr.R;
import com.tr.ui.base.JBaseFragment;

/**
 * 绑定邮箱-重新发送验证邮件fragment
 * 
 * @author Administrator
 * 
 */
public class ReSendVerifyEmailFragment extends JBaseFragment {
	
	private String email;
	private int type;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		email = getArguments().getString("email");
		type = getArguments().getInt("type");
	}
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_send_verify_email, null);
	}

	@Override
	public void onViewCreated(View container, Bundle savedInstanceState) {
		initControls(container);
	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);

		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}

	}

	private void initControls(View container) {
		
		TextView emailTv=(TextView) container.findViewById(R.id.tv_emailNum);
		emailTv.setText(email);
		container.findViewById(R.id.bt_re_send).setOnClickListener(mClickListener);
		container.findViewById(R.id.bt_second).setVisibility(View.GONE);
	}
	
	// 点击事件监听器
		private OnClickListener mClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.bt_re_send:
					//返回填入邮箱页面 发送验证邮箱页面
					BindingEmailFragment fragment=new BindingEmailFragment();
					Bundle bundle = new Bundle();
					bundle.putInt("type", type);
					fragment.setArguments(bundle);
					getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_bind_email, fragment).commitAllowingStateLoss();
					break;

				default:
					break;
				}
				
			}
			
		};
}
