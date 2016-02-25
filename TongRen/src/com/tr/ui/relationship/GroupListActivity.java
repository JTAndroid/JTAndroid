package com.tr.ui.relationship;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.App;
import com.tr.R;
import com.tr.api.IMReqUtil;
import com.tr.model.connections.GroupListItem;
import com.tr.model.im.MGetListMUC;
import com.tr.model.obj.IMBaseMessage;
import com.tr.model.obj.JTFile;
import com.tr.model.obj.MUCDetailMini;
import com.tr.model.obj.MUCMessage;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragmentActivity;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.image.LoadImage;
import com.utils.string.StringUtils;

/**
 * @Date 2014-3-17
 * @description 群組列表
 */

public class GroupListActivity extends JBaseFragmentActivity implements
		IBindData, SwipeRefreshLayout.OnRefreshListener, OnScrollListener {

	private ListView mListView;
	private List<GroupListItem> list = new ArrayList<GroupListItem>();
	private IMAdapter adapter;
	private View moreView; // 加载更多页面

	private int lastItem;
	private int count;
	private int mState = 0; // 0-正常状态 1-获取更多中 2-刷新中
	private SwipeRefreshLayout swipeLayout;
	private String fromActivityName;
	private JTFile mShareInfo;
	private List<JTFile> mShareInfoList;
	private List<MUCDetailMini> mListMUCDetailMini = new ArrayList<MUCDetailMini>();

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fromActivityName = getIntent().getStringExtra(ENavConsts.EFromActivityName); 
		mShareInfo = (JTFile) getIntent().getSerializableExtra(ENavConsts.EShareParam);
		mShareInfoList = (List<JTFile>) getIntent().getSerializableExtra(ENavConsts.EShareParamList);
		setContentView(R.layout.grouplist);

		swipeLayout = (SwipeRefreshLayout) this.findViewById(R.id.group_container);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

		moreView = mInflater.inflate(R.layout.grouplist, null);

		mListView = (ListView) this.findViewById(R.id.grouplist);
		adapter = new IMAdapter(this);
		
	    mListView.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
            	if(TextUtils.isEmpty(fromActivityName)){
            		if(arg2 >= mListMUCDetailMini.size()){
                		return;
                	}
            		MUCDetailMini mini = mListMUCDetailMini.get(arg2);
                	startMuc(mini.getId() + "");
            	}
            	else if(fromActivityName.equals(ENavConsts.EShareActivity)){
            		if(arg2 >= mListMUCDetailMini.size()){
                		return;
                	}
            		MUCDetailMini mini = mListMUCDetailMini.get(arg2);
            		if(mShareInfo != null){
            			IMBaseMessage shareMsg = new MUCMessage("");
        				shareMsg.setJtFile(mShareInfo);
        				shareMsg.setType(mShareInfo);
//        	            ENavigate.startIMGroupActivity(GroupListActivity.this, mini.getId() + "", shareMsg);
            		}
            		else if(mShareInfoList != null){
            			ArrayList<IMBaseMessage> shareMsgList = new ArrayList<IMBaseMessage>();
            			for(JTFile jtFile : mShareInfoList){
            				IMBaseMessage shareMsg = new MUCMessage("");
            				shareMsg.setJtFile(jtFile);
            				shareMsg.setType(jtFile);
            				shareMsgList.add(shareMsg);
            			}
//            			ENavigate.startIMGroupActivity(GroupListActivity.this, mini.getId() + "", shareMsgList);
            		}
    	            finish(); 
            	}
            }
        });

		mListView.setAdapter(adapter);
		mListView.setOnScrollListener(this);
		count = list.size();

		mState = STATE_NORMAL;
		startGetData();
	}
	
	private void startMuc(String mucID){
		ENavigate.startIMGroupActivity(this, mucID);
	}

	@Override
	public void bindData(int tag, Object object) {
		if (tag == EAPIConsts.IMReqType.IM_REQ_GET_LIST_MUC) {
			if (!this.hasDestroy()) {
				if (object != null) {
					MGetListMUC mGetListMuc = (MGetListMUC) object;
					if(mGetListMuc.getListMUCDetailMini()  != null){
						this.mListMUCDetailMini = mGetListMuc.getListMUCDetailMini();
						adapter.setData(mListMUCDetailMini);
						adapter.notifyDataSetChanged();
					}
				}
				stopLoading();
			}
			mState = STATE_NORMAL;
		} 
	}

	public class IMAdapter extends BaseAdapter {

		private Context mContext;
		private List<MUCDetailMini> mData;

		public IMAdapter(Context context) {
			this.mContext = context;
			mData = new ArrayList<MUCDetailMini>();
		}

		public void setData(List<MUCDetailMini> mData) {
			this.mData = mData;
		}

		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public Object getItem(int i) {
			return mData.get(i);
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public View getView(final int position, View convertView,
				final ViewGroup parent) {
			ItemHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.grouplistitem, parent, false);
				holder = new ItemHolder();

				holder.mImg = (ImageView) convertView
						.findViewById(R.id.HFI_IMAGE_Head);

				holder.mImageFourBG = convertView
						.findViewById(R.id.GroupListIMItemLayoutHeadRoot);
				ImageView v = (ImageView) convertView
						.findViewById(R.id.GL_IMAGE_12);
				holder.mImageFour.add(v);
				v = (ImageView) convertView.findViewById(R.id.GL_IMAGE_12);
				holder.mImageFour.add(v);
				v = (ImageView) convertView.findViewById(R.id.GL_IMAGE_21);
				holder.mImageFour.add(v);
				v = (ImageView) convertView.findViewById(R.id.GL_IMAGE_22);
				holder.mImageFour.add(v);
		            
				holder.mTitle = (TextView) convertView.findViewById(R.id.GroupList_NAME);
				convertView.setTag(holder);
			} else {
				holder = (ItemHolder) convertView.getTag();
			}
			final MUCDetailMini dataHolder = mData.get(position);
			String title = "";
			if(!TextUtils.isEmpty(dataHolder.getSubject())){
				title = dataHolder.getSubject();
			}else if(!TextUtils.isEmpty(dataHolder.getTitle())){
				title = dataHolder.getTitle();
			}
			holder.mTitle.setText(title);
			
			  if(dataHolder.getListImage() == null){
		        	holder.mImageFourBG.setVisibility(View.GONE);
		        	holder.mImg.setVisibility(View.VISIBLE);
		        }else if(dataHolder.getListImage().size() == 0){
		        	holder.mImageFourBG.setVisibility(View.GONE);
		        	holder.mImg.setVisibility(View.VISIBLE);
		        }else if(dataHolder.getListImage().size() == 1){
		        	holder.mImageFourBG.setVisibility(View.GONE);
		        	holder.mImg.setVisibility(View.VISIBLE);
		        	ImageLoader.getInstance().displayImage(dataHolder.getListImage().get(0), holder.mImg, LoadImage.mDefaultHead);
		        }else{
		        	holder.mImageFourBG.setVisibility(View.VISIBLE);
		        	holder.mImg.setVisibility(View.GONE);
		        	for(int j = 0; (j < dataHolder.getListImage().size())&&(j<=3); j++){
		        		holder.mImageFour.get(j).setVisibility(View.VISIBLE);
		        		ImageLoader.getInstance().displayImage(dataHolder.getListImage().get(j), holder.mImageFour.get(j), LoadImage.mDefaultHead);
		        	}
		        	
		        	for(int j = dataHolder.getListImage().size(); j<=3; j++){
		        		holder.mImageFour.get(j).setVisibility(View.INVISIBLE);
		        	}
		        }
			  
			return convertView;
		}

		private class ItemHolder {
			public ImageView mImg; // 单个头像
			public View mImageFourBG;//
			public List<ImageView> mImageFour = new ArrayList<ImageView>(); // 2-4个头像
			public TextView mTitle;
		}
	}

	public void stopLoading() {
		swipeLayout.setRefreshing(false);
	}

	// 出发下拉刷新
	public void onRefresh() {
		if (mState == STATE_NORMAL) {
			startGetData();
			mState = STATE_REFRESH;
		}
	}

	/**
	 * 获取页数据
	 */
	public void startGetData() {
		// sendMessage();
		swipeLayout.setRefreshing(true);
		IMReqUtil.getListMUC(App.getApplicationConxt(), this, null);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		lastItem = firstVisibleItem + visibleItemCount - 1; // 减1是因为上面加了个addFooterView
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// 下拉到空闲是，且最后一个item的数等于数据的总数时，进行更新
		if (lastItem == count && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
			moreView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void initJabActionBar() {
		jabGetActionBar().setTitle("群列表");
	}
}