package com.tr.ui.tongren;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tr.R;
import com.tr.api.TongRenReqUtils;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.tongren.adapter.OrgAdapter;
import com.tr.ui.tongren.home.OrganizationActivity;
import com.tr.ui.tongren.home.RecommendProjectActivity;
import com.tr.ui.tongren.model.project.Organization;
import com.tr.ui.widgets.NoScrollListview;
import com.tr.ui.widgets.RefreshScrollView;
import com.tr.ui.widgets.RefreshScrollView.OnRefreshScrollViewListener;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

public class OrganizationFragment extends JBaseFragment implements IBindData,OnClickListener{

	private NoScrollListview orgXlv;
	private OrgAdapter orgAdapter;
	private List<Organization> orgs = new ArrayList<Organization>();
	private List<Organization> orgs_myCreate = new ArrayList<Organization>();
	private List<Organization> orgs_myJoin = new ArrayList<Organization>();
	private RelativeLayout empty;
	private ImageView common_image_empty;
	private TextView common_text_empty;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View orgView = inflater.inflate(R.layout.fragment_organization, null);
		RefreshScrollView refreshScrollView = new RefreshScrollView(getActivity());
		refreshScrollView.setupContainer(getActivity(), orgView);
		refreshScrollView.setEnableRefresh(true);
		
		refreshScrollView.setOnRefreshScrollViewListener(new OnRefreshScrollViewListener() {
			
			@Override
			public void onRefresh() {
				getData();
			}
		});
		initView(refreshScrollView);
		return refreshScrollView;
	}
	
	private boolean mIsVisibleToUser;
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		mIsVisibleToUser = isVisibleToUser;
		if (mIsVisibleToUser /*&& !isFirstStart*/) {
			getData();
		}
	}

	private void initView(View orgView) {
		empty = (RelativeLayout) orgView.findViewById(R.id.empty);
		common_text_empty = (TextView) empty.findViewById(R.id.common_text_empty);
		common_image_empty = (ImageView) empty.findViewById(R.id.common_image_empty);
		orgXlv = (NoScrollListview) orgView.findViewById(R.id.orgXlv);
		orgAdapter = new OrgAdapter(getActivity(), orgs);
		orgXlv.setAdapter(orgAdapter);
		orgXlv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {
				if(orgs.get(position-1).getId()!=0){
					Intent intent = new Intent(getActivity(), OrganizationActivity.class);
					if(position>orgs_myCreate.size()){
						intent.putExtra("isMine", false);
					}else{
						intent.putExtra("isMine", true);
					}
					intent.putExtra("oid", orgs.get(position-1).getId() + "");
					getActivity().startActivityForResult(intent,TongRenFragment.REQ_ORG);
				}
			}
		});
		LinearLayout reCommendll = (LinearLayout) orgView.findViewById(R.id.reCommendll);
		reCommendll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), RecommendProjectActivity.class);
				startActivityForResult(intent, TongRenFragment.REQ_PROJECT);
			}
		});
	}

	private void getData() {
		getDataFromServer(EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMYCREATEORGANIZATIONS);// 创建
	}

	@Override
	public void bindData(int tag, Object object) {
		switch (tag) {
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMYCREATEORGANIZATIONS:// 创建
			orgs.clear();
			getDataFromServer(EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMYJOINORGANIZATION);// 参与
			if (object != null) {
				Organization myCreateOrg = new Organization();
				myCreateOrg.setName("创建");
				orgs_myCreate = (List<Organization>) object;
				orgs_myCreate.add(0, myCreateOrg);
				orgs.addAll(orgs_myCreate);
			}else{
				orgs_myCreate.clear();
			}
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMYJOINORGANIZATION:// 参与
			if (object != null) {
				Organization myJoinOrg = new Organization();
				myJoinOrg.setName("参与");
				orgs_myJoin = (List<Organization>) object;
				orgs_myJoin.add(0, myJoinOrg);
				orgs.addAll(orgs_myJoin);
			}else{
				orgs_myJoin.clear();
			}
			orgAdapter.setOrgList(orgs);
			orgAdapter.notifyDataSetChanged();
			
			if(orgs.size()==0){
				empty.setVisibility(View.VISIBLE);
				common_image_empty.setImageResource(R.drawable.organization_empty);
				common_text_empty.setText(R.string.common_text_empty);
			}else{
				empty.setVisibility(View.GONE);
			}
			break;
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == TongRenFragment.REQ_ORG) {
			getData();
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case EAPIConsts.handler.show_err:
				break;
			}
		}
	};

	private void getDataFromServer(int requestType) {
		JSONObject jsonObj = new JSONObject();
		try {
			switch (requestType) {
			case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMYJOINORGANIZATION:// 参与
				break;
			case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETMYCREATEORGANIZATIONS:// 创建
				jsonObj.put("status", "0");
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		TongRenReqUtils.doRequestOrg(getActivity(), this, jsonObj, handler,
				requestType);// 创建
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
