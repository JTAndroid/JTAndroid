package com.tr.ui.im;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tr.R;
import com.tr.image.ImageLoader;
import com.tr.model.obj.ConnectionsMini;
import com.tr.model.obj.MUCDetail;
import com.tr.navigate.ENavConsts;
import com.tr.ui.base.JBaseActivity;
import com.utils.string.StringUtils;

public class AtInformFriendsActivity extends JBaseActivity {

	@ViewInject(R.id.lv_at_friends)
	private ListView lvAtFriends;

	@ViewInject(R.id.et_presearch_name)
	private EditText etPresearchName;

	/** 不变的群好友列表 */
	private List<ConnectionsMini> fixedConnectionsMinis;
	/** 变动的 */
	private List<ConnectionsMini> connectionsMinis;

	/** 本次索引与上一次的索引 */
	private String lastIndex;
	private String currIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frg_at_infrom_friends);
		ViewUtils.inject(this);

		MUCDetail mucDetail = IMChatMessageCache.getInstance().getMucDetailCache();
		fixedConnectionsMinis = mucDetail.getListConnectionsMini();
		SortListContants(fixedConnectionsMinis);

		connectionsMinis = fixedConnectionsMinis;

		lvAtFriends.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent data = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable(ENavConsts.EConnectionsMini, connectionsMinis.get(position));
				data.putExtras(bundle);
				setResult(ENavConsts.ActivityReqCode.REQUEST_CODE_FOR_AT_FRIENDS, data);
				finish();
			}
		});

		etPresearchName.addTextChangedListener(searchWatcher);

		adapter = new AtFriendsAdapter();
		lvAtFriends.setAdapter(adapter);
	}

	private TextWatcher searchWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if (s.length() == 0) {
				connectionsMinis = fixedConnectionsMinis;
				adapter.notifyDataSetChanged();
			}
			else {
				connectionsMinis = new ArrayList<ConnectionsMini>();
				for (ConnectionsMini mini : fixedConnectionsMinis) {
					if (mini.getName().contains(etPresearchName.getText().toString())) {
						connectionsMinis.add(mini);
					}
				}
				adapter.notifyDataSetChanged();
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
		}
	};

	private AtFriendsAdapter adapter;

	@Override
	public void initJabActionBar() {
		android.app.ActionBar actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowHomeEnabled(true);
		actionbar.setTitle("选择要@的人");
	}

	private class AtFriendsAdapter extends BaseAdapter {

		Holder holder = null;

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return connectionsMinis.size();
		}

		@Override
		public Object getItem(int position) {
			return connectionsMinis.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				holder = new Holder();
				convertView = View.inflate(AtInformFriendsActivity.this, R.layout.list_item_at_a_friend, null);
				holder.ll_index = (LinearLayout) convertView.findViewById(R.id.ll_index);
				holder.tv_index = (TextView) convertView.findViewById(R.id.tv_index);
				holder.tv_friend_name = (TextView) convertView.findViewById(R.id.tv_friend_name);
				holder.iv_friend_avatar = (ImageView) convertView.findViewById(R.id.iv_friend_avatar);
				convertView.setTag(holder);
			}
			else {
				holder = (Holder) convertView.getTag();
			}
			// 设置tag
			holder.iv_friend_avatar.setTag(connectionsMinis.get(position).getId());
			currIndex = connectionsMinis.get(position).getFirstIndex();
			if (!StringUtils.isEmpty(connectionsMinis.get(position).getName())) {
				holder.tv_friend_name.setText(connectionsMinis.get(position).getName());
			}
			else {
				holder.tv_friend_name.setText("");
			}
			if (!StringUtils.isEmpty(connectionsMinis.get(position).getImage())) {
				ImageLoader.load(holder.iv_friend_avatar, connectionsMinis.get(position).getImage(), R.drawable.default_people_avatar);
			}
			else {
				holder.iv_friend_avatar.setImageResource(R.drawable.default_people_avatar);
			}

			if (StringUtils.isEmpty(lastIndex)) {
				holder.ll_index.setVisibility(View.VISIBLE);
				holder.tv_index.setText("A");
				lastIndex = "A";
			}
			else {
				if (!StringUtils.isEmpty(connectionsMinis.get(position).getFirstIndex())) {
					currIndex = connectionsMinis.get(position).getFirstIndex();
					if (currIndex.equalsIgnoreCase(lastIndex)) {
						holder.ll_index.setVisibility(View.GONE);
					}
					else {
						holder.ll_index.setVisibility(View.VISIBLE);
						lastIndex = currIndex;
						holder.tv_index.setText(currIndex.toUpperCase());
					}
				}
				else {
					holder.ll_index.setVisibility(View.GONE);
				}
			}

			return convertView;
		}

	}

	private class Holder {
		private LinearLayout ll_index;
		private TextView tv_index;
		private TextView tv_friend_name;
		private ImageView iv_friend_avatar;
	}

	/** 按照字母顺序对联系人排序 */
	private void SortListContants(List<ConnectionsMini> connectionsMinis) {
		Collections.sort(connectionsMinis);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		IMChatMessageCache.getInstance().releaseCache();
	}

}
