package com.tr.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import com.tr.App;
import com.tr.R;
import com.tr.model.model.PeopleWorkExperience;
import com.tr.ui.communities.model.Community;
import com.tr.ui.flow.CreateFlowActivtiy;
import com.tr.ui.home.frg.HomePageFrag;
import com.tr.ui.people.model.WorkExperience;
import com.tr.ui.widgets.title.menu.popupwindow.ViewHolder;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * @ClassName: WorkExperienceAdapter
 * @Description: 个人主页中工作经历适配器
 * @author cui
 * @date 2015-11-26 下午4:01:13
 * 
 */
public class WorkExperienceAdapter extends BaseAdapter {
	private Context mContext;
	private List<WorkExperience> list = new ArrayList<WorkExperience>();
	private Boolean ISNULL = false;
	private Boolean ISEIDT = false;// 是否可进行编辑的
	OnEditExperience editExperience = null;
    private ScrollView scrollView;
    private ListView listView;
    private int itemheight;
    private Handler mHandler;
	public void setOnEditExperience(OnEditExperience onEditExperience) {
		this.editExperience = onEditExperience;
	}

	public WorkExperienceAdapter(Context context, List<WorkExperience> items) {
		this.mContext = context;
		this.list = items;
	}

	@Override
	public int getCount() {
		if ((list != null ? list.size() : 0) == 0) {
			ISNULL = true;
			return 1;
		} else {
			ISNULL = false;
			return list.size();
		}
	}

	public void isNull(Boolean is) {
		this.ISNULL = is;
	}

	public void isEdit(Boolean is) {
		this.ISEIDT = is;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.list_item_homepage, null);
		}
		if (ISNULL) {
			ViewHolder.get(convertView, R.id.layout_has).setVisibility(View.GONE);
//			ViewHolder.get(convertView, R.id.text_isnull).setVisibility(View.VISIBLE);
		} else {
			WorkExperience workExperience = list.get(position);
			ViewHolder.get(convertView, R.id.layout_has).setVisibility(View.VISIBLE);
			ViewHolder.get(convertView, R.id.text_isnull).setVisibility(View.GONE);
			TextView text_edit = ViewHolder.get(convertView, R.id.text_edit);
			if (ISEIDT) {
				text_edit.setVisibility(View.VISIBLE);
				if (editExperience != null) {
					text_edit.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							editExperience.onCompleted(position, list.get(position));

						}
					});
				}
			}

			ImageView image_logo = ViewHolder.get(convertView, R.id.image_logo);
			image_logo.setBackgroundResource(R.drawable.icon_company);
			// TODO 加载图片 url 现在无
			// ImageLoader.getInstance().displayImage(uri, image_logo);
			TextView text_address = ViewHolder.get(convertView, R.id.text_address);
			text_address.setText(workExperience.getCompany());

			TextView text_state = ViewHolder.get(convertView, R.id.text_state);
			String state = "";
			if(!TextUtils.isEmpty(workExperience.getsTime())){
				 state = workExperience.getsTime();
			}
			if(!TextUtils.isEmpty(workExperience.geteTime())){
				state = state + "-" + workExperience.geteTime();
			}
			state = state + "\u3000" + workExperience.getPosition();
			text_state.setText(state);

			// 工作简介没有
			final TextView text_brief_introduction = ViewHolder.get(convertView, R.id.text_brief_introduction);
			final TextView see_moreTv = ViewHolder.get(convertView, R.id.see_moreTv);
//			see_moreTv.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					Message msg=mHandler.obtainMessage();
//					msg.what=1;
//					if(see_moreTv.getText().toString().equals("查看更多")){
//						see_moreTv.setText("收起");
//						text_brief_introduction.setEllipsize(null);
//						text_brief_introduction.setSingleLine(false);
//					}else{
//						see_moreTv.setText("查看更多");
//						text_brief_introduction.setEllipsize(TextUtils.TruncateAt.END); // 收缩
//						text_brief_introduction.setMaxLines(3);
//					}
//					mHandler.sendMessage(msg);
//				}
//			});
			String des = workExperience.getDesc();
			if (TextUtils.isEmpty(des)) {
				text_brief_introduction.setVisibility(View.GONE);
			} else {
				text_brief_introduction.setVisibility(View.VISIBLE);
				text_brief_introduction.setText(des);
//				if(des.length()>100){
//					see_moreTv.setVisibility(View.VISIBLE);
//					if(see_moreTv.getText().toString().equals("查看更多")){
//						text_brief_introduction.setEllipsize(TextUtils.TruncateAt.END); // 收缩
//						text_brief_introduction.setMaxLines(3);
//					}else{
//						text_brief_introduction.setEllipsize(null);
//						text_brief_introduction.setSingleLine(false);
//					}
//				}
//				else
//					see_moreTv.setVisibility(View.GONE);
			}
		}

		return convertView;
	}

	public void addWorkExperience(List<WorkExperience> mlist) {
		if (this.list != null)
			this.list.clear();
		this.list.addAll(mlist);
		notifyDataSetChanged();
	}

	public interface OnEditExperience {
		void onCompleted(int position, Object object);
	}

	public void parentView(View v) {
		this.scrollView = (ScrollView) v;
	}
	public void listView(View v) {
		this.listView = (ListView) v;
	}
	public void setItemHeight(int  height) {
		this.itemheight = height;
	}
	public void setHandler(Handler  handler) {
		this.mHandler = handler;
	}
}
