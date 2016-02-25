package com.tr.ui.im;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.tr.App;
import com.tr.R;
import com.tr.model.obj.Area;
import com.tr.model.obj.IMGroupCategory;
import com.tr.model.obj.InvestKeyword;
import com.tr.model.obj.InvestType;
import com.tr.model.obj.MoneyType;
import com.tr.model.obj.Trade;
import com.tr.navigate.ENavConsts;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.widgets.CateaoryGrid;
import com.tr.ui.widgets.HorizontalListView;
import com.utils.http.IBindData;

public class IMCreateGroupCategoryActivity extends JBaseFragmentActivity implements IBindData{
	
	CateaoryGrid moneyTypeGrid,moneyRangeGrid,investGrid,tradeGrid,areaGrid;
	ImageView bankrollEnd,investEnd,tradeEnd,areaEnd;
	View bankrollTv,investTv,tradeTv,areaTv;//分类
	EditText editText=null;//输入框
	HorizontalListView horizontalListView;//选中横向list
	ArrayList<IMGroupCategory> selectedGroupCategorys=new ArrayList<IMGroupCategory>();//保存选中数据
	InvestKeyword paramInvestKeyword;
	//当前焦点的 单选item
	IMGroupCategory moneyTypeIMGroupCategory;
	IMGroupCategory moneyRangeIMGroupCategory;
	IMGroupCategory areaIMGroupCategory;
	String fromActivityName;
	int actionInt=0;
	public final static int action_out=0;
	public final static int action_in=1;
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getParam();
		this.setContentView(R.layout.im_creategroup_choose);
		moneyTypeGrid = (CateaoryGrid) findViewById(R.id.currencyGrid);
		moneyTypeGrid.mIsSingleSelection=true;
		initGridAdapter(IMGroupCategory.type_moneyType,moneyTypeGrid);
		moneyTypeGrid.setOnItemClickListener(gridOnItemClickListener);
		moneyTypeGrid.setNumColumns(3);
		
		moneyRangeGrid = (CateaoryGrid) findViewById(R.id.bankrollGrid);
		moneyRangeGrid.mIsSingleSelection=true;
		initGridAdapter(IMGroupCategory.type_moneyRange,moneyRangeGrid);
		moneyRangeGrid.setOnItemClickListener(gridOnItemClickListener);
		moneyRangeGrid.setNumColumns(3);
		
		investGrid = (CateaoryGrid) findViewById(R.id.investGrid);
		investGrid.mIsSingleSelection=false;
		initGridAdapter(IMGroupCategory.type_invest,investGrid);
		investGrid.setOnItemClickListener(gridOnItemClickListener);
		investGrid.setNumColumns(3);
		
		tradeGrid = (CateaoryGrid) findViewById(R.id.tradeGrid);
		tradeGrid.mIsSingleSelection=false;
		initGridAdapter(IMGroupCategory.type_trade,tradeGrid);
		tradeGrid.setOnItemClickListener(gridOnItemClickListener);
		tradeGrid.setNumColumns(3);
		
		areaGrid = (CateaoryGrid) findViewById(R.id.areaGrid);
		areaGrid.mIsSingleSelection=true;
		initGridAdapter(IMGroupCategory.type_area,areaGrid);
		areaGrid.setOnItemClickListener(gridOnItemClickListener);
		areaGrid.setNumColumns(3);
		
		bankrollTv =  findViewById(R.id.bankrolltext);
		bankrollTv.setOnClickListener(onclick);
		investTv = findViewById(R.id.investtv);
		investTv.setOnClickListener(onclick);
		tradeTv =  findViewById(R.id.tradetv);
		tradeTv.setOnClickListener(onclick);
		areaTv =  findViewById(R.id.areatv);
		areaTv.setOnClickListener(onclick);
		
		bankrollEnd=(ImageView)findViewById(R.id.bankrollend);
		investEnd=(ImageView)findViewById(R.id.investend);
		tradeEnd=(ImageView)findViewById(R.id.tradeend);
		areaEnd=(ImageView)findViewById(R.id.areaend);
		
		
		editText=(EditText)findViewById(R.id.edit);
		editText.addTextChangedListener(new Watcher());
		
