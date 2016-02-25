package com.tr.ui.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.ls.LSInput;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.App;
import com.tr.R;
import com.tr.api.CommonReqUtil;
import com.tr.model.im.ChatDetail;
import com.tr.model.joint.AffairNode;
import com.tr.model.joint.ConnectionNode;
import com.tr.model.joint.KnowledgeNode;
import com.tr.model.joint.ResourceNode;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.model.obj.AffairsMini;
import com.tr.model.obj.Connections;
import com.tr.model.obj.ConnectionsMini;
import com.tr.model.obj.JTContactMini;
import com.tr.model.obj.JTFile;
import com.tr.model.obj.ResourceBase;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.common.JointResourceFragment.ResourceType;
import com.tr.ui.share.ShareActivity;
import com.tr.ui.widgets.JointResourcePeopleOperateDialog;
import com.tr.ui.widgets.JointResourcePeopleOperateDialog.OnOperateSelectListener;
import com.tr.ui.widgets.JointResourcePeopleOperateDialog.OperateType;
import com.utils.common.EConsts;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/**
 * 对接资源栏目
 * @author leon
 */
public class JointColumnActivity extends JBaseActivity implements OnItemClickListener,OnItemLongClickListener,IBindData{

	private final String TAG = getClass().getSimpleName();
	
	// 控件
	private ListView resourceLv;
	private GridView resourceGv;
	
	// 变量
	private ResourceType mTarResType; // 目标资源类型
	private String mTarResId; // 目标资源id
	private ResourceType mResType; // 对接资源类型
	private ResourceNode mResNode; // 资源栏目
	private ResourceAdapter mAdapter; // 适配器
	private ArrayList<ResourceNode> correctionResource;//纠错的资源集合，返回到前页刷新数据
	private List<ResourceNode> listNode ;
	private ArrayList<AffairsMini> listAffairMini;
	private ArrayList<Connections> listConnections;
	private ArrayList<KnowledgeMini2> listKnowledgeMini2s;
	
