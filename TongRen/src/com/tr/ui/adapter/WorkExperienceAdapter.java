package com.tr.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tr.R;
import com.tr.ui.people.model.WorkExperience;
import com.tr.ui.widgets.ExpandableTextView;
import com.tr.ui.widgets.title.menu.popupwindow.ViewHolder;

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
	private final SparseBooleanArray mCollapsedStatus;
	public void setOnEditExperience(OnEditExperience onEditExperience) {
		this.editExperience = onEditExperience;
	}

	public WorkExperienceAdapter(Context context, List<WorkExperience> items) {
		this.mContext = context;
		this.mCollapsedStatus = new SparseBooleanArray();
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
			// 工作简介
			ExpandableTextView expandableTextView=ViewHolder.get(convertView, R.id.expand_text_view);
			expandableTextView.setText(workExperience.desc, mCollapsedStatus, position);
			
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

}
