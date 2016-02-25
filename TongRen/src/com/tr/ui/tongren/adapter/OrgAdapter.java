package com.tr.ui.tongren.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tr.R;
import com.tr.image.ImageLoader;
import com.tr.ui.tongren.model.project.Organization;

public class OrgAdapter extends BaseAdapter {

	private Context mContext;
	private List<Organization> orgs = new ArrayList<Organization>();
	public OrgAdapter(Context context, List<Organization> orgs){
		this.mContext = context;
		this.orgs = orgs;
	}
	
	public void clear(){
		this.orgs.clear();
		notifyDataSetChanged();
	}
	
	public void setOrgList(List<Organization> orgs){
		this.orgs = orgs;
	}
	
	@Override
	public int getCount() {
		return orgs.size();
	}

	@Override
	public Organization getItem(int position) {
		return orgs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Organization org = orgs.get(position);
		final ItemHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.tongren_org_item, null);
			holder = new ItemHolder();
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.title_rl = (RelativeLayout) convertView.findViewById(R.id.title_rl);
			holder.orgDetail_rl = (RelativeLayout) convertView.findViewById(R.id.orgDetail_rl);
			holder.orgIv = (ImageView) convertView.findViewById(R.id.orgIv);
			holder.orgName = (TextView) convertView.findViewById(R.id.orgName);
			holder.orgCount = (TextView) convertView.findViewById(R.id.orgCount);
			holder.isCheck = (CheckBox) convertView.findViewById(R.id.isCheck);
			convertView.setTag(holder);
		}else{
			holder = (ItemHolder) convertView.getTag();
		}
		if(org.getName().equals("创建")){
			holder.title_rl.setVisibility(View.VISIBLE);
			holder.orgDetail_rl.setVisibility(View.GONE);
			holder.title.setText("我创建的组织");
		}else if(org.getName().equals("参与")){
			holder.title_rl.setVisibility(View.VISIBLE);
			holder.orgDetail_rl.setVisibility(View.GONE);
			holder.title.setText("我参与的组织");
		}else{
			holder.title_rl.setVisibility(View.GONE);
			holder.orgDetail_rl.setVisibility(View.VISIBLE);
			ImageLoader.load(holder.orgIv, org.getPath(), R.drawable.ic_default_avatar);
			holder.orgName.setText(org.getName());
			holder.orgCount.setText(org.getMemberSize()+"人");
		}
		return convertView;
	}

	class ItemHolder {
		public ImageView orgIv;
		public TextView orgName;
		public TextView orgCount;
		public CheckBox isCheck;
		public TextView title;
		public RelativeLayout title_rl,orgDetail_rl;
	}

}