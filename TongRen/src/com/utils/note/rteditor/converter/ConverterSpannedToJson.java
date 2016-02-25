package com.utils.note.rteditor.converter;

import android.text.Spanned;
import android.text.style.CharacterStyle;
import android.text.style.StrikethroughSpan;
import android.text.style.UnderlineSpan;

import com.utils.note.rteditor.api.format.RTFormat;
import com.utils.note.rteditor.api.format.RTHtml;
import com.utils.note.rteditor.api.media.RTAudio;
import com.utils.note.rteditor.api.media.RTImage;
import com.utils.note.rteditor.api.media.RTVideo;
import com.utils.note.rteditor.spans.AudioSpan;
import com.utils.note.rteditor.spans.BoldSpan;
import com.utils.note.rteditor.spans.ImageSpan;

/**
 * Created by zhangchangwei on 2015/8/18.
 */
public class ConverterSpannedToJson extends ConverterSpannedToHtml{
    public RTHtml<RTImage, RTAudio, RTVideo> convert(final Spanned text, RTFormat.Html rtFormat) {
        setIsJson(true);
        return super.convert(text,rtFormat);
    }

    @Override
    public boolean handleJsonText(CharacterStyle style,int start,int length) {
        if (style instanceof BoldSpan) {
            mOut.append("<b start = \"" + start + "\" length = \"" + length + "\">");
        }else if (style instanceof UnderlineSpan) {
            mOut.append("<u start = \"" + start + "\" length = \"" + length + "\">");
        }else if (style instanceof StrikethroughSpan) {
            mOut.append("<strike start = \"" + start + "\" length = \"" + length + "\">");
        }else if (style instanceof ImageSpan) {
            ImageSpan span = ((ImageSpan) style);
            RTImage image = span.getImage();
            mImages.add(image);
            String filePath = image.getFilePath(mRTFormat);
            mOut.append("<img src=\"" + filePath + "\" start = \"" + start + "\"/>");
            return false;    // don't output the dummy character underlying the image.
        } else if (style instanceof AudioSpan) {
            AudioSpan span = ((AudioSpan) style);
            RTAudio audio = span.getAudio();
            String filePath = audio.getFilePath(mRTFormat);
            mOut.append("<embed src=\"" + span.getAudio().getAudioPath() + "\" start = \"" + start + "\"/>");
            return false;    // don't output the dummy character underlying the audio file.
        }
        return true;
    }
}
