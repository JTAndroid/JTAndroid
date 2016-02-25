package com.tr.ui.adapter;

import java.util.ArrayList;
import java.util.List;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.R;
import com.tr.model.obj.IMRecord;
import com.tr.ui.widgets.SmileyParser;
import com.tr.ui.widgets.SmileyParser2;
import com.utils.common.JTDateUtils;
import com.utils.image.LoadImage;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class IMAdapter extends BaseAdapter {

	private SmileyParser parser;
	private SmileyParser2 parser2;

	private Context mContext;
	private List<IMRecord> mData;

	public IMAdapter(Context context) {
		mContext = context;
		mData = new ArrayList<IMRecord>();
		parser = SmileyParser.getInstance(context);
		parser2 = SmileyParser2.getInstance(context);
	}

	/*
	public void setData(List<IMRecord> mData) {
		this.mData = mData;
	}
	*/
	
	public void update(List<IMRecord> data){
		if(data ==null){
			return;
		}
		else{
			mData = data;
			notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int i) {
		return mData.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(final int position, View convertView,
			final ViewGroup parent) {
		ItemHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.home_frg_im_listview_item, parent, false);
			holder = new ItemHolder();
			holder.mImg = (ImageView) convertView
					.findViewById(R.id.HFI_IMAGE_Head);

			holder.mImageFourBG = convertView
					.findViewById(R.id.HomeFrgIMItemLayoutHeadRoot);
			ImageView v = (ImageView) convertView
					.findViewById(R.id.HFI_IMAGE_11);
			holder.mImageFour.add(v);
			v = (ImageView) convertView.findViewById(R.id.HFI_IMAGE_12);
			holder.mImageFour.add(v);
			v = (ImageView) convertView.findViewById(R.id.HFI_IMAGE_21);
			holder.mImageFour.add(v);
			v = (ImageView) convertView.findViewById(R.id.HFI_IMAGE_22);
			holder.mImageFour.add(v);

			holder.mTitle = (TextView) convertView
					.findViewById(R.id.HFI_IM_Title);
			holder.mContent = (TextView) convertView
					.findViewById(R.id.HFI_IM_CONTENT);
			holder.mSource = (TextView) convertView
					.findViewById(R.id.HFI_IM_Compere);
			holder.mTime = (TextView) convertView
					.findViewById(R.id.HFI_IM_TIME);
			
			holder.mSendTime = (TextView)convertView.findViewById(R.id.HFI_IM_MESSAGE_CREATE_TIME);

			holder.mTxtNewMessage = (TextView) convertView
					.findViewById(R.id.HFI_IM_NEW_COUNT);

			convertView.setTag(holder);
		} 
		else {
			holder = (ItemHolder) convertView.getTag();
		}

		// Retrieve the data holder
		final IMRecord dataHolder = mData.get(position);
		holder.mTitle.setText(dataHolder.getTitle());

		CharSequence dd = parser.addSmileySpans(dataHolder.getContent());
		CharSequence dd1 = parser2.addSmileySpans(dd);
		holder.mContent.setText(dd1);

		if (TextUtils.isEmpty(dataHolder.getCompereName()))
			holder.mSource.setText("");
		else
			holder.mSource.setText("主持人: " + dataHolder.getCompereName());

		if (dataHolder.getNewCount() > 0) {
			holder.mTxtNewMessage.setVisibility(View.VISIBLE);
			holder.mTxtNewMessage.setText(dataHolder.getNewCount() + "");
		} else {
			holder.mTxtNewMessage.setVisibility(View.GONE);
		}

		String testtime = dataHolder.getTime();// 测试
		String strTime = JTDateUtils.getTimeDisplay(testtime, mContext);

		holder.mTime.setText(strTime);
		if (dataHolder.getListImageUrl() == null) {
			holder.mImageFourBG.setVisibility(View.GONE);
			holder.mImg.setVisibility(View.VISIBLE);
		} else if (dataHolder.getListImageUrl().size() == 0) {
			holder.mImageFourBG.setVisibility(View.GONE);
			holder.mImg.setVisibility(View.VISIBLE);
		} else if (dataHolder.getListImageUrl().size() == 1) {
			holder.mImageFourBG.setVisibility(View.GONE);
			holder.mImg.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(
					dataHolder.getListImageUrl().get(0), holder.mImg,
					LoadImage.mDefaultHead);
			// ApolloUtils.getImageFetcher(getActivity()).loadHomeImage(dataHolder.getListImageUrl().get(0),
			// holder.mImg);
		} else {
			holder.mImageFourBG.setVisibility(View.VISIBLE);
			holder.mImg.setVisibility(View.GONE);
			for (int j = 0; (j < dataHolder.getListImageUrl().size())
					&& (j <= 3); j++) {
				// ApolloUtils.getImageFetcher(App.getApp().getApplicationContext()).loadHomeImage("http://192.168.101.131:3333/img/user/image/?module=user&userId=14359",
				// holder.mImageFour.get(j));
				// ImageLoader.getInstance().displayImage("http://192.168.101.131:3333/img/user/image/?module=user&userId=14359",
				// holder.mImageFour.get(j), LoadImage.mDefaultHead);
				holder.mImageFour.get(j).setVisibility(View.VISIBLE);
				ImageLoader.getInstance().displayImage(
						dataHolder.getListImageUrl().get(j),
						holder.mImageFour.get(j), LoadImage.mDefaultHead);
			}

			for (int j = dataHolder.getListImageUrl().size(); j <= 3; j++) {
				holder.mImageFour.get(j).setVisibility(View.INVISIBLE);
			}
		}
		
		// 最后一条消息的发送时间
		String sendtime = dataHolder.getTime();
		if(!TextUtils.isEmpty(sendtime)){ // 会议尚未开始,将会议开始时间显示在最下方
			strTime = JTDateUtils.getIMListTimeDisplay(sendtime, mContext, false);
			holder.mSendTime.setText(strTime);
		}
		else{
			holder.mSendTime.setText("");
		}
		
		// ApolloUtils.getImageFetcher((Activity)
		// mContext).loadHomeImage(dataHolder.getmImageUrl(), holder.mImg);
		return convertView;
	}

	private class ItemHolder {
		public ImageView mImg;// 单个头像
		public View mImageFourBG;//
		public List<ImageView> mImageFour = new ArrayList<ImageView>();// 2-4个头像
		public TextView mTitle;
		public TextView mContent;
		public TextView mSource;
		public TextView mTime;
		public TextView mSendTime;//消息发送时间
		public TextView mTxtNewMessage; // 新消息条数
	}
}
