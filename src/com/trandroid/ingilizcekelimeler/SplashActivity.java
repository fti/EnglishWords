package com.trandroid.ingilizcekelimeler;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends Activity {
    
	MyCountDowner mycounter;
	ImageView rotatingImage;
	Animation hyperspaceJump;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        
        Typeface font = Typeface.createFromAsset(getAssets(), "rock.ttf");
        TextView copyright = (TextView) findViewById(R.id.copyrightText);
        copyright.setTypeface(font);
        
        rotatingImage = (ImageView) findViewById(R.id.splash_logo);
        hyperspaceJump = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.rotate);
        rotatingImage.startAnimation(hyperspaceJump);
        mycounter = new MyCountDowner(4000, 4000);
        mycounter.start();
    }
      
    
    class MyCountDowner extends CountDownTimer{
    	boolean isopen = false;
		public MyCountDowner(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onFinish() {
			this.cancel();
			SplashActivity.this.finish();
            Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);  
		}
		
		@Override
		public void onTick(long millisUntilFinished) {
			
		}
    	
    }
    /**
     * Processes splash screen touch events
     */
    @Override
    public boolean onTouchEvent(MotionEvent evt)
    {
        if(evt.getAction() == MotionEvent.ACTION_DOWN)
        {	
                mycounter.onFinish();
        }
        return true;
    }    
} 