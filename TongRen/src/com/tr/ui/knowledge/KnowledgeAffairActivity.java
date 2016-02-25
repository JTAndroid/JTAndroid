package com.tr.ui.knowledge;

import java.util.ArrayList;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.R;
import com.tr.model.obj.AffairsMini;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.widgets.BasicListView;
import com.utils.common.JTDateUtils;

/**
 * @ClassName AffairAndKnowledgeActivity.java
 * @Description 知识详情页面--显示全部事件
 * @Author CJJ
 */
public class KnowledgeAffairActivity extends JBaseActivity {
	private final String TAG = getClass().getSimpleName();
	private KnowledgeRelatedAdapter mKnowledgeRelatedAdapter;
	private ArrayList<AffairsMini> mAffairsMiniList = new ArrayList<AffairsMini>();
	private String mTitle;
	private BasicListView affairLv;
	private TextView mKnowAffairTitle;

	@Override
	public void initJabActionBar() {
	}

	public KnowledgeAffairActivity() {
		super();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_know_affair_info);
		initComponent();
		initVar();
	}

	private void initVar() {
		this.mTitle = getIntent().getStringExtra("titleInfo");
		this.mAffairsMiniList = (ArrayList<AffairsMini>) getIntent()
				.getSerializableExtra("TYPE_AFFAIR");
		mKnowAffairTitle.setText(mTitle);
		affairLv.setAdapter(mKnowledgeRelatedAdapter);
	}

	private void initComponent() {
		mKnowAffairTitle = (TextView) findViewById(R.id.knoAffairTv);
		affairLv = (BasicListView) findViewById(R.id.knoAffairItemLv);
		mKnowledgeRelatedAdapter = new KnowledgeRelatedAdapter();
	}

	class KnowledgeRelatedAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mAffairsMiniList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mAffairsMiniList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(KnowledgeAffairActivity.this)
						.inflate(R.layout.list_item_know_affairs, null, false);
				holder.knowTitileIv = (TextView) convertView
						.findViewById(R.id.knowAffairItemTitleTv);
				holder.knowTiemIv = (TextView) convertView
						.findViewById(R.id.knowAffairItemTimeTv);
				holder.listItemAffairKnowLL = (LinearLayout) convertView
						.findViewById(R.id.listItemAffairKnowLL);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
	
			if (mAffairsMiniList != null) {
				String mModifytime = mAffairsMiniList.get(position).time;
				String title = mAffairsMiniList.get(position).title;
				if(!TextUtils.isEmpty(title)){
					holder.knowTitileIv.setText(title);
				}else{
					holder.knowTitileIv.setText("");
				}
				if(!TextUtils.isEmpty(mModifytime)){
					holder.knowTiemIv.setText(JTDateUtils.getTimewithHHmm(mModifytime));
				}else{
					holder.knowTiemIv.setText("");
				}
				holder.listItemAffairKnowLL
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								ENavigate.startNeedDetailsActivity(getApplicationContext(),String.valueOf(mAffairsMiniList.get(position).id),1); // 事件详情
							}
						});
			}
			return convertView;
		}

		private class ViewHolder {
			LinearLayout listItemAffairKnowLL;
			TextView knowTitileIv;
			TextView knowTiemIv;
		}
	}
}
