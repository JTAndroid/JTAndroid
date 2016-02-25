package com.utils.note.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.utils.note.rteditor.utils.Helper;


public class SineWave extends SurfaceView implements Callback{
	private final Handler mHandler = new Handler();
	private Paint mPaint = null;

	private static float amplifier = 15.0f;

	private static float frequency = 1.5f; // 2Hz

	private static float phase = 0f; // 相位

	private int height = 0;

	private int width = 0;

	private final Runnable mDrawCube = new Runnable() {
        public void run() {
            drawFrame();
        }
    };
    
    void drawFrame() {
        final SurfaceHolder holder = getHolder();
        Canvas c = null;
        try {
			c = holder.lockCanvas();
			if (c != null) {
				// draw something
                drawCube(c);
            }
        } finally {
            if (c != null) holder.unlockCanvasAndPost(c);
        }
        mHandler.removeCallbacks(mDrawCube);
        mHandler.postDelayed(mDrawCube, 1000 / 200);
    }

	public SineWave(Context context) {

		super(context);
		mPaint = new Paint();
		getHolder().addCallback(this);
	}

	// 如果不写下面的构造函数，则会报错：custom view SineWave is not using the 2- or 3-argument
	// View constructors

	public SineWave(Context context, AttributeSet attrs) {

		super(context, attrs);
		mPaint = new Paint();
		getHolder().addCallback(this);
	}

	public SineWave(Context context, float amplifier, float frequency,
					 float phase) {

		super(context);
		this.frequency = frequency;
		this.amplifier = amplifier;
		this.phase = phase;
		mPaint = new Paint();
		getHolder().addCallback(this);
	}

	public float GetAmplifier() {
		return amplifier;
	}

	public float GetFrequency() {
		return frequency;
	}

	public float GetPhase() {
		return phase;
	}

	public void Set(float amplifier, float frequency, float phase) {

//		this.frequency = frequency;

		this.amplifier = amplifier;
		if (phase != 0) {
			this.phase = phase;
		}

	}
	
	public void drawCube(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		mPaint.setAntiAlias(false);
		mPaint.setColor(Color.WHITE);
		amplifier = (amplifier * 2 > height) ? (height / 2) : amplifier;
		mPaint.setAlpha(200);
		mPaint.setDither(true);
		mPaint.setStrokeWidth(Helper.dip2px(3));
		float cy = height / 2;

		for (int i = 0; i < width - 1; i++)
		{
			Paint prePaint = new Paint(mPaint);
			prePaint.setStrokeWidth(Helper.dip2px(1));
			prePaint.setColor(Color.GRAY);
			if(amplifier > 10){
//				float prePhase = phase - 15;
				float preAmplifier = amplifier * 2/3;
				canvas.drawLine((float) i,cy- preAmplifier* (float) (Math.sin(phase * 2 * (float) Math.PI/ 360.0f + 2 * Math.PI * frequency * i/ width)),
						(float) (i + 1),cy- preAmplifier* (float) (Math.sin(phase * 2 * (float) Math.PI/ 360.0f + 2 * Math.PI * frequency* (i + 1) / width)), prePaint);
//				float pprePhase = phase - 30;
				float ppreAmplifier = amplifier * 1/3;
				canvas.drawLine((float) i,cy- ppreAmplifier* (float) (Math.sin(phase * 2 * (float) Math.PI/ 360.0f + 2 * Math.PI * frequency * i/ width)),
						(float) (i + 1),cy- ppreAmplifier* (float) (Math.sin(phase * 2 * (float) Math.PI/ 360.0f + 2 * Math.PI * frequency* (i + 1) / width)), prePaint);
				canvas.drawLine((float) i,cy,
						(float) (i + 1),cy, prePaint);
			}
			canvas.drawLine((float) i,cy- amplifier* (float) (Math.sin(phase * 2 * (float) Math.PI/ 360.0f + 2 * Math.PI * frequency * i/ width)),
					(float) (i + 1),cy- amplifier* (float) (Math.sin(phase * 2 * (float) Math.PI/ 360.0f + 2 * Math.PI * frequency* (i + 1) / width)), mPaint);
			
		}
		phase += 20;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		drawFrame();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		this.height = height;
		this.width = width;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
	}



}
