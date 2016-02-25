package com.tr.ui.widgets;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tr.R;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;




/**
 * A class for annotating a CharSequence with spans to convert textual emoticons
 * to graphical ones.
 */
public class SmileyParser2 {

    private static SmileyParser2 mInstance;
    private final Context mContext;
    private final String[] mSmileyTexts;
    private final Pattern mPattern;
    private final HashMap<String, Integer> mSmileyToRes;
    
    public static final int[] mIconIds = {
        R.drawable.f_static_020, R.drawable.f_static_021, R.drawable.f_static_022, R.drawable.f_static_023, R.drawable.f_static_024, 
        R.drawable.f_static_025, R.drawable.f_static_026, R.drawable.f_static_027, R.drawable.f_static_028, R.drawable.f_static_029, 
        R.drawable.f_static_030, R.drawable.f_static_031, R.drawable.f_static_032, R.drawable.f_static_033, R.drawable.f_static_034, 
        R.drawable.f_static_035, R.drawable.f_static_036, R.drawable.f_static_037, R.drawable.f_static_038, R.drawable.f_static_039, 
    };
    


    public static SmileyParser2 getInstance(Context context) {
    	if (mInstance == null) {
    		mInstance = new SmileyParser2(context);
    	}
        return mInstance;
    }
    

    private SmileyParser2(Context context) {
        mContext = context;
        mSmileyTexts = mContext.getResources().getStringArray(R.array.default_face_texts2);
        mSmileyToRes = buildSmileyToRes();
        mPattern = buildPattern();
    }
    

    /**
     * Builds the hashtable we use for mapping the string version
     * of a smiley (e.g. ":-)") to a resource ID for the icon version.
     */
    private HashMap<String, Integer> buildSmileyToRes() {
        final int length = mSmileyTexts.length;
        if (mIconIds.length != length) {
            // Throw an exception if someone updated DEFAULT_SMILEY_RES_IDS

            // and failed to update arrays.xml

            throw new IllegalStateException("Smiley resource ID/text mismatch");
        }

        HashMap<String, Integer> smileyToRes = new HashMap<String, Integer>(length);
        for (int i = 0; i < length; i++) {
            smileyToRes.put(mSmileyTexts[i], mIconIds[i]);
        }

        return smileyToRes;
    }

    
    /**
     * Builds the regular expression we use to find smileys in {@link #addSmileySpans}.
     */
    //构建正则表达式
    private Pattern buildPattern() {
        // Set the StringBuilder capacity with the assumption that the average

        // smiley is 3 characters long.

        StringBuilder patternString = new StringBuilder(mSmileyTexts.length * 3);

        // Build a regex that looks like (:-)|:-(|...), but escaping the smilies

        // properly so they will be interpreted literally by the regex matcher.

        patternString.append('(');
        for (String s : mSmileyTexts) {
            patternString.append(Pattern.quote(s));
            patternString.append('|');
        }
        // Replace the extra '|' with a ')'

        patternString.replace(patternString.length() - 1, patternString.length(), ")");

        return Pattern.compile(patternString.toString());
    }


    /**
     * Adds ImageSpans to a CharSequence that replace textual emoticons such
     * as :-) with a graphical version.
     *
     * @param text A CharSequence possibly containing emoticons
     * @return A CharSequence annotated with ImageSpans covering any
     * recognized emoticons.
     */
    //根据文本替换成图片
    public CharSequence addSmileySpans(CharSequence text) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        Matcher matcher = mPattern.matcher(text);
        
        while (matcher.find()) {
            int resId = mSmileyToRes.get(matcher.group());
            builder.setSpan(new ImageSpan(mContext, resId),
                            matcher.start(), matcher.end(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }
	public String[] getmSmileyTexts() {
		return mSmileyTexts;
	}
    
}



