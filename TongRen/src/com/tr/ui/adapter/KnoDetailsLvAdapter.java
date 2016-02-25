package com.tr.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tr.R;
import com.tr.model.joint.AffairNode;
import com.tr.model.joint.ConnectionNode;
import com.tr.model.joint.KnowledgeNode;
import com.tr.model.knowledge.KnowledgeDetailsLv;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.model.obj.AffairsMini;
import com.tr.model.obj.Connections;
import com.tr.ui.knowledge.KnowledgeAffairActivity;
import com.tr.ui.knowledge.KnowledgeDetailFragment;
import com.tr.ui.knowledge.KnowledgeMiniActivity;
import com.tr.ui.knowledge.KnowledgePeopleInfoActivity;
import com.tr.ui.widgets.BasicListView;

/**
 * @ClassName: KnoDetailsLvAdapter.java
 * @Description: 知识详情页列表适配器
 * @Author CJJ
 * @Version v 1.0
 * @Created 2014-11-04
 * @Updated 2014-11-11
 */
public class KnoDetailsLvAdapter extends BaseAdapter implements OnClickListener {

	/** 变量 */
	private List<KnowledgeDetailsLv> mKnowDetails = new ArrayList<KnowledgeDetailsLv>();
	private Context mContext;
	private LayoutInflater inflater;
	private Integer TYPE_COUNT = 4;
	private KnoDetailsItemGvAdapter connAdapter;
	private KnoDetailsItemGvAdapter orgAdapter;
	private ImageView mShowAllInfoIv;//
	private ArrayList<Connections> mConnectionList;
	private Activity activity;
	private boolean createByMySelf;

	public KnoDetailsLvAdapter(List<KnowledgeDetailsLv> mKnowDetails,
			Context mContext) {
		super();
		this.mKnowDetails = mKnowDetails;
		this.mContext = mContext;
	}

	public void setKnowDetailsList(List<KnowledgeDetailsLv> mKnowDetails,boolean createdByMySelf,
			Activity activity) {
		this.mKnowDetails = mKnowDetails;
		this.activity = activity;
		this.createByMySelf = createdByMySelf;
	}

	@Override
	public int getCount() {
		if (mKnowDetails == null) {
			return 0;
		} else {
			return this.mKnowDetails.size();
		}
	}

