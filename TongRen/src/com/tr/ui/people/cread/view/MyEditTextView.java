package com.tr.ui.people.cread.view;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tr.R;
import com.utils.log.ToastUtil;
/**
 * 条目控件
 * @author John
 *
 */
public class MyEditTextView extends LinearLayout {

	public String MyEdit_Id;
	private EditText myEditTextView_Et; // 输入框
	private TextView myEditTextView_Tv; // 标题
	private String textLabel; // 标题的String
	private String textHintLabel; // 默认的String
	private int image_id;
	private String edttext_inputtype; // 输入框的输入类型
	private int label_width; // 标题的宽度
	private String defaultText; // 默认的标题
	private boolean isReadOnly; // 只读
	private int delete_image_id; // 默认的图片资源
	private boolean isChoose; // 是否选择
	private boolean isDelete; // 是否显示删除图片
	private boolean isAddMore; // 是否动态更多
	private boolean isInput; // 是否可以输入
	private boolean isUnderline; // 是否有下划线
	private ImageView AddMore_Iv;
	private FrameLayout AddMore_hint_Fl;
	private boolean isAddMore_hint; // 是否显示点击加载更多
	private boolean isPopupwindow; // 是否可以下拉框
	private String txtReadOnly;
	private boolean isPopupwindow_Text; // 是否显示三角形
	private TextView hint_Tv; // 默认输入框的hint
	private LinearLayout edittext_ll;
	private ImageView dropdowntriangle;
	private ImageView right_arrow_iv;
	private TextView myEditText_Tv2;
	private boolean isRequired; // 是否必填
	private ImageView required_iv;
	private LinearLayout input_Ll;
	private boolean isDivider; // 是否显示边框
	private EditText myEditTextView_custom;
	private boolean isCustom; // 是否自定义标题
	private View input_unline;
	private EditText custom_Et;
	private boolean isCustom_Text; // 是否显示多行文本输入框
	private View custom_underline;
	private boolean isCheckBox;
	private boolean JustInput;
	private boolean JustLabel;
	private String hint_Text;
	private View overline;
	private boolean isOverline;
	private Context mContext;
	private boolean isPull;
	private ImageView down_arrow_iv;
	private LinearLayout myEditTextView_Rootview;
	private int MaxLength;

