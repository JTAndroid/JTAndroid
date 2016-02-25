package com.utils.note.rteditor.spans;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.style.DynamicDrawableSpan;
import android.util.Log;
import android.widget.Toast;

import com.utils.note.rteditor.utils.Helper;

/**
 * Created by zhangchangwei on 2015/8/12.
 */
public class WidthImageSpan extends DynamicDrawableSpan {

    private Drawable mDrawable;
    private Uri mContentUri;
    private int mResourceId;
    private Context mContext;
    private String mSource;
    private String mPath;

    public WidthImageSpan(Context context, Uri uri,String path) {
        this(context, uri, ALIGN_BOTTOM,path);
    }

    /**
     * @param verticalAlignment one of {@link DynamicDrawableSpan#ALIGN_BOTTOM} or
     * {@link DynamicDrawableSpan#ALIGN_BASELINE}.
     */
    public WidthImageSpan(Context context, Uri uri, int verticalAlignment,String path) {
        super(verticalAlignment);
        mContext = context;
        mContentUri = uri;
        mSource = uri.toString();
        mPath = path;
    }

    @Override
    public Drawable getDrawable() {
        Drawable drawable = null;
        Bitmap bitmap = null;
        try {
            InputStream is = mContext.getContentResolver().openInputStream(
                    mContentUri);
            bitmap = BitmapFactory.decodeStream(is);
            if(bitmap != null){
            	int[] screenSize = Helper.getScreenSize();
            	int width = screenSize[0] - Helper.dip2px(10);
            	drawable = new BitmapDrawable(mContext.getResources(), bitmap);
            	float scale = (float)(width) / drawable.getIntrinsicWidth();
            	drawable.setBounds(0, 0, width,
            			(int) (drawable.getIntrinsicHeight() * scale));
            }
            is.close();
        } catch (Exception e) {
            Log.e("sms", "Failed to loaded content " + mContentUri, e);
            Toast.makeText(mContext, "插入图片失败", Toast.LENGTH_SHORT).show();
        }
        return drawable;
    }

    /**
     * Returns the source string that was saved during construction.
     */
    public String getSource() {
        return mSource;
    }
}
