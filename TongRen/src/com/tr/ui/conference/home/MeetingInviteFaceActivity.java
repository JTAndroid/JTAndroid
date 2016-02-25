package com.tr.ui.conference.home;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.tr.R;
import com.tr.model.conference.MMeetingQuery;
import com.tr.navigate.ENavConsts;
import com.tr.ui.conference.common.BaseActivity;
import com.utils.http.IBindData;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 二维码邀请
 * 
 * @author d.c
 */

public class MeetingInviteFaceActivity extends BaseActivity implements IBindData {
	// 标题栏
	private LinearLayout mIvBackButton = null;
	private TextView mTvTitle = null;
	private ImageView mIvContact = null;
	private Button mBtnCopy = null;
	private TextView mMeetingName = null;
	private MMeetingQuery mMeetingQuery = null;

	public Bitmap CreateTwoDCode(String content) throws WriterException {
		BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 300, 300);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = 0xff000000;
				}
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	@Override
	public void initView() {
		setContentView(R.layout.hy_activity_meeting_invite_face);
		mIvBackButton = (LinearLayout) findViewById(R.id.hy_layoutTitle_backBtn);
		mTvTitle = (TextView) findViewById(R.id.hy_layoutTitle_title);
		mIvContact = (ImageView) findViewById(R.id.hy_layoutTitle_rightIconBtn);
		mMeetingName = (TextView) findViewById(R.id.hy_tv_invite_by_face);
		mBtnCopy = (Button) findViewById(R.id.hy_btn_invite_by_face);
	}

	@Override
	public void initData() {
		mTvTitle.setText("二维码名片");
		mIvContact.setBackgroundResource(R.drawable.hy_titlebar_right_contact);

		mMeetingQuery = (MMeetingQuery) getIntent().getSerializableExtra(ENavConsts.EMeetingDetail);
		if (null == mMeetingQuery) {
			Toast.makeText(this, "无效的会议数据", 0).show();
			finish();
		}

		try {
			ImageView iv_Code = (ImageView) findViewById(R.id.hy_iv_invite_by_face);
			iv_Code.setImageBitmap(CreateTwoDCode(mMeetingQuery.getId() + ""));
		} catch (WriterException e) {
			e.printStackTrace();
			Toast.makeText(this, "无效的会议数据", 0).show();
			finish();
		}

		mMeetingName.setText(mMeetingQuery.getMeetingName());
		mIvBackButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		mBtnCopy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void bindData(int tag, Object object) {
	}

}