package com.tr.ui.tongren.home.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.tr.R;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.common.view.XListView;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.tongren.model.review.Process;
import com.tr.ui.tongren.model.review.ProcessType;

public class OrganizationApplyTypeFragment extends JBaseFragment {

	private String reviewProcessId, applyType;
	private ArrayList<ProcessType> listProcessType;
	private XListView xlv;
	private ProcessAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HomeCommonUtils.initLeftCustomActionBar(getActivity(), getActivity()
				.getActionBar(), "申请类型", false, null, true, true);
		reviewProcessId = getArguments().getString("reviewProcessId");
		applyType = getArguments().getString("applyType");
		ArrayList<Process> listProcess = (ArrayList<Process>) getArguments().getSerializable("listProcess");
		initData(reviewProcessId, listProcess);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tongren_org_review_frg, null);
		adapter = new ProcessAdapter(getActivity());
		adapter.setData(listProcessType);
		xlv = (XListView) view.findViewById(R.id.reviewLv);
		xlv.setAdapter(adapter);
		xlv.setPullLoadEnable(false);
		xlv.setPullRefreshEnable(false);
		xlv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.putExtra("ProcessType", listProcessType.get(position - 1));
				getActivity().setResult(0, intent);
				getActivity().finish();
			}
		});
		return view;
	}
	
	private void initData(String reviewProcessId, ArrayList<Process> listProcess){
		for(Process process:listProcess){
			if(process.getId().equals(reviewProcessId)){
				listProcessType = (ArrayList<ProcessType>) process.getGenreList();
				break;
			}
		}
	}

	class ProcessAdapter extends BaseAdapter {

		private Context mContext;
		private List<ProcessType> listProcessType;

		public ProcessAdapter(Context mContext) {
			this.mContext = mContext;
		}

		public void setData(List<ProcessType> listProcessType) {
			this.listProcessType = listProcessType;
		}

		@Override
		public int getCount() {
			return listProcessType.size();
		}

		@Override
		public ProcessType getItem(int position) {
			return listProcessType.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ItemHolder holder;
			ProcessType pt = listProcessType.get(position);
			if (convertView == null) {
				holder = new ItemHolder();
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.tongren_org_newapply_item, null);
				holder.applyTv = (TextView) convertView
						.findViewById(R.id.applyTV);
				holder.isCheckIv = (ImageView) convertView
						.findViewById(R.id.isCheckIV);
				convertView.setTag(holder);
			} else {
				holder = (ItemHolder) convertView.getTag();
			}

			holder.applyTv.setText(pt.getName());
			if (pt.getName().equals(applyType)) {
				holder.isCheckIv.setVisibility(View.VISIBLE);
			} else {
				holder.isCheckIv.setVisibility(View.GONE);
			}

			return convertView;
		}

		class ItemHolder {
			public TextView applyTv;
			public ImageView isCheckIv;
		}

	}

}
