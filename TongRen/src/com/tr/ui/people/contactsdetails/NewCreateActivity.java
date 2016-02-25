package com.tr.ui.people.contactsdetails;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.tr.navigate.ENavigate;
import com.tr.ui.common.RelatedResourceActivity;
import com.tr.ui.common.JointResourceFragment.ResourceType;
import com.tr.ui.conference.initiatorhy.ConferenceIntroduceActivity;
import com.tr.ui.conference.initiatorhy.LocationActivity;
import com.tr.ui.knowledge.CreateKnowledgeActivity;
import com.tr.ui.knowledge.CreateKnowledgeActivity.ConnectionsGroupAdapter;
import com.tr.ui.organization.GetOrgIdUtils;
import com.tr.ui.organization.create_clientele.CreateClienteleActivity;
import com.tr.ui.organization.create_clientele.CreateClienteleActivity.KnowledgeGroupAdapter;
import com.tr.ui.organization.create_clientele.CreateClienteleActivity.RequirementGroupAdapter;
import com.tr.ui.organization.model.GetId;
import com.tr.ui.organization.model.meet.CustomerMeetingDetail;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.cread.RemarkActivity;
import com.tr.ui.people.model.MeetSave;
import com.tr.ui.widgets.BasicListView2;
import com.utils.common.EConsts;
import com.utils.http.EAPIConsts.OrganizationReqType;
import com.utils.http.EAPIConsts.PeopleRequestType;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;

/**
 * 添加会面
 * 
 * @author User
 * 
 */
