package com.tr.ui.home.frg;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tr.R;
import com.tr.model.obj.Connections;
import com.tr.ui.widgets.CircleImageView;
import com.utils.common.Util;
import com.utils.string.StringUtils;
/**
 * 通讯录列表ContactsAdapter
 * @author zhongshan
 *
 */
public class ContactsAdapter extends BaseAdapter{
	private Context mContext;
	private ArrayList<Connections> listConnections = new ArrayList<Connections>();
	
	public ArrayList<Connections> getListConnections() {
		return listConnections;
	}

	public void setListConnections(ArrayList<Connections> listConnections) {
		this.listConnections = listConnections;
		notifyDataSetChanged();
	}
	
	public ContactsAdapter(Context context,ArrayList<Connections> listConnections) {
		super();
		this.listConnections = listConnections;
		this.mContext = context;
	}


	@Override
	public int getCount() {
		return listConnections.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Connections connections = listConnections.get(position);
		String mNicks = "";
		mNicks = connections.getName();
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.fragment_contacts_main_listview_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tag = (TextView) convertView.findViewById(R.id.contactGroupTitleTv);
			viewHolder.nameTv = (TextView) convertView.findViewById(R.id.nameTv);
			viewHolder.avatarIv = (CircleImageView) convertView.findViewById(R.id.avatarIv);
			convertView.setTag(viewHolder);
		}
		viewHolder = (ViewHolder) convertView.getTag();
		viewHolder = (ViewHolder) convertView.getTag();
		String lastCatalog = null;
		String catalog = "";
		if (position == 0 && !StringUtils.isEmpty(mNicks)) {
			viewHolder.tag.setVisibility(View.VISIBLE);
			catalog = connections.getCharName() + "";
			viewHolder.tag.setText(catalog);
		} else if (position != 0 && !StringUtils.isEmpty(mNicks)) {
			catalog = connections.getCharName() + "";
			//上一个首字母
			lastCatalog = listConnections.get(position - 1).getCharName() + "";

			if (catalog.equals(lastCatalog)) {
				viewHolder.tag.setVisibility(View.GONE);
			} else {
				viewHolder.tag.setVisibility(View.VISIBLE);
				viewHolder.tag.setText(catalog);
			}
		} else {
			viewHolder.tag.setVisibility(View.GONE);
		}

		viewHolder.nameTv.setText(connections.getName());

		if (connections.type == Connections.type_persion) {
			Util.initAvatarImage(mContext, viewHolder.avatarIv, "", connections.getImage(), 0, 1);
		} else if (connections.type == Connections.type_org) {
			Util.initAvatarImage(mContext, viewHolder.avatarIv, "", connections.getImage(), 0, 2);
		}
		return convertView;
	}

	class ViewHolder {
		TextView tag;// 标头
		TextView nameTv;// 名字
		CircleImageView avatarIv;// 现在圆角的头像
	}
	}
