package com.tr.ui.knowledge;

import java.util.ArrayList;
import java.util.Map;

import android.os.Bundle;

import com.tr.R;
import com.tr.api.KnowledgeReqUtil;
import com.tr.model.knowledge.KnowledgeComment;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.knowledge.swipeback.SwipeBackActivity;
import com.utils.common.EConsts;
import com.utils.http.IBindData;
import com.utils.http.EAPIConsts.KnoReqType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tr.R;
import com.tr.api.KnowledgeReqUtil;
import com.tr.model.knowledge.KnowledgeComment;
import com.tr.model.obj.ConnectionsMini;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.common.view.MyXListView;
import com.tr.ui.common.view.MyXListView.IXListViewListener;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.widgets.BasicListView;
import com.tr.ui.widgets.CommonSmileyParser;
import com.tr.ui.widgets.ConnsEditDialog;
import com.tr.ui.widgets.SmileyParser;
import com.tr.ui.widgets.SmileyParser2;
import com.tr.ui.widgets.SmileyView;
import com.utils.common.EUtil;
import com.utils.common.JTDateUtils;
import com.utils.http.EAPIConsts.KnoReqType;
import com.utils.http.IBindData;
public class KnowledegeCommentDetailsActivity extends SwipeBackActivity implements IBindData{
	private long mKnowledgeId;
	private ArrayList<KnowledgeComment> newKnowCommentsList;
	private KnowledegeCommentDetailsFragment commentDetailsFragment;
	private Integer total;
	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(),"热门评论", false, null, false, true);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_knowledegecomment);
		mKnowledgeId = getIntent().getLongExtra(
				EConsts.Key.KNOWLEDGE_DETAIL_ID, 1L);
		commentDetailsFragment = new KnowledegeCommentDetailsFragment(mKnowledgeId);
		getSupportFragmentManager().beginTransaction().add(R.id.fragment_knowledegecomment, commentDetailsFragment).commit();
		initCommentData();
	}

	

	/**
	 * 初始化数据
	 */
	private void initCommentData() {
		KnowledgeReqUtil.doGetKnowledgeComment(this,
				this, mKnowledgeId, 0L, 0, 20, null);
	}
	@Override
	public void bindData(int tag, Object object) {
		if (KnoReqType.GetKnowledgeComment == tag) {// 获取知识评论
			if (object != null) {
				if (newKnowCommentsList != null) {
					newKnowCommentsList.clear();// 如果当前刷新中，则清除之前数据
				}
				Map<String, Object> hm = (Map<String, Object>) object;
				newKnowCommentsList = (ArrayList<KnowledgeComment>) hm
						.get("listKnowledgeComment");
				total = (Integer) hm.get("total");
				commentDetailsFragment.setNewKnowCommentsList(newKnowCommentsList,total,mKnowledgeId);
			}

		}
	}
}
