package com.tr.ui.tongren.adapter;

import java.util.ArrayList;
import java.util.List;

import com.tr.R;
import com.tr.ui.tongren.model.project.Operation;
import com.utils.time.TimeUtil;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ProjectOperationAdapter extends BaseAdapter {
	private Context mContext;
	private List<Operation> listOperation = new ArrayList<Operation>();
	public ProjectOperationAdapter(Context context) {
		this.mContext = context;
	}
	
	public List<Operation> getListOperation() {
		return listOperation;
	}

	public void setListOperation(List<Operation> listOperation) {
		this.listOperation = listOperation;
	}

	@Override
	public int getCount() {
		return getListOperation().size();
	}

	@Override
	public Object getItem(int arg0) {
		return getListOperation().get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ProjectOperationViewHolder holder ;
		if (arg1==null) {
			holder = new ProjectOperationViewHolder();
			arg1 = View.inflate(mContext, R.layout.adapter_projectoperation, null);
			holder.projectOperationTimeTv = (TextView) arg1.findViewById(R.id.projectOperationTimeTv);
			holder.projectOperationTextTv = (TextView) arg1.findViewById(R.id.projectOperationTextTv);
			arg1.setTag(holder);
		}else{
			holder = (ProjectOperationViewHolder) arg1.getTag();
		}
		Operation itemOperation = (Operation) getItem(arg0);
		holder.projectOperationTimeTv .setText( TimeUtil.TimeMillsToStringWithMinute(itemOperation.operactionTime));
		holder.projectOperationTextTv .setText(itemOperation.name);
		
		return arg1;
	}
	class ProjectOperationViewHolder{
		TextView projectOperationTimeTv,projectOperationTextTv;
	}
}
