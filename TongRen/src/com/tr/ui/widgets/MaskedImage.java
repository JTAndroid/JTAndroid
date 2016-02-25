package com.tr.ui.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 处理特效图片边框
 * @author Tony
 *
 */
public abstract class MaskedImage extends ImageView {
	private static final Xfermode MASK_XFERMODE;
	private Bitmap mask;
	private Paint paint;

	static {
		final PorterDuff.Mode localMode = PorterDuff.Mode.DST_IN;
		MASK_XFERMODE = new PorterDuffXfermode(localMode);
	}

	public MaskedImage(Context paramContext) {
		super(paramContext);
	}

	public MaskedImage(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
	}

	public MaskedImage(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
	}

	public abstract Bitmap createMask();

	protected void onDraw(Canvas canvas) {
		final Drawable drawable = getDrawable();
		if (drawable == null) {
			return;
		}
		
		try {
		    
			if (paint == null) {
				final Paint localPaint = new Paint();
				paint = localPaint;
				paint.setFilterBitmap(false);
				final Xfermode localXfermode1 = MASK_XFERMODE;
				paint.setXfermode(localXfermode1);
			}
			
			final int width = getWidth();
			final int height = getHeight();
			final int saveCount = canvas.saveLayer(0.0F, 0.0F, width, height, null, 31);
			drawable.setBounds(0, 0, width, height);
			drawable.draw(canvas);
			
			if ((mask == null) || (mask.isRecycled())) {
				final Bitmap bitmap = createMask();
				mask = bitmap;
			}
			
			final Bitmap bitmap = mask;
			final Paint localPaint = paint;
			canvas.drawBitmap(bitmap, 0.0F, 0.0F, localPaint);
			canvas.restoreToCount(saveCount);
			return;
		} catch (Exception e) {
		    e.printStackTrace();
		} catch (OutOfMemoryError e) {
			if (mask != null && !mask.isRecycled()) {
				setImageDrawable(null);
				mask.recycle();
			}
		}
	}
}
