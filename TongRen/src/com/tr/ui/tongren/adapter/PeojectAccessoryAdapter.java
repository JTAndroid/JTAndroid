package com.tr.ui.tongren.adapter;

import java.util.ArrayList;
import java.util.List;

import com.tr.R;
import com.tr.ui.tongren.model.project.ProjectFile;
import com.tr.ui.tongren.model.project.Resource;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PeojectAccessoryAdapter extends BaseAdapter{
	List<ProjectFile> listResource  = new ArrayList<ProjectFile>();
	private Context mContext;
	public PeojectAccessoryAdapter(Context context) {
		this.mContext = context;
	}
	public List<ProjectFile> getListResource() {
		return listResource;
	}

	public void setListResource(List<ProjectFile> listResource) {
		this.listResource = listResource;
	}

	@Override
	public int getCount() {
		return listResource.size();
	}

	@Override
	public ProjectFile getItem(int position) {
		return listResource.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView  = View.inflate(mContext, R.layout.adapter_projectassessorg, null);
		TextView accessoryName =   (TextView) convertView.findViewById(R.id.accessoryName);
		accessoryName.setText(getItem(position).fileIndex.fileTitle);
		return convertView;
	}

}
