package com.tr.ui.user.modified;

import java.io.FileDescriptor;
import java.io.IOException;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.nineoldandroids.animation.ObjectAnimator;
import com.tr.R;
import com.tr.model.im.MNotifyMessageBox;
import com.tr.model.obj.JTFile;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.conference.common.LocalPlayer;
import com.utils.common.EConsts;

public class LoadingGuideActivity extends JBaseActivity implements OnClickListener {
	private int[] pagers = { R.drawable.first_splash_pager, R.drawable.second_splash_pager, R.drawable.third_splash_pager };
//	private int[] pagers = { R.drawable.first_splash_pager_old, R.drawable.second_splash_pager_old, R.drawable.third_splash_pager_old };
	private String friendId;
	private boolean mBlnFromNotifyBox;
	private int pushMessageType; // 推送的消息类型
	private JTFile mShareInfo;

	private ImageView imageView_pic1;
	private ImageView imageView_pic2;
	private ImageView imageView_pic3;
	private ImageView splash_button;
	private ImageView splash_music_play;

	private Animation mFadeInScale1;
	private Animation mFadeInScale2;
	private Animation mFadeInScale3;
	private Animation mFadeIn;

	private LocalPlayer lcLocalPlayer;
	private ObjectAnimator musicPicAnim;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("xmx", "LoadingGuideActivity onCreate");
		SharedPreferences sharedPreferences = getSharedPreferences(EConsts.share_firstSplash, MODE_PRIVATE);
		friendId = getIntent().getStringExtra(ENavConsts.EFriendId);
		boolean flag = sharedPreferences.getBoolean("first_entry", true);
		if (!flag) {
			if ("null".equals(getIntent().getStringExtra("mJTMember"))) {
				mBlnFromNotifyBox = getIntent().hasExtra(ENavConsts.ENotifyParam);
				if (mBlnFromNotifyBox) {
					pushMessageType = getIntent().getIntExtra(ENavConsts.EPushMessageType, 0);
				}
				mShareInfo = (JTFile) getIntent().getSerializableExtra(ENavConsts.EShareParam);
				boolean mFromShare = getIntent().getBooleanExtra("mFromShare", false);
				Intent intent = new Intent(LoadingGuideActivity.this, LoginActivity.class);

				if (mBlnFromNotifyBox) { // 消息盒子
					intent.putExtra("message", "info");
					intent.putExtra(ENavConsts.ENotifyParam, MNotifyMessageBox.getInstance());
					intent.putExtra(ENavConsts.EPushMessageType, pushMessageType); // 消息推送类型
				} else if (mFromShare) {
					intent.putExtra(ENavConsts.EShareParam, mShareInfo);
				}
				intent.putExtra(ENavConsts.EFriendId, friendId);
				startActivity(intent);
				finish();
			} else {
				ENavigate.startLoginActivity(LoadingGuideActivity.this, friendId);
				finish();
			}
		} else {
//			setContentView(R.layout.activity_loading_guide);
//			pager = (ViewPager) findViewById(R.id.splashPager);
//			pager.setAdapter(new SplashPagerAdapter());
			setContentView(R.layout.activity_loading_guide_new);
			initView();
			initAnim();
			setAnimListener();
			setButtonClickListener();
			initMusic();
			imageView_pic1.startAnimation(mFadeInScale1);
			Editor editor = sharedPreferences.edit();
			editor.putBoolean("first_entry", false);
			editor.commit();
		}
	}

//	private ViewPager pager;
	 
