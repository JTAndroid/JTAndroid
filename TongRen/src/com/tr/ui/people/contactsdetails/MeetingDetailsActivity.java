package com.tr.ui.people.contactsdetails;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.App;
import com.tr.R;
import com.tr.api.OrganizationReqUtil;
import com.tr.api.PeopleReqUtil;
import com.tr.model.demand.ASSOData;
import com.tr.model.demand.ASSORPOK;
import com.tr.model.demand.DemandASSO;
import com.tr.model.demand.DemandASSOData;
import com.tr.model.joint.AffairNode;
import com.tr.model.joint.ConnectionNode;
import com.tr.model.joint.KnowledgeNode;
import com.tr.model.knowledge.Knowledge2;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.model.obj.AffairsMini;
import com.tr.model.obj.Connections;
import com.tr.navigate.ENavConsts;
import com.tr.ui.organization.model.Contacts;
import com.tr.ui.organization.model.Organ_Id;
import com.tr.ui.organization.model.RelatedContacts;
import com.tr.ui.organization.model.RelatedContents;
import com.tr.ui.organization.model.RelatedEvent;
import com.tr.ui.organization.model.RelatedKnowledge;
import com.tr.ui.organization.model.RelatedOrganization;
import com.tr.ui.organization.model.meet.CustomerMeet_Delete;
import com.tr.ui.organization.model.meet.CustomerMeetingDetail;
import com.tr.ui.people.contactsdetails.NewCreateActivity.ConnectionsGroupAdapter;
import com.tr.ui.people.contactsdetails.NewCreateActivity.KnowledgeGroupAdapter;
import com.tr.ui.people.contactsdetails.NewCreateActivity.RequirementGroupAdapter;
import com.tr.ui.people.contactsdetails.NewCreateActivity.RequirementGroupAdapter.ViewHolder;
import com.tr.ui.people.contactsdetails.fragment.MeetingDetailsFragment;
import com.tr.ui.widgets.BasicListView2;
import com.utils.common.EConsts;
import com.utils.http.EAPIConsts.OrganizationReqType;
import com.utils.http.EAPIConsts.PeopleRequestType;
import com.utils.http.IBindData;

/**
 * 会面详情(目前会面模块未显示)
 * @author John
 *
 */