		horizontalListView=(HorizontalListView)findViewById(R.id.choosedata);
		horizontalListView.setAdapter(new SelectAdapter(this));
		horizontalListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//取消焦点，删除元素，并刷新
				IMGroupCategory iMGroupCategory=(IMGroupCategory)arg1.getTag();
				int type=iMGroupCategory.getType();
				//多选处理，只处理多选
				if( type==IMGroupCategory.type_invest||type==IMGroupCategory.type_trade){
					iMGroupCategory.setFocuse(false);
					selectedGroupCategorys.remove(iMGroupCategory);
					//horizontalListView.setSelection(arg2-1);
					//根据类型取消焦点刷新数据
					if(type==IMGroupCategory.type_invest){
						ImageAdapter imageAdapter=(ImageAdapter)investGrid.getAdapter();
						imageAdapter.notifyDataSetChanged();
					}else if(type==IMGroupCategory.type_trade){
						ImageAdapter imageAdapter=(ImageAdapter)tradeGrid.getAdapter();
						imageAdapter.notifyDataSetChanged();
					}
					((SelectAdapter)horizontalListView.getAdapter()).notifyDataSetChanged();
					horizontalListView.setSelection(selectedGroupCategorys.size()-1);
				}
			}
		});
	}
	
	//读取传入的参数
    public void getParam(){
    	Intent intent=getIntent();  
    	fromActivityName = intent.getStringExtra(ENavConsts.EFromActivityName);  
    	actionInt= intent.getIntExtra(ENavConsts.EFromActivityType,0);  
    	if(intent.hasExtra(ENavConsts.datas)){
    		paramInvestKeyword=(InvestKeyword)intent.getSerializableExtra(ENavConsts.datas);  
    	}
    }
	
	
	public void doFinish(){
		InvestKeyword investKeyword=new InvestKeyword();
		for(IMGroupCategory iMGroupCategory:selectedGroupCategorys){
			if(iMGroupCategory.getType()==IMGroupCategory.type_moneyType){
				investKeyword.mMoneyType=(MoneyType)iMGroupCategory;
			}else if(iMGroupCategory.getType()==IMGroupCategory.type_moneyRange){
				investKeyword.mMoneyRange=iMGroupCategory.getName();
			}else if(iMGroupCategory.getType()==IMGroupCategory.type_invest){
				investKeyword.mListInvestType.add((InvestType)iMGroupCategory);
			}else if(iMGroupCategory.getType()==IMGroupCategory.type_trade){
				investKeyword.mListTrade.add((Trade)iMGroupCategory);
			}else if(iMGroupCategory.getType()==IMGroupCategory.type_area){
				//investKeyword.mListArea.add((Area)iMGroupCategory);
				investKeyword.mArea = (Area) iMGroupCategory;
			}
		}
    	Intent it = new Intent();
        it.putExtra(ENavConsts.redatas, investKeyword );  
        setResult(Activity.RESULT_OK, it);  
        finish();  
    }
	
	/**
	 * actionbar 中菜单点击事件
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.conference_create_next:
			doFinish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.im_chatmenu, menu);
		return true;
	}
	
	OnItemClickListener gridOnItemClickListener=new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			editText.setText("");
			CateaoryGrid cateaoryGrid=(CateaoryGrid)arg0;
			ImageAdapter adapter=(ImageAdapter)cateaoryGrid.getAdapter();
			
			IMGroupCategory iMGroupCategory=(IMGroupCategory)arg1.getTag();
			int type=iMGroupCategory.getType();
			
			if(cateaoryGrid.mIsSingleSelection){
				if(iMGroupCategory.isFocuse()){
					return;
				}
				if(type==IMGroupCategory.type_moneyType){
					moneyTypeIMGroupCategory.setFocuse(false);
					selectedGroupCategorys.remove(moneyTypeIMGroupCategory);
					moneyTypeIMGroupCategory=iMGroupCategory;
					moneyTypeIMGroupCategory.setFocuse(true);
					selectedGroupCategorys.add(0,moneyTypeIMGroupCategory);
					horizontalListView.setSelection(0);
				}else if(type==IMGroupCategory.type_moneyRange){
					moneyRangeIMGroupCategory.setFocuse(false);
					selectedGroupCategorys.remove(moneyRangeIMGroupCategory);
					moneyRangeIMGroupCategory=iMGroupCategory;
					moneyRangeIMGroupCategory.setFocuse(true);
					selectedGroupCategorys.add(1,moneyRangeIMGroupCategory);
					horizontalListView.setSelection(1);
				}else if(type==IMGroupCategory.type_area){
					areaIMGroupCategory.setFocuse(false);
					selectedGroupCategorys.remove(areaIMGroupCategory);
					areaIMGroupCategory=iMGroupCategory;
					areaIMGroupCategory.setFocuse(true);
					selectedGroupCategorys.add(2,areaIMGroupCategory);
					horizontalListView.setSelection(2);
				}
				adapter.notifyDataSetChanged();
				((SelectAdapter)horizontalListView.getAdapter()).notifyDataSetChanged();
			}else{//取得焦点，添加到 显示栏，刷新栏目
				iMGroupCategory.setFocuse(!iMGroupCategory.isFocuse());
				if(iMGroupCategory.isFocuse()){
					arg1.setBackgroundResource(iMGroupCategory.getResFocuse());
					//增加
					selectedGroupCategorys.add((IMGroupCategory)arg1.getTag());
					((SelectAdapter)horizontalListView.getAdapter()).notifyDataSetChanged();
					horizontalListView.setSelection(((SelectAdapter)horizontalListView.getAdapter()).getCount()-1);
					((SelectAdapter)horizontalListView.getAdapter()).notifyDataSetChanged();
				}else{
					arg1.setBackgroundResource(iMGroupCategory.getResdefault());
					//删除
					if(arg2!=0){
						horizontalListView.setSelection(arg2-1);
					}
					selectedGroupCategorys.remove((IMGroupCategory)arg1.getTag());
					((SelectAdapter)horizontalListView.getAdapter()).notifyDataSetChanged();
				}
				adapter.notifyDataSetChanged();
				
				
//				SelectAdapter selectAdapter=((SelectAdapter)horizontalListView.getAdapter());
//				int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED); 
//				int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED); 
//				
//				int screenWidth = DisplayUtil.getScreenWidth(IMCreateGroupCategoryActivity.this);
//				
//				int totalw=0;
//				int totalh=0;
//				
//				for(int i=0;i<iMGroupCategorys.size();i++){
//					View view=selectAdapter.getView(i,null,null);
//					view.measure(w, h); 
//					totalh =view.getMeasuredHeight(); 
//					int width =view.getMeasuredWidth(); 
//					if(totalw+width>screenWidth/3*2){
//						totalw=screenWidth/3*2;
//						break;
//					}else{
//						totalw+=width;
//					}
//				}
//					RelativeLayout.LayoutParams layout= (RelativeLayout.LayoutParams)horizontalListView.getLayoutParams();
//					layout.width=totalw;
//					layout.height=totalh;
//					horizontalListView.setLayoutParams(layout);
				
			}
		}
	};
	
	public class Watcher implements TextWatcher{
        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub 
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                int after) {
            // TODO Auto-generated method stub    
            
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                int count) {
        	((ImageAdapter)moneyRangeGrid.getAdapter()).getFilter().filter(editText.getText().toString());
        	((ImageAdapter)investGrid.getAdapter()).getFilter().filter(editText.getText().toString());
        	((ImageAdapter)tradeGrid.getAdapter()).getFilter().filter(editText.getText().toString());
        	((ImageAdapter)areaGrid.getAdapter()).getFilter().filter(editText.getText().toString());
        	((ImageAdapter)moneyTypeGrid.getAdapter()).getFilter().filter(editText.getText().toString());
        }
    }
	
	/**选中的显示类*/
    public class SelectAdapter extends BaseAdapter {
    	Context mContext=null;
    	public SelectAdapter(Context context){
    		mContext=context;
    	}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return selectedGroupCategorys.size();
		}
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			IMGroupCategory iMGroupCategory=selectedGroupCategorys.get(position);
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.im_creategroupcategory_select_item, parent,false);
			}
			TextView view=(TextView)convertView.findViewById(R.id.text);
			view.setText(iMGroupCategory.getName());
			
			convertView.setTag(iMGroupCategory);
			return convertView;
		}
    }
    
    
    
	View.OnClickListener onclick=new OnClickListener() {
		@Override
		public void onClick(View v) { 	
			if(bankrollTv==v){
				if(moneyRangeGrid.getVisibility()==View.VISIBLE){
					moneyRangeGrid.setVisibility(View.GONE);
					moneyTypeGrid.setVisibility(View.GONE);
					bankrollEnd.setVisibility(View.VISIBLE);
					((ImageView)findViewById(R.id.bankrolltext_tag)).setImageResource(R.drawable.zhankai);
				}else{
					moneyRangeGrid.setVisibility(View.VISIBLE);
					moneyTypeGrid.setVisibility(View.VISIBLE);
					bankrollEnd.setVisibility(View.GONE);
					((ImageView)findViewById(R.id.bankrolltext_tag)).setImageResource(R.drawable.shouqi);
				}
			}else if(investTv==v){
				if(investGrid.getVisibility()==View.VISIBLE){
					investGrid.setVisibility(View.GONE);
					investEnd.setVisibility(View.VISIBLE);
					((ImageView)findViewById(R.id.investtv_tag)).setImageResource(R.drawable.zhankai);
				}else{
					investGrid.setVisibility(View.VISIBLE);
					investEnd.setVisibility(View.GONE);
					((ImageView)findViewById(R.id.investtv_tag)).setImageResource(R.drawable.shouqi);
				}
			}else if(tradeTv==v){
				if(tradeGrid.getVisibility()==View.VISIBLE){
					tradeGrid.setVisibility(View.GONE);
					tradeEnd.setVisibility(View.VISIBLE);
					((ImageView)findViewById(R.id.tradetv_tag)).setImageResource(R.drawable.zhankai);
				}else{
					tradeGrid.setVisibility(View.VISIBLE);
					tradeEnd.setVisibility(View.GONE);
					((ImageView)findViewById(R.id.tradetv_tag)).setImageResource(R.drawable.shouqi);
				}
			}else if(areaTv==v){
				if(areaGrid.getVisibility()==View.VISIBLE){
					areaGrid.setVisibility(View.GONE);
					areaEnd.setVisibility(View.VISIBLE);
					((ImageView)findViewById(R.id.areatv_tag)).setImageResource(R.drawable.zhankai);
				}else{
					areaGrid.setVisibility(View.VISIBLE);
					areaEnd.setVisibility(View.GONE);
					((ImageView)findViewById(R.id.areatv_tag)).setImageResource(R.drawable.shouqi);
				}
			}
		}
	};

	public ImageAdapter initGridAdapter(int type,CateaoryGrid iMEditMumberGrid){
		
		if(type==IMGroupCategory.type_moneyType){
			ArrayList tempListMoneyType=(ArrayList)App.getApp().getAppData().getListMoneyType();
			ArrayList<IMGroupCategory> listMoneyType=(ArrayList<IMGroupCategory>)tempListMoneyType;
			for(IMGroupCategory iMGroupCategory:listMoneyType){
				iMGroupCategory.setFocuse(false);
				iMGroupCategory.setType(IMGroupCategory.type_moneyType);
			}
			boolean isSetOk=false;
			if(paramInvestKeyword!=null&&paramInvestKeyword.mMoneyType!=null){
				for(IMGroupCategory iMGroupCategory:listMoneyType){
					MoneyType tempMoneyType=(MoneyType)iMGroupCategory;
					if(tempMoneyType.tag.equals(paramInvestKeyword.mMoneyType.tag)){
						iMGroupCategory.setFocuse(true);
						selectedGroupCategorys.add(iMGroupCategory);
						moneyTypeIMGroupCategory=iMGroupCategory;
						isSetOk=true;
						break;
					}
				}
			}
			if(!isSetOk){
				moneyTypeIMGroupCategory=listMoneyType.get(0);
				moneyTypeIMGroupCategory.setFocuse(true);
				selectedGroupCategorys.add(moneyTypeIMGroupCategory);
			}
			
			ImageAdapter imageAdapter=new ImageAdapter(this,listMoneyType );
			iMEditMumberGrid.setAdapter(imageAdapter);
		}else if(type==IMGroupCategory.type_moneyRange){		
			ArrayList<IMGroupCategory> mros=new ArrayList<IMGroupCategory>();
			for(int i=0;i<App.getApp().getAppData().getListMoneyRange().size();i++){
				MoneyRangeObj mro=new MoneyRangeObj(App.getApp().getAppData().getListMoneyRange().get(i));
				mro.setType(MoneyRangeObj.type_moneyRange);
				mros.add(mro);
			}
			boolean isSetOk=false;
			if(paramInvestKeyword!=null&&paramInvestKeyword.mMoneyRange!=null){
				for(IMGroupCategory iMGroupCategory:mros){
					if(iMGroupCategory.getName().equals(paramInvestKeyword.mMoneyRange)){
						iMGroupCategory.setFocuse(true);
						selectedGroupCategorys.add(iMGroupCategory);
						moneyRangeIMGroupCategory=iMGroupCategory;
						isSetOk=true;
						break;
					}
				}
			}
			if(!isSetOk){
				moneyRangeIMGroupCategory=mros.get(0);
				moneyRangeIMGroupCategory.setFocuse(true);
				selectedGroupCategorys.add(moneyRangeIMGroupCategory);
			}
			
			ImageAdapter imageAdapter=new ImageAdapter(this,mros );
			iMEditMumberGrid.setAdapter(imageAdapter);
		}else if(type==IMGroupCategory.type_trade){
			ArrayList tempListTrade=(ArrayList)App.getApp().getAppData().getListTrade();
			ArrayList<IMGroupCategory> listTrade=(ArrayList<IMGroupCategory>)tempListTrade;
			for(IMGroupCategory iMGroupCategory:listTrade){
				iMGroupCategory.setFocuse(false);
				iMGroupCategory.setType(IMGroupCategory.type_trade);
			}
			
			
			if(paramInvestKeyword!=null&&paramInvestKeyword.mListTrade!=null){
				for(Trade trade : paramInvestKeyword.mListTrade){
					for(IMGroupCategory iMGroupCategory:listTrade){
						Trade tempTrade=(Trade)iMGroupCategory;
						if(tempTrade.getID().equals(trade.getID())){
							iMGroupCategory.setFocuse(true);
							selectedGroupCategorys.add(iMGroupCategory);
							break;
						}
					}
				}
			}
			
			
			ImageAdapter imageAdapter=new ImageAdapter(this,listTrade );
			iMEditMumberGrid.setAdapter(imageAdapter);
			
		}else if(type==IMGroupCategory.type_invest){
			if(actionInt==action_in){
				ArrayList tempListInInvestType=(ArrayList)App.getApp().getAppData().getListInInvestType();
				ArrayList<IMGroupCategory> listInInvestType=(ArrayList<IMGroupCategory>)tempListInInvestType;
				for(IMGroupCategory iMGroupCategory:listInInvestType){
					iMGroupCategory.setFocuse(false);
					iMGroupCategory.setType(IMGroupCategory.type_invest);
				}
				
				if(paramInvestKeyword!=null&&paramInvestKeyword.mListInvestType!=null){
					for(InvestType investType : paramInvestKeyword.mListInvestType){
						for(IMGroupCategory iMGroupCategory:listInInvestType){
							InvestType tempInvestType=(InvestType)iMGroupCategory;
							if(tempInvestType.getID().equals(investType.getID())){
								iMGroupCategory.setFocuse(true);
								selectedGroupCategorys.add(iMGroupCategory);
								break;
							}
						}
					}
				}
				
				ImageAdapter imageAdapter=new ImageAdapter(this,listInInvestType );
				iMEditMumberGrid.setAdapter(imageAdapter);
			}else if(actionInt==action_out){
				ArrayList tempListOutInvestType=(ArrayList)App.getApp().getAppData().getListOutInvestType();
				ArrayList<IMGroupCategory> listOutInvestType=(ArrayList<IMGroupCategory>)tempListOutInvestType;
				for(IMGroupCategory iMGroupCategory:listOutInvestType){
					iMGroupCategory.setFocuse(false);
					iMGroupCategory.setType(IMGroupCategory.type_invest);
				}
				
				if(paramInvestKeyword!=null&&paramInvestKeyword.mListInvestType!=null){
					for(InvestType investType : paramInvestKeyword.mListInvestType){
						for(IMGroupCategory iMGroupCategory:listOutInvestType){
							InvestType tempInvestType=(InvestType)iMGroupCategory;
							if(tempInvestType.getID().equals(investType.getID())){
								iMGroupCategory.setFocuse(true);
								selectedGroupCategorys.add(iMGroupCategory);
								break;
							}
						}
					}
				}
				
				ImageAdapter imageAdapter=new ImageAdapter(this,listOutInvestType);
				iMEditMumberGrid.setAdapter(imageAdapter);
			}
			
		}else if(type==IMGroupCategory.type_area){
			ArrayList tempListArea=(ArrayList)App.getApp().getAppData().getListArea();
			ArrayList<IMGroupCategory> listArea=(ArrayList<IMGroupCategory>)tempListArea;
			for(IMGroupCategory iMGroupCategory:listArea){
				iMGroupCategory.setFocuse(false);
				iMGroupCategory.setType(IMGroupCategory.type_area);
			}
			
			boolean isSetOk=false;
			if(paramInvestKeyword!=null&&paramInvestKeyword.mArea!=null){
				for(IMGroupCategory iMGroupCategory:listArea){
					Area tempArea=(Area)iMGroupCategory;
					if(tempArea.mName.equals(paramInvestKeyword.mArea.mName)){
						iMGroupCategory.setFocuse(true);
						selectedGroupCategorys.add(iMGroupCategory);
						areaIMGroupCategory=iMGroupCategory;
						isSetOk=true;
						break;
					}
				}
			}
			if(!isSetOk){
				areaIMGroupCategory=listArea.get(0);
				areaIMGroupCategory.setFocuse(true);
				selectedGroupCategorys.add(areaIMGroupCategory);
			}
			
			ImageAdapter imageAdapter=new ImageAdapter(this,listArea );
			iMEditMumberGrid.setAdapter(imageAdapter);
		}
		
		
		
//			ImageAdapter imageAdapter=null;
//			ArrayList<IMGroupCategory> data = new ArrayList<IMGroupCategory>();
//			for (int i = 0; i < 6; i++) {
//				IMGroupCategory iMGroupCategory=new IMGroupCategory();
//				iMGroupCategory.mTitle="String " + i;
//				iMGroupCategory.setFocuse(false);
//				iMGroupCategory.setType(type);
//				iMGroupCategory.setResdefault(R.color.item_gray);
//				iMGroupCategory.setResFocuse(R.color.item_yellow);
//				data.add(iMGroupCategory);
//				if(i==0&&type==IMGroupCategory.type_bankroll){
//					iMGroupCategorys.add(new iMGroupCategory);
//					iMGroupCategory.setFocuse(true);
//					bankrollIMGroupCategory=iMGroupCategory;
//				}else if(i==0&&type==IMGroupCategory.type_currency){
//					iMGroupCategorys.add(iMGroupCategory);
//					iMGroupCategory.setFocuse(true);
//					currencyIMGroupCategory=iMGroupCategory;
//				}
//			}
//			imageAdapter=new ImageAdapter(this, data);
//			iMEditMumberGrid.setAdapter(imageAdapter);
		return null;
	}
	
	
	/** 选人用的 */
	public static class ImageAdapter extends BaseAdapter implements Filterable {
		private ArrayList<IMGroupCategory> showdata;
		Context mContext=null;
		boolean removeState = false;
		private ArrayList<IMGroupCategory> baseData;

		public ImageAdapter(Context c, ArrayList<IMGroupCategory> data) {
			mContext = c;
			baseData=data;
			cloneData();
		}
		
		public void cloneData(){
			ArrayList<IMGroupCategory> tempIMGroupCategorys=new ArrayList<IMGroupCategory>();
			for(int i=0;i<baseData.size();i++){
				tempIMGroupCategorys.add( baseData.get(i));
			}
			showdata=tempIMGroupCategorys;
		}
		Filter newFilter;
		@Override
		public Filter getFilter() {
			if(newFilter!=null){
				return newFilter;
			}
	            newFilter = new Filter() {
	                @Override
	                protected void publishResults(CharSequence constraint, FilterResults results) {
	                	showdata=(ArrayList<IMGroupCategory>)results.values;
	                    notifyDataSetChanged();
	                }
	                @Override
	                protected FilterResults performFiltering(CharSequence prefix) {
	                    //ArrayList<String> i = new ArrayList<String>();
	                    FilterResults results = new FilterResults();        
	                    List<IMGroupCategory> filteredArrList = new ArrayList<IMGroupCategory>();
	                    
	                    if (prefix!= null && prefix.toString().length() > 0) {
	                    	String strData=null;
	                        for (int index = 0; index < baseData.size(); index++) {
	                            strData=baseData.get(index).getName();
	                            if(strData.contains(prefix)){
	                            	filteredArrList.add(baseData.get(index));
	                            }
	                        }
	                        results.values = filteredArrList;
	                        results.count = filteredArrList.size();                   
	                    }else{
	                        synchronized (baseData){
	                            results.values = baseData;
	                            results.count = baseData.size();
	                        }
	                    }
	                    return results;
	                }
	            };
	        return newFilter;
		}

		public int getCount() {
			return showdata.size();
		}

		public Object getItem(int position) {
			return showdata.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			IMGroupCategory iMGroupCategory=showdata.get(position);
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.im_creategroupcategory_grid_item, null);
			}
			convertView.setTag(iMGroupCategory);
			setFocus(iMGroupCategory,convertView);
			((TextView)convertView.findViewById(R.id.text)).setText(iMGroupCategory.getName());
			return convertView;
		}
		
		public void setFocus(IMGroupCategory iMGroupCategory,View view){
			if(iMGroupCategory.isFocuse()){
				if(iMGroupCategory.getResFocuse()!=0){
					view.setBackgroundResource(iMGroupCategory.getResFocuse());
				}
			}else{
				if(iMGroupCategory.getResdefault()!=0){
					view.setBackgroundResource(iMGroupCategory.getResdefault());
				}
			}
		}
	}

	class MoneyRangeObj extends IMGroupCategory{
		String data=null;
		public MoneyRangeObj(String str){
			data=str;
		}

		@Override
		public void initWithJson(JSONObject jsonObject) throws JSONException,
				MalformedURLException, ParseException {	
		}

		@Override
		public JSONObject toJSONObject() throws JSONException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return data;
		}
	}
	
	public ActionBar actionbar=null;
	
	@Override
	public void initJabActionBar() {
		actionbar = getActionBar();
		actionbar.setDisplayShowTitleEnabled(true);
		actionbar.setTitle("选择类型");
	}

	@Override
	public void bindData(int tag, Object object) {
		// TODO Auto-generated method stub
		
	}
}
