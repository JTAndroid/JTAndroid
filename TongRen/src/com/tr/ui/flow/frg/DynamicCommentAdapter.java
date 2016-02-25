package com.tr.ui.flow.frg;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.App;
import com.tr.R;
import com.tr.api.HomeReqUtil;
import com.tr.api.UserReqUtil;
import com.tr.model.api.DataBox;
import com.tr.model.obj.DynamicComment;
import com.tr.model.obj.DynamicNews;
import com.tr.ui.home.frg.FrgFlow.DynamicAdapter;
import com.tr.ui.widgets.SmileyParser;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

public class DynamicCommentAdapter extends BaseAdapter implements IBindData {

	private Context mContext;
	private List<DynamicComment> mListComment;
	private DynamicNews mDynamicNews;
	private int len;
	private SmileyParser parser;
	private FrgFlow frgflow;
	
	private int del_index;
	private DynamicComment del_comment;


	public DynamicCommentAdapter(Context context, FrgFlow frgflow) {
		this.mContext = context;
		mListComment = new ArrayList<DynamicComment>();
		this.parser = SmileyParser.getInstance(mContext);
		this.frgflow = frgflow;
		initPopUpWindow();
	}

	public void setData(List<DynamicComment> listComment, DynamicNews mDynamicNews, int len) {
		this.mListComment = listComment;
		this.mDynamicNews = mDynamicNews;
		this.len = len;
	}
	
	public void setLen(int len){
		this.len = len;
	}

	@Override
	public int getCount() {
		return len;
	}

	@Override
	public Object getItem(int i) {
		return mListComment.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		final ItemHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_listcomment_item, null);
			holder = new ItemHolder();
			holder.mTxtContent = (TextView) convertView.findViewById(R.id.HFLLICommentContent);
			holder.HFLLICommentName = (TextView) convertView.findViewById(R.id.HFLLICommentName);
			convertView.setTag(holder);
		} else {
			holder = (ItemHolder) convertView.getTag();
		}

		holder.HFLLICommentName.setVisibility(View.GONE);
		String mTxtContent = "";
		// 给各个元素赋值
		final DynamicComment comment = mListComment.get(position);

		String username = comment.getUserName();
		String targetUserName = comment.getTargetUserName();
		String content = comment.getComment();
		String replyText = "回复了";
		String str = ": ";
		
		SpannableString spanusername = new SpannableString(username);
		SpannableString spanreplyText = new SpannableString(replyText);
		SpannableString spancontent = new SpannableString(content);
		SpannableString spanstr = new SpannableString(str);
		
		spanusername.setSpan(new ForegroundColorSpan(0xff569ee2), 0, username.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spancontent.setSpan(new ForegroundColorSpan(0xff92adb5), 0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spanreplyText.setSpan(new ForegroundColorSpan(0xff92adb5), 0, replyText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spanstr.setSpan(new ForegroundColorSpan(0xff569ee2), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		holder.mTxtContent.setText("");//先清空
		holder.mTxtContent.append(spanusername);
		if(!TextUtils.isEmpty(targetUserName)){
			SpannableString spantargetUserName = new SpannableString(targetUserName);
			spantargetUserName.setSpan(new ForegroundColorSpan(0xff569ee2), 0, targetUserName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			holder.mTxtContent.append(spanreplyText);
			holder.mTxtContent.append(spantargetUserName);
		}
		holder.mTxtContent.append(spanstr);
		holder.mTxtContent.append(spancontent);
		
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				frgflow.mHomeInputLL.setVisibility(View.VISIBLE);
				frgflow.mHomeFrgCommentEt.setHint("回复 "+comment.getUserName());
				frgflow.editTextCaptureFocus();
				frgflow.flag = "comment";
				frgflow.mIndexComment = position;
				frgflow.mDynamicNews = mDynamicNews;
			}
		});
		
		convertView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				if((comment.getUserId()+"").equals(App.getUserID())){
					del_index = position;
					del_comment = comment;
					showPopWindow(parent, v, del_comment);
				}
				return true;
			}
		});
		
		replyTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismissPopwindow();
				frgflow.mHomeInputLL.setVisibility(View.VISIBLE);
				frgflow.mHomeFrgCommentEt.setHint("回复 "+comment.getUserName());
				frgflow.editTextCaptureFocus();
				frgflow.flag = "comment";
				frgflow.mIndexComment = position;
				frgflow.mDynamicNews = mDynamicNews;
			}
		});
		
		deleteTv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismissPopwindow();
				mListComment.remove(del_index);
				frgflow.delComment(del_comment);
			}
		});
		
		return convertView;
	}

	private class ItemHolder {
		public TextView mTxtContent;// 评论内容
		public TextView HFLLICommentName;// 评论人
	}

	@Override
	public void bindData(int tag, Object object) {
		
	}

	public List<DynamicComment> getmListComment() {
		return mListComment;
	}
	
	// popupwindow VIew
	private View inflateDeleteView;
	private TextView deleteTv;
	private TextView replyTv;
	private PopupWindow window;

	private int measuredHeigh;
	private int measuredWidth;
	private int linmitHeight;

	/** 初始化评论删除拷贝的 popupwindow */
	private void initPopUpWindow() {
		inflateDeleteView = View.inflate(mContext, R.layout.layout_sociality_delete, null);
		window = new PopupWindow(inflateDeleteView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		window.setOutsideTouchable(true);
		deleteTv = (TextView) inflateDeleteView.findViewById(R.id.delete);
		replyTv = (TextView) inflateDeleteView.findViewById(R.id.save);
		replyTv.setText("回复");

		inflateDeleteView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		measuredWidth = inflateDeleteView.getMeasuredWidth();
		measuredHeigh = inflateDeleteView.getMeasuredHeight();
		Rect frame = new Rect();
		Activity mActivity = (Activity) mContext;
		mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		int actionBarHeight = mActivity.getActionBar().getHeight();
		linmitHeight = statusBarHeight + actionBarHeight;
	}
	
	public void showPopWindow(final ViewGroup parent, View v, DynamicComment comment) {
		if(comment.getUserId()==Long.valueOf(App.getUserID())){
			replyTv.setVisibility(View.GONE);
		}
		window.setBackgroundDrawable(new ColorDrawable(-000000));
		int[] location = new int[2];
		v.getLocationInWindow(location);
		Activity mActivity = (Activity) mContext;
		int width = mActivity.getWindowManager().getDefaultDisplay().getWidth();
		if (linmitHeight >= location[1]) {
			this.window.showAtLocation(parent, Gravity.LEFT + Gravity.TOP, width / 2 - (measuredWidth / 2), linmitHeight - measuredHeigh + 10);
		} else {
			this.window.showAtLocation(parent, Gravity.LEFT + Gravity.TOP, width / 2 - (measuredWidth / 2), location[1] - measuredHeigh);
		}
	}
	
	private void dismissPopwindow() {
		if (window != null && window.isShowing()) {
			window.dismiss();
		}
	}

}
