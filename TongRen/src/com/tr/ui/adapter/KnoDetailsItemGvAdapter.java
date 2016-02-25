package com.tr.ui.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import cn.sharesdk.wechat.utils.j;

import com.hp.hpl.sparta.Text;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.tr.R;
import com.tr.model.obj.Connections;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.utils.common.EUtil;
import com.utils.image.LoadImage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @ClassName:     KnoDetailsItemGvAdapter.java
 * @Description:   知识详情页列表适配器
 * @Author         CJJ
 * @Version        v 1.0  
 * @Created        2014-11-04
 * @Updated 	   2014-11-11
 */
public class KnoDetailsItemGvAdapter extends BaseAdapter {

	/** 变量 */
	private Context mContext;
	private ArrayList<Connections> mList;
	private final int maxNum = 5;
	private Activity activity;
	private boolean createByMySelf;

	public KnoDetailsItemGvAdapter(Context mContext,
			ArrayList<Connections> mList, Activity activity) {
		super();
		this.mContext = mContext;
		this.mList = mList;
		this.activity = activity;
	}
	
	public void setCreateByMySelf(boolean createByMySelf) {
		this.createByMySelf = createByMySelf;
	}


	@Override
	public int getCount() {
		if (mList == null) {
			return 0;
		} else if (this.mList.size() <= maxNum) {
			return this.mList.size();
		} else {
			return maxNum;
		}
	}

	@Override
	public Object getItem(int position) {
		if (this.mList == null) {
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
					R.layout.grid_item_kno_details, null, false);
			holder.faceIv = (ImageView) convertView
					.findViewById(R.id.knoDetailGvIv);
			holder.knoDetailIv = (TextView) convertView
					.findViewById(R.id.knoDetailIv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (this.mList != null) {
			if(null != this.mList.get(position).getJtContactMini()){
				holder.knoDetailIv.setText(this.mList.get(position).getJtContactMini().getName().trim());
			}
			if(null != this.mList.get(position).getOrganizationMini()){
				holder.knoDetailIv.setText(this.mList.get(position).getOrganizationMini().getFullName().trim());
			}
			if (holder.faceIv != null) {
				String imageUrl = this.mList.get(position).getImage();
				if(!TextUtils.isEmpty(imageUrl)){
					ImageLoader.getInstance().displayImage(imageUrl, holder.faceIv,new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
							.bitmapConfig(Bitmap.Config.RGB_565).displayer(new RoundedBitmapDisplayer(10))
							.showImageOnFail(R.drawable.ic_know_people).build());
				}else{
					if("2".equals(mList.get(position).getType())){/*如果type为2则代表组织*/
//						holder.faceIv.setBackgroundResource(R.drawable.ic_organization);
						holder.faceIv.setBackgroundResource(R.drawable.default_portrait116);
					}
				}
				try {
					holder.faceIv.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							long id = EUtil.isEmpty(mList.get(position).getId()) ?  0L : Long.valueOf(mList.get(position).getId());
							int type = mList.get(position).type;
							Connections connections = (Connections) getItem(position);
							if(type==Connections.type_org ){
								long creaetById = EUtil.isEmpty(mList.get(position).getOrganizationMini().getOwnerid()) ?  0L :Long.valueOf(mList.get(position).getOrganizationMini().getOwnerid());
								if (!connections.getOrganizationMini().isOnline) {
									ENavigate.startClientDedailsActivity(activity, id, 1, 6);
								}else{
									ENavigate.startOrgMyHomePageActivity(activity, id,creaetById,true, ENavConsts.type_details_org);
	
								}
							}else if(type == Connections.type_persion ){
								
								if (!connections.getJtContactMini().isOnline) {
									ENavigate.startRelationHomeActivity(
											activity, id+"",false,2);
								}
								else {
									ENavigate.startRelationHomeActivity(
											activity, id+"",true,1);
								}
							}
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return convertView;
	}

	private class ViewHolder {
		ImageView faceIv;
		TextView knoDetailIv;
	}
}
