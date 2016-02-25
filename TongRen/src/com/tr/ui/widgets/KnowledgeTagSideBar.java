package com.tr.ui.widgets;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.tr.R;
import com.tr.model.knowledge.UserTag;

public class KnowledgeTagSideBar extends View {
	private char[] l;
	private ArrayList<UserTag> listUserTag = null;
	private ListView list;
	private TextView mDialogText;
	private int m_nItemHeight = 15;
	private int textSize = 0;
	Context context;

	public KnowledgeTagSideBar(Context context) {
		super(context);
		this.context = context;
		textSize = context.getResources().getDimensionPixelSize(R.dimen.sidebar_text_size);
		init();
	}

	public KnowledgeTagSideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		textSize = context.getResources().getDimensionPixelSize(R.dimen.sidebar_text_size);
		l = new char[] { '#', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '*' };
	}

	public void init() {

	}

	public KnowledgeTagSideBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public void setListView(ListView _list, ArrayList<UserTag> listUserTag) {
		list = _list;
		this.listUserTag = listUserTag;
	}

	public void setTextView(TextView mDialogText) {
		this.mDialogText = mDialogText;
	}

	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		if (list != null && listUserTag != null) {

		}
		else {
			return true;
		}
		int i = (int) event.getY();
		int idx = i / m_nItemHeight;
		if (idx >= l.length) {
			idx = l.length - 1;
		}
		else if (idx < 0) {
			idx = 0;
		}
		if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
			mDialogText.setVisibility(View.VISIBLE);
			mDialogText.setText("" + l[idx]);

			int position = getCharAt(l[idx]);
			if (position == -1) {
				return true;
			}
			list.setSelection(position);
		}
		else {
			mDialogText.setVisibility(View.INVISIBLE);
		}
		return true;
	}

	private int getCharAt(char c) {

		for (int i = 0; i < listUserTag.size(); i++) {
			UserTag tag = listUserTag.get(i);
			String spell = tag.getSpell();
			if (!TextUtils.isEmpty(spell)) {
				if (spell.charAt(0) == c)
					return i;
			}
			
		}
		return -1;
	}

	protected void onDraw(Canvas canvas) {
		m_nItemHeight = this.getHeight() / l.length;
		Paint paint = new Paint();
		paint.setColor(0xff595c61);
		paint.setTextSize(textSize);
		paint.setTextAlign(Paint.Align.CENTER);
		float widthCenter = getMeasuredWidth() / 2;
		for (int i = 0; i < l.length; i++) {
			canvas.drawText(String.valueOf(l[i]), widthCenter, m_nItemHeight + (i * m_nItemHeight), paint);
		}
		super.onDraw(canvas);
	}
}
