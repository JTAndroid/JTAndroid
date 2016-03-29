package com.tongmeng.alliance.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tongmeng.alliance.dao.HotComment;
import com.tongmeng.alliance.dao.ServerResultDao;
import com.tongmeng.alliance.util.Constant;
import com.tongmeng.alliance.util.HttpRequestUtil;
import com.tongmeng.alliance.util.Utils;
import com.tr.R;

import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentLickAdapter extends BaseAdapter {

	private final Context context;
	private List<HotComment> list;
	private Callback callback;

	private DisplayImageOptions options = new DisplayImageOptions.Builder()
	.showImageOnLoading(R.drawable.default_picture_liebiao) // 设置图片下载期间显示的图片
	.showImageForEmptyUri(R.drawable.default_picture_liebiao) // 设置图片Uri为空或是错误的时候显示的图片
	.showImageOnFail(R.drawable.default_picture_liebiao) // 设置图片加载或解码过程中发生错误显示的图片
	.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
	.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
	.build();
	
	public interface Callback {
		public void myClick(View view);
	}

	public CommentLickAdapter(Context context, List<HotComment> list) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.activity_actiondetal_lsitviewitem, null);
			viewHolder.img = (ImageView) convertView
					.findViewById(R.id.activity_actiondetail_listviewItem_reviewerIconImg);
			viewHolder.likeImg = (ImageView) convertView
					.findViewById(R.id.activity_actiondetail_listviewItem_likeImg);
			viewHolder.nameText = (TextView) convertView
					.findViewById(R.id.activity_actiondetail_listviewItem_reviewerNameText);
			viewHolder.createTime = (TextView) convertView
					.findViewById(R.id.activity_actiondetail_listviewItem_reviewerTimeText);
			viewHolder.commentText = (TextView) convertView
					.findViewById(R.id.activity_actiondetail_listviewItem_reviewerContentText);
			viewHolder.lickNoText = (TextView) convertView
					.findViewById(R.id.activity_actiondetail_listviewItem_likeNoText);
			viewHolder.likeImg.setTag(0);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final HotComment dao = list.get(position);

		ImageLoader.getInstance().displayImage(dao.getUserPic(),
				viewHolder.img, options);
		
		viewHolder.nameText.setText(dao.getUserName());
		viewHolder.commentText.setText(dao.getComment());
		viewHolder.createTime.setText(dao.getCreateTime());
		viewHolder.lickNoText.setText(dao.getApprovalCount()+"");
		
		viewHolder.likeImg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				callback.myClick(view);
			}
		});
		return convertView;
	}
	
	public class ViewHolder {
		private ImageView img;
		private TextView nameText;
		private TextView createTime;
		private TextView commentText;
		private TextView lickNoText;
		private ImageView likeImg;
	}

}
	
	

