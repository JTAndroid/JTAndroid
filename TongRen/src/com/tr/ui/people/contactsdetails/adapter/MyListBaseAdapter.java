/**
 * 
 */
package com.tr.ui.people.contactsdetails.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tr.R;
import com.tr.baidumapsdk.BNavigatorActivity;
import com.tr.model.demand.ASSOData;
import com.tr.model.demand.DemandASSOData;
import com.tr.model.obj.ResourceBase;
import com.tr.navigate.ENavigate;
import com.tr.ui.common.JointResourceFragment.ResourceType;
import com.tr.ui.organization.model.meet.CustomerMeetingDetail;
import com.tr.ui.people.contactsdetails.ContactsDetailsActivity;
import com.tr.ui.people.contactsdetails.MeetingDetailsActivity.ConnectionsGroupAdapter;
import com.tr.ui.people.contactsdetails.MeetingDetailsActivity.KnowledgeGroupAdapter;
import com.tr.ui.people.contactsdetails.MeetingDetailsActivity.RequirementGroupAdapter;
import com.tr.ui.people.cread.NewConnectionsActivity;
import com.tr.ui.widgets.BasicListView2;

public class MyListBaseAdapter extends BaseAdapter implements OnClickListener {
	private List<CustomerMeetingDetail> meetingDetail;
	private Context context;
	private LayoutInflater inflater;

	private AlertDialog dlg, dialog;

	private AlertDialog.Builder builder;

	private Window window;

	private ViewHolder viewHolder;

	private LinearLayout business_messageRelativeLayout,
			maincontacts_main_message;

	private TextView business_message_phone, message_phone;
	private ConnectionsGroupAdapter peopleGroupAdapter;
	private ConnectionsGroupAdapter organizationGroupAdapter;
	private KnowledgeGroupAdapter knowledgeGroupAdapter;
	private RequirementGroupAdapter requirementGroupAdapter;
	private static String decollatorStr = "、";

	public MyListBaseAdapter() {
		super();
	}

	public MyListBaseAdapter(List<CustomerMeetingDetail> meetingDetail,
			Context context) {
		super();
		this.meetingDetail = meetingDetail;
		this.context = context;
		this.inflater = LayoutInflater.from(context);
	}

