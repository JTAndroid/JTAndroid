package com.tr.ui.conference.initiatorhy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tr.R;
import com.tr.model.conference.MMeetingTopicQuery;
import com.tr.model.obj.JTContactMini;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.conference.utile.PeopleOrgknowleRequirmentLayoutUtil;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.image.AnimateFirstDisplayListener;
import com.utils.string.StringUtils;
import com.utils.time.Util;

public class MeetingSpeakerListActivity extends JBaseActivity implements OnItemClickListener {

	private Context mContext;
	private ListView speakerListView;
	private List<JTContactMini> speakerList;
	private AnimateFirstDisplayListener animateFirstDisplayListener;
	private DisplayImageOptions options;
	private SpeakerListAdapter adapter;
	private int clickPosition = -1;

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "主讲人列表", false, null,true, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.hy_speakerlist_activity);
		speakerListView = (ListView) findViewById(R.id.speakerlistView);
		initData();
	}

	private void initData() {
		speakerList = new ArrayList<JTContactMini>();
		Iterator<Entry<String, JTContactMini>> iterator = InitiatorDataCache.getInstance().inviteSpeakerSelectedMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, JTContactMini> entry = iterator.next();
			JTContactMini value = entry.getValue();
			if (value != null) {
				//初始化一个默认的议题
				if (value.lisMeetingTopicQuery.size()==0) {
					value.lisMeetingTopicQuery.add(new MMeetingTopicQuery());
				}
				speakerList.add(value);
			}
		}
		adapter = new SpeakerListAdapter(speakerList);
		speakerListView.setAdapter(adapter);
		speakerListView.setOnItemClickListener(this);
	}

	private class SpeakerListAdapter extends BaseAdapter {
		List<JTContactMini> speakerList = new ArrayList<JTContactMini>();

		public SpeakerListAdapter(List<JTContactMini> speakerList) {
			super();
			this.speakerList = speakerList;
		}

		public void setData(List<JTContactMini> speakerList) {
			this.speakerList = speakerList;
		}

		@Override
		public int getCount() {
			return speakerList.size();
		}

		@Override
		public JTContactMini getItem(int position) {
			return speakerList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = View.inflate(mContext, R.layout.hy_speakerlist_item, null);
				viewHolder = new ViewHolder();
				viewHolder.speakerPicIv = (ImageView) convertView.findViewById(R.id.speakerPicIv);
				viewHolder.speakerNameTv = (TextView) convertView.findViewById(R.id.speakerNameTv);
				convertView.setTag(viewHolder);
			}
			viewHolder = (ViewHolder) convertView.getTag();
			JTContactMini jtContactMini = speakerList.get(position);
			viewHolder.speakerNameTv.setText(jtContactMini.getName());
			if (options == null) {
				options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.default_people_avatar).showImageForEmptyUri(R.drawable.default_people_avatar)
						.showImageOnFail(R.drawable.default_people_avatar).cacheInMemory(true).cacheOnDisc(true).considerExifParams(false).build();
			}
			if (animateFirstDisplayListener == null) {
				animateFirstDisplayListener = new AnimateFirstDisplayListener();
			}
			
			com.utils.common.Util.initAvatarImage(mContext, viewHolder.speakerPicIv,jtContactMini.getName(), jtContactMini.getImage(), jtContactMini.getGender(),1);
//			ImageLoader.getInstance().displayImage(jtContactMini.getImage(), viewHolder.speakerPicIv, options, animateFirstDisplayListener);
			return convertView;
		}
	}

	private class ViewHolder {
		ImageView speakerPicIv;
		TextView speakerNameTv;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		clickPosition = position;
		ENavigate.startMeetingSpeakerTopicSettingActivity(this, adapter.getItem(position));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && data != null) {
			JTContactMini speakerContact = (JTContactMini) data.getSerializableExtra("speakerContact");
			if (speakerContact != null && clickPosition != -1 && speakerList.get(clickPosition).getId().equals(speakerContact.getId())) {
				speakerList.set(clickPosition, speakerContact);
				adapter.setData(speakerList);
				adapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem item = menu.add(0, 101, 0, "完成");
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (101 == item.getItemId()) {
			//检查有没有议题未填写名称
			for (int i = 0; i < speakerList.size(); i++) {
				JTContactMini jtContactMini = speakerList.get(i);
				List<MMeetingTopicQuery> lisMeetingTopicQuery = jtContactMini.lisMeetingTopicQuery;
				for (MMeetingTopicQuery mMeetingTopicQuery : lisMeetingTopicQuery) {
					if (StringUtils.isEmpty(mMeetingTopicQuery.getTopicContent())) {
						String name = jtContactMini.getName();
						Toast.makeText(mContext, "主讲人:"+name+" 有未填写名称的议题", 1).show();
						return true;
					}
				}
			}
			// 将list中的值赋值给map
			InitiatorDataCache.getInstance().inviteSpeakerSelectedMap.clear();
			for (int i = 0; i < speakerList.size(); i++) {
				if (speakerList.get(i) != null) {
					InitiatorDataCache.getInstance().inviteSpeakerSelectedMap.put(speakerList.get(i).getId(), speakerList.get(i));
				}
			}
			Util.activitySetResult(this, InitiatorHYActivity.class);
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

}
