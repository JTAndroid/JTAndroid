package com.tr.ui.home.frg;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
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
	private CommentShowType mShowType;
	private PopupWindow window;
	private SmileyParser parser;

	// popupwindow VIew
	private View inflateDeleteView;
	private TextView deleteTv;
	private TextView copyTv;

	private int measuredHeigh;
	private int measuredWidth;
	private int linmitHeight;

	public enum CommentShowType {
		CommentShowAll, CommentShowOneLine
	}

	public void dissmissPop() {
		dismissPopwindow();
	}

	public DynamicCommentAdapter(Context context, PopupWindow window) {
		this.mContext = context;
		mListComment = new ArrayList<DynamicComment>();
		this.window = window;
		this.parser = SmileyParser.getInstance(mContext);
	}

	public DynamicCommentAdapter(Context context) {
		this.mContext = context;
		mListComment = new ArrayList<DynamicComment>();
		this.parser = SmileyParser.getInstance(mContext);
	}

	public void setData(List<DynamicComment> listComment, DynamicNews mDynamicNews, int len, CommentShowType showType) {
		this.mListComment = listComment;
		this.mDynamicNews = mDynamicNews;
		this.len = len;
		this.mShowType = showType;
	}

	public void setPopUpWindowViewConfig(View inflateDeleteView, TextView deleteTv, TextView copyTv, int measuredHeigh, int measuredWidth, int linmitHeight) {
		this.copyTv = copyTv;
		this.inflateDeleteView = inflateDeleteView;
		this.deleteTv = deleteTv;
		this.measuredHeigh = measuredHeigh;
		this.measuredWidth = measuredWidth;
		this.linmitHeight = linmitHeight;
	}

	public void updateAdapter(List<DynamicComment> listComment) {
		if (listComment != null) {
			mListComment = listComment;
			notifyDataSetChanged();
		}
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
//			if (mShowType != CommentShowType.CommentShowAll) {
//				convertView = LayoutInflater.from(mContext).inflate(R.layout.home_frg_flow_listview_listcomment_item, null);
//			} else {
//				convertView = LayoutInflater.from(mContext).inflate(R.layout.home_frg_flow_listcomment_item, null);
//			}
			convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_listcomment_item, null);
			holder = new ItemHolder();
			holder.mTxtContent = (TextView) convertView.findViewById(R.id.HFLLICommentContent);
			holder.HFLLICommentName = (TextView) convertView.findViewById(R.id.HFLLICommentName);
			convertView.setTag(holder);
		} else {
			holder = (ItemHolder) convertView.getTag();
		}

		// 给各个元素赋值
		final DynamicComment comment = mListComment.get(position);

		// holder.mTxtSource.setText(comment.getName() + ": ");
		String title = comment.getUserName();
		String content = comment.getComment();
