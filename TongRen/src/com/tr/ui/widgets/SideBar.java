package com.tr.ui.widgets;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.tr.R;
import com.tr.db.ConnectionsCacheData;
import com.utils.string.StringUtils;

/**
 * 字母索引控件
 * 
 * @author gushi 最后修改
 */
public class SideBar extends View {
	public static final String TAG = "SideBar";

	private char[] letters;
	private ListView list;
	private TextView mDialogText;
	private int m_nItemHeight = 15;
	private int textSize = 0;
	Context context;

	private int j;

	private ArrayList<Character> letterList;

	private ConnectionsCacheData cnsCacheData;

	public SideBar(Context context) {
		super(context);
		this.context = context;
		textSize = context.getResources().getDimensionPixelSize(R.dimen.sidebar_text_size);
		init();
		this.letters = new char[] { '#', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
				'V', 'W', 'X', 'Y', 'Z', '*' };
	}

	public SideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		textSize = context.getResources().getDimensionPixelSize(R.dimen.sidebar_text_size);
		init();
		this.letters = new char[] { '#', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
				'V', 'W', 'X', 'Y', 'Z', '*' };
	}

	public SideBar(Context context, AttributeSet attrs, char[] letters) {
		super(context, attrs);
		textSize = context.getResources().getDimensionPixelSize(R.dimen.sidebar_text_size);
		this.letters = letters;
	}

	public void init() {

	}

	/**
	 * 重绘字母表
	 * 
	 * @param letters
	 */
	public void redrawLetters(char[] letters) {
		if (letters != null && letters.length > 0) {
			this.letters = letters;
			invalidate(); // 重绘
		}
	}

	public SideBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public void setListView(ListView _list, ArrayList<Character> letterList, ConnectionsCacheData cnsCacheData) {
		list = _list;
		this.letterList = letterList;
		this.cnsCacheData = cnsCacheData;
	}

	public void setTextView(TextView mDialogText) {
		this.mDialogText = mDialogText;
	}

	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);

		String MSG = "onTouchEvent()";

		if (list != null && letterList != null) {

		} else {
			return true;
		}
		int i = (int) event.getY();
		int idx = i / m_nItemHeight;
		if (idx >= letterList.size()) {
			idx = letterList.size() - 1;
		} else if (idx < 0) {
			idx = 0;
		}
		if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
			mDialogText.setVisibility(View.VISIBLE);
			mDialogText.setText("" + letterList.get(idx));

			int position = cnsCacheData.getCharAt(letterList.get(idx));
			// Log.i(TAG, MSG + " letters[idx] = " + letters[idx] +
			// " position = " + position);
			if (position == -1) {
				return true;
			}
			// 加1 是因为 listview 从0开始, 但 我们的listview第0条 显示的不是人脉等对象 页是一个自定义样式 应该是加1
			// 但是 我的listview还有一个header 所以 要加2
			list.setSelection(position + 1);
			System.out.println(position + "移动的位置!!!!!!!!");
		} else {
			mDialogText.setVisibility(View.INVISIBLE);
		}
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (letterList == null ) {
			return;
		}
		if (letterList.isEmpty()) {
			return;
		}
		m_nItemHeight = this.getHeight() / letterList.size();
		Paint paint = new Paint();
		paint.setColor(0xff595c61);
		paint.setTextSize(textSize);
		paint.setTextAlign(Paint.Align.CENTER);
		float widthCenter = getMeasuredWidth() / 2;
		for (int i = 0; i < letterList.size(); i++) {
			canvas.drawText(String.valueOf(letterList.get(i)), widthCenter, m_nItemHeight + (i * m_nItemHeight), paint);
		}
		super.onDraw(canvas);
	}
}
