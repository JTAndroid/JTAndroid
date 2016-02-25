package com.tr.ui.demand.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tr.R;
import com.tr.api.DemandReqUtil;
import com.tr.model.demand.DemandComment;
import com.tr.model.demand.DemandCommentListItem;
import com.tr.model.page.JTPage;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.common.view.MyXListView;
import com.tr.ui.common.view.MyXListView.IXListViewListener;
import com.tr.ui.demand.NeedDetailsActivity;
import com.tr.ui.demand.util.OnNeedDetails;
import com.tr.ui.demand.util.OnNeedRefresh;
import com.tr.ui.demand.util.TextStrUtil;
import com.utils.common.ViewHolder;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.time.TimeUtil;

/**
 * @ClassName: NeedCommentFragment.java
 * @author ZCS
 * @Date 2015年3月10日 下午2:10:14
 * @Description: 需求评论
 */
public class NeedCommentFragment extends JBaseFragment implements
		OnClickListener, IBindData, OnNeedRefresh {
	private Context cxt;
	private View rootView;
	private MyXListView infoLv;
	private JTPage jtpage;// 分页加载对象
	private Myadapter adapter;
	/**
	 * 评论总数
	 */
	private TextView commentCountTv;
	private String demandId;// 需求详情的id
	private List<DemandComment> demandComment = new ArrayList<DemandComment>();
	private EditText demandCommonEt;
	private CheckBox visableCb;// 仅创建者可见
	private OnNeedDetails onNeed;
	private Button submitTv;
	
	public NeedCommentFragment(Context cxt, String demandId,
			OnNeedDetails onNeed) {
		this.cxt = cxt;
		this.onNeed = onNeed;
		this.demandId = demandId;
		this.onNeed.getNeedRefresh(this, 1);
	}
	public NeedCommentFragment(
			) {
	}
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (!isVisibleToUser && getActivity() != null) {
			InputMethodManager imm = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imm != null) {
				imm.hideSoftInputFromWindow(getActivity().getWindow()
						.getDecorView().getWindowToken(), 0);
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.demand_need_comment_view_pager,
				container, false);
		commentCountTv = (TextView) rootView.findViewById(R.id.commentCountTv);
		infoLv = (MyXListView) rootView.findViewById(R.id.infoLv);
		FragmentActivity activity = getActivity();
		if (activity instanceof NeedDetailsActivity) {
			infoLv.KnowledegeShow = true;
		}
		demandCommonEt = (EditText) rootView.findViewById(R.id.demandCommonEt);
		rootView.findViewById(R.id.phoneIv).setOnClickListener(this);// 电话
		rootView.findViewById(R.id.botemDefaultLl).setVisibility(View.GONE);// 消息及电话
		rootView.findViewById(R.id.replyLl).setVisibility(View.VISIBLE);// 回复
		submitTv = (Button) rootView.findViewById(R.id.submitTv);
		submitTv.setOnClickListener(this);
		submitTv.setEnabled(false);
		submitTv.setPadding(12, 4, 12, 4);
		visableCb = (CheckBox) rootView.findViewById(R.id.visableCb);
		initData();
		demandCommonEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (isComment) { // true 的时候能发评论，再判断是否输入了数据
					String keyWordStr = demandCommonEt.getText().toString()
							.trim();
					if (TextUtils.isEmpty(keyWordStr)) {
						submitTv.setEnabled(false);
						submitTv.setBackgroundResource(R.drawable.comment_send);
					} else {
						submitTv.setEnabled(true);
						submitTv.setBackgroundResource(R.drawable.comment_send1);
					}
				}
			}
		});
		return rootView;
	}

	private void initData() {
		adapter = new Myadapter();
		commentCountTv.setText("评论");
		infoLv.showFooterView(false);
		// 设置xlistview可以加载、刷新
		infoLv.setPullLoadEnable(true);
		infoLv.setPullRefreshEnable(true);
		infoLv.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				DemandReqUtil.getDemandCommentList(cxt,
						NeedCommentFragment.this, demandId, 0, 20, null);
			}

			@Override
			public void onLoadMore() {
				startGetData();
			}
		});

		infoLv.setAdapter(adapter);
		startGetData();
	}

	/**
	 * 获取页数据
	 */
	public void startGetData() {
		int nowIndex = 0;
		if (jtpage != null) {
			nowIndex = jtpage.getIndex() + 1;
		} else {
			showLoadingDialog("");
		}
		DemandReqUtil.getDemandCommentList(cxt, this, demandId, nowIndex, 20,
				null);
	}

	class Myadapter extends BaseAdapter {

		@Override
		public int getCount() {
			return demandComment != null ? demandComment.size() : 0;
		}

		@Override
		public DemandComment getItem(int position) {
			return demandComment.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(getActivity(),
						R.layout.demand_need_details_discuss, null);
			}
			ImageView headIv = ViewHolder.get(convertView, R.id.headIv);
			TextView nameTv = ViewHolder.get(convertView, R.id.nameTv);
			TextView contentTv = ViewHolder.get(convertView, R.id.contentTv);
			TextView commentTimeTv = ViewHolder.get(convertView,
					R.id.commentTimeTv);
			DemandComment comment = getItem(position);
			commentTimeTv.setText(TimeUtil.TimeFormat(comment.createTime));
			contentTv.setText(comment.content);
			ImageLoader.getInstance().displayImage(
					comment.picPath,
					headIv,
					new DisplayImageOptions.Builder()
							.bitmapConfig(Bitmap.Config.RGB_565)
							.cacheInMemory(true).cacheOnDisc(true)
							/*.displayer(new RoundedBitmapDisplayer(10))*/
							.showImageOnFail(R.drawable.ic_default_avatar)
							.build());
			nameTv.setText(comment.createrName);
			return convertView;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.submitTv:// 回复按钮
			showLoadingDialog("");
			InputMethodManager m = (InputMethodManager) getActivity()
					.getSystemService(Activity.INPUT_METHOD_SERVICE);
			m.hideSoftInputFromWindow(
					demandCommonEt.getApplicationWindowToken(), 0);
			DemandReqUtil.addDemandComment(cxt, this, demandId, visableCb
					.isChecked() == true ? 1 : 0, demandCommonEt.getText()
					.toString(), null);
			break;
		}
	}

	private boolean isComment = true;// 是否允许发送

	@Override
	public void bindData(int tag, Object object) {
		infoLv.stopLoadMore();
		infoLv.stopRefresh();
		infoLv.showFooterView(false);
		dismissLoadingDialog();
		if (object == null) {
//			isComment = false;// 不允许
			return;
		}
		if (tag == EAPIConsts.demandReqType.demand_DemandCommentList) {
			Map<Integer, Object> map = (Map<Integer, Object>) object;
//			isComment = (Boolean) map.get(1);
			DemandCommentListItem mGetDynamic = (DemandCommentListItem) map
					.get(2);
			if (mGetDynamic != null) {
				jtpage = mGetDynamic.getJtPage();
				if (jtpage == null) {
					return;
				}
				if (jtpage.getIndex() == 0) {
					demandComment.clear();
				}
				if ((jtpage != null) && (jtpage.getLists() != null)) {
					for (int i = 0; i < jtpage.getLists().size(); i++) {
						demandComment.add((DemandComment) jtpage.getLists().get(i));
					}
					commentCountTv.setText(TextStrUtil.getCommentNum("评论",
							jtpage.getTotal()));
					onNeed.toNeedDetail(2, jtpage.getTotal());
					adapter.notifyDataSetChanged();
				}
				// 如果没有下一页就停止下拉刷新效果
				if (jtpage.getIndex() >= jtpage.getTotalPage() - 1) {
					infoLv.setPullLoadEnable(false);
				}
			}
		} else if (tag == EAPIConsts.demandReqType.demand_addDemandComment) {
			if (object instanceof Boolean) {
				demandCommonEt.setText("");
				getRefresh();
			} else {
				showToast((String) object);
			}
		}
	}

	public void getRefresh() {
		jtpage = null;
		startGetData();
	}

	@Override
	public void getRefresh(String demandId) {
		this.demandId = demandId;
		getRefresh();//刷新
	}

}
