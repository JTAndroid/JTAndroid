package com.tr.ui.conference.initiatorhy.search;

import java.util.ArrayList;
import java.util.List;

import com.tr.model.conference.MExpFriendContact;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.model.obj.JTContactMini;
import com.tr.model.obj.KnowledgeMini;
import com.tr.model.obj.RequirementMini;
import com.tr.ui.conference.initiatorhy.InitiatorDataCache;



import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;

@SuppressLint("DefaultLocale")
public class SearchEditWatcher implements TextWatcher{
	public final static int SEARCH_NONE = 0;
	public final static int SEARCH_INVITE_ATTEND_FRIEND = 1;
	public final static int SEARCH_INVITE_SPEAKER_FRIEND = 2;
	public final static int SEARCH_SHARE_PEOPLEHUB = 3;
	public final static int SEARCH_SHARE_DEMAND = 4;
	public final static int SEARCH_SHARE_KNOWLEADGE = 5;
	public static final int SEARCH_SHARE_FLOW = 6;
	
	private Context context;
	private int searchType;
	private boolean isPorbidSearch, isSearching, isInterrputSearch;
	private String searchKey;
	private ArrayList<Object> searchResultList = new ArrayList<Object>();
	private SearchHandler searchHandler;
	private SearchThread searchThread;
	private OnSearchListener searchListener;
	
