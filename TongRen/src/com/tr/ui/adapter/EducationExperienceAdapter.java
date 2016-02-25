package com.tr.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tr.R;
import com.tr.ui.people.model.Education;
import com.tr.ui.widgets.title.menu.popupwindow.ViewHolder;

/**
 * @ClassName: WorkExperienceAdapter
 * @Description: 个人主页中教育经历适配器
 * @author cui
 * @date 2015-11-26 下午4:01:13
 * 
 */
public class EducationExperienceAdapter extends BaseAdapter {
	private Context mcContext;
	private List<Education> list = new ArrayList<Education>();
	private Boolean ISNULL = false;
	private Boolean ISEIDT = false;// 是否可进行编辑的
	OnEditExperience editExperience = null;

	public void setOnEditExperience(OnEditExperience onEditExperience) {
		this.editExperience = onEditExperience;
	}

	public EducationExperienceAdapter(Context context, List<Education> items) {
		this.mcContext = context;
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
			convertView = View.inflate(mcContext, R.layout.list_item_homepage, null);
		}
		if (ISNULL) {
			ViewHolder.get(convertView, R.id.layout_has).setVisibility(View.GONE);
//			ViewHolder.get(convertView, R.id.text_isnull).setVisibility(View.VISIBLE);
		} else {
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
			image_logo.setBackgroundResource(R.drawable.icon_school);
			// TODO 加载图片 url 现在无
			// ImageLoader.getInstance().displayImage(uri, image_logo);
			TextView text_address = ViewHolder.get(convertView, R.id.text_address);
			text_address.setText(list.get(position).school);

			TextView text_state = ViewHolder.get(convertView, R.id.text_state);
			String state = list.get(position).stime + "-" + list.get(position).etime + "\u3000" + list.get(position).educationalBackgroundType;
			text_state.setText(state);

			TextView text_brief_introduction = ViewHolder.get(convertView, R.id.text_brief_introduction);
			if (TextUtils.isEmpty(list.get(position).desc))
				text_brief_introduction.setVisibility(View.GONE);
			else {
				text_brief_introduction.setVisibility(View.VISIBLE);
				text_brief_introduction.setText(list.get(position).desc);
			}
		}

		return convertView;
	}

	public void addEducationExperience(List<Education> mlist) {
		if (this.list != null)
			this.list.clear();
		this.list.addAll(mlist);
		notifyDataSetChanged();
	}

	public interface OnEditExperience {
		void onCompleted(int position, Object object);
	}
}
