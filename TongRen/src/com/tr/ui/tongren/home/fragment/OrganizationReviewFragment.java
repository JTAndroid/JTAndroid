package com.tr.ui.tongren.home.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tr.App;
import com.tr.R;
import com.tr.api.TongRenReqUtils;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.tongren.home.fragment.OrganizationApplyFragment.ApplyAdapter;
import com.tr.ui.tongren.home.fragment.OrganizationApplyFragment.ApplyAdapter.ViewHolder;
import com.tr.ui.tongren.model.review.Apply;
import com.tr.ui.tongren.model.review.ApplyList;
import com.tr.ui.tongren.model.review.ApplyPerson;
import com.tr.ui.widgets.MessageDialog;
import com.tr.ui.widgets.MessageDialog.OnDialogFinishListener;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.time.TimeUtil;

public class OrganizationReviewFragment extends JBaseFragment implements IBindData{

	private XListView reviewLv;
	private String oid;
	private ApplyAdapter adapter;
	private int index = 1;
	private int apply_position;
	private String applicationId;
	private String type;
	private String flag = "agree";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		oid = getArguments().getString("oid");
		index = 1;
		showLoadingDialog();
//		getDataFromServer(EAPIConsts.TongRenRequestType.TONGREN_REQ_GETREVIEWAPPLICATIONLIST);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tongren_org_review_frg, null);
		reviewLv = (XListView) view.findViewById(R.id.reviewLv);
		reviewLv.setPullLoadEnable(true);
		reviewLv.setPullRefreshEnable(true);
		adapter = new ApplyAdapter(getActivity());
		reviewLv.setAdapter(adapter);
		reviewLv.setXListViewListener(new IXListViewListener() {
			
			@Override
			public void onRefresh() {
				index = 1;
				getDataFromServer(EAPIConsts.TongRenRequestType.TONGREN_REQ_GETREVIEWAPPLICATIONLIST);
			}
			
			@Override
			public void onLoadMore() {
				index++;
				getDataFromServer(EAPIConsts.TongRenRequestType.TONGREN_REQ_GETREVIEWAPPLICATIONLIST);
			}
		});
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		index = 1;
		getDataFromServer(EAPIConsts.TongRenRequestType.TONGREN_REQ_GETREVIEWAPPLICATIONLIST);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		MenuItem item = menu.findItem(R.id.menu_apply);
		if(item!=null){
			item.setVisible(false);
		}
		super.onCreateOptionsMenu(menu, inflater);
	}

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case EAPIConsts.handler.show_err:
				break;
			}
		}
	};
	
	private void getDataFromServer(int requestType) {
		showLoadingDialog();
		JSONObject jsonObj = new JSONObject();
		try {
			switch (requestType) {
			case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETREVIEWAPPLICATIONLIST://我的审批列表
				jsonObj.put("orgId", oid);
				jsonObj.put("type", "0");//查询类型(0所有的 1 未审核的 2 已经审核)
				jsonObj.put("index", index++);// 请求的第几页页，默认从第1页开始
				break;
			case EAPIConsts.TongRenRequestType.TONGREN_REQ_SIGNOFF://我的审批（同意、驳回）type 2同意 3驳回
				jsonObj.put("applicationId", applicationId);
				jsonObj.put("type", type);
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		TongRenReqUtils.doRequestOrg(getActivity(), this, jsonObj, handler, requestType);
	}
	
	@Override
	public void bindData(int tag, Object object) {
		dismissLoadingDialog();
		reviewLv.stopLoadMore();
		reviewLv.stopRefresh();
		if(object == null){
			return;
		}
		switch(tag){
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_GETREVIEWAPPLICATIONLIST:
			ApplyList applylist = (ApplyList) object;
			if(index == 1){
				adapter.setList(applylist.getResult());
			}else{
				adapter.addList(applylist.getResult());
			}
			adapter.notifyDataSetChanged();
			break;
		case EAPIConsts.TongRenRequestType.TONGREN_REQ_SIGNOFF:
			boolean iscancel = (Boolean) object;
			if(iscancel){
				if(flag.equals("agree")){
					adapter.getItem(apply_position).setStatus("2");
				}else if(flag.equals("noagree")){
					adapter.getItem(apply_position).setStatus("3");
				}
				adapter.notifyDataSetChanged();
			}
			break;
		}
		
	}
	
	class ApplyAdapter extends BaseAdapter{

		private Context mContext;
		private List<Apply> result = new ArrayList<Apply>();
		
		public ApplyAdapter(Context mContext) {
			this.mContext = mContext;
		}

		public void setList(List<Apply> result){
			this.result = result;
		}

		public void addList(List<Apply> result){
			this.result.addAll(result);
		}
		
		public void clear(){
			this.result.clear();
		}
		
		@Override
		public int getCount() {
			return result.size();
		}

		@Override
		public Apply getItem(int position) {
			return result.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final Apply apply = result.get(position);
			ViewHolder holder;
			if(convertView == null){
				holder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.tongren_org_review_item, null);
				holder.applyNo = (TextView) convertView.findViewById(R.id.applyNo);
				holder.applyUser = (TextView) convertView.findViewById(R.id.applyUser);
				holder.applyType = (TextView) convertView.findViewById(R.id.applyType);
				holder.applyContent = (TextView) convertView.findViewById(R.id.applyContent);
				holder.applyTime = (TextView) convertView.findViewById(R.id.applyTime);
				holder.addTime = (TextView) convertView.findViewById(R.id.addTime);
				holder.applyPerson1 = (TextView) convertView.findViewById(R.id.applyPerson1);
				holder.applyPerson2 = (TextView) convertView.findViewById(R.id.applyPerson2);
				holder.agree = (TextView) convertView.findViewById(R.id.agree);
				holder.noagree = (TextView) convertView.findViewById(R.id.noagree);
				holder.applyStatus = (TextView) convertView.findViewById(R.id.applyStatus);
				holder.button_ll = (LinearLayout) convertView.findViewById(R.id.button_ll);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.applyNo.setText(apply.getApplicationNo());
			holder.applyUser.setText(apply.getApplyUserName());
			holder.applyType.setText(apply.getReviewGenreName());
			holder.applyContent.setText(apply.getApplyRereason());
			holder.applyTime.setText(TimeUtil.getDate(apply.getStartTime(), "yyyy/MM/dd HH:mm")+"-"+TimeUtil.getDate(apply.getEndTime(), "yyyy/MM/dd HH:mm"));
			holder.addTime.setText(TimeUtil.getDate(apply.getApplyDate(), "yyyy/MM/dd HH:mm"));
			
			if(apply.getStatus().equals("1")){
				holder.applyStatus.setVisibility(View.VISIBLE);
				holder.applyStatus.setText("已撤回");
				holder.applyStatus.setTextColor(0xFFF75A47);
				holder.button_ll.setVisibility(View.GONE);
			}else if(apply.getStatus().equals("2")){
				holder.applyStatus.setVisibility(View.VISIBLE);
				holder.applyStatus.setText("已同意");
				holder.applyStatus.setTextColor(0xFF66CCCC);
				holder.button_ll.setVisibility(View.GONE);
			}else if(apply.getStatus().equals("3")){
				holder.applyStatus.setVisibility(View.VISIBLE);
				holder.applyStatus.setText("已驳回");
				holder.applyStatus.setTextColor(0xFFF75A47);
				holder.button_ll.setVisibility(View.GONE);
			}else{
				holder.applyStatus.setVisibility(View.GONE);
				holder.button_ll.setVisibility(View.VISIBLE);
			}
			
			if(apply.getReviewDetail()!=null){
				if(apply.getReviewDetail().size()>0){
					ApplyPerson aPerson = null;
					for(int i=0;i<apply.getReviewDetail().size();i++){
						aPerson = apply.getReviewDetail().get(i);
						if(aPerson.getReviewUserId().equals(App.getUserID())){
							if(i>0){
								holder.applyPerson1.setText("上一级审批人:"+apply.getReviewDetail().get(i-1).getUserName());
							}else{
								holder.applyPerson1.setText("开始");
							}
							if(i == apply.getReviewDetail().size()-1){
								holder.applyPerson2.setText("结束");
							}else{
								holder.applyPerson2.setText("下一级审批人:"+apply.getReviewDetail().get(i+1).getUserName());
							}
							break;
						}
					}
				}
			}
			holder.agree.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					MessageDialog msgdialog = new MessageDialog(getActivity());
					msgdialog.setContent("确认要同意吗?");
					msgdialog.show();
					msgdialog.setOnDialogFinishListener(new OnDialogFinishListener() {
						@Override
						public void onFinish(String content) {
							flag = "agree";
							apply_position = position;
							applicationId = apply.getId();
							type = "2";
							getDataFromServer(EAPIConsts.TongRenRequestType.TONGREN_REQ_SIGNOFF);
						}

						@Override
						public void onCancel(String content) {
							// TODO Auto-generated method stub
							
						}
					});
				}
			});
			
			holder.noagree.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					MessageDialog msgdialog = new MessageDialog(getActivity());
					msgdialog.setContent("确认要驳回吗?");
					msgdialog.show();
					msgdialog.setOnDialogFinishListener(new OnDialogFinishListener() {
						@Override
						public void onFinish(String content) {
							flag = "noagree";
							apply_position = position;
							applicationId = apply.getId();
							type = "3";
							getDataFromServer(EAPIConsts.TongRenRequestType.TONGREN_REQ_SIGNOFF);
						}

						@Override
						public void onCancel(String content) {
							// TODO Auto-generated method stub
							
						}
					});
				}
			});
			
			return convertView;
		}
		
		class ViewHolder{
			public TextView applyStatus;
			public TextView applyNo;
			public TextView applyUser;
			public TextView applyType;
			public TextView applyContent;
			public TextView applyTime;
			public TextView addTime;
			public TextView applyPerson1;
			public TextView applyPerson2;
			public TextView agree;
			public TextView noagree;
			public LinearLayout button_ll;
		}
	}

}
