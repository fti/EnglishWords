package com.trandroid.ingilizcekelimeler;

import com.mopub.mobileads.MoPubView;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class HomeActivity extends BaseActivity{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Typeface font = Typeface.createFromAsset(getAssets(), "rock.ttf");
        TextView copyright = (TextView) findViewById(R.id.copyrightText);
        copyright.setTypeface(font);
        
        MoPubView mpv = (MoPubView) findViewById(R.id.adLayout);
		mpv.setAdUnitId(MoPub_ID);
		mpv.loadAd();
    }
    public void onUds(View v){
		Intent i = new Intent(this, ReadActivity.class);
		i.putExtra("type", Word.Type_UDS);
		i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(i);
	}
	public void onToefl(View v){
		Intent i = new Intent(this, ReadActivity.class);
		i.putExtra("type", Word.Type_TOEFL);
		i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(i);
	}
	public void onKpds(View v){
		Intent i = new Intent(this, ReadActivity.class);
		i.putExtra("type", Word.Type_KPDS);
		i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(i);
	}

	public void onBiz(View v){
		Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://trandroid.com/hakkimizda.html"));
    	startActivity(myIntent);
	}
}