	public MyEditTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.MyEditTextView);
		textLabel = typedArray.getString(R.styleable.MyEditTextView_text_label);
		textHintLabel = typedArray
				.getString(R.styleable.MyEditTextView_text_hint_label);
		image_id = typedArray.getResourceId(
				R.styleable.MyEditTextView_image_id, 0);
		edttext_inputtype = typedArray
				.getString(R.styleable.MyEditTextView_edttext_inputtype);
		label_width = Math.round(typedArray.getDimension(
				R.styleable.MyEditTextView_label_width, 0));
		defaultText = typedArray
				.getString(R.styleable.MyEditTextView_default_text);
		txtReadOnly = typedArray
				.getString(R.styleable.MyEditTextView_text_readonly);
		delete_image_id = typedArray.getResourceId(
				R.styleable.MyEditTextView_delete_image_id, 0);
		isChoose = typedArray.getBoolean(R.styleable.MyEditTextView_isChoose,
				false);
		isDelete = typedArray.getBoolean(R.styleable.MyEditTextView_isDelete,
				false);
		isAddMore = typedArray.getBoolean(R.styleable.MyEditTextView_isAddMore,
				false);
		isInput = typedArray.getBoolean(R.styleable.MyEditTextView_isInput,
				false);
		isPull =  typedArray.getBoolean(R.styleable.MyEditTextView_isPull,
				false);
		isUnderline = typedArray.getBoolean(
				R.styleable.MyEditTextView_isUnderline, false);
		isAddMore_hint = typedArray.getBoolean(
				R.styleable.MyEditTextView_isAddMore_hint, false);
		isReadOnly = typedArray.getBoolean(
				R.styleable.MyEditTextView_isReadOnly, false);
		isPopupwindow = typedArray.getBoolean(
				R.styleable.MyEditTextView_isPopupwindow, false);
		isRequired = typedArray.getBoolean(
				R.styleable.MyEditTextView_isRequired, false);
		isPopupwindow_Text = typedArray.getBoolean(
				R.styleable.MyEditTextView_isPopupwindow_Text, false);
		isDivider = typedArray.getBoolean(R.styleable.MyEditTextView_isDivider,
				false);
		isCustom = typedArray.getBoolean(R.styleable.MyEditTextView_isCustom,
				false);
		isCustom_Text = typedArray.getBoolean(
				R.styleable.MyEditTextView_isCustom_Text, false);
		isCheckBox = typedArray.getBoolean(
				R.styleable.MyEditTextView_isCheckBox, false);
		JustInput = typedArray.getBoolean(R.styleable.MyEditTextView_JustInput,
				false);
		JustLabel = typedArray.getBoolean(R.styleable.MyEditTextView_JustLabel,
				false);
		isOverline = typedArray.getBoolean(
				R.styleable.MyEditTextView_isOverline, false);
		hint_Text = typedArray.getString(R.styleable.MyEditTextView_hint_Text);
		MaxLength = typedArray.getInteger(R.styleable.MyEditTextView_MaxLength, 50);
		this.init(context);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public MyEditTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.MyEditTextView);
		textLabel = typedArray.getString(R.styleable.MyEditTextView_text_label);
		textHintLabel = typedArray
				.getString(R.styleable.MyEditTextView_text_hint_label);
		image_id = typedArray.getResourceId(
				R.styleable.MyEditTextView_image_id, 0);
		edttext_inputtype = typedArray
				.getString(R.styleable.MyEditTextView_edttext_inputtype);
		label_width = Math.round(typedArray.getDimension(
				R.styleable.MyEditTextView_label_width, 0));
		defaultText = typedArray
				.getString(R.styleable.MyEditTextView_default_text);
		txtReadOnly = typedArray
				.getString(R.styleable.MyEditTextView_text_readonly);
		delete_image_id = typedArray.getResourceId(
				R.styleable.MyEditTextView_delete_image_id, 0);
		isChoose = typedArray.getBoolean(R.styleable.MyEditTextView_isChoose,
				false);
		isDelete = typedArray.getBoolean(R.styleable.MyEditTextView_isDelete,
				false);
		isAddMore = typedArray.getBoolean(R.styleable.MyEditTextView_isAddMore,
				false);
		isInput = typedArray.getBoolean(R.styleable.MyEditTextView_isInput,
				false);
		isUnderline = typedArray.getBoolean(
				R.styleable.MyEditTextView_isUnderline, false);
		isAddMore_hint = typedArray.getBoolean(
				R.styleable.MyEditTextView_isAddMore_hint, false);
		isReadOnly = typedArray.getBoolean(
				R.styleable.MyEditTextView_isReadOnly, false);
		isPopupwindow = typedArray.getBoolean(
				R.styleable.MyEditTextView_isPopupwindow, false);
		isRequired = typedArray.getBoolean(
				R.styleable.MyEditTextView_isRequired, false);
		isPopupwindow_Text = typedArray.getBoolean(
				R.styleable.MyEditTextView_isPopupwindow_Text, false);
		isDivider = typedArray.getBoolean(R.styleable.MyEditTextView_isDivider,
				false);
		isCustom = typedArray.getBoolean(R.styleable.MyEditTextView_isCustom,
				false);
		isCustom_Text = typedArray.getBoolean(
				R.styleable.MyEditTextView_isCustom_Text, false);
		isPull =  typedArray.getBoolean(R.styleable.MyEditTextView_isPull,
				false);
		MaxLength = typedArray.getInteger(R.styleable.MyEditTextView_MaxLength, 50);
		this.init(context);
	}

	public boolean isAddMore_hint() {
		return isAddMore_hint;
	}

	/**
	 * 显示点击加载更多字样
	 * 
	 * @param isAddMore_hint
	 */
	public void setAddMore_hint(boolean isAddMore_hint) {
		this.isAddMore_hint = isAddMore_hint;
		if (isAddMore_hint) {
			AddMore_hint_Fl.setVisibility(View.VISIBLE);
		} else {
			AddMore_hint_Fl.setVisibility(View.GONE);
		}
	}

	public boolean isCheckBox() {
		return isCheckBox;
	}

	public void setCheckBox(boolean isCheckBox) {
		this.isCheckBox = isCheckBox;
		if (isCheckBox) {
			checkbox_cb.setVisibility(View.VISIBLE);
		}
	}

	public String getMyEdit_Id() {
		return MyEdit_Id;
	}

	public void setMyEdit_Id(String myEdit_Id) {
		MyEdit_Id = myEdit_Id;
	}

	public boolean isDelete() {
		return isDelete;
	}

	public boolean isAddMore() {
		return isAddMore;
	}

	public ImageView getAddMore_Iv() {
		return AddMore_Iv;
	}

	public void setAddMore_Iv(ImageView addMore_Iv) {
		AddMore_Iv = addMore_Iv;
	}

	public CheckBox getCheckbox_cb() {
		return checkbox_cb;
	}

	public void setCheckbox_cb(CheckBox checkbox_cb) {
		this.checkbox_cb = checkbox_cb;
	}

	public boolean isPopupwindow_Text() {
		return isPopupwindow_Text;
	}

	/**
	 * 显示三角形
	 * 
	 * @param isPopupwindow_Text
	 */
	public void setPopupwindow_Text(boolean isPopupwindow_Text) {
		this.isPopupwindow_Text = isPopupwindow_Text;
		if (isPopupwindow_Text) {
			dropdowntriangle.setVisibility(View.VISIBLE);
		} else {
			dropdowntriangle.setVisibility(View.GONE);
		}
	}

	public MyEditTextView(Context context) {
		super(context);
		mContext = context;
		this.init(context);
	}

	public TextView getMyEditTextView_Tv() {
		return myEditTextView_Tv;
	}

	public void setMyEditTextView_Tv(TextView myEditTextView_Tv) {
		this.myEditTextView_Tv = myEditTextView_Tv;
	}

	public EditText getMyEditTextView_Et() {
		return myEditTextView_Et;
	}

	public void setMyEditTextView_Et(EditText myEditTextView_Et) {
		this.myEditTextView_Et = myEditTextView_Et;
	}

	/**
	 * 是否选择填写
	 * 
	 * @param isChoose
	 */
	public void setChoose(boolean isChoose) {
		this.isChoose = isChoose;
		if (isChoose) {
			myEditText_Tv2.setVisibility(View.VISIBLE);
			myedittext_Addmore_Rl.setVisibility(View.VISIBLE);
			myEditTextView_Et.setVisibility(View.GONE);
			myEditTextView_Et.setEnabled(false);
			right_arrow_iv.setVisibility(View.VISIBLE);
			right_arrow_iv.setEnabled(true);
		}
	}
	/**
	 * 是否选择填写
	 * 
	 * @param isChoose
	 */
	public void setChooseNoIcon(boolean isChoose) {
		this.isChoose = isChoose;
		if (isChoose) {
			myEditText_Tv2.setVisibility(View.VISIBLE);
			myedittext_Addmore_Rl.setVisibility(View.VISIBLE);
			myEditTextView_Et.setVisibility(View.GONE);
			myEditTextView_Et.setEnabled(false);
		}
	}

	/**
	 * 显示删除图片
	 * 
	 * @param isDelete
	 */
	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
		if (isDelete) {
			right_arrow_iv.setVisibility(View.GONE);
			myedittext_Addmore_Rl.setVisibility(View.VISIBLE);
			AddMore_Iv.setBackgroundResource(R.drawable.people_column_delete);
			AddMore_Iv.setVisibility(View.VISIBLE);
			AddMore_Iv.setEnabled(true);
		}

	}

	/**
	 * 显示加载更多图片
	 * 
	 * @param isAddMore
	 */
	public void setAddMore(boolean isAddMore) {
		this.isAddMore = isAddMore;
		if (isAddMore) {
			myEditTextView_Tv.setEnabled(true);
			myEditText_Tv2.setVisibility(View.GONE);
			myedittext_Addmore_Rl.setVisibility(View.VISIBLE);
			myEditTextView_Et.setVisibility(View.VISIBLE);
			myEditTextView_Et.setEnabled(true);
			right_arrow_iv.setVisibility(View.GONE);
			right_arrow_iv.setEnabled(false);
			AddMore_Iv.setEnabled(true);
			AddMore_Iv.setVisibility(View.VISIBLE);
			AddMore_Iv.setBackgroundResource(R.drawable.people_column_add);
			myedittext_Addmore_Rl.setVisibility(View.VISIBLE);
		}
	}

	public void setInput(boolean isInput) {
		this.isInput = isInput;
	}

	public void setUnderline(boolean isUnderline) {
		this.isUnderline = isUnderline;
	}

	public boolean isReadOnly() {
		return isReadOnly;
	}

	public boolean isCustom() {
		return isCustom;
	}

	public boolean isCustom_Text() {
		return isCustom_Text;
	}

	/**
	 * 显示多行文本框
	 * 
	 * @param isCustom_Text
	 */
	public void setCustom_Text(boolean isCustom_Text) {
		this.isCustom_Text = isCustom_Text;
		if (isCustom_Text) {
			myEditTextView_custom.setVisibility(View.VISIBLE);
			myEditTextView_custom.setHint("自定义文本");
			custom_Et.setVisibility(View.VISIBLE);
			custom_underline.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 标题是否可输入
	 * 
	 * @param isCustom
	 */
	public void setCustom(boolean isCustom) {
		this.isCustom = isCustom;
		if (isCustom) {
			myEditTextView_custom.setGravity(Gravity.CENTER_VERTICAL);
			myEditTextView_custom.setVisibility(View.VISIBLE);
			myEditTextView_Tv.setVisibility(View.GONE);
			input_unline.setVisibility(View.VISIBLE);
			myEditText_Tv2.setVisibility(View.GONE);
			myEditTextView_Et.setVisibility(View.VISIBLE);
			myEditTextView_Et.setEnabled(true);
		}
	}

	public void setLine(boolean isLine) {
		if (isLine) {
			input_unline.setVisibility(View.GONE);
		}
	}

	public RelativeLayout getMyedittext_Addmore_Rl() {
		return myedittext_Addmore_Rl;
	}

	public void setMyedittext_Addmore_Rl(RelativeLayout myedittext_Addmore_Rl) {
		this.myedittext_Addmore_Rl = myedittext_Addmore_Rl;
	}

	/**
	 * 获取标题
	 * 
	 * @return
	 */
	public String getTextLabel() {
		if (isCustom) {
			return myEditTextView_custom.getText().toString().trim();
		} else if (isCustom_Text) {
			return custom_Et.getText().toString().trim();
		} else {
			return textLabel;
		}

	}

	public String getHintLabel() {

		return hint_Tv2.getText().toString().trim();
	}

	public boolean isChoose() {
		return isChoose;
	}

	public boolean isPopupwindow() {
		return isPopupwindow;
	}

	public void setPopupwindow(boolean isPopupwindow) {
		this.isPopupwindow = isPopupwindow;
	}

	/**
	 * 设置标题
	 * 
	 * @param textLabel
	 */
	public void setTextLabel(String textLabel) {
		this.textLabel = textLabel;
		if (isCustom) {
			myEditTextView_custom.setText(textLabel);
		} else if (isCustom_Text) {
			custom_Et.setText(textLabel);
		} else {
			myEditTextView_Tv.setText(textLabel);
		}
	}

	public void setTextHintLabel(String textHintLabel) {
		this.textHintLabel = textHintLabel;
		myEditTextView_Et.setHint(textHintLabel);
		myEditTextView_Et.setHintTextColor(getResources().getColor(R.color.dialog_color));
	}

	public void setImage_id(int image_id) {
		this.image_id = image_id;
		// img.setBackgroundResource(image_id);
	}

	/**
	 * 设置输入类型
	 * 
	 */
	public void setNumEdttext_inputtype() {
		
			myEditTextView_Et.setInputType(InputType.TYPE_CLASS_PHONE);
	}

	/**
	 * 设置标题宽度
	 * 
	 * @param label_width
	 */
	public void setLabel_width(int label_width) {
		this.label_width = label_width;
		myEditTextView_Tv.setWidth(label_width);
	}

	/**
	 * 设置是否只读
	 * 
	 * @param isReadOnly
	 */
	public void setReadOnly(boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
		if (isReadOnly) {
			myEditText_Tv2.setVisibility(View.VISIBLE);
			myEditTextView_Et.setVisibility(View.GONE);
			myEditTextView_Et.setEnabled(false);
		}
	}

	/**
	 * 设置输入框默认字符串
	 * 
	 * @param defaultText
	 */
	public void setDefaultText(String defaultText) {
		this.defaultText = defaultText;
		myEditTextView_Et.setText(defaultText);
	}

	public boolean isJustLabel() {
		return JustLabel;
	}

	public void setJustLabel(boolean justLabel) {
		JustLabel = justLabel;
		if (JustLabel) {
			myEditTextView_Tv.setVisibility(View.VISIBLE);
			myEditTextView_custom.setVisibility(View.GONE);
			myEditTextView_Et.setVisibility(View.GONE);
			myEditTextView_Et.setEnabled(false);
			custom_Et.setVisibility(View.VISIBLE);
			custom_underline.setVisibility(View.VISIBLE);
		}
	}

	public boolean isOverline() {
		return isOverline;
	}

	public void setOverline(boolean isOverline) {
		if (isOverline) {
			overline.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 获取输入框的输入类型
	 * 
	 * @return
	 */
	private int getType() {
		if (edttext_inputtype != null && edttext_inputtype.trim().length() > 0) {
			if (edttext_inputtype.equals("textPassword")) {
				return InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD;
			} else if (edttext_inputtype.equals("textPhonetic")) {
				return InputType.TYPE_CLASS_PHONE
						| InputType.TYPE_TEXT_VARIATION_PHONETIC;
			} else if (edttext_inputtype.equals("textEmailAddress")) {
				return InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
			}
		}
		// return InputType.TYPE_CLASS_TEXT | InputType.TYPE_NULL;
		return -999;
	}

	public void setCustomtextLabel(String CustomtextLabel) {
		custom_Et.setText(CustomtextLabel);
	}
	
	private void init(final Context context) {
		UUID uuid = UUID.randomUUID();
		MyEdit_Id = uuid.toString();
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.people_weiget_input_edittext,
				null);
		// this.setOrientation(LinearLayout.HORIZONTAL);
		// this.setLayoutParams(new
		// LinearLayout.LayoutParams(width,LayoutParams.WRAP_CONTENT));
		myEditTextView_Tv = (TextView) view
				.findViewById(R.id.myEditTextView_Tv);
		hint_Tv = (TextView) view.findViewById(R.id.hint_Tv);
		myEditTextView_Et = (EditText) view
				.findViewById(R.id.myEditTextView_Et);
		AddMore_Iv = (ImageView) view.findViewById(R.id.Addmore_iv);
		required_iv = (ImageView) view.findViewById(R.id.required_iv);
		right_arrow_iv = (ImageView) view.findViewById(R.id.right_arrow_iv);
		down_arrow_iv =  (ImageView) view.findViewById(R.id.down_arrow_iv);
		AddMore_hint_Fl = (FrameLayout) view.findViewById(R.id.AddMore_hint_Fl);
		hint_Tv2 = (TextView) view.findViewById(R.id.hint_Tv);
		edittext_ll = (LinearLayout) view.findViewById(R.id.edittext_ll);
		myEditTextView_Rootview = (LinearLayout) view.findViewById(R.id.myEditTextView_Rootview);
		dropdowntriangle = (ImageView) view.findViewById(R.id.dropdowntriangle);
		myEditText_Tv2 = (TextView) view.findViewById(R.id.myEditText_Tv2);
		input_Ll = (LinearLayout) view.findViewById(R.id.input_Ll);
		overline = view.findViewById(R.id.overline);
		myEditTextView_custom = (EditText) view
				.findViewById(R.id.myEditTextView_custom);
		custom_Et = (EditText) view.findViewById(R.id.custom_Et);
		input_unline = view.findViewById(R.id.input_unline);
		custom_underline = view.findViewById(R.id.custom_underline);
		checkbox_cb = (CheckBox) view.findViewById(R.id.checkbox_cb);
		myedittext_Addmore_Rl = (RelativeLayout) view
				.findViewById(R.id.myedittext_Addmore_Rl);
		myEditTextView_Fl = (FrameLayout) view
				.findViewById(R.id.myEditTextView_Fl);
		if (isCheckBox) {
			checkbox_cb.setVisibility(View.VISIBLE);
			myedittext_Addmore_Rl.setVisibility(View.VISIBLE);
		}

		myEditTextView_Tv.setText(textLabel);
		if (label_width != 0) {
			myEditTextView_Tv.setWidth(label_width);
		}
		// myEditTextView_Et.setHint(textHintLabel);
		// myEditTextView_Et.setHintTextColor(Color.GRAY);

		int inputType = getType();
		if (inputType != -999) {
			myEditTextView_Et.setInputType(inputType);
		}
		if (defaultText != null && defaultText.trim().length() > 0) {
			myEditTextView_Et.setText(defaultText);
		}

		if (isAddMore) {
			myEditTextView_Et.setVisibility(View.VISIBLE);
			myEditTextView_Et.setEnabled(true);
			myEditText_Tv2.setVisibility(View.GONE);
			AddMore_Iv.setEnabled(false);
			myedittext_Addmore_Rl.setVisibility(View.VISIBLE);
			AddMore_Iv.setVisibility(View.VISIBLE);
			AddMore_Iv.setBackgroundResource(R.drawable.people_column_add);
		}
		if (MaxLength!=0) {
			myEditTextView_Et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MaxLength)});
		}
		if (isReadOnly) {
			myEditText_Tv2.setVisibility(View.VISIBLE);
			myEditTextView_Et.setVisibility(View.GONE);
			myEditTextView_Et.setEnabled(false);
		}
		if (!TextUtils.isEmpty(textHintLabel)) {
			myEditTextView_Et.setHint(textHintLabel);
			myEditTextView_Et.setHintTextColor(getResources().getColor(R.color.dialog_color));
			myEditText_Tv2.setHint(textHintLabel);
			myEditText_Tv2.setHintTextColor(getResources().getColor(R.color.dialog_color));
		}
		if (!TextUtils.isEmpty(hint_Text)) {
			hint_Tv2.setText(hint_Text);
		}
		if (isDelete) {
			myEditTextView_Et.setVisibility(View.VISIBLE);
			myEditTextView_Et.setEnabled(true);
			myEditText_Tv2.setVisibility(View.GONE);
			right_arrow_iv.setVisibility(View.GONE);
			right_arrow_iv.setEnabled(false);
			AddMore_Iv.setBackgroundResource(R.drawable.people_column_delete);
			AddMore_Iv.setEnabled(true);
			AddMore_Iv.setVisibility(View.VISIBLE);
			myedittext_Addmore_Rl.setVisibility(View.VISIBLE);
		}
		if (isChoose) {
			this.setBackgroundResource(R.drawable.create_edittext_choose_selector);
			myEditTextView_Et.setVisibility(View.GONE);
			myEditTextView_Et.setEnabled(false);
			myEditText_Tv2.setVisibility(View.VISIBLE);
			myedittext_Addmore_Rl.setVisibility(View.VISIBLE);
			right_arrow_iv.setVisibility(View.VISIBLE);
			right_arrow_iv.setEnabled(true);
		}

		if (isAddMore_hint) {
			AddMore_hint_Fl.setVisibility(View.VISIBLE);
		}
		if (isDivider) {
			input_Ll.setBackgroundResource(R.drawable.people_normalbox);
			input_Ll.setVisibility(View.VISIBLE);
			
		}
		if (isPull) {
			down_arrow_iv.setVisibility(View.VISIBLE);
			myedittext_Addmore_Rl.setVisibility(View.VISIBLE);
		}
		if (isCustom) {
			myEditTextView_custom.setVisibility(View.VISIBLE);
			myEditTextView_Tv.setVisibility(View.GONE);
			input_unline.setVisibility(View.VISIBLE);
		}
		if (isPopupwindow_Text) {
			dropdowntriangle.setVisibility(View.VISIBLE);
			myedittext_Addmore_Rl.setVisibility(View.VISIBLE);
		}
		if (JustInput) {
			myEditTextView_Tv.setVisibility(View.GONE);
		}
		if (isCustom_Text) {
			myEditTextView_custom.setVisibility(View.VISIBLE);
			myEditTextView_custom.setHint("自定义文本");
			custom_Et.setVisibility(View.VISIBLE);
			custom_underline.setVisibility(View.VISIBLE);
			myedittext_Addmore_Rl.setVisibility(View.VISIBLE);
		}
		if (JustLabel) {
			myEditTextView_Tv.setVisibility(View.VISIBLE);
			myEditTextView_custom.setVisibility(View.GONE);
			myEditTextView_Et.setVisibility(View.GONE);
			myedittext_Addmore_Rl.setVisibility(View.VISIBLE);
			myEditTextView_Et.setEnabled(false);
			custom_Et.setVisibility(View.VISIBLE);
			custom_underline.setVisibility(View.VISIBLE);
		}
		myEditTextView_custom.setOnFocusChangeListener(mOnFocusChangeListener);
		custom_Et.setOnFocusChangeListener(mOnFocusChangeListener);
		
	
		this.addView(view, new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

	}
	
	

	public LinearLayout getMyEditTextView_Rootview() {
		return myEditTextView_Rootview;
	}

	public void setMyEditTextView_Rootview(LinearLayout myEditTextView_Rootview) {
		this.myEditTextView_Rootview = myEditTextView_Rootview;
	}

	public  void RotateUp(){
		down_arrow_iv.clearAnimation();
		final RotateAnimation animation =new RotateAnimation(0f,180f,Animation.RELATIVE_TO_SELF, 
				0.5f,Animation.RELATIVE_TO_SELF,0.5f); 
		animation.setDuration(500);//设置动画持续时间 
		animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态 
		down_arrow_iv.setAnimation(animation);
		/** 开始动画 */ 
		animation.startNow(); 
	}
	public  void RotateDown(){
		down_arrow_iv.clearAnimation();
		final RotateAnimation animation =new RotateAnimation(180f,360f,Animation.RELATIVE_TO_SELF, 
				0.5f,Animation.RELATIVE_TO_SELF,0.5f); 
		animation.setDuration(500);//设置动画持续时间 
		animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态 
		down_arrow_iv.setAnimation(animation);
		/** 开始动画 */ 
		animation.startNow(); 
	}
	public boolean isRequired() {
		return isRequired;
	}

	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
		if (isRequired) {
			required_iv.setVisibility(View.VISIBLE);
			input_Ll.setVisibility(View.VISIBLE);
			myedittext_Addmore_Rl.setVisibility(View.VISIBLE);
		}
	}

	public void setHint(String str) {
		custom_Et.setHint(str);
	}

	public String getText() {
		if (isChoose) {
			return myEditText_Tv2.getText().toString().trim();
		} else if (isCustom_Text) {
			return custom_Et.getText().toString().trim();
		} else if (JustLabel) {
			return custom_Et.getText().toString().trim();
		} else {
			return myEditTextView_Et.getText().toString().trim();
		}
	}

	private OnFocusChangeListener mOnFocusChangeListener = new OnFocusChangeListener() {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			EditText textView = (EditText) v;
			String hint;
			if (hasFocus) {
				hint = textView.getHint().toString();
				textView.setTag(hint);
				textView.setHint("");
				textView.addTextChangedListener(new TextWatcher() {

					@Override
					public void afterTextChanged(Editable arg0) {

					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
						// Log.d("start", ""+start);
						// Log.d("count", ""+count);
						// Log.d("after", ""+after);
					}

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
						Log.d("length", s + "#" + s.length());
						if (s.length() == 6) {
							ToastUtil.showToast(mContext, "最多只能输入12个字符");
						}
						// Log.d("before", ""+before);
						// Log.d("count", ""+count);
					}

				});
			} else {
				if (TextUtils.isEmpty(textView.getText().toString())) {
					textView.setHint("自定义");
				}
			}
		}
	};

	public void setText(String txt) {
		if (isChoose) {
			myEditText_Tv2.setText(txt);
		} else if (isCustom_Text) {
			custom_Et.setText(txt);
		} else if (JustLabel) {
			custom_Et.setText(txt);
		} else if (isReadOnly) {
			myEditText_Tv2.setText(txt);
		} else {
			myEditTextView_Et.setText(txt);
		}
	}
	public void NoSingleLine(){
		myEditTextView_Et.setSingleLine(false);
	}
	public EditText getEditText() {
		return myEditTextView_Et;
	}

	private static PopupWindow popupWindow;
	private RelativeLayout myedittext_Addmore_Rl;
	private CheckBox checkbox_cb;
	private TextView hint_Tv2;
	private FrameLayout myEditTextView_Fl;

	public FrameLayout getMyEditTextView_Fl() {
		return myEditTextView_Fl;
	}

	public void setMyEditTextView_Fl(FrameLayout myEditTextView_Fl) {
		this.myEditTextView_Fl = myEditTextView_Fl;
	}

	/**
	 * 动态增加新的条目
	 * 
	 * @param e
	 *            需要动态的控件
	 * @param context
	 *            上下文
	 * @param parent
	 *            控件的父控件
	 * @param text
	 *            显示的标题
	 * @param str
	 *            下拉框的字符串数组
	 * @param list
	 *            arraylist将控件动态增加数组中
	 */
	public void MakeAddMoreMethod(final MyEditTextView e,
			final Context context,int type, final LinearLayout parent,
			final String text, final ArrayList<MyEditTextView> list) {
		e.getMyedittext_Addmore_Rl().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final MyEditTextView editTextView = new MyEditTextView(context);
				if (e.isReadOnly()) {
					editTextView.setDelete(true);
					editTextView.setCustom(true);
				} else {
					editTextView.setTextLabel(text);
					editTextView.setDelete(true);
				}
				editTextView.setCustom(true);
				parent.addView(editTextView,parent.indexOfChild(e)-1);
				editTextView.getAddMore_Iv().setBackgroundResource(
						R.drawable.people_column_delete);
				editTextView.getAddMore_Iv().setOnClickListener(
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								parent.removeView(editTextView);
								list.remove(editTextView);
							}
						});
				list.add(editTextView);
			}
		});

	}
	/**
	 * 显示下拉框
	 * 
	 * @param context
	 *            上下文
	 * @param anchor
	 *            所需要显示下拉框的控件
	 * @param str
	 *            下拉框中所显示的字符串数组
	 */
	public void makePopupWindows(final Context context, final View anchor,
			final String[] str) {
		final MyEditTextView etv = (MyEditTextView) anchor;
		final TextView myEditTextView_Tv = etv.getMyEditTextView_Tv();
		final EditText myEditTextView_Et = etv.getMyEditTextView_Et();
		etv.getMyEditTextView_Fl().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow = new PopupWindow(context);
				popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
				popupWindow.setBackgroundDrawable(new BitmapDrawable());
				popupWindow.setFocusable(true);
				popupWindow.setOutsideTouchable(true);

				final ListView contentView = new ListView(context);
				contentView.setAdapter(new BaseAdapter() {
					@Override
					public View getView(int position, View convertView,
							ViewGroup parent) {
						convertView = View.inflate(context,
								R.layout.people_list_item_popupwindow, null);
						TextView popupwindow_Tv = (TextView) convertView
								.findViewById(R.id.popupwindow_Tv);
						popupwindow_Tv.setText(str[position]);
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
						return str.length;
					}
				});
				contentView.setDivider(null);
				contentView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						if (position == contentView.getLastVisiblePosition()) {
							etv.setCustom(true);
							etv.setLine(true);
						}
						if (popupWindow.isShowing()) {
							popupWindow.dismiss();
						}
						String text = str[position];
						System.out.println(text);
						if (!etv.isPopupwindow_Text()) {
							myEditTextView_Et.setText(text);
						} else {
							myEditTextView_Tv.setText(text);
						}
					}
				});
				if (!etv.isPopupwindow_Text()) {
					popupWindow.setWidth(anchor.getWidth());
				} else {
					popupWindow.setWidth(myEditTextView_Tv.getWidth());
				}
				popupWindow.setContentView(contentView);
				popupWindow.showAsDropDown(etv, 0, 0);
			}
		});

	}

	public void setInputType(int typeClassNumber) {
		// TODO Auto-generated method stub

	}
	
	
}
