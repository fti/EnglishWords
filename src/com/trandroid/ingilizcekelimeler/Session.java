package com.trandroid.ingilizcekelimeler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Session {

	private static final String KEY = "ingilizce.kelimeler.session";
	
	static final String LAST_ID = "lastid_";
	static final String LAST_ID_KPDS = "lastid_kpds";
	static final String LAST_ID_TOEFL = "lastid_toefl";
	static final String LAST_ID_UDS = "lastid_uds";
	
	static final String SHOW_TYPE = "show_type";
	static final int SHOW_BILEMEDIKLERIM = 0;
	static final int SHOW_BILDIKLERIM = 1;
	static final int SHOW_ALL = 2;
	
	static final String SPEECH_RATE = "speech_rate";
	static final float SPEECH_RATE_VERYSLOW = 0.3f; 
	static final float SPEECH_RATE_SLOW = 0.5f;
	static final float SPEECH_RATE_NORMAL = 1f;
	static final float SPEECH_RATE_FAST = 1.5f;
	
	static final String HISTORY_IDS = "history_ids";
	static final String HISTORY_RECORDS = "history_records";
	
	Context context;
	
	public Session() {}
	
	public Session(Context context) {
		this.context = context;
	}
	
	public boolean saveLastId(String type, int lastId){
		Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
		editor.putInt(LAST_ID+type, lastId);
    	if(editor.commit()) return true;
    	return false;
	}
	public int getShowType() {
		SharedPreferences prefs = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
		return prefs.getInt(SHOW_TYPE, 0);
	}
	public boolean saveShowType(int showtype){
		Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
		editor.putInt(SHOW_TYPE, showtype);
		if(editor.commit()) return true;
		return false;
	}
	public int getLastId(String type) {
		SharedPreferences prefs = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
		return prefs.getInt(LAST_ID+type, 1);
	}
	
	public boolean saveSpeechRate(float speechRate){
		Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
		editor.putFloat(SPEECH_RATE, speechRate);
		if(editor.commit()) return true;
		return false;
	}
	public float getSpeechRate() {
		SharedPreferences prefs = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
		return prefs.getFloat(SPEECH_RATE, 1.0f);
	}
	
    public boolean clearAllSession() {
        Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
        editor.clear();
        return editor.commit();
    }
    
   
    public static boolean isNull(String s){
    	return s==null || "".equals(s);
    }

    public static boolean emailCont(String s) {
		
	    Matcher m = Pattern.compile(".+@.+\\.[a-z]+").matcher(s);

        if (m.matches())
          return true;
		
		return false;
	}
 }