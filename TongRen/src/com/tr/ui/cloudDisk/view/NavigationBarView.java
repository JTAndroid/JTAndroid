package com.tr.ui.cloudDisk.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.tr.R;
import com.tr.api.CloudDiskReqUtil;
import com.tr.model.home.MSuggestionType;
import com.tr.ui.cloudDisk.FileManagementActivity;
import com.tr.ui.cloudDisk.model.UserCategory;
import com.tr.ui.widgets.CreateCatalogAlertDialog;
import com.tr.ui.widgets.KnowledgeSquareMenuPopupWindow.OnMyHomeMenuItemClickListener;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

public class NavigationBarView extends LinearLayout {
	private EditText fileSearchEt_navigationbar;
	private ImageView fileCreateCatalogIv_navigationbar;
	private TextView screen_navigationbar;
	private TextView select_navigationbar;
	private CreateCatalogAlertDialog mCreateCatalogAlertDialog;
	public Context context ;
	private LinearLayout screen_Ll;
	private List<UserCategory> suggestionTypeLists;
	private OnGetDataClickListener mItemClickListener;
	private PopupWindow popupWindow;
	public String [] screen = {"全部","我创建的","我收藏的"};
	public NavigationBarView(Context context) {
		super(context);
		this.context =context;
		init(context);
	}
	public NavigationBarView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context =context;
		init(context);
	}
	public NavigationBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context =context;
		init(context);
	}
	
	public EditText getFileSearchEt_navigationbar() {
		return fileSearchEt_navigationbar;
	}
	public void setFileSearchEt_navigationbar(EditText fileSearchEt_navigationbar) {
		this.fileSearchEt_navigationbar = fileSearchEt_navigationbar;
	}
	private void init(final Context context) {
		View view = View.inflate(context, R.layout.weiget_navigationbar, null);
		fileSearchEt_navigationbar = (EditText) view.findViewById(R.id.fileSearchEt_navigationbar);
		fileCreateCatalogIv_navigationbar = (ImageView) view.findViewById(R.id.fileCreateCatalogIv_navigationbar);
		screen_navigationbar = (TextView) view.findViewById(R.id.screen_navigationbar);
		select_navigationbar = (TextView) view.findViewById(R.id.select_navigationbar);
		screen_Ll = (LinearLayout) view.findViewById(R.id.screen_Ll);
		
		screen_navigationbar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow = new PopupWindow(context);
				popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
				popupWindow.setWidth(LayoutParams.WRAP_CONTENT);
				popupWindow.setBackgroundDrawable(new BitmapDrawable());
				popupWindow.setFocusable(true);
				popupWindow.setOutsideTouchable(true);
				
				final ListView contentView = new ListView(context);
				contentView.setAdapter(new BaseAdapter() {
					@Override
					public View getView(int position, View convertView, ViewGroup parent) {
						convertView = View.inflate(context,
								R.layout.people_list_item_popupwindow, null);
						TextView popupwindow_Tv = (TextView) convertView
								.findViewById(R.id.popupwindow_Tv);
						popupwindow_Tv.setText(screen[position]);
						return convertView;
					}

					@Override
					public long getItemId(int position) {
						return 0;
					}

					@Override
					public Object getItem(int position) {
						return null;
					}

					@Override
					public int getCount() {
						return screen.length;
					}
				});
				contentView.setDivider(null);
				contentView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						
						if (popupWindow.isShowing()) {
							popupWindow.dismiss();
						}
						String text = screen[position];
						System.out.println(text);
						select_navigationbar.setText(text);
					}
				});
			
				popupWindow.setContentView(contentView);
				popupWindow.showAsDropDown(screen_navigationbar);
			}
		});
		
		fileCreateCatalogIv_navigationbar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mItemClickListener.showAlertDialog();
			}
		});
		
		fileSearchEt_navigationbar = (EditText) view.findViewById(R.id.fileSearchEt_navigationbar);
		//搜素文件或目录
		fileSearchEt_navigationbar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
		public boolean onEditorAction(TextView v, int actionId,KeyEvent event) {
				if (actionId ==EditorInfo.IME_ACTION_SEARCH) {
				//隐藏软键盘
				InputMethodManager imm = (InputMethodManager)context.getSystemService(context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(fileSearchEt_navigationbar.getWindowToken(), 0);
				//请求网络搜索
				if (!TextUtils.isEmpty(fileSearchEt_navigationbar.getText().toString())) {
					mItemClickListener.getListData(fileSearchEt_navigationbar.getText().toString());
				}
				
				
				return true;
			}
			return false;
		}
	});
		
		
		
		this.addView(view,new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));  
	}
	public void NoScreen(boolean b){
		if (b) {
			screen_Ll.setVisibility(View.GONE);
		}else{
			screen_Ll.setVisibility(View.VISIBLE);
		}
		
	}
	
	/**
	 * 设置监听器
	 * 
	 * @param listener
	 */
	public void setOnGetDataClickListener(OnGetDataClickListener listener) {
		mItemClickListener = listener;
	}
	//接口
	public interface OnGetDataClickListener {
		public void getListData(String keyWord);//获取搜索的List数据集合
		public void showAlertDialog();//显示创建或更新目录对话框

	}
	
}
	
