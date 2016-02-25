package com.tr.ui.tongren.home.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tr.R;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.common.view.XListView;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.tongren.model.review.Process;

public class OrganizationApplyNameFragment extends JBaseFragment {

	private XListView xlv;
	private ProcessAdapter adapter;
	private ArrayList<Process> listProcess;
	private String applyName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		listProcess = (ArrayList<Process>) getArguments().get("listProcess");
		applyName = getArguments().getString("applyName");
		HomeCommonUtils.initLeftCustomActionBar(getActivity(), getActivity()
				.getActionBar(), "申请名称", false, null, true, true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tongren_org_review_frg, null);

		adapter = new ProcessAdapter(getActivity());
		adapter.setData(listProcess);
		xlv = (XListView) view.findViewById(R.id.reviewLv);
		xlv.setAdapter(adapter);
		xlv.setPullLoadEnable(false);
		xlv.setPullRefreshEnable(false);
		xlv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.putExtra("Process", listProcess.get(position - 1));
				getActivity().setResult(0, intent);
				getActivity().finish();
			}
		});
		return view;
	}

	class ProcessAdapter extends BaseAdapter {

		private Context mContext;
		private List<Process> listProcess;

		public ProcessAdapter(Context mContext) {
			this.mContext = mContext;
		}

		public void setData(List<Process> listProcess) {
			this.listProcess = listProcess;
		}

		@Override
		public int getCount() {
			return listProcess.size();
		}

		@Override
		public Process getItem(int position) {
			return listProcess.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ItemHolder holder;
			Process process = listProcess.get(position);
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

			holder.applyTv.setText(process.getReviewName());
			if (process.getReviewName().equals(applyName)) {
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
