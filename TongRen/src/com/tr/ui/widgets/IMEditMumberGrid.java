package com.tr.ui.widgets;

import java.io.Serializable;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tr.R;
import com.tr.model.obj.ConnectionsMini;
import com.utils.common.Util;

/**
 * This class is inherit from GridView, overriding onMeasure method to remove
 * scroll function in this gridview , and to avoid only one row displayed when
 * embedded in a ScrollView control.
 * 
 * @author yingjun.bai@renren-inc.com
 */
public class IMEditMumberGrid extends GridView {
	public final static String IME_ADD = "-100";// 增加
	public final static String IME_REMOVE = "-200";// 移除
//控制最后item添加的显隐 ：1从事务跳转过来
	public static int eStartIMGroupChatType = 0;

	public IMEditMumberGrid(Context context) {
		super(context);
	}

	public IMEditMumberGrid(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setData(List<HeadName> data) {
		ImageAdapter adapter = (ImageAdapter) this.getAdapter();
		adapter.setData(data);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_MOVE)
			return true;
		return super.dispatchTouchEvent(ev);
	}

	/** 选人用的 */
	public static class ImageAdapter extends BaseAdapter {
		private int headInOneRow;
		private int allItemCount;
		private List<HeadName> data;
		Context mContext = null;
		boolean removeState = false;

		public ImageAdapter(Context c, List<HeadName> data, int headInOneRow, int type) {
			mContext = c;
			this.headInOneRow = headInOneRow;
			this.data = data;
			eStartIMGroupChatType = type;
			populateData();
		}

		public void setData(List<HeadName> data) {
			this.data = data;
			populateData();
			this.notifyDataSetChanged();
		}

		public void setRemoveState(boolean removeState) {
			this.removeState = removeState;
		}

		public boolean getRemoveState() {
			return removeState;
		}

		private void populateData() {
			data.add(new HeadName(R.drawable.chat_add, IME_ADD));// add friend
			// data.add(new HeadName(R.drawable.im_add_merchant_icon,
			// IME_REMOVE));
		}

		public int getCount() {
			
			return 1==eStartIMGroupChatType?data.size()-1:data.size();
		}

		public Object getItem(int position) {
			return data.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.im_editmumber_grid_item, null);

				holder = new ViewHolder();
				holder.imageView = (ImageView) convertView.findViewById(R.id.im_detai_grid_item_image);
				holder.textView = (TextView) convertView.findViewById(R.id.im_detail_grid_item_label);
				holder.deleteImg = (ImageView) convertView.findViewById(R.id.im_detail_grid_delete_image);
				holder.rl_tag = (RelativeLayout) convertView.findViewById(R.id.rl_tag);
				holder.tagIv = (ImageView) convertView.findViewById(R.id.im_detai_grid_item_tagIv);
				holder.tagTv = (TextView) convertView.findViewById(R.id.im_detai_grid_item_tagTv);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			HeadName item = data.get(position);
			int len = getCount();
			if (eStartIMGroupChatType !=1 ) {
				len= getCount() - 1;
			}
			if (position < (len)) {

				holder.imageView.setVisibility(View.VISIBLE);
				holder.textView.setVisibility(View.VISIBLE);
				holder.textView.setText(item.getName());
				if(item.isCreater()){
					holder.rl_tag.setVisibility(View.VISIBLE);
					holder.tagIv.setImageResource(R.drawable.detail_create);
					holder.tagTv.setText("创建者");
				}else if(item.IsFriend()){
					holder.rl_tag.setVisibility(View.VISIBLE);
					holder.tagIv.setImageResource(R.drawable.detail_friend);
					holder.tagTv.setText("好友");
				}else{
					holder.rl_tag.setVisibility(View.GONE);
				}
				
				if (removeState) {
					// Display delete icon
					holder.deleteImg.setVisibility(View.VISIBLE);
					// remove add friend icon from the gridview
					if (item.getHeadId() == R.drawable.im_add_persion) {
						holder.imageView.setVisibility(View.GONE);
						holder.textView.setVisibility(View.GONE);
						holder.deleteImg.setVisibility(View.GONE);
					}
				} else {
					holder.deleteImg.setVisibility(View.GONE);
					convertView.setVisibility(View.VISIBLE);
				}
			} else {
				holder.imageView.setVisibility(View.VISIBLE);
				holder.textView.setVisibility(View.GONE);
				holder.deleteImg.setVisibility(View.GONE);
			}
//			holder.imageView.setAdjustViewBounds(true);
//			holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			if (position == (data.size() - 1)) {
				holder.rl_tag.setVisibility(View.GONE);
				if (eStartIMGroupChatType != 1) {
					holder.imageView.setImageResource(R.drawable.hy_add_attendee_normal);
					holder.imageView.setVisibility(View.VISIBLE);
				} else {
					holder.imageView.setVisibility(View.GONE);
				}
			} else {
				Log.d("xmx","pic im name:"+item.getName()+",pic:"+item.getImage());
				Util.initAvatarImage(mContext, holder.imageView, item.getName(), item.getImage(), 1, 1);
			}

			return convertView;
		}

		class ViewHolder {
			public ImageView imageView;
			public TextView textView;
			public ImageView deleteImg;
			public RelativeLayout rl_tag;
			public ImageView tagIv;
			public TextView tagTv;

		}
	}

	public static class HeadName implements Serializable{

		int imageID;// 本地图片id，默认为-1，此时表示用网络图片
		String name;
		String image;
		String userID;
		boolean isFriend;
		boolean isCreater;
		int type;

		public HeadName(int imageID, String name) {
			this.imageID = imageID;// R.drawable.ic_add_pic;
			this.image = "";
			this.userID = IME_ADD;
			this.name = name;
		}
		
		public HeadName() {
			super();
			// TODO Auto-generated constructor stub
		}

		public HeadName(ConnectionsMini mini) {
			imageID = 0;
			this.image = mini.getImage();
			this.userID = mini.getId();
			this.name = mini.getName();
			this.isFriend=mini.getFriendRelation()==null?false:mini.getFriendRelation();
		}

		public int getImageID() {
			return imageID;
		}

		public void setImageID(int imageID) {
			this.imageID = imageID;
		}

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public String getUserID() {
			return userID;
		}

		public void setUserID(String userID) {
			this.userID = userID;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getHeadId() {
			return imageID;
		}

		public String getName() {
			return name;
		}
		
		public void setIsFriend(boolean isFriend){
			this.isFriend = isFriend;
		}
		
		public boolean IsFriend(){
			return isFriend;
		}

		public boolean isCreater() {
			return isCreater;
		}

		public void setCreater(boolean isCreater) {
			this.isCreater = isCreater;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}
		
	}

}