//	private class SplashPagerAdapter extends PagerAdapter {
//		@Override
//		public int getCount() {
//			return pagers.length;
//		}
//	
//		@Override
//		public boolean isViewFromObject(View arg0, Object arg1) {
//			return arg0 == arg1;
//		}
//	
//		@Override
//		public Object instantiateItem(ViewGroup container, int position) {
//			View view = View.inflate(LoadingGuideActivity.this, R.layout.activity_splash_viewpagers, null);
//			ImageView img = (ImageView) view.findViewById(R.id.splash_viewpager);
//			img.setBackgroundResource(pagers[position]);
//			Button button = (Button) view.findViewById(R.id.splash_start_app);
//			if (position == 2) {
//				button.setVisibility(View.VISIBLE);
//				button.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						ENavigate.startLoginActivity(LoadingGuideActivity.this, friendId);
//					}
//				});
//			}
//			container.addView(view);
//			return view;
//		}
//	
//		@Override
//		public void destroyItem(ViewGroup container, int position, Object object) {
//			container.removeView((View) object);
//		}
//	}
	
	private void initMusic() {
		AssetManager assets = getAssets();
//		FileDescriptor fileDescriptor = null;
		AssetFileDescriptor openFd = null;
		try {
			openFd = assets.openFd("splash.mp3");
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (openFd!=null) {
			lcLocalPlayer = new LocalPlayer();
			lcLocalPlayer.startPlay(openFd, new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					lcLocalPlayer.start();
				}
			});
		}
		if (lcLocalPlayer != null) {
			lcLocalPlayer.start();
			changeMusicButtonStatus(lcLocalPlayer.isPlay());
		}
	}

	private void changeMusicButtonStatus(boolean isplay) {
		if (isplay) {
			musicPicAnim.start();
			splash_music_play.setBackgroundResource(R.drawable.splash_music_play);
		} else {
			musicPicAnim.cancel();
			splash_music_play.setBackgroundResource(R.drawable.splash_music_puse);
		}
	}

	private void initView() {
		imageView_pic1 = (ImageView) findViewById(R.id.imageView_pic1);
		imageView_pic2 = (ImageView) findViewById(R.id.imageView_pic2);
		imageView_pic3 = (ImageView) findViewById(R.id.imageView_pic3);
		splash_music_play = (ImageView) findViewById(R.id.splash_music_play);
		splash_button = (ImageView) findViewById(R.id.splash_button);
	}

	private void initAnim() {
		
		mFadeIn = AnimationUtils.loadAnimation(this,R.anim.welcome_fade_in);
		mFadeIn.setDuration(5000);
		
		mFadeInScale1 = AnimationUtils.loadAnimation(this, R.anim.welcome_fade_in_scale);
		mFadeInScale1.setDuration(5000);
		mFadeInScale2 = AnimationUtils.loadAnimation(this, R.anim.welcome_fade_in_scale);
		mFadeInScale2.setDuration(5000);
		mFadeInScale3 = AnimationUtils.loadAnimation(this, R.anim.welcome_fade_in_scale);
		mFadeInScale3.setDuration(5000);
		
		musicPicAnim = ObjectAnimator.ofFloat(splash_music_play, "rotation", 0F, 359F).setDuration(5000);
		musicPicAnim.setRepeatCount(-1);
		musicPicAnim.setRepeatMode(ObjectAnimator.RESTART);
	}

	private void setAnimListener() {
		
		
		mFadeInScale1.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {

			}

			public void onAnimationRepeat(Animation animation) {

			}

			public void onAnimationEnd(Animation animation) {
				if (imageView_pic1.getVisibility() == View.VISIBLE && imageView_pic2.getVisibility() == View.VISIBLE && imageView_pic3.getVisibility() == View.VISIBLE) {
					imageView_pic1.setVisibility(View.GONE);
					imageView_pic1.clearAnimation();
					imageView_pic2.startAnimation(mFadeInScale2);
				}
			}
		});
		mFadeInScale2.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {
				imageView_pic1.clearAnimation();
			}

			public void onAnimationRepeat(Animation animation) {

			}

			public void onAnimationEnd(Animation animation) {
				if (imageView_pic1.getVisibility() == View.GONE && imageView_pic2.getVisibility() == View.VISIBLE && imageView_pic3.getVisibility() == View.VISIBLE) {
					imageView_pic2.setVisibility(View.GONE);
					imageView_pic3.startAnimation(mFadeInScale3);
				}
			}
		});
		mFadeInScale3.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {
				imageView_pic1.startAnimation(mFadeIn);
			}

			public void onAnimationRepeat(Animation animation) {

			}

			public void onAnimationEnd(Animation animation) {
				if (imageView_pic1.getVisibility() == View.GONE && imageView_pic2.getVisibility() == View.GONE && imageView_pic3.getVisibility() == View.VISIBLE) {
					imageView_pic1.setVisibility(View.VISIBLE);
					imageView_pic2.setVisibility(View.VISIBLE);
					imageView_pic1.startAnimation(mFadeInScale1);
				}

			}
		});
		
	}

	private void setButtonClickListener() {
		splash_button.setOnClickListener(this);
		splash_music_play.setOnClickListener(this);
	}

	@Override
	public void initJabActionBar() {
		jabGetActionBar().hide(); // 隐藏ActionBar
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.splash_button:
			mFadeInScale1.cancel();
			mFadeInScale2.cancel();
			mFadeInScale3.cancel();
			if (lcLocalPlayer!=null) {
				lcLocalPlayer.stopPlay();
				changeMusicButtonStatus(lcLocalPlayer.isPlay());
			}
			ENavigate.startLoginActivity(LoadingGuideActivity.this, friendId);
			finish();
			break;
		case R.id.splash_music_play:
			if (lcLocalPlayer != null) {
				if (lcLocalPlayer.isPlay()) {
					lcLocalPlayer.pause();
					changeMusicButtonStatus(lcLocalPlayer.isPlay());
				} else {
					lcLocalPlayer.start();
					changeMusicButtonStatus(lcLocalPlayer.isPlay());
				}
			}
			break;
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (lcLocalPlayer!=null) {
			lcLocalPlayer.pause();
			changeMusicButtonStatus(lcLocalPlayer.isPlay());
		}
	}
	
}