//		String allString = title + content;
//		int titleColor = App.getApp().getResources().getColor(R.color.home_index_dynamic_text_color);
//		int contentColor = App.getApp().getResources().getColor(R.color.str_fous_list_item_tv);
//		SpannableStringBuilder spannableStr = new SpannableStringBuilder(allString);
//		spannableStr.setSpan(new ForegroundColorSpan(titleColor), 0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		spannableStr.setSpan(new ForegroundColorSpan(contentColor), title.length(), allString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		holder.HFLLICommentName.setText(title+":");
		holder.mTxtContent.setText(parser.addSmileySpans(content));
		
		
		holder.mTxtContent.setTag(position);
		
		holder.mTxtContent.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {

				dismissPopwindow();
				int  thisPosition = (Integer) holder.mTxtContent.getTag();
				
				final DynamicComment thisComment = mListComment.get(position);
				
				long commentId = thisComment.getId();// 获取评论id

				if (App.getUserID().equals(String.valueOf(thisComment.getUserId()))) {// 是自己的评论，显示复制和删除
					deleteTv.setVisibility(View.VISIBLE);
				} else {// 只显示复制
					deleteTv.setVisibility(View.GONE);
				}

				deleteTv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						HomeReqUtil.deleteDynamicComment(mContext, DynamicCommentAdapter.this, null, thisComment.getId());
						dismissPopwindow();
					}
				});

				copyTv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
						cmb.setText(thisComment.getComment());
						Toast.makeText(mContext, "已复制", 0).show();
						dismissPopwindow();
					}
				});

				showPopWindow(parent, v);
				return false;
			}

		});

		return convertView;
	}

	public void showPopWindow(final ViewGroup parent, View v) {
		this.window.setBackgroundDrawable(new ColorDrawable(-000000));
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

	/**
	 * 表情转换
	 * 
	 * @param str
	 * @return
	 */
	/*private CharSequence smileyParser(String str) {
		SmileyParser smileyParser;// 表情匹配
		SmileyParser2 smileyParser2;// 表情匹配
		smileyParser = SmileyParser.getInstance(mContext);
		smileyParser2 = SmileyParser2.getInstance(mContext);
		CharSequence charSequence1 = smileyParser.addSmileySpans(str);
		CharSequence charSequence2 = smileyParser2.addSmileySpans(charSequence1);
		return charSequence2;
	}*/

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}

	private class ItemHolder {
		public TextView mTxtContent;// 评论内容
		public TextView HFLLICommentName;// 评论人
	}

	private void dismissPopwindow() {
		if (window != null && window.isShowing()) {
			window.dismiss();
		}
	}

	@Override
	public void bindData(int tag, Object object) {
		if (object != null) {
			if (tag == EAPIConsts.HomeReqType.HOME_REQ_DELETE_DYNAMIC_COMMENT) {//删除
				String result = (String) object;
				if ("true".equals(result)) {
					//获取评论列表
					UserReqUtil.doGetDynamicListComment(
							mContext,
							DynamicCommentAdapter.this,
							UserReqUtil.getDoGetDynamicListCommentParams(
									mDynamicNews.getId(), 0, 20), null);
				}
			}
			else if (tag == EAPIConsts.ReqType.GET_DYNAMIC_LIST_COMMENT) {//评论列表

				DataBox dataBox = (DataBox) object;
				if (null != dataBox.mDynamicCommentList) {
					mListComment.clear();
					mListComment = dataBox.mDynamicCommentList;
					
					if (mShowType != CommentShowType.CommentShowAll) {
						this.len = mListComment.size() > 2 ? 2 : mListComment.size();
//						FrgFlow.changeDynamicListUI(mListComment, index);
						ArrayList<DynamicComment> comments = new ArrayList<DynamicComment>();
						if (mListComment.size() > 2) {
							comments.add(mListComment.get(0));
							comments.add(mListComment.get(1));
						}
						else {
							comments.addAll(mListComment);
						}
						mDynamicNews.setComments(comments );
						mDynamicNews.setCount(mListComment.size());
						mAdapterDynamic.notifyDataSetChanged();
						
					}else {//显示全部评论
						this.len = mListComment.size();
						if (mHFCommentNum!=null) {
							mHFCommentNum.setText(""+len);
						}
					}
					if (this.len==0&&mAdapterDynamic!=null) {
						mAdapterDynamic.notifyDataSetChanged();
					}
				}
			
			}
			notifyDataSetChanged();
		}
	}

	public List<DynamicComment> getmListComment() {
		return mListComment;
	}

	public void setParentAdapter(DynamicAdapter mAdapterDynamic) {
		this.mAdapterDynamic = mAdapterDynamic;
	}
	private DynamicAdapter mAdapterDynamic;

	public void setIndex(int position) {
		this.index = position;
	}
	private int index;

	public void setCountLayout(TextView mHFCommentNum) {
		this.mHFCommentNum = mHFCommentNum;
	}
	private TextView mHFCommentNum;
}
