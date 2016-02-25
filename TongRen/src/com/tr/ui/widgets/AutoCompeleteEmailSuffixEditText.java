package com.tr.ui.widgets;

//import com.baidu.navisdk.util.common.StringUtils;

import com.utils.string.StringUtils;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.text.Html;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

/**
 * 
 * @author Sun Jia'nan
 * @description 自动补全邮箱后缀的editText
 *
 */
public class AutoCompeleteEmailSuffixEditText extends EditText {

	private String mString;
	
	//上一次自动补全的文本
	private String lastAutoText = "";
	//上一次输入的文本
	private String lastText = "";
	//输入的文本长度
	private int textLength = 0;

	private String[] emailSufixs = new String[] {"@gintong.com", "@qq.com", "@163.com", "@126.com", "@gmail.com", "@sina.com", "@sina.cn", "@hotmail.com", "@yahoo.cn", "@sohu.com", "@foxmail.com", "@139.com", "@yeah.net",
			"@vip.qq.com", "@vip.sina.com", "@yahoo.com", "@msn.com", "@aol.com", "@ask.com", "@live.com", "@0355.net", "@163.net", "@263.net", "@3721.net", "@yeah.net", "@googlemail.com",
			"@mail.com", "@aim.com", "@walla.com", "@inbox.com", "@21cn.com", "@tom.com", "@etang.com", "@eyou.com", "@56.com", "@x.cn", "@chinaren.com", "@sogou.com", "@citiz.com" };
	private String suffixString;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (getSelectionStart() > textLength) {
				setSelection(textLength);
			}
		};
	};

	public AutoCompeleteEmailSuffixEditText(Context context) {
		super(context);
	}

	public AutoCompeleteEmailSuffixEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AutoCompeleteEmailSuffixEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
		super.onTextChanged(text, start, lengthBefore, lengthAfter);
		
		//这里的代码类似于一个闭合的二叉树结构
		
		//代码的核心部分  因为这个地方无形中形成了一个递归  因此比对上一次文本与这次文本是否相同作为递归出口
		
		if (text.toString().contains("@") && !lastText.equals(getText().toString())) {
			// Toast.makeText(context, text + "start: " + start, 0).show();

			suffixString = "";
			if (text.toString().contains(lastAutoText) && !StringUtils.isEmpty(lastAutoText)) {
				text = text.subSequence(0, text.toString().lastIndexOf(lastAutoText));
			}
			mString = "<font color=\"#000000\">" + text.toString() + "</font>";
			String subString = text.toString().substring(text.toString().indexOf("@"));
			
			
			//循环遍历邮箱后缀
			for (int i = 0; i < emailSufixs.length; i++) {
				if (emailSufixs[i].startsWith(subString)) {
					lastAutoText = emailSufixs[i].substring(subString.length());
					suffixString = "<font color=\"#b1b1b1\">" + lastAutoText + "</font>";
					break;
				}
			}

			lastText = Html.fromHtml(mString + suffixString).toString();
			setText(Html.fromHtml(mString + suffixString));
			textLength = text.length();
			// 重新设置光标的位置
			if (start + lengthAfter >= textLength) {
				setSelection(text.length());
			}
			else {
				setSelection(start + lengthAfter);
			}
		}
		//当不包含"@"时的情况
		else if (!StringUtils.isEmpty(mString) && !lastText.equals(getText().toString()) && !StringUtils.isEmpty(suffixString)) {
			if (text.toString().contains(lastAutoText) && !StringUtils.isEmpty(lastAutoText)) {
				text = text.subSequence(0, text.toString().lastIndexOf(lastAutoText));
			}
			mString = "<font color=\"#000000\">" + text.toString() + "</font>";
			lastText = Html.fromHtml(mString).toString();
			setText(Html.fromHtml(mString));
			textLength = text.length();
			if (start + lengthAfter >= textLength) {
				setSelection(text.length());
			}
			else {
				setSelection(start + lengthAfter);
			}
		}
		textLength = text.length();
		lastText = getText().toString();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			//延时处理--> 保证光标已经发生变化   否则取到的是上一次的光标位置
			handler.sendEmptyMessageDelayed(0, 1);
			break;
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
		//失去焦点
		if (!focused) {
			mString = "<font color=\"#000000\">" + getText().toString() + "</font>";
			lastText = Html.fromHtml(mString).toString();
			setText(lastText);
		}
		super.onFocusChanged(focused, direction, previouslyFocusedRect);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