public class MeetingDetailsActivity extends Activity implements
		OnCheckedChangeListener, OnClickListener, IBindData {

	private ScrollView scroll;

	private TextView add, delete_meeting_contant, meeting_delete_yes,
			meeting_delete_no, delete_meeting_cancel;

	private ImageView meeting_more_image, meeting_back;

	private AlertDialog dlg, dialog;

	private AlertDialog.Builder builder;

	private Window window;

	private TextView infomation_text;

	private TextView times_text;

	private TextView monthly_text;

	private TextView remind_time_text;

	private TextView schedule_classification_text;

	private ImageView schedule_classification_image;

	private TextView address_info_text;

	private BasicListView2 people;

	private BasicListView2 organization;

	private BasicListView2 knowledge;

	private BasicListView2 requirement;

	private LinearLayout people_Ll;

	private LinearLayout organization_Ll;

	private LinearLayout knowledge_Ll;

	private LinearLayout requirement_Ll;

	private ConnectionsGroupAdapter peopleGroupAdapter;

	private ConnectionsGroupAdapter organizationGroupAdapter;

	private KnowledgeGroupAdapter knowledgeGroupAdapter;

	private RequirementGroupAdapter requirementGroupAdapter;

	private ASSORPOK relatedInformation;

	private static String decollatorStr = "、";

	private CustomerMeetingDetail customerMeetingDetail;

	private TextView delete_meeting_edit;

	private int showColorImage;

	private long meetid;

	private long meet_deleteid;

	private int tYPE;

	private long id;

	private TextView remark_text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_meeting_details);

		infomation_text = (TextView) findViewById(R.id.infomation_text);
		remark_text = (TextView) findViewById(R.id.remark_text);
		times_text = (TextView) findViewById(R.id.times_text);
		monthly_text = (TextView) findViewById(R.id.monthly_text);
		remind_time_text = (TextView) findViewById(R.id.remind_time_text);
		schedule_classification_text = (TextView) findViewById(R.id.schedule_classification_text);
		schedule_classification_image = (ImageView) findViewById(R.id.schedule_classification_image);
		address_info_text = (TextView) findViewById(R.id.address_info_text);
		scroll = (ScrollView) findViewById(R.id.meeting_pullToRefreshScrollView);

		meeting_back = (ImageView) findViewById(R.id.meeting_back);
		meeting_back.setOnClickListener(this);

		meeting_more_image = (ImageView) findViewById(R.id.meeting_more_image);
		meeting_more_image.setOnClickListener(this);

		people = (BasicListView2) findViewById(R.id.people_meeting);
		organization = (BasicListView2) findViewById(R.id.organization_meeting);
		knowledge = (BasicListView2) findViewById(R.id.knowledge_meeting);
		requirement = (BasicListView2) findViewById(R.id.requirement_meeting);
		people_Ll = (LinearLayout) findViewById(R.id.people_meeting_Ll);
		organization_Ll = (LinearLayout) findViewById(R.id.organization_meeting_Ll);
		knowledge_Ll = (LinearLayout) findViewById(R.id.knowledge_meeting_Ll);
		requirement_Ll = (LinearLayout) findViewById(R.id.requirement_meeting_Ll);
		id = this.getIntent().getLongExtra("Org_Id", -1);

		meet_deleteid = this.getIntent().getLongExtra("OneMeetID", -1);
		customerMeetingDetail = (CustomerMeetingDetail) this.getIntent()
				.getSerializableExtra("customerMeetingDetail");
		tYPE = this.getIntent().getIntExtra("TYPE", -1);
		meetid = this.getIntent().getLongExtra("MeetId", -1);
		showColorImage = this.getIntent().getIntExtra("showColorImage", 0);
		if (showColorImage != 0) {
			schedule_classification_image.setImageResource(showColorImage);
		}
		if (customerMeetingDetail != null) {
			infomation_text.setText(customerMeetingDetail.title);
			if (customerMeetingDetail.time != null) {
				times_text.setText(customerMeetingDetail.time + " "
						+ customerMeetingDetail.meetDate);
			}
			remark_text.setText(customerMeetingDetail.content);
			remind_time_text.setText(customerMeetingDetail.remindTime);
			schedule_classification_text.setText(customerMeetingDetail.color);
			if ("0".equals(customerMeetingDetail.repeadType)) {
				monthly_text.setText("不提醒");
			} else if ("1".equals(customerMeetingDetail.repeadType)) {
				monthly_text.setText("每天");
			} else if ("2".equals(customerMeetingDetail.repeadType)) {
				monthly_text.setText("每周");
			} else if ("3".equals(customerMeetingDetail.repeadType)) {
				monthly_text.setText("每月");
			} else if ("4".equals(customerMeetingDetail.repeadType)) {
				monthly_text.setText("每年");
			}
			if ("0".equals(customerMeetingDetail.color)) {
				schedule_classification_text.setText("无颜色");
			} else if ("1".equals(customerMeetingDetail.color)) {
				schedule_classification_text.setText("绿色");
			} else if ("2".equals(customerMeetingDetail.color)) {
				schedule_classification_text.setText("蓝色");
			} else if ("3".equals(customerMeetingDetail.color)) {
				schedule_classification_text.setText("橙色");
			} else if ("4".equals(customerMeetingDetail.color)) {
				schedule_classification_text.setText("红色");
			}
			relatedInformation = customerMeetingDetail.relevance;

		}
		initListViewData();
		String repeadType = this.getIntent().getStringExtra("repeadType");
		monthly_text.setText(repeadType);
	}

	// 四大组件
	private void initListViewData() {
		if (relatedInformation != null) {
			peopleGroupAdapter = new ConnectionsGroupAdapter(this,
					relatedInformation.p);
			people.setAdapter(peopleGroupAdapter);
			organizationGroupAdapter = new ConnectionsGroupAdapter(this,
					relatedInformation.o);
			organization.setAdapter(organizationGroupAdapter);
			knowledgeGroupAdapter = new KnowledgeGroupAdapter(this,
					relatedInformation.k);
			knowledge.setAdapter(knowledgeGroupAdapter);
			requirementGroupAdapter = new RequirementGroupAdapter(this,
					relatedInformation.r);
			requirement.setAdapter(requirementGroupAdapter);
			updateAllUI();
		}
	}

	public void updateAllUI() {
		if (!relatedInformation.p.isEmpty()) {
			people_Ll.setVisibility(View.VISIBLE);
			peopleGroupAdapter
					.setListRelatedConnectionsNode(relatedInformation.p);
			peopleGroupAdapter.notifyDataSetChanged();
		}
		if (!relatedInformation.o.isEmpty()) {
			organization_Ll.setVisibility(View.VISIBLE);
			organizationGroupAdapter
					.setListRelatedConnectionsNode(relatedInformation.o);
			organizationGroupAdapter.notifyDataSetChanged();
		}
		if (!relatedInformation.k.isEmpty()) {
			knowledge_Ll.setVisibility(View.VISIBLE);
			knowledgeGroupAdapter
					.setListRelatedKnowledgeNode(relatedInformation.k);
			knowledgeGroupAdapter.notifyDataSetChanged();
		}
		if (!relatedInformation.r.isEmpty()) {
			requirement_Ll.setVisibility(View.VISIBLE);
			requirementGroupAdapter
					.setListRelatedAffairNode(relatedInformation.r);
			requirementGroupAdapter.notifyDataSetChanged();
		}

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

			viewHolder.deleteIv.setVisibility(View.INVISIBLE);

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

			viewHolder.deleteIv.setVisibility(View.INVISIBLE);

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

			viewHolder.deleteIv.setVisibility(View.INVISIBLE);

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.meeting_details, menu);
		return true;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.meeting_back:
			// Intent intent = new Intent(this, ContactsDetailsActivity.class);
			// intent.putExtra("Meeting", true);
			// startActivityForResult(intent, 0);
			finish();
			break;

		case R.id.meeting_more_image:

			dlg = new AlertDialog.Builder(this).create();
			dlg.show();
			dlg.setCanceledOnTouchOutside(true);
			window = dlg.getWindow();
			// *** 主要就是在这里实现这种效果的.
			// 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
			window.setContentView(R.layout.people_meeting_more_choose);

			WindowManager.LayoutParams lp = window.getAttributes();

			window.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);

			lp.y = 20;

			window.setAttributes(lp);

			delete_meeting_contant = (TextView) window
					.findViewById(R.id.delete_meeting_contant);

			delete_meeting_contant
					.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {

							dlg.dismiss();

							builder = new Builder(MeetingDetailsActivity.this);

							dialog = builder.show();

							window = dialog.getWindow();

							window.setContentView(R.layout.people_delete_meeting);

							meeting_delete_yes = (TextView) window
									.findViewById(R.id.meeting_delete_yes);
							meeting_delete_yes
									.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {
											// Intent intent = new Intent(
											// MeetingDetailsActivity.this,
											// ContactsDetailsActivity.class);
											// intent.putExtra("Meeting", true);
											// startActivityForResult(intent,
											// 0);
											dialog.dismiss();
											CustomerMeet_Delete delete = new CustomerMeet_Delete();
											Organ_Id people_delete = new Organ_Id();
											if (meet_deleteid != -1) {
												delete.detailId = meet_deleteid;
												people_delete.id = meet_deleteid;
											} else if (meetid != -1) {
												delete.detailId = meetid;
												people_delete.id = meetid;
											}
											if (tYPE == ENavConsts.CLIENT) {
												OrganizationReqUtil
														.doRequestWebAPI(
																MeetingDetailsActivity.this,
																MeetingDetailsActivity.this,
																delete,
																null,
																OrganizationReqType.MEET_DELETEBYID);
											} else if (tYPE == ENavConsts.PEOPLE) {
												PeopleReqUtil
														.doRequestWebAPI(
																MeetingDetailsActivity.this,
																MeetingDetailsActivity.this,
																people_delete,
																null,
																PeopleRequestType.PEOPLE_REQ_MEET_DELETE);
											}
										
										}
									});

							meeting_delete_no = (TextView) window
									.findViewById(R.id.meeting_delete_no);
							meeting_delete_no
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {

											dialog.dismiss();

										}
									});
						}
					});

			delete_meeting_edit = (TextView) window
					.findViewById(R.id.delete_meeting_edit);
			delete_meeting_edit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MeetingDetailsActivity.this,
							NewCreateActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("customerMeetingDetail",
							customerMeetingDetail);
					intent.putExtra("showColorImage", showColorImage);
					intent.putExtra("OneMeetID", meet_deleteid);
					intent.putExtra("TYPE", tYPE);
					intent.putExtra("Org_Id", id);
					intent.putExtras(bundle);
					startActivityForResult(intent, 9);
					finish();
				}
			});

			delete_meeting_cancel = (TextView) window
					.findViewById(R.id.delete_meeting_cancel);

			delete_meeting_cancel
					.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							dlg.dismiss();
						}
					});

			break;

		}

	}

	@Override
	public void bindData(int tag, Object object) {
		boolean success = (Boolean) object ;
		if (success) {
			Toast.makeText(
					MeetingDetailsActivity.this,
					"删除成功", Toast.LENGTH_SHORT)
					.show();
			finish();
		}
	}

}