	@Override
	public void initJabActionBar() {
		mResType = (ResourceType) getIntent().getSerializableExtra(EConsts.Key.JOINT_RESOURCE_TYPE);
		mResNode = (ResourceNode) getIntent().getSerializableExtra(EConsts.Key.JOINT_RESOURCE_NODE);
		mTarResType = (ResourceType) getIntent().getSerializableExtra(EConsts.Key.TARGET_RESOURCE_TYPE);
		mTarResId = getIntent().getStringExtra(EConsts.Key.TARGET_RESOURCE_ID);
		jabGetActionBar().setTitle(mResNode.getMemo()); // 标题
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_joint_column);
		initVars();
		initControls();
	}
	
	private void initVars(){
		switch(mResType){
		case Affair:
			listAffairMini =  ((AffairNode) mResNode).getListAffairMini();
			break;
		case People:
		case Organization:
			listConnections =  ((ConnectionNode) mResNode).getListConnections();
			break;
		case Knowledge:
			listKnowledgeMini2s =  ((KnowledgeNode) mResNode).getListKnowledgeMini2();
			break;
		default:
			break;
		}
		correctionResource = new ArrayList<ResourceNode>();
		mAdapter = new ResourceAdapter(this);
	}
	
	private void initControls(){
		resourceLv = (ListView) findViewById(R.id.resourceLv);
		resourceGv = (GridView) findViewById(R.id.resourceGv);
		if(mResType != ResourceType.People){
			resourceGv.setVisibility(View.GONE);
			resourceLv.setAdapter(mAdapter);
			resourceLv.setOnItemClickListener(this);
			resourceLv.setOnItemLongClickListener(this);
		}
		else{
			resourceLv.setVisibility(View.GONE);
			resourceGv.setAdapter(mAdapter);
			resourceGv.setOnItemClickListener(this);
			resourceGv.setOnItemLongClickListener(this);
		}
		
		// 标题
		this.jabGetActionBar().setTitle(mResNode.getMemo());
	}
	
	class ResourceAdapter extends BaseAdapter{

		private Context context;
		
		public ResourceAdapter(Context context){
			this.context = context;
		}
		
		@Override
		public int getCount() {
			switch(mResType){
			case Affair:
				return listAffairMini.size();
			case People:
			case Organization:
				return listConnections.size();
			case Knowledge:
				return listKnowledgeMini2s.size();	
			default:
				break;
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Object resource = null;
			ViewHolder holder;
			if(convertView == null){
				switch(mResType){
				case Affair:
					convertView = LayoutInflater.from(context).inflate(R.layout.list_item_joint_aff, parent, false);
					break;
				case People:
					convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_joint_people, parent, false);
					break;
				case Organization:
					convertView = LayoutInflater.from(context).inflate(R.layout.list_item_joint_org, parent, false);
					break;
				case Knowledge:
					convertView = LayoutInflater.from(context).inflate(R.layout.list_item_joint_kno, parent, false);
					break;
				default:
					break;
				}
				holder = new ViewHolder();
				holder.init(convertView);
				convertView.setTag(holder);
			}
			else{
				holder = (ViewHolder) convertView.getTag();
			}
			switch(mResType){
			case Affair:
				resource = listAffairMini.get(position);
				break;
			case People:
				resource = listConnections.get(position);
				break;
			case Organization:
				resource = listConnections.get(position);
				break;
			case Knowledge:
				resource = listKnowledgeMini2s.get(position);
				break;
			default:
				break;
			}
			holder.build(mResType, resource);
			return convertView;
		}
		
		class ViewHolder implements OnClickListener{
			
			ImageView avatarIv; // 头像
			TextView typeTv; // 类型
			TextView titleTv; // 标题
			TextView nameTv; // 姓名
			TextView tradeTv; // 行业
			
			public void init(View view){
				avatarIv = (ImageView) view.findViewById(R.id.avatarIv);
				typeTv = (TextView) view.findViewById(R.id.typeTv);
				titleTv = (TextView) view.findViewById(R.id.titleTv);
				nameTv = (TextView) view.findViewById(R.id.nameTv);
				tradeTv = (TextView) view.findViewById(R.id.tradeTv);
			}
			
			public void build(ResourceType resType, Object res){
				switch(resType){
				case Affair:
					AffairsMini affair = (AffairsMini) res;
					titleTv.setText(affair.title);
					nameTv.setText(affair.connections.type == Connections.type_persion ? 
							affair.connections.getName() : affair.connections.organizationMini.fullName);
					nameTv.setOnClickListener(this);
					nameTv.setTag(affair);
					break;
				case People:
					Connections people = (Connections) res;
					if (people.jtContactMini.getImage()!=null&&!people.jtContactMini.getImage().equals("")&&!people.jtContactMini.getImage().isEmpty()) {
						ImageLoader.getInstance().displayImage(people.jtContactMini.getImage(), avatarIv);
					}else {
						avatarIv.setBackgroundResource(R.drawable.ic_know_people);
					}
					nameTv.setText(people.jtContactMini.name);
					break;
				case Organization:
					Connections organization = (Connections) res;
					if (organization.organizationMini.mLogo!=null&&!organization.organizationMini.mLogo.contains("")&&!organization.organizationMini.mLogo.isEmpty()) {
						ImageLoader.getInstance().displayImage(organization.organizationMini.mLogo, avatarIv);
					}else {
						avatarIv.setBackgroundResource(R.drawable.ic_organization);
					}
					nameTv.setText(organization.organizationMini.fullName);
					nameTv.setOnClickListener(null);
					tradeTv.setText(organization.organizationMini.mTrade);
					break;
				case Knowledge:
					KnowledgeMini2 knowledge = (KnowledgeMini2) res;
					titleTv.setText(knowledge.title);
					JTContactMini jtContactMini = knowledge.connections.jtContactMini;
					nameTv.setText(knowledge.connections.type == Connections.type_persion ?
								knowledge.connections.getName() : knowledge.connections.organizationMini.fullName);
					nameTv.setOnClickListener(this);
					nameTv.setTag(knowledge);
					typeTv.setText(getKnoType(knowledge.type));
					break;
				default:
					break;
				}
			}
			//获取知识类型
			private String getKnoType(int type) {
				switch (type) {
				case 1:
					return "【资讯】";
				case 2:
					return "【投融工具】";
				case 3:
					return "【行业】";
				case 4:
					return "【经典案例】";
				case 5:
					return "【图书报告】";
				case 6:
					return "【资产管理】";
				case 7:
					return "【宏观】";
				case 8:
					return "【观点】";
				case 9:
					return "【判例】";
				case 10:
					return "【法律法规】";
				case 11:
					return "【文章】";
				default:
					break;
				}
				return "";
			}

			
			@Override
			public void onClick(View v) {
				JointResourcePeopleOperateDialog dialog = new JointResourcePeopleOperateDialog(context);
				ResourceBase resource = (ResourceBase) v.getTag();
				dialog.setOnSelectListener(new OnOperateSelectListener(){

					private JTFile jtFile;

					@Override
					public void onOperateSelect(OperateType operType, ResourceBase resource) {
						String phoneNumber ="";
						String desc = "";
						String thatID = "";
						String thatImage = "";
						String thatName = "";
						if(resource instanceof KnowledgeMini2){
							KnowledgeMini2 knowledge = (KnowledgeMini2) resource;
							phoneNumber = knowledge.connections.getMobilePhone();
							desc = knowledge.desc;
							
							jtFile = knowledge.toJTFile();
							thatID = knowledge.connections.getId();
							thatImage = knowledge.connections.getImage();
							thatName = knowledge.connections.getName();
						}
						else if(resource instanceof AffairsMini){
							AffairsMini affairsMini = (AffairsMini) resource;
							phoneNumber = affairsMini.connections.getMobilePhone();
							desc = affairsMini.title;
							
							thatID = affairsMini.connections.getId();
							thatImage = affairsMini.connections.getImage();
							thatName = affairsMini.connections.getName();
						}
						switch(operType){
						case SMS: // 短信
							if (TextUtils.isEmpty(phoneNumber)) {
								showToast("请完善联系方式");
							}
								Uri smsToUri = Uri.parse("smsto:"+phoneNumber);  
								Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);  
								startActivity(intent);
							break;
						case TEL: // 拨号
							if (TextUtils.isEmpty(phoneNumber)) {
								showToast("请完善联系方式");
							}
							Intent call = new Intent();
							call.setAction(Intent.ACTION_DIAL);
							call.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							call.setData(Uri.parse("tel: "+phoneNumber));
							startActivity(call);
							break;
						case FORWARD: // 转发/畅聊
							ChatDetail chartWithPeople = new ChatDetail();
							chartWithPeople.setThatID(thatID);
							chartWithPeople.setThatImage(thatImage);
							chartWithPeople.setThatName(thatName);
							ENavigate.startIMActivity(JointColumnActivity.this, chartWithPeople);
							break;
//						case INVITE: // 邀请
//							Uri smToUri = Uri.parse("smsto:"+phoneNumber);
//							Intent intent1 = new Intent(Intent.ACTION_SENDTO, smToUri);  
//							intent1.putExtra(Intent.EXTRA_TEXT, "我正在使用金桐app，一款集商务社交、投融资项目对接、个人资源管理的商务应用神器！ 推荐给你，快来哦，轻点 http://app.gintong.com 即可下载。");    
//							startActivity(intent1);   
////							showToast("邀请");
//							break;
						case SHARE: // 分享
//							 Intent share=new Intent(Intent.ACTION_SEND);    
//							 share.setType("image/*");    
//							 share.putExtra(Intent.EXTRA_SUBJECT, "Share");    
//							 share.putExtra(Intent.EXTRA_TEXT, desc+" (分享自金桐网)");    
//							 share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
//				             startActivity(share);    
							 Intent share=new Intent(Intent.ACTION_SEND);    
							 share.setType("image/*");    
							 share.putExtra(Intent.EXTRA_SUBJECT, "Share");    
							 share.putExtra(Intent.EXTRA_TEXT, "我正在使用金桐app，一款集商务社交、投融资项目对接、个人资源管理的商务应用神器！ 推荐给你，快来哦，轻点 http://app.gintong.com 即可下载。");    
							 share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
				             startActivity(share);    
							break;
						default:
							break;
						}
					}
				});
				
				if(resource instanceof KnowledgeMini2){
					resource.setResourceType(ResourceBase.ResourceType.Knowledge);
				}
				else if(resource instanceof AffairsMini){
					resource.setResourceType(ResourceBase.ResourceType.Affair);
				}
				dialog.show(v, resource);
			}
		}
		
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			final int position, long id) {
		
		new AlertDialog.Builder(this)
    	.setTitle("提示")
    	.setMessage("对本条目纠错？")
    	.setPositiveButton("确定", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				listNode = new ArrayList<ResourceNode>();
	            switch(mResType){
	            case Affair:
	            	AffairNode affairNode = new AffairNode();
	            	affairNode.setMemo(mResNode.getMemo());
	            	affairNode.setType(mResNode.getType());
	            	affairNode.getListAffairMini().add(((AffairNode) mResNode).getListAffairMini().get(position));
	            	listNode.add(affairNode);
	            	break;
	            case People:
	            case Organization:
	            	ConnectionNode peopleNode = new ConnectionNode();
	            	peopleNode.setMemo(mResNode.getMemo());
	            	peopleNode.setType(mResNode.getType());
	            	peopleNode.getListConnections().add(((ConnectionNode) mResNode).getListConnections().get(position));
	            	listNode.add(peopleNode);
	            	break;
	            case Knowledge:
	            	KnowledgeNode knowledgeNode = new KnowledgeNode();
	            	knowledgeNode.setMemo(mResNode.getMemo());
	            	knowledgeNode.setType(mResNode.getType());
	            	knowledgeNode.getListKnowledgeMini2().add(((KnowledgeNode) mResNode).getListKnowledgeMini2().get(position));
	            	listNode.add(knowledgeNode);
	            	break;
	            default:
	            	break;
	            }
	            showLoadingDialog();
	    		CommonReqUtil.doCorrectJointResource(JointColumnActivity.this, JointColumnActivity.this, JointResourceFragment.convertResourceType2Int(mTarResType), 
	    				mTarResId, listNode, null);
			}
    	})
    	.setNegativeButton("取消", null)
    	.create()
    	.show();
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch(mResType){
		case People:
			break;
		case Organization:
			break;
		case Affair:
			break;
		case Knowledge:
			ENavigate.startKnowledgeOfDetailActivity(
					this, ((KnowledgeNode) mResNode).getListKnowledgeMini2()
							.get(position).id, ((KnowledgeNode) mResNode)
							.getListKnowledgeMini2().get(position).type);
			break;
		default:
			break;
		}
	}

	@Override
	public void bindData(int tag, Object object) {
		if(tag == EAPIConsts.CommonReqType.CorrectJointResult){ // 生态对接纠错
			dismissLoadingDialog();
			if(object != null){
				@SuppressWarnings("unchecked")
				HashMap<String, Object> dataMap = (HashMap<String, Object>) object;
				boolean success = (Boolean) dataMap.get("success");
				if(success&&listNode!=null){
					showToast("操作成功");
					ResourceNode resourceNode = listNode.get(0);
					correctionResource.add(resourceNode);
					if (resourceNode instanceof AffairNode) {//事务
						listAffairMini.remove(((AffairNode) resourceNode).getListAffairMini().get(0));
	    			}else if(resourceNode instanceof ConnectionNode){//人脉或组织
	    				listConnections.remove(((ConnectionNode) resourceNode).getListConnections().get(0));
					}else if(resourceNode instanceof KnowledgeNode){//知识
						listKnowledgeMini2s.remove(((KnowledgeNode) resourceNode).getListKnowledgeMini2().get(0));
					}
					mAdapter.notifyDataSetChanged();
				}
				else{
					showToast("操作失败");
				}
			}
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home){ // 取消选择
			setResult(RESULT_OK, new Intent().putExtra("correctionResource", correctionResource));
			finish();
		}
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent= new Intent();
			intent.putExtra("correctionResource", correctionResource);
//			intent.putExtra("culumName", mResNode.getMemo());
			setResult(RESULT_OK, intent);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
