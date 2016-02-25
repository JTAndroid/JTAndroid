package com.tr.ui.adapter;

import java.util.ArrayList;
import java.util.List;
import com.tr.R;
import com.tr.model.obj.JTFile;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 文件列表适配器
 * @author leon
 */
public class FileAdapter extends BaseAdapter{

	private Context mContext;
	private List<JTFile> mListJTFile;
	
	public FileAdapter(Context context){
		mContext = context;
		mListJTFile = new ArrayList<JTFile>();
	}
	
	public FileAdapter(Context context,List<JTFile> listJTFile){
		mContext = context;
		if(listJTFile!=null){
			mListJTFile = listJTFile;
		}
		else{
			mListJTFile = new ArrayList<JTFile>();
		}
	}
	
	/**
	 * 更新适配器
	 * @param listJTFile
	 */
	public void updateAdapter(List<JTFile> listJTFile){
		if(listJTFile!= null){
			mListJTFile = listJTFile;
		}
		else{
			mListJTFile.clear();
		}
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListJTFile.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mListJTFile.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.list_item_file, parent, false);
			holder = new ViewHolder();
			holder.fileNameTv = (TextView) convertView.findViewById(R.id.fileNameTv);
			holder.createTimeTv = (TextView) convertView
					.findViewById(R.id.createTimeTv);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}
		initControls(holder,position);
		return convertView;
	}
	
	private void initControls(ViewHolder holder,int position){
		holder.fileNameTv.setText(mListJTFile.get(position).mFileName);
		holder.createTimeTv.setText(DateFormat.format("yyyy-MM-dd kk:mm:ss",
				mListJTFile.get(position).mCreateTime));
	};

	class ViewHolder{
		TextView fileNameTv;
		TextView createTimeTv;
	}
}
