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
public class SmileyParser {

    private static SmileyParser mInstance;
    private final Context mContext;
    private final String[] mSmileyTexts;
    private final Pattern mPattern;
    private final HashMap<String, Integer> mSmileyToRes;
    
    public static final int[] mIconIds = {
        R.drawable.f_static_000, R.drawable.f_static_001, R.drawable.f_static_002, R.drawable.f_static_003, R.drawable.f_static_004, 
        R.drawable.f_static_005, R.drawable.f_static_006, R.drawable.f_static_007, R.drawable.f_static_008, R.drawable.f_static_009, 
        R.drawable.f_static_010, R.drawable.f_static_011, R.drawable.f_static_012, R.drawable.f_static_013, R.drawable.f_static_014, 
        R.drawable.f_static_015, R.drawable.f_static_016, R.drawable.f_static_017, R.drawable.f_static_018, R.drawable.f_static_019, 
    };
    
    public static final int[] mEnhancedIconIds = {
    	
        R.drawable.f_static_000, R.drawable.f_static_001, R.drawable.f_static_002, R.drawable.f_static_003, R.drawable.f_static_004, 
        R.drawable.f_static_005, R.drawable.f_static_006, R.drawable.f_static_007, R.drawable.f_static_008, R.drawable.f_static_009, 
        R.drawable.f_static_010, R.drawable.f_static_011, R.drawable.f_static_012, R.drawable.f_static_013, R.drawable.f_static_014, 
        R.drawable.f_static_015, R.drawable.f_static_016, R.drawable.f_static_017, R.drawable.f_static_018, R.drawable.f_static_019,
        R.drawable.f_static_020, R.drawable.f_static_021, R.drawable.f_static_022, R.drawable.f_static_023, R.drawable.f_static_024, 
        R.drawable.f_static_025, R.drawable.f_static_026, R.drawable.f_static_027, R.drawable.f_static_028, R.drawable.f_static_029, 
        R.drawable.f_static_030, R.drawable.f_static_031, R.drawable.f_static_032, R.drawable.f_static_033, R.drawable.f_static_034, 
        R.drawable.f_static_035, R.drawable.f_static_036, R.drawable.f_static_037, R.drawable.f_static_038, R.drawable.f_static_039,
        
        R.drawable.f_static_040, R.drawable.f_static_041, R.drawable.f_static_042, R.drawable.f_static_043, R.drawable.f_static_044, 
        R.drawable.f_static_045, R.drawable.f_static_046, R.drawable.f_static_047, R.drawable.f_static_048, R.drawable.f_static_049, 
        R.drawable.f_static_050, R.drawable.f_static_051, R.drawable.f_static_052, R.drawable.f_static_053, R.drawable.f_static_054, 
        R.drawable.f_static_055, R.drawable.f_static_056, R.drawable.f_static_057, R.drawable.f_static_058, R.drawable.f_static_059,
        R.drawable.f_static_060, R.drawable.f_static_061, R.drawable.f_static_062, R.drawable.f_static_063, R.drawable.f_static_064, 
        R.drawable.f_static_065, R.drawable.f_static_066, R.drawable.f_static_067, R.drawable.f_static_068, R.drawable.f_static_069, 
        R.drawable.f_static_070, R.drawable.f_static_071, R.drawable.f_static_072, R.drawable.f_static_073, R.drawable.f_static_074, 
        R.drawable.f_static_075, R.drawable.f_static_076, R.drawable.f_static_077, R.drawable.f_static_078, R.drawable.f_static_079,
        
        R.drawable.f_static_080, R.drawable.f_static_081, R.drawable.f_static_082, R.drawable.f_static_083, R.drawable.f_static_084, 
        R.drawable.f_static_085, R.drawable.f_static_086, R.drawable.f_static_087, R.drawable.f_static_088, R.drawable.f_static_089, 
        R.drawable.f_static_090, R.drawable.f_static_091, R.drawable.f_static_092, R.drawable.f_static_093, R.drawable.f_static_094, 
        R.drawable.f_static_095, R.drawable.f_static_096, R.drawable.f_static_097, R.drawable.f_static_098, R.drawable.f_static_099,
        R.drawable.f_static_100, R.drawable.f_static_101, R.drawable.f_static_102, R.drawable.f_static_103, R.drawable.f_static_104, 
        R.drawable.f_static_105, R.drawable.f_static_106, R.drawable.f_static_107, R.drawable.f_static_108, R.drawable.f_static_109, 
        R.drawable.f_static_110, R.drawable.f_static_111, R.drawable.f_static_112, R.drawable.f_static_113, R.drawable.f_static_114, 
        R.drawable.f_static_115, R.drawable.f_static_116, R.drawable.f_static_117, R.drawable.f_static_118, R.drawable.f_static_119,
        
        R.drawable.f_static_120, R.drawable.f_static_121, R.drawable.f_static_122, R.drawable.f_static_123, R.drawable.f_static_124, 
        R.drawable.f_static_125, R.drawable.f_static_126, R.drawable.f_static_127, R.drawable.f_static_128, R.drawable.f_static_129, 
        R.drawable.f_static_130, R.drawable.f_static_131, R.drawable.f_static_132, R.drawable.f_static_133, R.drawable.f_static_134, 
        R.drawable.f_static_135, R.drawable.f_static_136, R.drawable.f_static_137, R.drawable.f_static_138, R.drawable.f_static_139,
        R.drawable.f_static_140, R.drawable.f_static_141, R.drawable.f_static_142
    };

    public static SmileyParser getInstance(Context context) {
    	if (mInstance == null) {
    		mInstance = new SmileyParser(context);
    	}
        return mInstance;
    }
    

    private SmileyParser(Context context) {
        mContext = context;
        mSmileyTexts = mContext.getResources().getStringArray(R.array.face_texts);
        mSmileyToRes = buildSmileyToRes();
        mPattern = buildPattern();
    }
    

    /**
     * Builds the hashtable we use for mapping the string version
     * of a smiley (e.g. ":-)") to a resource ID for the icon version.
     */
    private HashMap<String, Integer> buildSmileyToRes() {
        final int length = mSmileyTexts.length;
        if (mEnhancedIconIds.length != length) {
            // Throw an exception if someone updated DEFAULT_SMILEY_RES_IDS

            // and failed to update arrays.xml

            throw new IllegalStateException("Smiley resource ID/text mismatch");
        }

        HashMap<String, Integer> smileyToRes = new HashMap<String, Integer>(length);
        for (int i = 0; i < length; i++) {
            smileyToRes.put(mSmileyTexts[i], mEnhancedIconIds[i]);
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
    	String smile = text.toString();
    	smile = smile.replace("\\[", "[");//web端表情
    	smile = smile.replace("\\]", "]");
        SpannableStringBuilder builder = new SpannableStringBuilder(smile);
        Matcher matcher = mPattern.matcher(smile);
        
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



