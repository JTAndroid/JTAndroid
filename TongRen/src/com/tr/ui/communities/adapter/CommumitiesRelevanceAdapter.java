package com.tr.ui.communities.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.R;
import com.tr.model.demand.DemandASSOData;
import com.tr.ui.communities.ModulesType;
import com.tr.ui.widgets.title.menu.popupwindow.ViewHolder;
import com.utils.common.Util;

/**
 * 社群详情关联的数据人脉/公司/知识/需求adapter
 * 
 */
public class CommumitiesRelevanceAdapter extends BaseAdapter {

	private Context mContext;
	private List<DemandASSOData> conn;
	private ModulesType mModulesType;

	public CommumitiesRelevanceAdapter(Context context, ModulesType mModulesType) {
		this.mContext = context;
		this.mModulesType = mModulesType;
	}

	@Override
	public int getCount() {
		return null != conn ? conn.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setData(List<DemandASSOData> conn) {
		if (this.conn != null)
			this.conn.clear();
		if (null != conn)
			this.conn = conn;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DemandASSOData adata = conn.get(position);
		if (convertView == null)
			convertView = View.inflate(mContext, R.layout.adapter_commumitiescontent, null);
		ImageView people_type = ViewHolder.get(convertView, R.id.people_type);// 类别
																				// 默认隐藏
		ImageView imageView = ViewHolder.get(convertView, R.id.picture_Iv);// 头像
		TextView title = ViewHolder.get(convertView, R.id.contentNameTv);// 标题
		TextView content = ViewHolder.get(convertView, R.id.text_contentTv);// 内容
																			// 默认隐藏

		LinearLayout layout_TimeTV = ViewHolder.get(convertView, R.id.layout_TimeTV);// 时间、地址|行业
																						// 默认隐藏

		TextView time = ViewHolder.get(convertView, R.id.commumitiesNumTv);// 时间
																			// 默认隐藏因为DemandASSOData就没有时间这个字段
		TextView addressAndHy = ViewHolder.get(convertView, R.id.addressAndHy);// 地址|行业
		switch (this.mModulesType) {
		case PeopleModules:// 人脉、好友（标题是 name属性 内容是公司加职位，其他隐藏）
			people_type.setVisibility(View.VISIBLE);
			content.setVisibility(View.VISIBLE);
			if (adata.type == 2)// 人脉
				people_type.setImageResource(R.drawable.contactpeopletag);
			if (adata.type == 3)
				people_type.setImageResource(R.drawable.contactfriendtag);
			title.setText(adata.name);
			content.setText(adata.company + adata.career);
			ImageLoader.getInstance().displayImage(adata.picPath, imageView);
			break;
		case OrgAndCustomModules:// 企业即组织（标题是 name属性 内容是行业，其他隐藏）
			people_type.setVisibility(View.VISIBLE);
			content.setVisibility(View.VISIBLE);
			if (adata.type == 4)// 组织
				people_type.setImageResource(R.drawable.contactorganizationtag);
			if (adata.type == 5)// 客户
				people_type.setImageResource(R.drawable.contactclienttag);
			title.setText(adata.name);
			content.setText(adata.hy);
			ImageLoader.getInstance().displayImage(adata.picPath, imageView);
			break;
		case DemandModules:// 1融资事件，0投资事件（标题是 title属性 title下是时间
							// 、地址|行业，其他隐藏为空的也隐藏）
			people_type.setVisibility(View.VISIBLE);
			layout_TimeTV.setVisibility(View.VISIBLE);
			if (adata.type == 1) {// 融资事件
				initMyimage(imageView, "融资", 1);
				people_type.setImageResource(R.drawable.demand_me_need02);
			}
			if (adata.type == 0) {// 投资事件
				initMyimage(imageView, "投资", 0);
				people_type.setImageResource(R.drawable.demand_me_need01);
			}
			title.setText(adata.title);

			if (TextUtils.isEmpty(adata.address) && TextUtils.isEmpty(adata.hy))
				layout_TimeTV.setVisibility(View.GONE);
			if (!TextUtils.isEmpty(adata.address) && !TextUtils.isEmpty(adata.hy))
				addressAndHy.setText(adata.address + "|" + adata.hy);
			else
				addressAndHy.setText(adata.address + adata.hy);
			break;
		case KnowledgeModules:// 知识（标题是 title属性 title下是内容 、时间 、地址|行业，其他隐藏为空的也隐藏）
			// DemandASSOData就没有知识的内容字段故不显示内容,时间字段也没有故不显示
			imageView.setVisibility(View.GONE);
			title.setText(adata.title);
			
			break;

		default:
			break;
		}

		return convertView;
	}

	private void initMyimage(ImageView avatarIv, String name, int type) {
		Bitmap bm = null;
		int resid = 0;
		if (type == 1) {// 融资
			resid = R.drawable.no_avatar_but_gender;// 粉色
		} else if (type == 0) {// 投资
			resid = R.drawable.no_avatar_client_organization;// 浅蓝色
		}
		bm = Util.createBGBItmap(mContext, resid, R.color.avatar_text_color, R.dimen.avatar_text_size, name);
		avatarIv.setImageBitmap(bm);
	}

}
