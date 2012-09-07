package com.trandroid.ingilizcekelimeler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;

public class BaseActivity extends Activity {
    
	public static final String MoPub_ID = "abc";
	 
	  @Override
	    public boolean onCreateOptionsMenu(Menu m) {
			super.onCreateOptionsMenu(m);
			m.add(0, 1, 0, "biz?");
			m.add(0, 2, 0, "iletişim");
			m.add(0, 3, 0, "uyg.");
			m.add(0, 4, 0, "ses!");
			return true;
	    }
	    
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	        case 1:
	        	Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://trandroid.com/hakkimizda.html"));
	        	startActivity(myIntent);
	        	return true;
	        case 2:
	        	mailat();
	        	return true;
	        case 3:
	        	Intent myIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://trandroid.com/uygulamalarimiz.html"));
	    		startActivity(myIntent2);
	        	return true;
	        case 4:
	        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    		builder.setTitle(getString(R.string.msgalerttitle));
	    		builder.setMessage(getString(R.string.msgalert))
	    		       .setCancelable(false)
	    		       .setPositiveButton("Kapat", new DialogInterface.OnClickListener() {
	    		           public void onClick(DialogInterface dialog, int id) {
	    		                dialog.cancel();
	    		           }
	    		       });
	    		AlertDialog alert = builder.create();
	    		alert.show();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	        }
	    }

		 public void mailat() {
		    	String phoneModel = android.os.Build.MODEL;
		    	String androidVersion = android.os.Build.VERSION.RELEASE;
		    	Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);  
		    	String aEmailList[] = { "apps@trandroid.com"};  
		    	emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);  
		    	emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "İnglizce Kelimeler - trandroid");  
		    	emailIntent.setType("plain/text");  
		    	emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml("<br/><br/><span>model: "+phoneModel+"&nbsp;&nbsp;android version: "+androidVersion));  
		    	startActivity(emailIntent);  
		}		

    
    protected void onResume() {
		super.onResume();
		overridePendingTransition(R.anim.slide_left,R.anim.slide_right);
	};
}