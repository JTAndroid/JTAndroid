package com.tr.ui.tongren.home;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.tr.App;
import com.tr.R;
import com.tr.api.TongRenReqUtils;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.common.view.MyXListView;
import com.tr.ui.common.view.MyXListView.IXListViewListener;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.tongren.TongRenFragment;
import com.tr.ui.tongren.adapter.ProjectAdapter;
import com.tr.ui.tongren.adapter.ProjectAdapter.ProjectType;
import com.tr.ui.tongren.model.project.Apply;
import com.tr.ui.tongren.model.project.PageNumber;
import com.tr.ui.tongren.model.project.Publish;
import com.tr.ui.tongren.model.project.RecommendPagePublish;
import com.utils.http.EAPIConsts;
import com.utils.http.EAPIConsts.TongRenRequestType;
import com.utils.http.IBindData;

public class RecommendProjectActivity extends JBaseActivity implements
		IBindData {

	private MyXListView recommendProjectXLv;
	private ProjectAdapter projectAdapter;
	private int pageNumberIndex;
	private List<Publish> pagePublishList = new ArrayList<Publish>();

	@Override
	public void initJabActionBar() {
		ActionBar jabGetActionBar = jabGetActionBar();
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar, "项目推荐",
				false, null, true, true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recommendproject);
		initView();
		initData();
	}

	private void initData() {
		projectAdapter = new ProjectAdapter(this);
		projectAdapter.setProjectType(ProjectType.RECOMMEND);
		recommendProjectXLv.setAdapter(projectAdapter);
		ImageView imageView = new ImageView(this);
		imageView.setBackgroundResource(R.drawable.project_recommend_banner);
		recommendProjectXLv.addHeaderView(imageView);
		getData();
		recommendProjectXLv.setOnItemClickListener(new OnItemClickListener() {


			private int status = -1;

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				status = -1;
				if (arg2 > 1) {
					Publish createPublish = (Publish) projectAdapter
							.getItem(arg2-2);
					Set<Apply> applySet = createPublish.getApplySet();
					for (Apply apply : applySet) {
							if (App.getUserID().equals(apply.proposerId+"")) {
								status = 4;
							}
							
					}
					ENavigate.startProjectDetailsActivityForResult(
							RecommendProjectActivity.this,
							createPublish.getProjectId() + "", 0 + "", "","",
							ProjectType.RECOMMEND, status,createPublish.getPublisherId()+"",0,"0", 0, 99);
				}
			}
		});

	}

	private void getData() {
		showLoadingDialog();
		PageNumber pageNumber = new PageNumber();
		pageNumber.pageNumber = pageNumberIndex + "";
		TongRenReqUtils
				.doRequestWebAPI(
						this,
						this,
						pageNumber,
						null,
						EAPIConsts.TongRenRequestType.TONGREN_REQ_GETPAGEPUBLISHVALIDITY);
	}

	private void initView() {
		recommendProjectXLv = (MyXListView) findViewById(R.id.recommendProjectXLv);
		recommendProjectXLv.setPullLoadEnable(true);
		recommendProjectXLv.setPullRefreshEnable(true);
		recommendProjectXLv.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				pageNumberIndex = 0;
				getData();
			}

			@Override
			public void onLoadMore() {
				pageNumberIndex++;
				getData();
			}
		});
	}

	@Override
	public void bindData(int tag, Object object) {
		if (object != null) {
			switch (tag) {
			case TongRenRequestType.TONGREN_REQ_GETPAGEPUBLISHVALIDITY:
				RecommendPagePublish pagePublish = (RecommendPagePublish) object;
				recommendProjectXLv.stopLoadMore();
				recommendProjectXLv.stopRefresh();
				if (pageNumberIndex == 0) {
					projectAdapter.setListPublish(pagePublish.result);
				} else {
					pagePublishList.addAll(pagePublish.result);
					projectAdapter.setListPublish(pagePublishList);
				}

				projectAdapter.notifyDataSetChanged();
				break;

			default:
				break;
			}
		}
		dismissLoadingDialog();
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 99) {
			getData();
		}
	}
}
