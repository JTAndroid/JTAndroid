package com.tr.ui.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tr.R;
import com.tr.model.obj.AffairsMini;
import com.tr.navigate.ENavigate;
import com.tr.ui.knowledge.KnowledgeAffairActivity;
import com.utils.common.JTDateUtils;
/**
 * @ClassName:     AffairItemLvAdapter.java
 * @Description:   知识详情页-- 显示事件
 * @Author         CJJ
 * @Created        2014-11-15
 */
public class AffairItemLvAdapter extends BaseAdapter{
	/** 变量*/
	private Context mContext;
	private ArrayList<AffairsMini> mList;
	private Activity activity;
	private final int maxNum = 3;
	
	public AffairItemLvAdapter(Context mContext,
			ArrayList<AffairsMini> mList,Activity activity) {
		super();
		this.mContext = mContext;
		this.mList = mList;
		this.activity= activity;
	}

	@Override
	public int getCount() {
		if (mList == null) {
			return 0;
		} else if(this.mList.size() <=maxNum){
			return this.mList.size();
		}else{
			return maxNum;
		}
	}

	@Override
	public Object getItem(int position) {
		if (mList == null) {
			return null;
		} else {
			return this.mList.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(this.mContext).inflate(
					R.layout.list_item_req_kno_details, null, false);
			holder.mTimeHolder = (TextView) convertView.findViewById(R.id.knoDItemTimeTv);
			holder.mTitleHolder = (TextView) convertView.findViewById(R.id.knoDItemTitleTv);
			holder.listItemReqKnowLL = (LinearLayout) convertView.findViewById(R.id.listItemReqKnowLL);
			holder.listItemReqKnoDetailsIv = (ImageView) convertView.findViewById(R.id.listItemReqKnoDetailsIv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.mTitleHolder.setText(mList.get(position).title);
		String mModifytime = mList.get(position).time;
		if (!TextUtils.isEmpty(mModifytime)) {
			/** 格式化时间 */
			mModifytime = JTDateUtils.formatDate(mModifytime,JTDateUtils.DATE_FORMAT_4);
		} else {
			mModifytime = "";
		}
		holder.mTimeHolder.setText(mModifytime);
		
		holder.listItemReqKnowLL.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ENavigate.startNeedDetailsActivity(activity, String.valueOf(mList.get(position).id), 1);
//				ENavigate.startRequirementDetailActivity(activity,mList.get(position).id); // 事件详情
			}
		});
		if((position+1) == getCount()){
			holder.listItemReqKnoDetailsIv.setVisibility(View.GONE);
		}else{
			holder.listItemReqKnoDetailsIv.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	private class ViewHolder {
		LinearLayout listItemReqKnowLL;
		TextView mTitleHolder;
		TextView mTimeHolder;
		ImageView listItemReqKnoDetailsIv;
	}

}
