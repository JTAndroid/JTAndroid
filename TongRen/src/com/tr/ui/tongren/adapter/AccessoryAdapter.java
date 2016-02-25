package com.tr.ui.tongren.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tr.R;
import com.tr.ui.tongren.model.project.Resource;
import com.utils.common.EUtil;
import com.utils.time.TimeUtil;

public class AccessoryAdapter extends BaseAdapter{
	List<Resource> listResource  = new ArrayList<Resource>();
	private Context mContext;
	public AccessoryAdapter(Context context) {
		this.mContext = context;
	}
	public List<Resource> getListResource() {
		return listResource;
	}

	public void setListResource(List<Resource> listResource) {
		this.listResource = listResource;
	}

	@Override
	public int getCount() {
		return listResource.size();
	}

	@Override
	public Object getItem(int position) {
		return listResource.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		convertView = View.inflate(mContext, R.layout.adapter_accessory, null);
		TextView  accessoryName = (TextView) convertView.findViewById(R.id.accessoryName);
		TextView  accessoryFrom = (TextView) convertView.findViewById(R.id.accessoryFrom);
		TextView  accessoryTime = (TextView) convertView.findViewById(R.id.accessoryTime);
		ImageView accessoryTypeLogo = (ImageView) convertView.findViewById(R.id.accessoryTypeLogo);
		Resource resourceItem = (Resource) getItem(position);
		String suffixName = resourceItem.titleName.substring(resourceItem.titleName.lastIndexOf(".")+1, resourceItem.titleName.length()).toUpperCase();
		int fileSourceId = 0;// 资源文件ID
		if (EUtil.PIC_FILE_STR.contains(suffixName)) {// 图片
			fileSourceId = R.drawable.picture_fang;
		} else if (EUtil.VIDEO_FILE_STR.contains(suffixName)) {// 视频
			fileSourceId = R.drawable.video_fang;
		} else if ("PDF".contains(suffixName)) {//pdf文件
			fileSourceId = R.drawable.pdf_fang;
		} else if ("PPT".contains(suffixName) || "pptx".contains(suffixName)) {//ppt文件
			fileSourceId = R.drawable.ppt_fang;
		} else if ("XLS".contains(suffixName) || "XLSX".equals(suffixName)) {//excel文件
			fileSourceId = R.drawable.file_excel_fang;
		} else if (EUtil.DOC_FILE_STR.contains(suffixName)) {//word文档
			fileSourceId = R.drawable.word_fang;
		} else if (EUtil.AUDIO_FILE_STR.contains(suffixName)) {//音频文件
			fileSourceId = R.drawable.file_audio_fang;
		} else if ("ZIP".contains(suffixName) || "RAR".contains(suffixName)) {//压缩文件
			fileSourceId = R.drawable.file_zip;
		} else {
			fileSourceId = R.drawable.file_other;
		}
		accessoryTypeLogo.setImageResource(fileSourceId);
		accessoryName.setText(resourceItem.titleName);
		accessoryFrom.setText(resourceItem.createName);
		accessoryTime.setText(TimeUtil.TimeMillsToStringWithMinute(resourceItem.createTime));
		return convertView;
	}
	
}
