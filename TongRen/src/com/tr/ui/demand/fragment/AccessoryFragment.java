package com.tr.ui.demand.fragment;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.R;
import com.tr.ui.common.view.XListView;
import com.tr.ui.demand.AccessoryActivity;
import com.utils.common.EUtil;
import com.utils.http.IBindData;

/**
 * @ClassName: NeedCommentFragment.java
 * @author ZCS
 * @Date 2015年3月10日 下午2:10:14
 * @Description: 附件-全部-文档-压缩
 */
public class AccessoryFragment extends Fragment implements OnClickListener,
		IBindData {

	private Context cxt;
	private View rootView;
	private XListView infoLv;
	protected String TAG = "NeedCommentFragment";
	private Myadapter adapter;
	private TextView sizeTv;

	private List<String> fileList = new ArrayList<String>();

	public enum TypeDocument {
		all, document, accessory
	}

	public AccessoryFragment(Context cxt, TypeDocument type,TextView sizeTv) {
		this.cxt = cxt;
		setData(type);
		this.sizeTv = sizeTv;
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.demand_accessory_all_view_pager,
				container, false);
		// commentCountTv = (TextView)
		// rootView.findViewById(R.id.commentCountTv);
		infoLv = (XListView) rootView.findViewById(R.id.infoLv);
		initData();
		return rootView;
	}

	private void setData(TypeDocument type) {
		if(AccessoryActivity.fileAllList==null||AccessoryActivity.fileAllList.size()<=0){
			return;
		}
		switch (type) {
		case all:// 全部
			for (String string : AccessoryActivity.fileAllList) {
				fileList.add(string);
			}
			break;

		case document:// 文档
			for (String string : AccessoryActivity.fileAllList) {
				if (string
						.matches("^.*?\\.(doc|docx|ppt|pptx|pdf|txt|xls|xlsx)$")) {
					fileList.add(string);
				}
			}
			break;
		case accessory:// 压缩文件
			for (String string : AccessoryActivity.fileAllList) {
				if (string.matches("^.*?\\.(rar|zip|7z)$")) {
					fileList.add(string);
				}
			}
			break;
		}
	}

	private void initData() {
		adapter = new Myadapter();
		infoLv.showFooterView(false);
		// 设置xlistview可以加载、刷新
		infoLv.setPullLoadEnable(false);
		infoLv.setPullRefreshEnable(false);
		infoLv.setAdapter(adapter);
		//点击条目时
		infoLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				View v = view.findViewById(R.id.rightCb);
				View v2 = view.findViewById(R.id.documentSizeTv);
				String path = (String) v.getTag();
				long size = (Long) v2.getTag();
				if (v.isSelected()) {//如果已选中
					AccessoryActivity.fileSelectList.remove(path);
					AccessoryActivity.countSize=AccessoryActivity.countSize-size<0?0:AccessoryActivity.countSize-size;
				} else {
					if(AccessoryActivity.countSize+size>1024*1024*100){//100兆
						Toast.makeText(getActivity(), "超过100M", 0).show();
						return;
					}else if(size>20*1024*1024){//大于20兆
						Toast.makeText(getActivity(), "大于20M", 0).show();
						return;
					}
					AccessoryActivity.countSize+=size;
					AccessoryActivity.fileSelectList.add(path);
				}
				sizeTv.setText("已选 "+fileSize(AccessoryActivity.countSize));
				adapter.notifyDataSetChanged();

			}
		});
	}

	public void refreshData() {
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}

	class Myadapter extends BaseAdapter {

		@Override
		public int getCount() {
			return fileList==null?0:fileList.size();
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
			ViewHoulderItem vh;
			if (convertView == null) {
				vh = new ViewHoulderItem();
				convertView = View.inflate(getActivity(),
						R.layout.demand_accessory_all_item, null);
				vh.documentImgIv = (ImageView) convertView.findViewById(R.id.documentImgIv);
				vh.documentNameTv = (TextView) convertView.findViewById(R.id.documentNameTv);
				vh.documentSizeTv = (TextView) convertView.findViewById(R.id.documentSizeTv);
				vh.rightCb = (ImageView) convertView.findViewById(R.id.rightCb);
				convertView.setTag(vh);
			}
			vh = (ViewHoulderItem) convertView.getTag();
			String path = fileList.get(position);
			String[] pathsub = path.split("/");
			String[] suf = pathsub[pathsub.length-1].split("\\.");
			String suffixName = suf[suf.length-1].toUpperCase();
			
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
			vh.documentImgIv.setBackgroundResource(fileSourceId);
			
//			int index = 4;
//			if (path.matches("^.*?\\.(xls|xlsx)$")) {
//				index = 0;
//			} else if (path.matches("^.*?\\.(ppt|pptx)$")) {
//				index = 1;
//				vh.documentImgIv.getDrawable().setLevel(1);// 设置显示的图片
//			} else if (path.matches("^.*?\\.(doc|docx)$")) {
//				index = 2;
//			} else if (path.matches("^.*?\\.pdf$")) {
//				index = 3;
//			} else if (path.matches("^.*?\\.txt$")) {
//				index = 4;
//			} else if (path.matches("^.*?\\.(zip|rar|7z)$")) {
//				index = 5;
//			}
//			vh.documentImgIv.getDrawable().setLevel(index);// 设置显示的图片
			vh.rightCb.setSelected(AccessoryActivity.fileSelectList.contains(path) ? true : false);
			vh.documentNameTv
					.setText(path.substring(path.lastIndexOf('/') + 1));
			long size = new File(path).length();
			vh.documentSizeTv.setText(fileSize(size));
			vh.documentSizeTv.setTag(size);
			vh.rightCb.setTag(path);// 保存路徑
			return convertView;
		}

	}

	private String fileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("0.00");
		String str = fileS < 1024 ? df.format((double) fileS) + "B"
				: fileS < 1048576 ? df.format((double) fileS / 1024) + "K"
						: fileS < 1073741824 ? df
								.format((double) fileS / 1048576) + "M" : df
								.format((double) fileS / 1073741824) + "G";
		return str;
	}

	class ViewHoulderItem {
		ImageView documentImgIv;
		TextView documentNameTv;
		TextView documentSizeTv;
		ImageView rightCb;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.submitTv:// 回复按钮
			/*
			 * Toast.makeText(cxt.getApplicationContext(), "完成",
			 * Toast.LENGTH_SHORT).show();
			 */
			/*
			 * botemDefaultLl.setVisibility(View.VISIBLE);
			 * replyLl.setVisibility(View.GONE);
			 */
			break;
		}

	}

	@Override
	public void bindData(int tag, Object object) {
		// TODO Auto-generated method stub

	}

}