public class NewCreateActivity extends BaseActivity implements OnClickListener,
		OnItemClickListener, IBindData {

	public static final int REQUEST_CODE_KNOWLEDGE_CONTENT_ACTIVITY = 1001;
	public static final int REQUEST_CODE_RELATED_RESOURCE_ACTIVITY = 1002;
	public static final int REQUEST_CODE_GLOBAL_KNOWLEDGE_CATEGORY_ACTIVITY = 1003;
	public static final int REQUEST_CODE_GLOBAL_KNOWLEDGE_TAG_ACTIVITY = 1004;
	public static final int REQUEST_CODE_KNOWLEDGE_PERMISSION_ACTIVITY = 1005;

	public static final int REQUEST_CODE_RELATED_RESOURCE_PEOPLE = 2001;
	public static final int REQUEST_CODE_RELATED_RESOURCE_ORGANIZATION = 2002;
	public static final int REQUEST_CODE_RELATED_RESOURCE_KNOWLEDGE = 2003;
	public static final int REQUEST_CODE_RELATED_RESOURCE_AFFAIR = 2004;

	private Knowledge2 knowledge2;

	private int requestCode = 0;

	public static final int STATE_ADD = 0;
	public static final int STATE_EDIT = 1;

	public int currentRequestCode = 0;
	public int currentRequestState = STATE_ADD;
	public int currentRequestEditPosition = -1;

	private ImageView tell_text_image, tell_image,
			schedule_classification_image, plusImage;

	private LinearLayout time_LinearLayout, color_LinearLayout,
			map_LinearLayout, remark_LinearLayout, addMeetingContent;

	private Intent intent;

	private TextView addMetting_complete, times_text, monthly_text,
			remind_time_text, schedule_classification_text

			;

	private AlertDialog.Builder builder;

	private Window window;

	private String showTime, showReminder, showReperat, showColor;

	private int showColorImage;

	private ImageView addbackimage;
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
	private ArrayList<ConnectionNode> connectionNodeList;
	private ArrayList<ConnectionNode> connectionNodeList2;
	private ArrayList<KnowledgeNode> knowledgeNodeList;
	private ArrayList<AffairNode> affairNodeList;
	private static String decollatorStr = "、";
	private EditText client_shuodianshenm;
	// private GetId getId2;
	// private long org;
	private CustomerMeetingDetail customerMeetingDetail;
	private String hour;
	private String address;
	private Boolean isSuccess = false;
	private long id;
	private ASSORPOK relatedInformation;
	private long onemeetid;
	private long delay;
	private long period;
	public Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			// 得到我们选择的铃声
			Uri pickedUri = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
			// 将我们选择的铃声设置成为默认
			if (pickedUri != null) {
				RingtoneManager.setActualDefaultRingtoneUri(context,
						RingtoneManager.TYPE_ALARM, pickedUri);

				mMediaPlayer = new MediaPlayer();
				try {
					mMediaPlayer.setDataSource(context, pickedUri);
					// final AudioManager audioManager = (AudioManager)
					// mContext.getSystemService(Context.AUDIO_SERVICE);
					mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
					mMediaPlayer.setLooping(true);
					mMediaPlayer.prepare();
					mMediaPlayer.start();
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					vibrator = (Vibrator) context
							.getSystemService(Context.VIBRATOR_SERVICE);
					long[] pattern = { 800, 150, 400, 130 }; // OFF/ON/OFF/ON...
					vibrator.vibrate(pattern, 2);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	};
	private MediaPlayer player;
	private int max;
	private int current;
	private MediaPlayer mMediaPlayer;
	private Vibrator vibrator;
	private long timeLong;
	private int type;
	private MeetSave meetsave;

	private long meetid;
	private String remark;
	private TextView remark_Tv;
	private RelativeLayout plusImage_Rl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.client_activity_new_create);
		initVars();
		client_shuodianshenm = (EditText) findViewById(R.id.client_shuodianshenm);
		
		id = this.getIntent().getLongExtra("Org_Id", 1);
		meetid = this.getIntent().getLongExtra("OneMeetID", -1);
		type = this.getIntent().getIntExtra("TYPE", -1);

		knowledge2 = new Knowledge2();
		tell_text_image = (ImageView) findViewById(R.id.tell_text_image);
		tell_text_image.setOnClickListener(this);

		
		tell_image = (ImageView) findViewById(R.id.tell_image);
		addbackimage = (ImageView) findViewById(R.id.addmeetBackIv);
		addbackimage.setOnClickListener(this);
		tell_image.setOnClickListener(this);
		remark_Tv = (TextView) findViewById(R.id.remark_Tv);
		time_LinearLayout = (LinearLayout) findViewById(R.id.time_LinearLayout);
		time_LinearLayout.setOnClickListener(this);

		color_LinearLayout = (LinearLayout) findViewById(R.id.color_LinearLayout);
		color_LinearLayout.setOnClickListener(this);

		// map_LinearLayout = (LinearLayout)
		// findViewById(R.id.map_LinearLayout);
		// map_LinearLayout.setOnClickListener(this);

		remark_LinearLayout = (LinearLayout) findViewById(R.id.remark_LinearLayout);
		remark_LinearLayout.setOnClickListener(this);

		addMetting_complete = (TextView) findViewById(R.id.addMetting_complete);
		addMetting_complete.setOnClickListener(this);

		times_text = (TextView) findViewById(R.id.times_text);

		monthly_text = (TextView) findViewById(R.id.monthly_text);

		remind_time_text = (TextView) findViewById(R.id.remind_time_text);

		schedule_classification_image = (ImageView) findViewById(R.id.schedule_classification_image);

		schedule_classification_text = (TextView) findViewById(R.id.schedule_classification_text);

		connectionNodeList = new ArrayList<ConnectionNode>();
		connectionNodeList2 = new ArrayList<ConnectionNode>();
		knowledgeNodeList = new ArrayList<KnowledgeNode>();
		affairNodeList = new ArrayList<AffairNode>();

		plusImage = (ImageView) findViewById(R.id.plusImage);
	
		plusImage_Rl = (RelativeLayout)	findViewById(R.id.plusImage_Rl);
		plusImage_Rl.setOnClickListener(this);
		people = (BasicListView2) findViewById(R.id.people_meeting);
		organization = (BasicListView2) findViewById(R.id.organization_meeting);
		knowledge = (BasicListView2) findViewById(R.id.knowledge_meeting);
		requirement = (BasicListView2) findViewById(R.id.requirement_meeting);
		people_Ll = (LinearLayout) findViewById(R.id.people_meeting_Ll);
		organization_Ll = (LinearLayout) findViewById(R.id.organization_meeting_Ll);
		knowledge_Ll = (LinearLayout) findViewById(R.id.knowledge_meeting_Ll);
		requirement_Ll = (LinearLayout) findViewById(R.id.requirement_meeting_Ll);

		showColorImage = this.getIntent().getIntExtra("showColorImage", 0);
		if (showColorImage != 0) {
			schedule_classification_image.setImageResource(showColorImage);
		}
		CustomerMeetingDetail meet = (CustomerMeetingDetail) this.getIntent()
				.getSerializableExtra("customerMeetingDetail");
		if (meet != null) {
			client_shuodianshenm.setText(meet.title);
			if (meet.time != null) {
				times_text.setText(meet.time + " " + meet.meetDate);
			}
			remind_time_text.setText(meet.remindTime);
			schedule_classification_text.setText(meet.color);
			if ("0".equals(meet.repeadType)) {
				monthly_text.setText("不提醒");
			} else if ("1".equals(meet.repeadType)) {
				monthly_text.setText("每天");
			} else if ("2".equals(meet.repeadType)) {
				monthly_text.setText("每周");
			} else if ("3".equals(meet.repeadType)) {
				monthly_text.setText("每月");
			} else if ("4".equals(meet.repeadType)) {
				monthly_text.setText("每年");
			}
			
			if ("0".equals(meet.color)) {
				schedule_classification_text.setText("无颜色");
			} else if ("1".equals(meet.color)) {
				schedule_classification_text.setText("绿色");
			} else if ("2".equals(meet.color)) {
				schedule_classification_text.setText("蓝色");
			} else if ("3".equals(meet.color)) {
				schedule_classification_text.setText("橙色");
			} else if ("4".equals(meet.color)) {
				schedule_classification_text.setText("红色");
			}
			relatedInformation = meet.relevance;
			createKnowNewASSO(relatedInformation);
			if (!relatedInformation.p.isEmpty()) {
				people_Ll.setVisibility(View.VISIBLE);
			}
			if (!relatedInformation.r.isEmpty()) {
				requirement_Ll.setVisibility(View.VISIBLE);
			}
			if (!relatedInformation.o.isEmpty()) {
				organization_Ll.setVisibility(View.VISIBLE);
			}
			if (!relatedInformation.k.isEmpty()) {
				knowledge_Ll.setVisibility(View.VISIBLE);
			}
		}
		initListViewData();
	}

	public ASSORPOK createNewASSO() {
		DemandASSO asso = new DemandASSO();
		List<com.tr.model.demand.ASSOData> p = new ArrayList<com.tr.model.demand.ASSOData>();
		// 人脉信息
		if (connectionNodeList != null) {
			for (ConnectionNode node : connectionNodeList) {
				List<DemandASSOData> conn = new ArrayList<DemandASSOData>();
				for (Connections obj : node.getListConnections()) {
					DemandASSOData assoData = new DemandASSOData();
					assoData.type = 2;
					assoData.id = obj.getId();
					assoData.name = obj.getName();
					assoData.ownerid = App.getUserID();
					assoData.ownername = App.getNick();
					assoData.career = obj.getCareer();
					assoData.company = obj.getCompany();
					conn.add(assoData);
				}
				p.add(new ASSOData(node.getMemo(), conn));
			}
		}
		List<ASSOData> o = new ArrayList<ASSOData>();
		// 组织信息
		if (connectionNodeList2 != null) {
			for (ConnectionNode node : connectionNodeList2) {
				List<DemandASSOData> conn = new ArrayList<DemandASSOData>();
				for (Connections obj : node.getListConnections()) {
					DemandASSOData assoData = new DemandASSOData();
					assoData.type = 4; //
					assoData.id = obj.getId();
					assoData.name = obj.getName();
					assoData.ownerid = App.getUserID();
					assoData.ownername = App.getNick();
					// 当前组织没有行业和地址
					// assoData.setAddress(obj.getAttribute());
					// assoData.setHy(obj.getOrganizationMini());
					conn.add(assoData);
				}
				o.add(new ASSOData(node.getMemo(), conn));
			}
		}
		// 知识
		List<ASSOData> k = new ArrayList<ASSOData>();
		if (knowledgeNodeList != null) {
			for (KnowledgeNode node : knowledgeNodeList) {
				List<DemandASSOData> conn = new ArrayList<DemandASSOData>();
				for (KnowledgeMini2 obj : node.getListKnowledgeMini2()) {
					DemandASSOData assoData = new DemandASSOData();
					assoData.type = 6; //
					assoData.id = obj.id + "";
					assoData.title = obj.title;
					// assoData.setOwnerid(App.getUserID()); //创建id 没有
					// assoData.setOwnername(App.getNick());// 创建人 没有
					assoData.columnpath = obj.columnpath;
					assoData.columntype = obj.type + "";//
					conn.add(assoData);
				}
				k.add(new ASSOData(node.getMemo(), conn));
			}
		}
		// 事件 （需求）
		List<ASSOData> r = new ArrayList<ASSOData>();
		if (affairNodeList != null) {
			for (AffairNode node : affairNodeList) {
				List<DemandASSOData> conn = new ArrayList<DemandASSOData>();
				for (AffairsMini obj : node.getListAffairMini()) {
					DemandASSOData assoData = new DemandASSOData();
					assoData.type = 1; //
					assoData.id = obj.id + "";
					assoData.title = obj.title;
					assoData.name = obj.name;
					assoData.ownerid = App.getUserID();
					assoData.ownername = App.getNick();
					assoData.requirementtype = obj.reserve; // 事件类型
					conn.add(assoData);
				}
				r.add(new ASSOData(node.getMemo(), conn));
			}
		}
		asso.value = new ASSORPOK(r, p, o, k);
		return asso.value;

	}

	public void createKnowNewASSO(ASSORPOK relatedInformation2) {
		if (!relatedInformation2.p.isEmpty()) {

			ConnectionNode Pnode = new ConnectionNode();
			ArrayList<Connections> ParrayList = new ArrayList<Connections>();

			List<ASSOData> Pass = relatedInformation2.p;
			for (ASSOData assoData : Pass) {
				Connections Pobj = new Connections();
				List<DemandASSOData> conn2 = assoData.conn;
				Pnode.setMemo(assoData.tag);
				StringBuffer buffer = new StringBuffer();
				for (int j = 0; j < conn2.size(); j++) {
					String name = conn2.get(j).name;
					buffer.append(name);
					if (j != conn2.size() - 1) {
						buffer.append(decollatorStr);
					}
				}
				Pobj.setName(buffer.toString());
				ParrayList.add(Pobj);
			}
			Pnode.setListConnections(ParrayList);
			connectionNodeList.add(Pnode);
			knowledge2.setListRelatedConnectionsNode(connectionNodeList);

		}
		if (!relatedInformation2.o.isEmpty()) {

			ConnectionNode Onode = new ConnectionNode();
			ArrayList<Connections> OarrayList = new ArrayList<Connections>();

			List<ASSOData> Oass = relatedInformation2.o;
			for (ASSOData assoData : Oass) {
				Connections Oobj = new Connections();
				List<DemandASSOData> conn2 = assoData.conn;
				Onode.setMemo(assoData.tag);

				StringBuffer buffer = new StringBuffer();
				for (int j = 0; j < conn2.size(); j++) {
					String name = conn2.get(j).name;
					buffer.append(name);
					if (j != conn2.size() - 1) {
						buffer.append(decollatorStr);
					}
				}
				Oobj.setName(buffer.toString());
				OarrayList.add(Oobj);
			}
			Onode.setListConnections(OarrayList);
			connectionNodeList2.add(Onode);
			knowledge2.setListRelatedConnectionsNode(connectionNodeList2);

		}
		if (!relatedInformation2.k.isEmpty()) {

			KnowledgeNode Knode = new KnowledgeNode();
			ArrayList<KnowledgeMini2> KarrayList = new ArrayList<KnowledgeMini2>();

			List<ASSOData> Kass = relatedInformation2.k;
			for (ASSOData assoData : Kass) {
				KnowledgeMini2 Kobj = new KnowledgeMini2();
				List<DemandASSOData> conn2 = assoData.conn;
				Knode.setMemo(assoData.tag);

				StringBuffer buffer = new StringBuffer();
				for (int j = 0; j < conn2.size(); j++) {
					String name = conn2.get(j).title;
					buffer.append(name);
					if (j != conn2.size() - 1) {
						buffer.append(decollatorStr);
					}
				}
				Kobj.title = buffer.toString();

			}
			Knode.setListKnowledgeMini2(KarrayList);
			knowledgeNodeList.add(Knode);
			knowledge2.setListRelatedKnowledgeNode(knowledgeNodeList);

		}
		if (!relatedInformation2.r.isEmpty()) {

			AffairNode Anode = new AffairNode();
			ArrayList<AffairsMini> AarrayList = new ArrayList<AffairsMini>();

			List<ASSOData> Aass = relatedInformation2.r;
			for (ASSOData assoData : Aass) {
				AffairsMini Aobj = new AffairsMini();
				List<DemandASSOData> conn2 = assoData.conn;
				Anode.setMemo(assoData.tag);
				StringBuffer buffer = new StringBuffer();
				for (int j = 0; j < conn2.size(); j++) {
					String name = conn2.get(j).title;
					buffer.append(name);
					if (j != conn2.size() - 1) {
						buffer.append(decollatorStr);
					}
				}
				Aobj.title = buffer.toString();
				AarrayList.add(Aobj);
			}
			Anode.setListAffairMini(AarrayList);
			affairNodeList.add(Anode);
			knowledge2.setListRelatedAffairNode(affairNodeList);

		}
	}

	// 四大组件
	private void initListViewData() {
		Knowledge2 knowledge1 = (Knowledge2) getIntent().getSerializableExtra(
				EConsts.Key.KNOWLEDGE2);
		if (knowledge1 != null) {
			knowledge2 = knowledge1;
			// 暂时不进行处理
			// knowledge2.setContent(EUtil.filterHtml(knowledge2.getContent()));
		}

		knowledge2.setType(1);
		knowledge2.setSaved(true);
		knowledge2.setTaskId("");

		people.setOnItemClickListener(this);
		peopleGroupAdapter = new ConnectionsGroupAdapter(this,
				knowledge2.getListRelatedConnectionsNode());
		people.setAdapter(peopleGroupAdapter);
		organization.setOnItemClickListener(this);
		organizationGroupAdapter = new ConnectionsGroupAdapter(this,
				knowledge2.getListRelatedOrganizationNode());
		organization.setAdapter(organizationGroupAdapter);
		knowledge.setOnItemClickListener(this);
		knowledgeGroupAdapter = new KnowledgeGroupAdapter(this,
				knowledge2.getListRelatedKnowledgeNode());
		knowledge.setAdapter(knowledgeGroupAdapter);
		requirement.setOnItemClickListener(this);
		requirementGroupAdapter = new RequirementGroupAdapter(this,
				knowledge2.getListRelatedAffairNode());
		requirement.setAdapter(requirementGroupAdapter);
		updateAllUI();
	}

	public void updateAllUI() {
		if (knowledge2 == null) {
			return;
		}

		// 更新
		peopleGroupAdapter.setListRelatedConnectionsNode(knowledge2
				.getListRelatedConnectionsNode());
		peopleGroupAdapter.notifyDataSetChanged();

		organizationGroupAdapter.setListRelatedConnectionsNode(knowledge2
				.getListRelatedOrganizationNode());
		organizationGroupAdapter.notifyDataSetChanged();
		knowledgeGroupAdapter.setListRelatedKnowledgeNode(knowledge2
				.getListRelatedKnowledgeNode());
		knowledgeGroupAdapter.notifyDataSetChanged();
		requirementGroupAdapter.setListRelatedAffairNode(knowledge2
				.getListRelatedAffairNode());
		requirementGroupAdapter.notifyDataSetChanged();

	}

	private void initVars() {

		String MSG = "initVars()";

		knowledge2 = new Knowledge2();

		Knowledge2 knowledge = (Knowledge2) getIntent().getSerializableExtra(
				EConsts.Key.KNOWLEDGE2);
		if (knowledge != null) {
			knowledge2 = knowledge;
			// 暂时不进行处理
			// knowledge2.setContent(EUtil.filterHtml(knowledge2.getContent()));
		}

		knowledge2.setType(1);
		knowledge2.setSaved(true);
		knowledge2.setTaskId("");

		// 请求码
		requestCode = getIntent().getIntExtra(EConsts.Key.REQUEST_CODE, 0);

	}

	public class ConnectionsGroupAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<ConnectionNode> listRelatedConnectionsNode;

		public ConnectionsGroupAdapter() {
			super();
		}

		public ConnectionsGroupAdapter(Context context,
				ArrayList<ConnectionNode> listRelatedConnectionsNode) {
			super();
			this.context = context;
			if (listRelatedConnectionsNode != null) {
				this.listRelatedConnectionsNode = listRelatedConnectionsNode;
			} else {
				this.listRelatedConnectionsNode = new ArrayList<ConnectionNode>();
			}
		}

		public ArrayList<ConnectionNode> getListRelatedConnectionsNode() {
			return listRelatedConnectionsNode;
		}

		public void setListRelatedConnectionsNode(
				ArrayList<ConnectionNode> listRelatedConnectionsNode) {
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

			viewHolder.deleteIv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					listRelatedConnectionsNode.remove(position);
					notifyDataSetChanged();
					if (listRelatedConnectionsNode.isEmpty()) {
						LinearLayout layout = (LinearLayout) parent.getParent();
						layout.setVisibility(View.GONE);
					}
				}
			});

			ConnectionNode connectionNode = listRelatedConnectionsNode
					.get(position);
			String key = connectionNode.getMemo();
			viewHolder.keyTv.setText(key + " : ");
			ArrayList<Connections> listConnections = connectionNode
					.getListConnections();
			StringBuilder valueSb = new StringBuilder();
			for (int i = 0; i < listConnections.size(); i++) {
				valueSb.append(listConnections.get(i).getName());
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
		private ArrayList<KnowledgeNode> listRelatedKnowledgeNode;

		public KnowledgeGroupAdapter() {
			super();
		}

		public KnowledgeGroupAdapter(Context context,
				ArrayList<KnowledgeNode> listRelatedKnowledgeNode) {
			super();
			this.context = context;
			if (listRelatedKnowledgeNode != null) {
				this.listRelatedKnowledgeNode = listRelatedKnowledgeNode;
			} else {
				this.listRelatedKnowledgeNode = new ArrayList<KnowledgeNode>();
			}
		}

		public ArrayList<KnowledgeNode> getListRelatedKnowledgeNode() {
			return listRelatedKnowledgeNode;
		}

		public void setListRelatedKnowledgeNode(
				ArrayList<KnowledgeNode> listRelatedKnowledgeNode) {
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

			viewHolder.deleteIv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					listRelatedKnowledgeNode.remove(position);
					notifyDataSetChanged();
					if (listRelatedKnowledgeNode.isEmpty()) {
						LinearLayout layout = (LinearLayout) parent.getParent();
						layout.setVisibility(View.GONE);
					}
				}
			});

			KnowledgeNode knowledgeNode = listRelatedKnowledgeNode
					.get(position);
			String key = knowledgeNode.getMemo();
			viewHolder.keyTv.setText(key + " : ");
			ArrayList<KnowledgeMini2> listKnowledgeMini2 = knowledgeNode
					.getListKnowledgeMini2();
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
		private ArrayList<AffairNode> listRelatedAffairNode;

		public RequirementGroupAdapter() {
			super();
		}

		public RequirementGroupAdapter(Context context,
				ArrayList<AffairNode> listRelatedAffairNode) {
			super();
			this.context = context;
			if (listRelatedAffairNode != null) {
				this.listRelatedAffairNode = listRelatedAffairNode;
			} else {
				this.listRelatedAffairNode = new ArrayList<AffairNode>();
			}
		}

		public ArrayList<AffairNode> getListRelatedAffairNode() {
			return listRelatedAffairNode;
		}

		public void setListRelatedAffairNode(
				ArrayList<AffairNode> listRelatedAffairNode) {
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

			viewHolder.deleteIv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					listRelatedAffairNode.remove(position);
					notifyDataSetChanged();
					if (listRelatedAffairNode.isEmpty()) {
						LinearLayout layout = (LinearLayout) parent.getParent();
						layout.setVisibility(View.GONE);
					}
				}
			});

			AffairNode affairNode = listRelatedAffairNode.get(position);
			String key = affairNode.getMemo();
			viewHolder.keyTv.setText(key + " : ");
			ArrayList<AffairsMini> listaAffairsMini = affairNode
					.getListAffairMini();
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
		getMenuInflater().inflate(R.menu.new_create, menu);
		return true;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		// 点击云转文字,金桐需求在写
		case R.id.tell_image:

			break;
		// 点击录音功能，金桐需求再写

		case R.id.tell_text_image:

			break;
		case R.id.plusImage_Rl:// 跳转套关联资源界面
			if (!TextUtils.isEmpty(client_shuodianshenm.getText().toString())) {
				ENavigate.startRelatedResourceActivityForResult(
						NewCreateActivity.this, 10001, client_shuodianshenm.getText().toString(), ResourceType.People,
						null);
			}else{
				Toast.makeText(context, "标题不能为空，请填写后关联资源", 0).show();
			}
			
			break;

		// 时间选择器界面
		case R.id.time_LinearLayout:

			intent = new Intent(NewCreateActivity.this, TimeActivity.class);

			startActivityForResult(intent, 1);

			break;

		case R.id.color_LinearLayout:

			intent = new Intent(NewCreateActivity.this, ColorActivity.class);

			startActivityForResult(intent, 2);

			break;
		// 地图界面
		case R.id.map_LinearLayout:

			// intent = new Intent(NewCreateActivity.this,
			// LocationActivity.class);
			//
			// startActivityForResult(intent, 3);

			break;
		// 备注界面
		case R.id.remark_LinearLayout:
			Intent intent = new Intent(this, RemarkActivity.class);
			startActivityForResult(intent, 1006);
			break;
		// 点击完成跳转到会面详情界面
		case R.id.addMetting_complete:
			customerMeetingDetail = new CustomerMeetingDetail();
			customerMeetingDetail.time = showTime;
			ASSORPOK createNewASSO = createNewASSO();
			customerMeetingDetail.relevance = createNewASSO;
			customerMeetingDetail.meetDate = hour;
			if ("无颜色".equals(showColor)) {
				customerMeetingDetail.color = "0";
			} else if ("绿色".equals(showColor)) {
				customerMeetingDetail.color = "1";
			} else if ("蓝色".equals(showColor)) {
				customerMeetingDetail.color = "2";
			} else if ("橙色".equals(showColor)) {
				customerMeetingDetail.color = "3";
			} else if ("红色".equals(showColor)) {
				customerMeetingDetail.color = "4";
			}
			if ("不提醒".equals(showReperat)) {
				customerMeetingDetail.repeadType = "0";
			} else if ("每天".equals(showReperat)) {
				customerMeetingDetail.repeadType = "1";
			} else if ("每周".equals(showReperat)) {
				customerMeetingDetail.repeadType = "2";
			} else if ("每月".equals(showReperat)) {
				customerMeetingDetail.repeadType = "3";
			} else if ("每年".equals(showReperat)) {
				customerMeetingDetail.repeadType = "4";
			}

			customerMeetingDetail.remindTime = showReminder;
			if (!TextUtils.isEmpty(showReminder)) {
				if (showReminder.contains("分钟")) {
					customerMeetingDetail.remindType = "1";
				} else if (showReminder.contains("小时")) {
					customerMeetingDetail.remindType = "2";
				} else if (showReminder.contains("天")) {
					customerMeetingDetail.remindType = "3";
				}
			} else {
				customerMeetingDetail.remindType = "0";
			}
			customerMeetingDetail.content = remark;
			customerMeetingDetail.address = address;
			customerMeetingDetail.title = client_shuodianshenm.getText()
					.toString().trim();

			if (type == ENavConsts.CLIENT) {
				if (customerMeetingDetail != null) {
					customerMeetingDetail.customerId = id;
					if (meetid != -1) {
						customerMeetingDetail.id = meetid;
					}
					OrganizationReqUtil.doRequestWebAPI(this, this,
							customerMeetingDetail, null,
							OrganizationReqType.MEET_SAVE);

				} else {
					finish();
				}

			} else if (type == ENavConsts.PEOPLE) {
				if (customerMeetingDetail != null) {
					customerMeetingDetail.personId = id;
					if (meetid != -1) {
						customerMeetingDetail.id = meetid;
					}
					PeopleReqUtil.doRequestWebAPI(this, this,
							customerMeetingDetail, null,
							PeopleRequestType.PEOPLE_REQ_MEET_SAVE);

				} else {
					finish();
				}

			}

			break;
		case R.id.addmeetBackIv:
			finish();

		}

	}

	// 处理返回来的数据
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {

			switch (resultCode) {

			case 1:

				showTime = data.getStringExtra("TIMES").toString();
				hour = data.getStringExtra("HOURS");
				times_text.setText(showTime + " " + hour);
				showReminder = data.getStringExtra("REMINDERTIMES").toString();
				remind_time_text.setText(showReminder);

				showReperat = data.getStringExtra("REPEATS").toString();
				monthly_text.setText(showReperat);
				String shijian = showTime + hour;
				// 设定时间的模板
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH:mm");
				// 得到指定模范的时间
				try {
					Date d1 = sdf.parse(shijian);
					Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
					timeLong = d1.getTime() - curDate.getTime();
					System.out
							.println(timeLong
									+ "timeLongtimeLongtimeLongtimeLongtimeLongtimeLongtimeLongtimeLongtimeLongtimeLongtimeLongtimeLongtimeLongtimeLong");
				} catch (ParseException e) {
					e.printStackTrace();
				}
				long time = data.getLongExtra("TIME", 0);
				if (showReminder.contains("分钟")) {
					delay = timeLong - time * 60000;
				} else if (showReminder.contains("小时")) {
					delay = timeLong - time * 3600000;
				} else if (showReminder.contains("天")) {
					delay = timeLong - time * 86400000;
				}
				if (showReperat.contains("每天")) {
					period = 86400000;
				} else if (showReperat.contains("每周")) {
					period = 604800000;
				} else if (showReperat.contains("每月")) {
					period = 2678400000l;
				} else if (showReperat.contains("每年")) {
					period = Long.MAX_VALUE;
				}
				// if delay < 0 or period <= 0.
				if (delay != 0 && period > 0) {
					Timer timer = new Timer();
					timer.schedule(new TimerTask() {
						@Override
						public void run() {
							handler.sendEmptyMessage(0);
						}
					}, delay, period);
				} else if (delay != 0) {
					Timer timer = new Timer();
					timer.schedule(new TimerTask() {

						@Override
						public void run() {
							handler.sendEmptyMessage(0);
						}
					}, delay);
				}

				// 动态添加控件

				break;
			case 2:

				showColorImage = data.getIntExtra("COLORIMAGE", -1);
				schedule_classification_image.setImageResource(showColorImage);

				showColor = data.getStringExtra("COLOR").toString();
				schedule_classification_text.setText(showColor);

				break;

			}
			if (1006 == requestCode) { //
				remark = data.getStringExtra("Remark_Activity");
				remark_Tv.setText(remark);
			}
			if (10001 == requestCode) { //
				// 关联资源
				if (resultCode == Activity.RESULT_OK) {

					if (data.hasExtra(EConsts.Key.RELATED_PEOPLE_NODE)) {
						// 数据去重
						ConnectionNode connectionNode = (ConnectionNode) data
								.getSerializableExtra(EConsts.Key.RELATED_PEOPLE_NODE);
						connectionNodeList = knowledge2
								.getListRelatedConnectionsNode();

						if (currentRequestState == STATE_EDIT
								& currentRequestCode == REQUEST_CODE_RELATED_RESOURCE_PEOPLE) {
							connectionNodeList.set(currentRequestEditPosition,
									connectionNode);
						} else {
							connectionNodeList.add(connectionNode);
						}
						people_Ll.setVisibility(View.VISIBLE);
						peopleGroupAdapter
								.setListRelatedConnectionsNode(knowledge2
										.getListRelatedConnectionsNode());
						peopleGroupAdapter.notifyDataSetChanged();
					}

					// 相关资源
					if (data.hasExtra(EConsts.Key.RELATED_ORGANIZATION_NODE)) {
						// 数据去重
						ConnectionNode connectionNode = (ConnectionNode) data
								.getSerializableExtra(EConsts.Key.RELATED_ORGANIZATION_NODE);
						connectionNodeList2 = knowledge2
								.getListRelatedOrganizationNode();

						if (currentRequestState == STATE_EDIT
								& currentRequestCode == REQUEST_CODE_RELATED_RESOURCE_ORGANIZATION) {
							connectionNodeList2.set(currentRequestEditPosition,
									connectionNode);
						} else {
							// 加入列表
							connectionNodeList2.add(connectionNode);
						}
						organization_Ll.setVisibility(View.VISIBLE);
						organizationGroupAdapter
								.setListRelatedConnectionsNode(knowledge2
										.getListRelatedOrganizationNode());
						organizationGroupAdapter.notifyDataSetChanged();
					}

					if (data.hasExtra(EConsts.Key.RELATED_KNOWLEDGE_NODE)) {
						// 数据去重
						KnowledgeNode knowledgeNode = (KnowledgeNode) data
								.getSerializableExtra(EConsts.Key.RELATED_KNOWLEDGE_NODE);
						knowledgeNodeList = knowledge2
								.getListRelatedKnowledgeNode();

						if (currentRequestState == STATE_EDIT
								& currentRequestCode == REQUEST_CODE_RELATED_RESOURCE_KNOWLEDGE) {
							knowledgeNodeList.set(currentRequestEditPosition,
									knowledgeNode);
						} else {
							knowledgeNodeList.add(knowledgeNode);
						}
						knowledge_Ll.setVisibility(View.VISIBLE);
						knowledgeGroupAdapter
								.setListRelatedKnowledgeNode(knowledge2
										.getListRelatedKnowledgeNode());
						knowledgeGroupAdapter.notifyDataSetChanged();
					}

					if (data.hasExtra(EConsts.Key.RELATED_AFFAIR_NODE)) {
						// 数据去重\

						AffairNode affairNode = (AffairNode) data
								.getSerializableExtra(EConsts.Key.RELATED_AFFAIR_NODE);
						affairNodeList = knowledge2.getListRelatedAffairNode();

						if (currentRequestState == STATE_EDIT
								& currentRequestCode == REQUEST_CODE_RELATED_RESOURCE_AFFAIR) {
							affairNodeList.set(currentRequestEditPosition,
									affairNode);
						} else {
							affairNodeList.add(affairNode);
						}
						requirement_Ll.setVisibility(View.VISIBLE);
						requirementGroupAdapter
								.setListRelatedAffairNode(knowledge2
										.getListRelatedAffairNode());
						requirementGroupAdapter.notifyDataSetChanged();
					}

				}
			}
		}
	}

	/**
	 * 计算两个日期型的时间相差多少时间
	 * 
	 * @param startDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @return
	 */
	public String twoDateDistance(Date startDate, Date endDate) {

		if (startDate == null || endDate == null) {
			return null;
		}
		long timeLong = endDate.getTime() - startDate.getTime();
		if (timeLong < 60 * 1000)
			return timeLong / 1000 + "秒前";
		else if (timeLong < 60 * 60 * 1000) {
			timeLong = timeLong / 1000 / 60;
			return timeLong + "分钟前";
		} else if (timeLong < 60 * 60 * 24 * 1000) {
			timeLong = timeLong / 60 / 60 / 1000;
			return timeLong + "小时前";
		} else if (timeLong < 60 * 60 * 24 * 1000 * 7) {
			timeLong = timeLong / 1000 / 60 / 60 / 24;
			return timeLong + "天前";
		} else if (timeLong < 60 * 60 * 24 * 1000 * 7 * 4) {
			timeLong = timeLong / 1000 / 60 / 60 / 24 / 7;
			return timeLong + "周前";
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
			return sdf.format(startDate);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		currentRequestEditPosition = position;
		currentRequestState = STATE_EDIT;

		if (parent == people) { // 编辑人脉
			currentRequestCode = REQUEST_CODE_RELATED_RESOURCE_PEOPLE;
			ENavigate.startRelatedResourceActivityForResult(this,
					REQUEST_CODE_RELATED_RESOURCE_ACTIVITY, null,
					ResourceType.People, knowledge2
							.getListRelatedConnectionsNode().get(position));
		} else if (parent == organization) { // 编辑组织
			currentRequestCode = REQUEST_CODE_RELATED_RESOURCE_ORGANIZATION;
			ENavigate.startRelatedResourceActivityForResult(this,
					REQUEST_CODE_RELATED_RESOURCE_ACTIVITY, null,
					ResourceType.Organization, knowledge2
							.getListRelatedOrganizationNode().get(position));
		} else if (parent == knowledge) { // 编辑知识
			currentRequestCode = REQUEST_CODE_RELATED_RESOURCE_KNOWLEDGE;
			ENavigate.startRelatedResourceActivityForResult(this,
					REQUEST_CODE_RELATED_RESOURCE_ACTIVITY, null,
					ResourceType.Knowledge, knowledge2
							.getListRelatedKnowledgeNode().get(position));
		} else if (parent == requirement) { // 编辑事件
			currentRequestCode = REQUEST_CODE_RELATED_RESOURCE_AFFAIR;
			ENavigate.startRelatedResourceActivityForResult(this,
					REQUEST_CODE_RELATED_RESOURCE_ACTIVITY, null,
					ResourceType.Affair, knowledge2.getListRelatedAffairNode()
							.get(position));
		}

	}

	@Override
	public void bindData(int tag, Object object) {
		switch (tag) {
		case OrganizationReqType.MEET_SAVE:
			if (object != null) {
				onemeetid = (Long) object;
				intent = new Intent(NewCreateActivity.this,
						MeetingDetailsActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("customerMeetingDetail",
						customerMeetingDetail);
				intent.putExtra("TYPE", type);
				intent.putExtra("showColorImage", showColorImage);
				intent.putExtra("OneMeetID", onemeetid);
				intent.putExtra("Org_Id", id);
				intent.putExtra("repeadType", showReperat);
				intent.putExtras(bundle);
				startActivityForResult(intent, 9);
				finish();
			}
			break;
		case PeopleRequestType.PEOPLE_REQ_MEET_SAVE:
			meetsave = (MeetSave) object;
			if (meetsave != null) {
				if (meetsave.success) {
					intent = new Intent(NewCreateActivity.this,
							MeetingDetailsActivity.class);
					Bundle bundle1 = new Bundle();
					bundle1.putSerializable("customerMeetingDetail",
							customerMeetingDetail);
					intent.putExtra("showColorImage", showColorImage);
					intent.putExtra("TYPE", type);
					intent.putExtra("Org_Id", id);
					intent.putExtra("repeadType", showReperat);
					intent.putExtra("OneMeetID", meetsave.id);
					intent.putExtras(bundle1);
					startActivityForResult(intent, 9);
					System.out
					.println(meetsave.id
							+ "meetsave.idmeetsave.idmeetsave.idmeetsave.idmeetsave.idmeetsave.idmeetsave.id");
				}
				
				finish();
			}
		
			break;
		default:
			break;
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			if (this.mMediaPlayer != null) {
				if (mMediaPlayer.isPlaying()) {
					this.mMediaPlayer.stop();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (null != vibrator) {
				vibrator.cancel();
				vibrator = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
