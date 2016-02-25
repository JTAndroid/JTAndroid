package com.tr.ui.widgets;



import com.tr.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * 圆角图片处理
 * @author Tony
 *
 */
public class RoundedCornersImage extends MaskedImage {
	private int cornerRadius = (int)getResources().getDimension(R.dimen.image_corner_radius);

	public RoundedCornersImage(Context paramContext) {
		super(paramContext);
	}

	public RoundedCornersImage(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
	}
	
	public void setCornerRadius(int cornerRadius) {
	    this.cornerRadius = cornerRadius;
	}

    @Override
    public Bitmap createMask()
    {
        //　创建一个圆角的Bitmap
        final int width = getWidth();
        final int height = getHeight();
        final Bitmap.Config localConfig = Bitmap.Config.ARGB_8888;
        final Bitmap localBitmap = Bitmap.createBitmap(width, height, localConfig);
        final Canvas localCanvas = new Canvas(localBitmap);
        final Paint localPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        localPaint.setColor(Color.BLACK);
        final  RectF localRectF = new RectF(0.0F, 0.0F, width, height);
        final float rx = this.cornerRadius;
        final float ry = this.cornerRadius;
        localCanvas.drawRoundRect(localRectF, rx, ry, localPaint);
        return localBitmap;
    }
}