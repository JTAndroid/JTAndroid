package com.tr.ui.home.frg;

import java.util.ArrayList;
import java.util.List;

import com.tr.App;
import com.tr.R;
import com.tr.api.IMReqUtil;
import com.tr.image.ImageLoader;
import com.tr.model.im.ChatDetail;
import com.tr.model.im.IMCacheUtils;
import com.tr.model.im.MGetListIMRecord;
import com.tr.model.obj.IMBaseMessage;
import com.tr.model.obj.IMRecord;
import com.tr.model.obj.JTFile;
import com.tr.model.page.JTPage;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.home.MainActivity;
import com.tr.ui.widgets.SmileyParser;
import com.tr.ui.widgets.SmileyParser2;
import com.utils.common.JTDateUtils;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.image.LoadImage;
import com.utils.log.KeelLog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

/**
 * @ClassName: FrgIM.java
 * @Description:  首页-聊天界面 
 * @author         xuxinjian
 * @version        V1.0  
 * @Date           2014-3-28 上午7:52:34
 */

public class FrgIM extends JBaseFragment implements IBindData, SwipeRefreshLayout.OnRefreshListener, OnScrollListener{
	public static final String TAG="FrgIM";
	private ListView mListView;
	private List<IMRecord> list = new ArrayList<IMRecord>();
	private IMAdapter adapter;
	
	private View moreView; // 加载更多页面
	private TextView mvTextView;
	private View mvProgressBar;
	
	private int lastItem;
    private int count;
    private JTPage mPage;
    private int nowIndex = -1;
	private int mState = 0; // 0-正常状态 1-获取更多中 2-刷新中
	private SwipeRefreshLayout swipeLayout;
	
	public View mBGNoContent;
	public View mBGHasContent;
	public Button mBtnAddIM;
	
	private final static String mRecordID = "imrecord";
	
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		String MSG = "onCreateView()";
		View view = inflater
				.inflate(R.layout.home_frg_im, container, false);
		
		swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.home_frg_im_swipe_container);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright, 
	            android.R.color.holo_green_light, 
	            android.R.color.holo_orange_light, 
	            android.R.color.holo_red_light);
		swipeLayout.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		
		moreView = inflater.inflate(R.layout.pulldown_footer, null);
		this.mvTextView = (TextView)moreView.findViewById(R.id.pulldown_footer_text);
		this.mvProgressBar = (View)moreView.findViewById(R.id.pulldown_footer_loading);
		
		mListView = (ListView) view.findViewById(R.id.home_frg_im_listview);
	    adapter = new IMAdapter(this.getActivity());	
	    // 读取缓存文件
	    list = IMCacheUtils.readIMRecordList(mRecordID);
	    	
	    mListView.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                // TODO Auto-generated method stub
            	KeelLog.d(TAG, "onItemClick");
            	if(arg2 >= list.size()){
            		loadMoreData();
            		return;
            	}
            	
            	IMRecord imRecord = list.get(arg2);
            	imRecord.setNewCount(0);
            	if(imRecord.getType() == IMRecord.TYPE_CHAT){
            		//私聊
            		ChatDetail chatDetail = new ChatDetail();
            		
            		chatDetail.setThatID(imRecord.getId());
            		String imgUrl = "";
            		if(imRecord.getListImageUrl().size() > 0){
            			imgUrl = imRecord.getListImageUrl().get(0);
            		}
            		chatDetail.setThatImage(imgUrl);
            		chatDetail.setThatName(imRecord.getTitle());
            		
            		ENavigate.startIMActivity(getActivity(), chatDetail);
            		
            	}else{
            		//群聊
            		ENavigate.startIMGroupActivity(getActivity(), imRecord.getId());
            	}
            }
        });
	    // add by leon 
	    // 列表长按事件
	    mListView.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				KeelLog.d(TAG, "onItemLongClick");
				if (arg2 >= list.size()) {
					return false;
				}
				final IMRecord imRecord = list.get(arg2);
            	imRecord.setNewCount(0);
            	if(imRecord.getType() == IMRecord.TYPE_MUC){ // 是否是未开始的会议
            		// 会议尚未开始
					if (JTDateUtils.getIntervalMoreTime(
							imRecord.getStartTime(),DateFormat.format("yyyy-MM-dd kk:mm:ss",
									System.currentTimeMillis())+ "") < 0) {
						return false;
					}
            	}
            	new AlertDialog.Builder(getActivity())
            	.setTitle(imRecord.getTitle())
            	.setMessage("确定删除该畅聊?")
            	.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						if(getActivity()!=null){
							((MainActivity)getActivity()).showLoadingDialog("");
						}
						if(imRecord.getType() == IMRecord.TYPE_CHAT){
							IMReqUtil.deleteIMFromList(getActivity(),FrgIM.this, null, imRecord.getId(), "");
						}
						else{
							IMReqUtil.deleteIMFromList(getActivity(),FrgIM.this, null, "", imRecord.getId());
						}
					}
				})
				.setNegativeButton("取消", null)
				.create().show();
            	//返回true,不让点击事件向下传递
				return true;
			}
	    });
	    //list.clear();
	    adapter.setData(list);
	    mListView.addFooterView(moreView);
	    mListView.setAdapter(adapter);
	    mListView.setOnScrollListener(this); //设置listview的滚动事件
		count = list.size();
		
	    mState = STATE_NORMAL;
	    
	    onRefresh();
	    
	    mBGNoContent = view.findViewById(R.id.home_frg_no_content_bg);
	    mBGHasContent = view.findViewById(R.id.home_frg_im_swipe_container);
	    mBtnAddIM = (Button)view.findViewById(R.id.home_frg_im_no_content_add_requirement);
	    mBtnAddIM.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ENavigate.startIMRelationSelectActivity(getActivity(), null, null, 0,null,null);
			}
		});
	    mBGNoContent.setVisibility(View.GONE);
	    mBGHasContent.setVisibility(View.VISIBLE);
		return view;
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void bindData(int tag, Object object) {
		// TODO Auto-generated method stub
		if (tag == EAPIConsts.IMReqType.IM_REQ_GET_LISTIM) {
			if (this.isResumed()) {
				if (object != null) {
					MGetListIMRecord getListIMRecord = (MGetListIMRecord) object;

					mPage = getListIMRecord.getJtPage();
					if (mPage != null) {
						if (mState == STATE_REFRESH) {
							list.clear();// 如果当前刷新中，则清除之前数据
						}
						if ((mPage.getLists() != null) && (mPage.getLists().size() > 0)) {
							nowIndex = mPage.getIndex();
							for (int i = 0; i < mPage.getLists().size(); i++) {
								list.add((IMRecord) mPage.getLists().get(i));
							}
							IMCacheUtils.writeIMRecordList(mRecordID, list);

							count = list.size();
							adapter.setData(list);
							adapter.notifyDataSetChanged();
						}
					}
				}
				if(list.size() > 0){
					boolean isCanVisible=false;
					for(IMRecord iMRecord:list){
						if(iMRecord.getNewCount()!=0){
							isCanVisible=true;
							break;
						}else{
						}
					}
					if(isCanVisible){
						if((MainActivity)getActivity()!=null){
							((MainActivity)getActivity()).setIMSizeViewTag(View.VISIBLE);
						}
					}else{
						if((getActivity())!=null){
							((MainActivity)getActivity()).setIMSizeViewTag(View.INVISIBLE);
						}
					}
					adapter.notifyDataSetChanged();
					mBGNoContent.setVisibility(View.GONE);
					mBGHasContent.setVisibility(View.VISIBLE);
				}else{
					mBGNoContent.setVisibility(View.VISIBLE);
					mBGHasContent.setVisibility(View.GONE);
				}
				stopLoading();
			}
			mState = STATE_NORMAL;
		}
		else if(tag == EAPIConsts.IMReqType.IM_DELETE_IM_FROM_LIST){
			if(getActivity()!=null){
				((MainActivity)getActivity()).dismissLoadingDialog();
			}
			if (!this.isHasPause() ){
				if(object !=null){
					boolean result = (Boolean) object;
					if(result){ // 删除成功
						showToast("删除成功");
						mState = STATE_NORMAL;
						onRefresh();
					}
					else{ // 删除失败
						showToast("删除失败");
					}
				}
			}
		}
		else if (tag == EAPIConsts.IMReqType.IM_REQ_SEND_MESSAGE){
			if (this.isResumed() ){
				KeelLog.d("binddata:");
			}
		}
	}
	
	
	
	public class IMAdapter extends BaseAdapter {

		private SmileyParser parser;
		private SmileyParser2 parser2;
		
	    private Context mContext;
	    private List<IMRecord> mData;

	    public IMAdapter(Context context) {
	        this.mContext = context;
	        mData = new ArrayList<IMRecord>();
	        
	    	parser = SmileyParser.getInstance(context);
			parser2=SmileyParser2.getInstance(context);;
	    }

	    public void setData(List<IMRecord> mData) {
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
	    public View getView(final int position, View convertView, final ViewGroup parent) {
	        ItemHolder holder;
	        if (convertView == null) {
	            convertView = LayoutInflater.from(mContext).inflate(R.layout.home_frg_im_listview_item, 
	            		parent, false);
	            holder = new ItemHolder();
	            holder.mImg = (ImageView) convertView.findViewById(R.id.HFI_IMAGE_Head);
	            
	            holder.mImageFourBG = convertView.findViewById(R.id.HomeFrgIMItemLayoutHeadRoot);
	            ImageView v = (ImageView) convertView.findViewById(R.id.HFI_IMAGE_11);
	            holder.mImageFour.add(v);
	            v = (ImageView) convertView.findViewById(R.id.HFI_IMAGE_12);
	            holder.mImageFour.add(v);
	            holder.mImageFour.add(v);
	            v = (ImageView) convertView.findViewById(R.id.HFI_IMAGE_22);
	            holder.mImageFour.add(v);
	            
	            holder.mTitle = (TextView) convertView.findViewById(R.id.HFI_IM_Title);
	            holder.mContent = (TextView)convertView.findViewById(R.id.HFI_IM_CONTENT);
	            holder.mSource = (TextView)convertView.findViewById(R.id.HFI_IM_Compere);
	            holder.mTime = (TextView)convertView.findViewById(R.id.HFI_IM_TIME);
	            holder.mSendTime = (TextView)convertView.findViewById(R.id.HFI_IM_MESSAGE_CREATE_TIME);
	            
	            
	            holder.mTxtNewMessage = (TextView)convertView.findViewById(R.id.HFI_IM_NEW_COUNT);
	            
	            holder.mBGConferenceStartTime 		= (View)convertView.findViewById(R.id.HomeFrgIMTimeBG);
	            holder.mBGConferenceStartTimeLine 	= (View)convertView.findViewById(R.id.HomeFrgIMTimeBG_line);
	            
	            convertView.setTag(holder);
	        } else {
	            holder = (ItemHolder) convertView.getTag();
	        }

	        // Retrieve the data holder
	        final IMRecord dataHolder = mData.get(position);
	        holder.mTitle.setText(dataHolder.getTitle());
	       
	        CharSequence dd = parser.addSmileySpans(dataHolder.getContent());
			CharSequence dd1 = parser2.addSmileySpans(dd);
			holder.mContent.setText(dd1);
	        
	        if(TextUtils.isEmpty(dataHolder.getCompereName()))
	        	holder.mSource.setText("");
	        else
	        	holder.mSource.setText("主持人: " + dataHolder.getCompereName());
	        
	        if(dataHolder.getNewCount() > 0){
	        	holder.mTxtNewMessage.setVisibility(View.VISIBLE);
	        	holder.mTxtNewMessage.setText(dataHolder.getNewCount() + "");
	        }else{
	        	holder.mTxtNewMessage.setVisibility(View.GONE);
	        }
	        
	        // 会议开始时间
			String testtime = dataHolder.getConferenceStartTime();// 测试
			if(!TextUtils.isEmpty(testtime)){
				//会议尚未开始,将会议开始时间显示在最下方
				holder.mBGConferenceStartTime.setVisibility(View.VISIBLE);
				holder.mBGConferenceStartTimeLine.setVisibility(View.VISIBLE);
				String strTime = JTDateUtils.getTimeDisplay(testtime, getActivity());
				holder.mTime.setText(strTime);
			}else{
				//会议已开始，不显示会议开始时间
				holder.mBGConferenceStartTime.setVisibility(View.GONE);
				holder.mBGConferenceStartTimeLine.setVisibility(View.INVISIBLE);
			}
			
			  // 消息发送时间
			String sendtime = dataHolder.getTime();
			if(!TextUtils.isEmpty(sendtime))
			{
				//会议尚未开始,将会议开始时间显示在最下方
				String strTime = JTDateUtils.getIMListTimeDisplay(sendtime, getActivity(), false);
				holder.mSendTime.setText(strTime);
			}else{
				holder.mSendTime.setText("");
			}
			
	        if(dataHolder.getListImageUrl() == null){
	        	holder.mImageFourBG.setVisibility(View.GONE);
	        	holder.mImg.setVisibility(View.VISIBLE);
	        }else if(dataHolder.getListImageUrl().size() == 0){
	        	holder.mImageFourBG.setVisibility(View.GONE);
	        	holder.mImg.setVisibility(View.VISIBLE);
	        }else if(dataHolder.getListImageUrl().size() == 1){
	        	holder.mImageFourBG.setVisibility(View.GONE);
	        	holder.mImg.setVisibility(View.VISIBLE);
	        	ImageLoader.load(holder.mImg, dataHolder.getListImageUrl().get(0), R.drawable.ic_default_avatar);
	        }else{
	        	holder.mImageFourBG.setVisibility(View.VISIBLE);
	        	holder.mImg.setVisibility(View.GONE);
	        	for(int j = 0; (j < dataHolder.getListImageUrl().size())&&(j<=3); j++){
	        		holder.mImageFour.get(j).setVisibility(View.VISIBLE);
	        		ImageLoader.load(holder.mImageFour.get(j), dataHolder.getListImageUrl().get(j), R.drawable.ic_default_avatar);
	        	}
	        	
	        	for(int j = dataHolder.getListImageUrl().size(); j<=3; j++){
	        		holder.mImageFour.get(j).setVisibility(View.INVISIBLE);
	        	}
	        }
	        return convertView;
	    }

	    private class ItemHolder {
	        public ImageView mImg;//单个头像
	        public View mImageFourBG;//
	        public List<ImageView> mImageFour = new ArrayList<ImageView>();//2-4个头像
	        public TextView mTitle;
	        public TextView mContent;
	        public TextView mSource;
	        public TextView mTime;//会议开始时间
	        public TextView mSendTime;//消息发送时间
	        public TextView mTxtNewMessage;//新消息条数
	    	public View mBGConferenceStartTime;//会议开始时间背景条
	    	public View mBGConferenceStartTimeLine;//会议开始时间背景条分割线
	    }
	}

	  private void addLists(int n){
  			 n += list.size();
	    	 for(int i=list.size();i<n;i++){
	    		 IMRecord imItemData = new IMRecord();
	    		 imItemData.setTitle("聊天标题" + i);
	    		 imItemData.setContent("聊天内容" + i);
	    		 imItemData.setStartTime(i + "小时前");
	    		 imItemData.setCompereName("唐古拉");
		         list.add(imItemData);
		     }
	    }
	    
		public void stopLoading(){
			swipeLayout.setRefreshing(false);
			mvProgressBar.setVisibility(View.GONE);
			
			if(mPage!=null && !mPage.hasMore()){
				mvTextView.setText("没有更多");
			}else{
				mvTextView.setText("更多");
			}
		}
		
		 @Override
		 public void onPause() {
			 super.onPause();
		 }
		
		 @Override
		 public void onResume(){
			 super.onResume();
			 onRefresh();
		 }
		
		// 出发下拉刷新
		public void onRefresh() {
			if(mState == STATE_NORMAL){
				swipeLayout.setRefreshing(true);
				startGetData(0);
				this.mState = STATE_REFRESH;
			}
		}
		
		/*
		public void getChatMessage(){
			String jtContactID = "" + App.getApp().getmAppData().getUser().mID;
			IMReqUtil.getChatMessage(getActivity(), this, null,  jtContactID, null, true);
		}
		*/
		
		public void sendMessage(){
			String jtContactID = "" + App.getApp().getAppData().getUser().mID;
			String mucID = "";
			String text = "hello";
			JTFile file = new JTFile();
			String fromTime = "2014-04-17 16:00:00";
			int type = IMBaseMessage.TYPE_TEXT;
			//IMReqUtil.sendMessage(getActivity(), this, null,  jtContactID, mucID, text, file, fromTime, type);
		}
		
		public void createMuc(){
//			String subject = "";
//			String title = "随便聊聊";
//			String orderTime = EUtil.getFormatFromDate(new Date());
//			List<String> listJTContactID = new ArrayList<String>();
//			List<String> listOrganizationID = new ArrayList<String>();
//			listJTContactID.add("1");
//			listJTContactID.add("2");
//			listOrganizationID.add("5");
//			listOrganizationID.add("6");
//			String content = "歇会唠唠嗑";
//			List<JTFile> listJTFile = new ArrayList<JTFile>();
//			JTFile jtFile = new JTFile();
//			jtFile.setmType(JTFile.TYPE_IMAGE);
//			jtFile.setmSuffixName("png");
//			jtFile.setmUrl("http://192.168.101.130/02.jpg");
//			listJTFile.add(jtFile);
//			listJTFile.add(jtFile);
//			listJTFile.add(jtFile);
//			
//			IMReqUtil.createMUC(getActivity(), this, null,  subject, title,
//					orderTime, listJTContactID, listOrganizationID, content, listJTFile);
		}
		
		/**
		 * 获取页数据
		 */
		public void startGetData(int pageIndex) {
			IMReqUtil.getListIMRecord(getActivity(), this, null,  pageIndex, 20);
		}
		
		private void loadMoreData() { // 加载更多数据
			if(mPage!=null && !mPage.hasMore())
				return;
			
			if(mState == STATE_NORMAL){
				mvProgressBar.setVisibility(View.VISIBLE);
				mvTextView.setText("正在加载");
				startGetData(nowIndex+1);
				this.mState = STATE_GETMORE;
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {

			Log.i(TAG, "firstVisibleItem=" + firstVisibleItem
					+ "\nvisibleItemCount=" + visibleItemCount + "\ntotalItemCount"
					+ totalItemCount);
			lastItem = firstVisibleItem + visibleItemCount - 1; // 减1是因为上面加了个addFooterView
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			Log.i(TAG, "scrollState=" + scrollState);
			// 下拉到空闲是，且最后一个item的数等于数据的总数时，进行更新
			if (lastItem == count && scrollState == this.SCROLL_STATE_IDLE) {
				Log.i(TAG, "拉到最底部");
				moreView.setVisibility(view.VISIBLE);
				loadMoreData();
			}
		}
		
	
}