	@Override
	public Object getItem(int position) {
		if (mKnowDetails == null) {
			return null;
		} else {
			return this.mKnowDetails.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		return mKnowDetails.get(position).getType();
	}

	@Override
	public int getViewTypeCount() {
		return TYPE_COUNT + 1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolderPic holderPic = null;
		ViewHolderWord holderWord = null;

		int type = getItemViewType(position);
		if (convertView == null) {
			holderPic = new ViewHolderPic();
			holderWord = new ViewHolderWord();
			inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.list_item_kno_details,
					null, false);
			// 按当前所需的样式，确定new的布局
			switch (type) {
			case KnowledgeDetailFragment.TYPE_CONNECTIONS:
				holderPic.gridViewHolder = (GridView) convertView
						.findViewById(R.id.knoDetailsItemGv);
				holderPic.mTitle = (TextView) convertView
						.findViewById(R.id.knoDetailsItemTv);
				holderPic.mArrowIv = (ImageView) convertView
						.findViewById(R.id.knowArrowIv);
				holderPic.mGoKnowDetailCommentRl = (LinearLayout) convertView
						.findViewById(R.id.goKnowDetailCommentRl);
				convertView.setTag(holderPic);
				convertView.findViewById(R.id.knoDetailsItemLv).setVisibility(
						View.GONE);
				break;
			case KnowledgeDetailFragment.TYPE_ORGANIZATION:
				holderPic.gridViewHolder = (GridView) convertView
						.findViewById(R.id.knoDetailsItemGv);
				holderPic.mTitle = (TextView) convertView
						.findViewById(R.id.knoDetailsItemTv);
				holderPic.mArrowIv = (ImageView) convertView
						.findViewById(R.id.knowArrowIv);
				holderPic.mGoKnowDetailCommentRl = (LinearLayout) convertView
						.findViewById(R.id.goKnowDetailCommentRl);
				convertView.setTag(holderPic);
				convertView.findViewById(R.id.knoDetailsItemLv).setVisibility(
						View.GONE);
				break;
			case KnowledgeDetailFragment.TYPE_KNOWLEDGE:
				holderWord.listViewHolder = (BasicListView) convertView
						.findViewById(R.id.knoDetailsItemLv);
				holderWord.listViewHolder.setHaveScrollbar(false);
				holderWord.listViewHolder.setItemsCanFocus(false);
				holderWord.mTitle = (TextView) convertView
						.findViewById(R.id.knoDetailsItemTv);
				holderWord.mArrowIv = (ImageView) convertView
						.findViewById(R.id.knowArrowIv);
				holderWord.mGoKnowDetailCommentRl = (LinearLayout) convertView
						.findViewById(R.id.goKnowDetailCommentRl);
				convertView.setTag(holderWord);
				convertView.findViewById(R.id.knoDetailsItemGv).setVisibility(
						View.GONE);
				break;
			case KnowledgeDetailFragment.TYPE_AFFAIR:
				holderWord.listViewHolder = (BasicListView) convertView
						.findViewById(R.id.knoDetailsItemLv);
				holderWord.listViewHolder.setHaveScrollbar(false);
				holderWord.listViewHolder.setItemsCanFocus(false);
				holderWord.mTitle = (TextView) convertView
						.findViewById(R.id.knoDetailsItemTv);
				holderWord.mArrowIv = (ImageView) convertView
						.findViewById(R.id.knowArrowIv);
				holderWord.mGoKnowDetailCommentRl = (LinearLayout) convertView
						.findViewById(R.id.goKnowDetailCommentRl);
				convertView.setTag(holderWord);
				convertView.findViewById(R.id.knoDetailsItemGv).setVisibility(
						View.GONE);
				break;
			default:
				break;
			}
		} else {
			switch (type) {
			case KnowledgeDetailFragment.TYPE_CONNECTIONS:
				holderPic = (ViewHolderPic) convertView.getTag();
				break;
			case KnowledgeDetailFragment.TYPE_ORGANIZATION:
				holderPic = (ViewHolderPic) convertView.getTag();
				break;
			case KnowledgeDetailFragment.TYPE_KNOWLEDGE:
				holderWord = (ViewHolderWord) convertView.getTag();
				break;
			case KnowledgeDetailFragment.TYPE_AFFAIR:
				holderWord = (ViewHolderWord) convertView.getTag();
				break;
			}
		}
		// 设置资源
		switch (type) {
		case KnowledgeDetailFragment.TYPE_CONNECTIONS:
			holderPic.mTitle.setText(mKnowDetails.get(position)
					.getRelatedPeopleList().getMemo());
			final ConnectionNode connectionNode = mKnowDetails.get(position)
					.getRelatedPeopleList();
			connAdapter = new KnoDetailsItemGvAdapter(mContext,
					connectionNode.getListConnections(),activity);
			connAdapter.setCreateByMySelf(createByMySelf);
			holderPic.gridViewHolder.setAdapter(connAdapter);
			mShowAllInfoIv = holderPic.mArrowIv;
			final String conTitle = holderPic.mTitle.getText().toString();
			holderPic.mGoKnowDetailCommentRl
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							startActivity(connectionNode.getListConnections(),
									KnowledgeDetailFragment.TYPE_CONNECTIONS,
									conTitle);
						}
					});
			break;
		case KnowledgeDetailFragment.TYPE_ORGANIZATION:
			holderPic.mTitle.setText(mKnowDetails.get(position)
					.getRelatedOrganizationList().getMemo());
			final String orgTitle = mKnowDetails.get(position)
					.getRelatedOrganizationList().getMemo();
			final ConnectionNode organiNode = mKnowDetails.get(position)
					.getRelatedOrganizationList();
			mConnectionList = organiNode.getListConnections();
			mShowAllInfoIv = holderPic.mArrowIv;
			holderPic.mGoKnowDetailCommentRl
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							startActivity(organiNode.getListConnections(),
									KnowledgeDetailFragment.TYPE_ORGANIZATION,
									orgTitle);
						}
					});
			orgAdapter = new KnoDetailsItemGvAdapter(mContext,
					organiNode.getListConnections(), activity);
			holderPic.gridViewHolder.setAdapter(orgAdapter);
			break;
		case KnowledgeDetailFragment.TYPE_KNOWLEDGE:
			holderWord.mTitle.setText(mKnowDetails.get(position)
					.getRelatedKnowledgeList().getMemo());
			final String knowTitle = mKnowDetails.get(position)
					.getRelatedKnowledgeList().getMemo();
			final KnowledgeNode knowledgeNode = mKnowDetails.get(position)
					.getRelatedKnowledgeList();
			KnoDetailsItemLvAdapter knoAdapter = new KnoDetailsItemLvAdapter(
					mContext, knowledgeNode.getListKnowledgeMini2(), activity);
			if(null != knowledgeNode.getListKnowledgeMini2() && knowledgeNode.getListKnowledgeMini2().size() >= 3 ){
				holderWord.mArrowIv.setVisibility(View.VISIBLE);
				holderWord.mGoKnowDetailCommentRl
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						startActivity(
								knowledgeNode.getListKnowledgeMini2(),
								KnowledgeDetailFragment.TYPE_KNOWLEDGE,
								knowTitle);
					}
				});
			}else{
				holderWord.mArrowIv.setVisibility(View.GONE);
			}
			holderWord.listViewHolder.setAdapter(knoAdapter);
			break;
		case KnowledgeDetailFragment.TYPE_AFFAIR:
			holderWord.mTitle.setText(mKnowDetails.get(position)
					.getRelatedAffairList().getMemo());
			final String affairTitle = mKnowDetails.get(position)
					.getRelatedAffairList().getMemo();
			final AffairNode affairNode = mKnowDetails.get(position)
					.getRelatedAffairList();
			AffairItemLvAdapter affairAdapter = new AffairItemLvAdapter(
					mContext, affairNode.getListAffairMini(), activity);
			if(null != affairNode.getListAffairMini() && affairNode.getListAffairMini().size() >= 3){
				holderWord.mArrowIv.setVisibility(View.VISIBLE);
				holderWord.mGoKnowDetailCommentRl
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						startActivity(
								affairNode.getListAffairMini(),
								KnowledgeDetailFragment.TYPE_AFFAIR,
								affairTitle);
					}
				});
			}else{
				holderWord.mArrowIv.setVisibility(View.GONE);
			}
			holderWord.listViewHolder.setAdapter(affairAdapter);
			break;
		}
		return convertView;
	}

	private class ViewHolderPic {
		ImageView mArrowIv;
		TextView mTitle;
		LinearLayout mGoKnowDetailCommentRl;
		GridView gridViewHolder;
	}

	private class ViewHolderWord {
		ImageView mArrowIv;
		TextView mTitle;
		LinearLayout mGoKnowDetailCommentRl;
		BasicListView listViewHolder;
	}

	/**
	 * 跳转到展示页面
	 * 
	 * @param mList
	 * @param type
	 */
	public void startActivity(Object mList, int type, String title) {
		Intent intent = null;
		switch (type) {
		case KnowledgeDetailFragment.TYPE_CONNECTIONS:
			intent = new Intent(mContext, KnowledgePeopleInfoActivity.class);
			intent.putExtra("Connections", (ArrayList<Connections>) mList);
			break;
		case KnowledgeDetailFragment.TYPE_ORGANIZATION:
			intent = new Intent(mContext, KnowledgePeopleInfoActivity.class);
			intent.putExtra("Connections", (ArrayList<Connections>) mList);
			break;
		case KnowledgeDetailFragment.TYPE_KNOWLEDGE:
			intent = new Intent(mContext, KnowledgeMiniActivity.class);
			intent.putExtra("TYPE_KNOWLEDGE", (ArrayList<KnowledgeMini2>) mList);
			break;
		case KnowledgeDetailFragment.TYPE_AFFAIR:
			intent = new Intent(mContext, KnowledgeAffairActivity.class);
			intent.putExtra("TYPE_AFFAIR", (ArrayList<AffairsMini>) mList);
			break;
		default:
			break;
		}
		intent.putExtra("titleInfo", title);
		mContext.startActivity(intent);
	}

	@Override
	public void onClick(View arg0) {
	}
}