	public SearchEditWatcher(Context context){
		this.context = context;
	}
	public SearchEditWatcher(Context context, int searchType){
		this.context = context;
		this.searchType = searchType;
	}
	public void setSearchType(int searchType){
		this.searchType = searchType;
	}
	public void setOnSearchListener(OnSearchListener searchListener){
		this.searchListener = searchListener;
	}
	public void setSearchEnabled(boolean enabled){
		isPorbidSearch = (enabled == true ? false : true);
	}
	public void resetSearch(){
		searchResultList.clear();
		isPorbidSearch = isSearching = isInterrputSearch = false;
		searchKey = null;
	}
	public ArrayList<Object> getSearchList(){
		return searchResultList;
	}
	

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
//		if(isPorbidSearch){
//			return;
//		}
//		try {
//			if(searchHandler == null){
//				searchHandler = new SearchHandler();
//			}
//			String editableStr = s.toString();
//			if(SearchUtil.isEmpty(editableStr)){
//				resetSearch();
//				Message msg = searchHandler.obtainMessage();	
//				msg.what = 1;
//				searchHandler.sendMessage(msg);
//				return;
//			}
//			if(isSearching){
//				isInterrputSearch = true;
//				boolean flag = true;
//				while(flag){
//					if(!isSearching){
//						flag = false;
//						isInterrputSearch = false;
//						break;
//					}
//				}
//			}
//			searchKey = editableStr;
//			searchResultList.clear();
//			searchThread = new SearchThread();
//			searchThread.start();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub
		if(isPorbidSearch){
			return;
		}
		try {
			if(searchHandler == null){
				searchHandler = new SearchHandler();
			}
			String editableStr = arg0.toString();
			if(SearchUtil.isEmpty(editableStr)){
				resetSearch();
				Message msg = searchHandler.obtainMessage();	
				msg.what = 1;
				searchHandler.sendMessage(msg);
				return;
			}
			if(isSearching){
				isInterrputSearch = true;
				boolean flag = true;
				while(flag){
					if(!isSearching){
						flag = false;
						isInterrputSearch = false;
						break;
					}
				}
			}
			searchKey = editableStr;
			searchResultList.clear();
			searchThread = new SearchThread();
			searchThread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
	}
	private boolean compare(JTContactMini item){
		if(SearchUtil.isEmpty(item.name)){
			return false;
		}
		try {
			if(item.name.contains(searchKey)){
				return true;
			}
			if(SearchUtil.isEnglish(searchKey)){
				String lowerKey = searchKey.toLowerCase();
				if(item.name.toLowerCase().contains(lowerKey)){
					return true;
				}
				String firstLetters = SearchUtil.getEveryWordFirstLetter(item.name);
				if(!SearchUtil.isEmpty(firstLetters)){
					if(firstLetters.toLowerCase().contains(lowerKey)){
						return true;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
	private boolean compare(RequirementMini item){
		if(SearchUtil.isEmpty(item.mTitle)){
			return false;
		}
		try {
			if(item.mTitle.contains(searchKey)){
				return true;
			}
			if(SearchUtil.isEnglish(searchKey)){
				String lowerKey = searchKey.toLowerCase();
				if(item.mTitle.toLowerCase().contains(lowerKey)){
					return true;
				}
				String firstLetters = SearchUtil.getEveryWordFirstLetter(item.mTitle);
				if(!SearchUtil.isEmpty(firstLetters)){
					if(firstLetters.toLowerCase().contains(lowerKey)){
						return true;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
	private boolean compare(KnowledgeMini2 item){
		if(SearchUtil.isEmpty(item.title)){
			return false;
		}
		try {
			if(item.title.contains(searchKey)){
				return true;
			}
			if(SearchUtil.isEnglish(searchKey)){
				String lowerKey = searchKey.toLowerCase();
				if(item.title.toLowerCase().contains(lowerKey)){
					return true;
				}
				String firstLetters = SearchUtil.getEveryWordFirstLetter(item.title);
				if(!SearchUtil.isEmpty(firstLetters)){
					if(firstLetters.toLowerCase().contains(lowerKey)){
						return true;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
	private boolean doSearch(){
		if(SearchUtil.isEmpty(searchKey)){
			resetSearch();
			return true;
		}
		switch (searchType) {
			case SEARCH_INVITE_ATTEND_FRIEND:
			case SEARCH_INVITE_SPEAKER_FRIEND:
			case SEARCH_SHARE_FLOW:
			case SEARCH_SHARE_PEOPLEHUB:{
				List<MExpFriendContact> friList;
				
				if(searchType == SEARCH_SHARE_PEOPLEHUB){
					friList = InitiatorDataCache.getInstance().sharePeoplehubList;
				}else{
					friList = InitiatorDataCache.getInstance().friendList;
				}
				
				if(SearchUtil.isEmpty(friList)){
					return false;
				}
				for(MExpFriendContact expItem : friList){
					if(isInterrputSearch){
						resetSearch();
						return true;
					}
					if(!SearchUtil.isEmpty(expItem.contactList)){
						for(JTContactMini jtcItem : expItem.contactList){
							if(isInterrputSearch){
								resetSearch();
								return true;
							}
							if(compare(jtcItem)){
								searchResultList.add(jtcItem);
							}
						}
					}
				}
			}break;
			case SEARCH_SHARE_DEMAND:{
				List<RequirementMini> demandList = InitiatorDataCache.getInstance().shareDemandList;
				if(SearchUtil.isEmpty(demandList)){
					return false;
				}
				for(RequirementMini item : demandList){
					if(isInterrputSearch){
						resetSearch();
						return true;
					}
					if(compare(item)){
						searchResultList.add(item);
					}
				}
			}break;
			case SEARCH_SHARE_KNOWLEADGE:{
				List<KnowledgeMini2> knowleadgeList = InitiatorDataCache.getInstance().shareKnowleadgeList;
				if(SearchUtil.isEmpty(knowleadgeList)){
					return false;
				}
				for(KnowledgeMini2 item : knowleadgeList){
					if(isInterrputSearch){
						resetSearch();
						return true;
					}
					if(compare(item)){
						searchResultList.add(item);
					}
				}
			}break;
			default:
				break;
		}
		return false;
	}
	@SuppressLint("HandlerLeak")
	private class SearchHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:{
				if(searchListener == null){
					return;
				}
				searchListener.onSearchListener(searchType, searchResultList);
			}break;
		}
			super.handleMessage(msg);
		}
	}
	private class SearchThread extends Thread{
		@Override
		public void run(){
			super.run();
			isSearching = true;
			try {
				if(!doSearch()){
					Message msg = searchHandler.obtainMessage();	
					msg.what = 1;
					searchHandler.sendMessage(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			isSearching = false;
			return;
		}
	}
	public interface OnSearchListener {
		void onSearchListener(int searchType, ArrayList<Object> resultList);
	}
}