	public void putCustomerMeetingDetail_List(
			List<CustomerMeetingDetail> meetingDetail) {
		this.meetingDetail = meetingDetail;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return meetingDetail != null && !meetingDetail.isEmpty() ? meetingDetail
				.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return meetingDetail.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {

			convertView = inflater.inflate(R.layout.people_list_item, parent,
					false);

			viewHolder = new ViewHolder(convertView);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		CustomerMeetingDetail meeting = meetingDetail.get(position);
		if (TextUtils.isEmpty(meeting.meetDate)) {
			viewHolder.time_Rl.setVisibility(View.GONE);

		}
		if (TextUtils.isEmpty(meeting.address)) {
			viewHolder.bNavigatorLinearLayout.setVisibility(View.GONE);

		}
		if (TextUtils.isEmpty(meeting.title)) {
			viewHolder.meeting_dec_Ll.setVisibility(View.GONE);

		}
		viewHolder.timeTv.setText(meeting.meetDate);
		viewHolder.tv_time.setText(meeting.time);
		viewHolder.meeting_address.setText(meeting.address);
		viewHolder.meeting_dec.setText(meeting.title);
		ImageView iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
		if ("0".equals(meeting.color)) {
			iv_icon.setBackgroundResource(R.drawable.people_nocolor);
		} else if ("1".equals(meeting)) {
			iv_icon.setBackgroundResource(R.drawable.people_green);
		} else if ("2".equals(meeting.color)) {
			iv_icon.setBackgroundResource(R.drawable.people_blue);
		} else if ("3".equals(meeting.color)) {
			iv_icon.setBackgroundResource(R.drawable.people_orange);
		} else if ("4".equals(meeting.color)) {
			iv_icon.setBackgroundResource(R.drawable.people_red);
		}
		knowledgeGroupAdapter = new KnowledgeGroupAdapter(context,
				meeting.relevance.k);
		peopleGroupAdapter = new ConnectionsGroupAdapter(context,
				meeting.relevance.p);
		requirementGroupAdapter = new RequirementGroupAdapter(context,
				meeting.relevance.r);
		organizationGroupAdapter = new ConnectionsGroupAdapter(context,
				meeting.relevance.o);
		if (meeting.relevance != null) {

			if (!meeting.relevance.p.isEmpty()) {
				viewHolder.contactsName_Rl.setVisibility(View.VISIBLE);

				viewHolder.contactsName_lv.setAdapter(peopleGroupAdapter);
				peopleGroupAdapter
						.setListRelatedConnectionsNode(meeting.relevance.p);
				peopleGroupAdapter.notifyDataSetChanged();
			}
			if (!meeting.relevance.r.isEmpty()) {
				viewHolder.regi_Rl.setVisibility(View.VISIBLE);

				viewHolder.regi_Lv.setAdapter(requirementGroupAdapter);
				requirementGroupAdapter
						.setListRelatedAffairNode(meeting.relevance.r);
				requirementGroupAdapter.notifyDataSetChanged();
			}
			if (!meeting.relevance.o.isEmpty()) {
				viewHolder.organ_Rl.setVisibility(View.VISIBLE);

				viewHolder.organ_Lv.setAdapter(organizationGroupAdapter);
				organizationGroupAdapter
						.setListRelatedConnectionsNode(meeting.relevance.o);
				organizationGroupAdapter.notifyDataSetChanged();
			}
			if (!meeting.relevance.k.isEmpty()) {
				viewHolder.know_Rl.setVisibility(View.VISIBLE);

				viewHolder.know_Lv.setAdapter(knowledgeGroupAdapter);
				knowledgeGroupAdapter
						.setListRelatedKnowledgeNode(meeting.relevance.k);
				knowledgeGroupAdapter.notifyDataSetChanged();
			}
		}
		if (meeting.relevance != null) {
			updateAllUI(meeting);
		}

		viewHolder.bNavigatorLinearLayout.setOnClickListener(this);

		return convertView;
	}

	public void updateAllUI(CustomerMeetingDetail meeting) {

		// 更新
		peopleGroupAdapter.setListRelatedConnectionsNode(meeting.relevance.p);
		peopleGroupAdapter.notifyDataSetChanged();
		organizationGroupAdapter
				.setListRelatedConnectionsNode(meeting.relevance.o);
		organizationGroupAdapter.notifyDataSetChanged();
		knowledgeGroupAdapter.setListRelatedKnowledgeNode(meeting.relevance.k);
		knowledgeGroupAdapter.notifyDataSetChanged();
		requirementGroupAdapter.setListRelatedAffairNode(meeting.relevance.r);
		requirementGroupAdapter.notifyDataSetChanged();

	}

	public static class ViewHolder {

		private TextView tv_time, meeting_address, meeting_dec, timeTv;

		private LinearLayout bNavigatorLinearLayout, meeting_dec_Ll;

		private RelativeLayout regi_Rl, know_Rl, organ_Rl, contactsName_Rl,
				time_Rl;
		private BasicListView2 regi_Lv, know_Lv, organ_Lv, contactsName_lv;

		

		public ViewHolder(View convertView) {

			tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			timeTv = (TextView) convertView.findViewById(R.id.timeTv);
			bNavigatorLinearLayout = (LinearLayout) convertView
					.findViewById(R.id.bNavigatorLinearLayout);
			meeting_dec_Ll = (LinearLayout) convertView
					.findViewById(R.id.meeting_dec_Ll);
			meeting_address = (TextView) convertView
					.findViewById(R.id.meeting_address);
			meeting_dec = (TextView) convertView.findViewById(R.id.meeting_dec);

			
			
			regi_Rl = (RelativeLayout) convertView.findViewById(R.id.regi_Rl);
			know_Rl = (RelativeLayout) convertView.findViewById(R.id.know_Rl);
			organ_Rl = (RelativeLayout) convertView.findViewById(R.id.organ_Rl);
			contactsName_Rl = (RelativeLayout) convertView
					.findViewById(R.id.contactsName_Rl);
			time_Rl = (RelativeLayout) convertView.findViewById(R.id.time_Rl);
			regi_Lv = (BasicListView2) convertView.findViewById(R.id.regi_Lv);
			know_Lv = (BasicListView2) convertView.findViewById(R.id.know_Lv);
			organ_Lv = (BasicListView2) convertView.findViewById(R.id.organ_Lv);
			contactsName_lv = (BasicListView2) convertView
					.findViewById(R.id.contactsName_lv);
		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.bNavigatorLinearLayout:

			// Intent intents = new Intent(context, BNavigatorActivity.class);
			//
			// Bundle bundle = new Bundle();
			//
			// bundle.putString("KEY", "钓鱼台");
			//
			// intents.putExtras(bundle);
			//
			// context.startActivity(intents);

			break;

		}

	}

	// 激活发短信页面，并将电话号码携带过去

	public void sendMessage(String message_number_business) {
		Uri uri = Uri.parse("smsto:" + message_number_business);

		Intent it = new Intent(Intent.ACTION_SENDTO, uri);

		context.startActivity(it);
	}

	// 激活打电话页面，并将电话号码携带过去

	public void callPhone(String number_business) {
		Intent intent = new Intent("android.intent.action.DIAL");

		intent.setClassName("com.android.contacts",
				"com.android.contacts.DialtactsActivity");

		intent.setData(Uri.parse("tel:" + number_business));

		context.startActivity(intent);
	}

	public class ConnectionsGroupAdapter extends BaseAdapter {

		private Context context;
		private List<ASSOData> listRelatedConnectionsNode;

		public ConnectionsGroupAdapter() {
			super();
		}

		public ConnectionsGroupAdapter(Context context,
				List<ASSOData> listRelatedConnectionsNode) {
			super();
			this.context = context;
			if (listRelatedConnectionsNode != null) {
				this.listRelatedConnectionsNode = listRelatedConnectionsNode;
			} else {
				this.listRelatedConnectionsNode = new ArrayList<ASSOData>();
			}
		}

		public List<ASSOData> getListRelatedConnectionsNode() {
			return listRelatedConnectionsNode;
		}

		public void setListRelatedConnectionsNode(
				List<ASSOData> listRelatedConnectionsNode) {
			if (listRelatedConnectionsNode != null) {
				this.listRelatedConnectionsNode = listRelatedConnectionsNode;
			}
		}

		@Override
		public int getCount() {
			return listRelatedConnectionsNode.size();
		}

		@Override
		public Object getItem(int position) {
			return listRelatedConnectionsNode.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				final ViewGroup parent) {

			ViewHolder viewHolder;

			if (convertView == null) {
				viewHolder = new ViewHolder();

				convertView = LayoutInflater.from(context).inflate(
						R.layout.list_item_connections_group, null);
				viewHolder.keyTv = (TextView) convertView
						.findViewById(R.id.keyTv);
				viewHolder.valueTv = (TextView) convertView
						.findViewById(R.id.valueTv);
				viewHolder.deleteIv = (ImageView) convertView
						.findViewById(R.id.deleteIv);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.deleteIv.setVisibility(View.GONE);

			ASSOData connectionNode = listRelatedConnectionsNode.get(position);
			String key = connectionNode.tag;
			viewHolder.keyTv.setText(key + " : ");
			ArrayList<DemandASSOData> listConnections = (ArrayList<DemandASSOData>) connectionNode.conn;
			StringBuilder valueSb = new StringBuilder();
			for (int i = 0; i < listConnections.size(); i++) {
				valueSb.append(listConnections.get(i).name);
				if (i != listConnections.size() - 1) {
					valueSb.append(decollatorStr);
				}
			}
			viewHolder.valueTv.setText(valueSb.toString());

			return convertView;
		}

		class ViewHolder {
			TextView keyTv;
			TextView valueTv;
			ImageView deleteIv;
		}

	}

	public class KnowledgeGroupAdapter extends BaseAdapter {

		private Context context;
		private List<ASSOData> listRelatedKnowledgeNode;

		public KnowledgeGroupAdapter() {
			super();
		}

		public KnowledgeGroupAdapter(Context context,
				List<ASSOData> listRelatedKnowledgeNode) {
			super();
			this.context = context;
			if (listRelatedKnowledgeNode != null) {
				this.listRelatedKnowledgeNode = listRelatedKnowledgeNode;
			} else {
				this.listRelatedKnowledgeNode = new ArrayList<ASSOData>();
			}
		}

		public List<ASSOData> getListRelatedKnowledgeNode() {
			return listRelatedKnowledgeNode;
		}

		public void setListRelatedKnowledgeNode(
				List<ASSOData> listRelatedKnowledgeNode) {
			if (listRelatedKnowledgeNode != null) {
				this.listRelatedKnowledgeNode = listRelatedKnowledgeNode;
			}
		}

		@Override
		public int getCount() {
			return listRelatedKnowledgeNode.size();
		}

		@Override
		public Object getItem(int position) {
			return listRelatedKnowledgeNode.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				final ViewGroup parent) {

			ViewHolder viewHolder;

			if (convertView == null) {
				viewHolder = new ViewHolder();

				convertView = LayoutInflater.from(context).inflate(
						R.layout.list_item_connections_group, null);
				viewHolder.keyTv = (TextView) convertView
						.findViewById(R.id.keyTv);
				viewHolder.valueTv = (TextView) convertView
						.findViewById(R.id.valueTv);
				viewHolder.deleteIv = (ImageView) convertView
						.findViewById(R.id.deleteIv);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.deleteIv.setVisibility(View.GONE);

			ASSOData knowledgeNode = listRelatedKnowledgeNode.get(position);
			String key = knowledgeNode.tag;
			viewHolder.keyTv.setText(key + " : ");
			List<DemandASSOData> listKnowledgeMini2 = knowledgeNode.conn;
			StringBuilder valueSb = new StringBuilder();
			for (int i = 0; i < listKnowledgeMini2.size(); i++) {
				valueSb.append(listKnowledgeMini2.get(i).title);
				if (i != listKnowledgeMini2.size() - 1) {
					valueSb.append(decollatorStr);
				}
			}

			viewHolder.valueTv.setText(valueSb.toString());

			return convertView;
		}

		class ViewHolder {
			TextView keyTv;
			TextView valueTv;
			ImageView deleteIv;
		}

	}

	public class RequirementGroupAdapter extends BaseAdapter {

		private Context context;
		private List<ASSOData> listRelatedAffairNode;

		public RequirementGroupAdapter() {
			super();
		}

		public RequirementGroupAdapter(Context context,
				List<ASSOData> listRelatedAffairNode) {
			super();
			this.context = context;
			if (listRelatedAffairNode != null) {
				this.listRelatedAffairNode = listRelatedAffairNode;
			} else {
				this.listRelatedAffairNode = new ArrayList<ASSOData>();
			}
		}

		public List<ASSOData> getListRelatedAffairNode() {
			return listRelatedAffairNode;
		}

		public void setListRelatedAffairNode(
				List<ASSOData> listRelatedAffairNode) {
			if (listRelatedAffairNode != null) {
				this.listRelatedAffairNode = listRelatedAffairNode;
			}
		}

		@Override
		public int getCount() {
			return listRelatedAffairNode.size();
		}

		@Override
		public Object getItem(int position) {
			return listRelatedAffairNode.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				final ViewGroup parent) {

			ViewHolder viewHolder;

			if (convertView == null) {
				viewHolder = new ViewHolder();

				convertView = LayoutInflater.from(context).inflate(
						R.layout.list_item_connections_group, null);
				viewHolder.keyTv = (TextView) convertView
						.findViewById(R.id.keyTv);
				viewHolder.valueTv = (TextView) convertView
						.findViewById(R.id.valueTv);
				viewHolder.deleteIv = (ImageView) convertView
						.findViewById(R.id.deleteIv);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.deleteIv.setVisibility(View.GONE);

			ASSOData affairNode = listRelatedAffairNode.get(position);
			String key = affairNode.tag;
			viewHolder.keyTv.setText(key + " : ");
			List<DemandASSOData> listaAffairsMini = affairNode.conn;
			StringBuilder valueSb = new StringBuilder();
			for (int i = 0; i < listaAffairsMini.size(); i++) {
				valueSb.append(listaAffairsMini.get(i).title);
				if (i != listaAffairsMini.size() - 1) {
					valueSb.append(decollatorStr);
				}
			}

			viewHolder.valueTv.setText(valueSb.toString());

			return convertView;
		}

		class ViewHolder {
			TextView keyTv;
			TextView valueTv;
			ImageView deleteIv;
		}

	}
}
