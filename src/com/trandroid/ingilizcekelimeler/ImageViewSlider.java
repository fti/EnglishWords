package com.trandroid.ingilizcekelimeler;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class ImageViewSlider extends ImageView implements SliderInterface {
	  private int speed=500;
	  
	  public ImageViewSlider(final Context ctxt, AttributeSet attrs) {
	    super(ctxt, attrs);
	  }
	  
	  public void toggle(int toWhere, boolean isOpen, int speed) {
		  this.speed = speed;
		  toggle(toWhere, isOpen);
		  this.speed = 500;
	  }
	  public void toggle(int toWhere, boolean isOpen) {
	    TranslateAnimation anim=null;
	    
	    switch (toWhere) {
		case toLeft:
			anim=new TranslateAnimation(isOpen?0.0f:-1*getRootView().getWidth(), isOpen?-1*getRootView().getWidth():0.0f, 0.0f, 0.0f);
			break;
		case toRight:
			anim=new TranslateAnimation(isOpen?0.0f:getRootView().getWidth(), isOpen?getRootView().getWidth():0.0f, 0.0f, 0.0f);
			
			break;
		case toUp:
			anim=new TranslateAnimation(0.0f, 0.0f,isOpen?0.0f:-1*getHeight(), isOpen?-1*getHeight():0.0f);
			break;
		case toDown:
			anim=new TranslateAnimation(0.0f, 0.0f,isOpen?0.0f:getHeight(), isOpen?getHeight():0.0f);
			
			break;

		default:
			break;
		}
	    if (!isOpen) {
	        setVisibility(View.VISIBLE);
	    }else{
	    	anim.setAnimationListener(collapseListener);
	     }
	    
	    anim.setDuration(speed);
	    anim.setInterpolator(new AccelerateInterpolator(1.0f));
	    startAnimation(anim);
	  }
	  
	  Animation.AnimationListener collapseListener=new Animation.AnimationListener() {
	    public void onAnimationEnd(Animation animation) {
	      setVisibility(View.GONE);
	    }
	    
	    public void onAnimationRepeat(Animation animation) {
	      // not needed
	    }
	    
	    public void onAnimationStart(Animation animation) {
	      // not needed
	    }
	  };
	}