package com.tr.ui.user.modified;

import java.lang.reflect.Field;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tr.R;
import com.tr.api.UserReqUtil;
import com.tr.model.api.DataBox;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.base.JBaseFragmentActivity;
import com.utils.common.EUtil;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;

/**
 * 绑定邮箱-填入邮箱发送验证邮件 fragment
 * 
 * @author Administrator
 * 
 */
public class BindingEmailFragment extends JBaseFragment {

	private JBaseFragmentActivity mParentActivity; // 父Activity
	private boolean hasEmail = false;

	private ImageView delelte_email_content;
	private Button mSend;
	private TextView mEmail;// 描述 type=1邮箱 或type=2 新邮箱
	private EditText mInEmail;
	private int mtype;// 为1 表示直接绑定邮箱 2是解绑旧的后绑定新的

	private final String TIP_ILLEGAL_ACCOUNT = "请输入正确的邮箱";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mtype = getArguments().getInt("type");
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_about_binding_email, null);
	}

	@Override
	public void onViewCreated(View container, Bundle savedInstanceState) {
		initControls(container);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mParentActivity = (JBaseFragmentActivity) activity;
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
		mEmail = (TextView) container.findViewById(R.id.tv_email);
		mInEmail = (EditText) container.findViewById(R.id.accountEt);
		mInEmail.setHint("请输入邮箱");

		delelte_email_content = (ImageView) container
				.findViewById(R.id.delelte_email_content);
		delelte_email_content.setOnClickListener(mClickListener);

		mInEmail.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!StringUtils.isEmpty(s.toString())) {
					mSend.setVisibility(View.VISIBLE);
					hasEmail = true;
				} else {
					delelte_email_content.setVisibility(View.INVISIBLE);
					hasEmail = false;
				}
				if (hasEmail) {
					mSend.setClickable(true);
					mSend.setBackgroundResource(R.drawable.sign_in);
				} else {
					mSend.setClickable(false);
					mSend.setBackgroundResource(R.drawable.sign_in_normal);
				}
			}
		});
		mSend = (Button) container.findViewById(R.id.bt_send);
		mSend.setOnClickListener(mClickListener);
		mSend.setClickable(false);
		if (mtype == 2) {

			mEmail.setText("新邮箱");
			mInEmail.setHint("请输入新的邮箱地址");
			mSend.setText("确认更改");
		}
	}

	// 点击事件监听器
	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.delelte_email_content:
				mInEmail.setText("");
				break;
			case R.id.bt_send:
				// TODO 邮箱正确 发送验证邮件
				nextStep();
				break;
			default:
				break;
			}

		}

	};

	// 验证是否是正确的邮箱格式
	private boolean infoIntegrityCheck() {
		if (EUtil.isEmail(mInEmail.getText().toString())) {
			return true;
		} else {
			showToast(TIP_ILLEGAL_ACCOUNT);
			return false;
		}
	}

	/** 下一步 */
	private void nextStep() {
		if (!infoIntegrityCheck()) {
			// 如果邮箱不正确返回
			return;
		}
		// 显示加载框
		mParentActivity.showLoadingDialog();
		// 若邮箱正确执行下面
		// 请求数据，判断手机或邮箱是否可绑
		UserReqUtil.doSet_MobileOrEmailwhetherCanBinding(mParentActivity,
				mBindData, "2", mInEmail.getText().toString().trim(), null);

	}

	// 接口回调函数
	private IBindData mBindData = new IBindData() {

		@Override
		public void bindData(int type, Object object) {
			if (mParentActivity.isLoadingDialogShowing()) {
				mParentActivity.dismissLoadingDialog();
			}
			if (type == EAPIConsts.ReqType.SEND_VALIDATE_EMAIL) {
				// 处理数据
				if (object != null) {
					DataBox dataBox = (DataBox) object;

					if (dataBox.mIsSuccess) {
						showToast("发送成功");
						// TODO 跳转到邮箱发送完毕重新发送验证邮件页面
						ReSendVerifyEmailFragment fragment = new ReSendVerifyEmailFragment();
						Bundle bundle = new Bundle();
						bundle.putString("email", mInEmail.getText().toString()
								.trim());
						bundle.putInt("type", mtype);
						fragment.setArguments(bundle);
						getActivity().getSupportFragmentManager()
								.beginTransaction()
								.replace(R.id.fragment_bind_email, fragment)
								.commitAllowingStateLoss();
					} else {
						showToast("发送失败");

					}
				}
			}
			if (type == EAPIConsts.ReqType.SET_MOBILE_EMAIL_WHETHER_CAN_BINDING) {
				// 处理数据
				if (object != null) {
					DataBox dataBox = (DataBox) object;

					// "msg": "1:该手机号已被占用2:该邮箱已被占用  3:可绑"
					verifyUserStatus(Integer.valueOf(dataBox.mMessage));
				}
			}
		}
	};

	// "msg": "1:该手机号已被占用2:该邮箱已被占用 3:可绑"
	private void verifyUserStatus(int userStatus) {
		switch (userStatus) {
		case 1:// TODO 邮箱验证成功跳转到绑定新邮箱页面
			showToast("该手机号已被占用");
			break;
		case 2:
			showToast("该邮箱已被占用");
			break;
		case 3:
			// showToast("可绑");
			if (mtype == 1)
				UserReqUtil.doSet_SendValidateEmail(mParentActivity, mBindData, "3", mInEmail.getText().toString().trim(), "1", null);
			else
				UserReqUtil.doSet_SendValidateEmail(mParentActivity, mBindData, "2", mInEmail.getText().toString().trim(), "1", null);
			break;
		default:
			break;
		}
	}

}
