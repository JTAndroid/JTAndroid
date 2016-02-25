package com.tr.ui.knowledge;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.R;
import com.tr.model.obj.Connections;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.adapter.KnoDetailsNoScrollGv;
import com.tr.ui.base.JBaseActivity;
import com.utils.common.EUtil;
/**
 * @ClassName      KnowledgePeopleInfoActivity.java
 * @Description    知识详情页面
 * @Author         CJJ
 */
public class KnowledgePeopleInfoActivity extends JBaseActivity{
	private final String TAG = getClass().getSimpleName();
	private KnowledgeRelatedAdapter mKnowledgeRelatedAdapter;
	private ArrayList<Connections> mList =new ArrayList<Connections>();
	private String mTitle;
	private KnoDetailsNoScrollGv mGridView;
	private Context mContext;
	private TextView mKnowTitle;
	@Override
	public void initJabActionBar() {
	}
	public KnowledgePeopleInfoActivity() {
		super();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_know_people_info);
		initComponent();
		initVar();
	}


	private void initVar() {
		this.mTitle = getIntent().getStringExtra("titleInfo");
		this.mList = (ArrayList<Connections>) getIntent().getSerializableExtra("Connections");
		mKnowTitle.setText(mTitle);
	}
	private void initComponent() {
		mKnowledgeRelatedAdapter = new KnowledgeRelatedAdapter();
		mGridView = (KnoDetailsNoScrollGv) findViewById(R.id.knoPeopleItemGv);
		mGridView.setAdapter(mKnowledgeRelatedAdapter);
		mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mKnowTitle = (TextView)findViewById(R.id.knoPeopleTv);
	}
	
	class KnowledgeRelatedAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.grid_item_kno_details, null, false);
				holder.faceIv = (ImageView) convertView
						.findViewById(R.id.knoDetailGvIv);
				holder.knoDetailIv = (TextView) convertView
						.findViewById(R.id.knoDetailIv);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (mList != null) {
				if(null != mList.get(position).getJtContactMini()){
					holder.knoDetailIv.setText(mList.get(position).getJtContactMini().getName());
				}
				if(null != mList.get(position).getOrganizationMini()){
					holder.knoDetailIv.setText(mList.get(position).getOrganizationMini().getFullName());
				}
				if (holder.faceIv != null) {
					String imageUrl = mList.get(position).getImage();
					if(!TextUtils.isEmpty(imageUrl)){
						ImageLoader.getInstance().displayImage(imageUrl, holder.faceIv);
					}else{
						if("2".equals(mList.get(position).getType())){/*如果type为2则代表组织*/
							holder.faceIv.setBackgroundResource(R.drawable.ic_organization);
						}
					}
					holder.faceIv.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
//							ENavigate.startConnectionsDetailActivity(KnowledgePeopleInfoActivity.this
//									,mList.get(position)); // 人脉和组织详情 
							long id = EUtil.isEmpty(mList.get(position).getId()) ?  0L : Long.valueOf(mList.get(position).getId());
							int type = mList.get(position).type;
							if(type==Connections.type_org ){
								long creaetById = EUtil.isEmpty(mList.get(position).getOrganizationMini().getOwnerid()) ?  0L :Long.valueOf(mList.get(position).getOrganizationMini().getOwnerid());
								ENavigate.startOrgMyHomePageActivity(KnowledgePeopleInfoActivity.this, id,creaetById,true, ENavConsts.type_details_org);
							}else if(type == Connections.type_persion ){
								ENavigate.startContactsDetailsActivity(KnowledgePeopleInfoActivity.this, 2, id, 210);
							}
						}
					});
				}
			}
			return convertView;
		}
		private class ViewHolder {
			ImageView faceIv;
			TextView knoDetailIv;
		}
	}
}
