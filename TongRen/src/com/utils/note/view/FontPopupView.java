package com.utils.note.view;

import android.content.Context;
import android.text.Layout;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.tr.R;
import com.utils.note.rteditor.RTManager;
import com.utils.note.rteditor.effects.Effects;

/**
 * Created by zhangchangwei on 2015/8/13.
 */
public class FontPopupView {
	private Context _context;
	private RTManager _rtManager;
	private View _view;

	public FontPopupView(Context context, RTManager rtManager) {
		_context = context;
		_rtManager = rtManager;
		getView();
	}

	public View getView() {
		if (_view == null) {
			_view = View.inflate(_context, R.layout.rte_pop_toolbar_font, null);
			CheckBox font_bold = (CheckBox) _view.findViewById(R.id.font_blod);
			CheckBox font_udl = (CheckBox) _view.findViewById(R.id.font_udl);
			CheckBox font_strok = (CheckBox) _view
					.findViewById(R.id.font_strok);
			RadioGroup rg_font = (RadioGroup) _view.findViewById(R.id.rg_font);

			rg_font.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					if (checkedId == R.id.rb_font_left) {
						_rtManager.setJustify(false);
						_rtManager.onEffectSelected(Effects.ALIGNMENT,
								Layout.Alignment.ALIGN_NORMAL);
					} else if (checkedId == R.id.rb_font_center) {
						_rtManager.setJustify(false);
						_rtManager.onEffectSelected(Effects.ALIGNMENT,
								Layout.Alignment.ALIGN_CENTER);
					} else if (checkedId == R.id.rb_font_right) {
						_rtManager.setJustify(false);
						_rtManager.onEffectSelected(Effects.ALIGNMENT,
								Layout.Alignment.ALIGN_OPPOSITE);
					} else if (checkedId == R.id.rb_font_full) {
//						_rtManager.onEffectSelected(Effects.ALIGNMENT,
//								Layout.Alignment.ALIGN_NORMAL);
//						_rtManager.setJustify(true);
						_rtManager.setJustify(false);
						_rtManager.onEffectSelected(Effects.ALIGNMENT,
								Layout.Alignment.ALIGN_CENTER);
					}
				}
			});
			MyOnCheckedChangeListener listener = new MyOnCheckedChangeListener();
			font_bold.setOnCheckedChangeListener(listener);
			font_udl.setOnCheckedChangeListener(listener);
			font_strok.setOnCheckedChangeListener(listener);
		}
		return _view;
	}

	class MyOnCheckedChangeListener implements
			CompoundButton.OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			int id = buttonView.getId();
			if (id == R.id.font_blod) {
				_rtManager.onEffectSelected(Effects.BOLD, isChecked);
			} else if (id == R.id.font_udl) {
				_rtManager.onEffectSelected(Effects.UNDERLINE, isChecked);
			} else if (id == R.id.font_strok) {
				_rtManager.onEffectSelected(Effects.STRIKETHROUGH, isChecked);
			}
		}

	}
}
