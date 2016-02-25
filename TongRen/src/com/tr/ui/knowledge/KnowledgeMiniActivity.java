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
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.model.obj.AffairsMini;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.widgets.BasicListView;
import com.utils.common.JTDateUtils;

/**
 * @ClassName KnowledgeMiniActivity.java
 * @Description 知识详情
 * @Author CJJ
 */
public class KnowledgeMiniActivity extends JBaseActivity {
	private final String TAG = getClass().getSimpleName();
	private KnowledgeRelatedAdapter mKnowledgeRelatedAdapter;
	private ArrayList<KnowledgeMini2> mKnowMiniList = new ArrayList<KnowledgeMini2>();
	private String mTitle;
	private BasicListView affairLv;
	private TextView mKnowAffairTitle;

	@Override
	public void initJabActionBar() {
	}

	public KnowledgeMiniActivity() {
		super();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_know_mini_info);
		initComponent();
		initVar();
	}

	private void initVar() {
		this.mTitle = getIntent().getStringExtra("titleInfo");
		this.mKnowMiniList = (ArrayList<KnowledgeMini2>) getIntent()
				.getSerializableExtra("TYPE_KNOWLEDGE");
		if(null == mKnowMiniList){
			mKnowMiniList = new ArrayList<KnowledgeMini2>();
		}
		mKnowAffairTitle.setText(mTitle);
		affairLv.setAdapter(mKnowledgeRelatedAdapter);
	}

	private void initComponent() {
		mKnowAffairTitle = (TextView) findViewById(R.id.knoMiniTv);
		affairLv = (BasicListView) findViewById(R.id.knoMiniItemLv);
		mKnowledgeRelatedAdapter = new KnowledgeRelatedAdapter();
	}

	class KnowledgeRelatedAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mKnowMiniList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mKnowMiniList.get(arg0);
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
				convertView = LayoutInflater.from(KnowledgeMiniActivity.this)
						.inflate(R.layout.list_item_know_mini, null, false);
				holder.knowTitileIv = (TextView) convertView
						.findViewById(R.id.knowItemTitleMiniTv);
				holder.knowTiemIv = (TextView) convertView
						.findViewById(R.id.knowItemTimeMiniTv);
				holder.listItemKnowMiniLL = (LinearLayout) convertView
						.findViewById(R.id.listItemKnowMiniLL);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
	
			if (mKnowMiniList != null) {
				String mModifytime = mKnowMiniList.get(position).modifytime;
				String title = mKnowMiniList.get(position).title;
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
				holder.listItemKnowMiniLL
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								ENavigate.startKnowledgeOfDetailActivity(KnowledgeMiniActivity.this,mKnowMiniList.get(position).id,mKnowMiniList.get(position).type);// 知识详情 
							}
						});
			}
			return convertView;
		}

		private class ViewHolder {
			LinearLayout listItemKnowMiniLL;
			TextView knowTitileIv;
			TextView knowTiemIv;
		}
	}
}
