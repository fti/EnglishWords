package com.trandroid.ingilizcekelimeler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mopub.mobileads.MoPubView;

public class ReadActivity extends BaseActivity implements TextToSpeech.OnInitListener{

	private static final int MY_DATA_CHECK_CODE = 1234;
	TextView wordNo, wordName, wordMean, headerText;
	ImageViewSlider biliyorum, bilmiyorum, sonraki;
	Session session;
	WordLoader loader;
	DBAdapter db = null;
	Context context;
	List<Word> wList;
	private TextToSpeech mTts;
	String type;
	int limit = 0;
	int showType = 0;
	int lastShownId = 1;
	
	Word w;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reading);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		type = getIntent().getExtras()!= null? getIntent().getExtras().getString("type"):Word.Type_KPDS;
		context = getApplicationContext();
		session = new Session(context);
		lastShownId = session.getLastId(type);
		showType = session.getShowType();
		
		initialize();
		
		db = new DBAdapter(context);
		db.open();
		boolean isFirst = db.isFirstTime(type);
		db.close();
		
		if (isFirst) {
			loader = new WordLoader();
			loader.execute();
		}else{
			showNextWord();
		}
	}
	@Override
	public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = mTts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("Read", "Language is not available.");
            } else {}
        } else {
            Log.e("Read", "Could not initialize TextToSpeech.");
        }
		
	} 
	public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == MY_DATA_CHECK_CODE)
        {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS || resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_DATA)
            {
                // success, create the TTS instance
                mTts = new TextToSpeech(this, this);
                mTts.setPitch(0.6f);
                mTts.setSpeechRate(session.getSpeechRate());
            }
            else
            {
                // missing data, install it
                Intent installIntent = new Intent();
                installIntent.setAction(
                        TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
    }
	 @Override
	    public void onDestroy() {
	        // Don't forget to shutdown!
	        if (mTts != null) {
	            mTts.stop();
	            mTts.shutdown();
	        }
	        super.onDestroy();
	    }
	private void showNextWord() {
		
		if (wList.isEmpty()) {
			db.open();
			wList = db.getWords(type, lastShownId, showType);
			if (wList.isEmpty()) {
				lastShownId = 1;
				wList = db.getWords(type, lastShownId, showType);
			}
			if (wList.isEmpty()) {
				String msgText = "";
				if (showType==Session.SHOW_BILDIKLERIM) {
					msgText = getString(R.string.msgbildiklerim);
				}else if (showType == Session.SHOW_BILEMEDIKLERIM) {
					msgText = getString(R.string.msgbilemediklerim);
				}else {
					msgText = getString(R.string.msgerror);
				}
				Toast.makeText(this, msgText, Toast.LENGTH_LONG).show();
				showType = Session.SHOW_ALL;
				session.saveShowType(showType);
			}
			db.close();
			showNextWord();
		}else {
			w = wList.remove(0);
			wordMean.setText(w.mean);
			wordName.setText(w.word);
			wordNo.setText(w.orderid+"");
			lastShownId = w.id;
		}
		session.saveLastId(type, lastShownId);
	}

	private boolean parseInsert(String type) {
		String line = "";
		try {
			db.open();
			BufferedReader bf = new BufferedReader(new InputStreamReader(ReadActivity.this.getAssets().open(type), "UTF-8"));
			int orderid = 1;
			while((line=bf.readLine())!= null){
				String[] wordarray=line.split("###",2);
				Word w = new Word();
				w.word = wordarray[0];
				w.mean = wordarray[1];
				w.type = type;
				w.orderid = orderid;
				db.insertWord(w);
				orderid++;
			}

		} catch (Exception e) {
			Log.e("PARSING "+type, line+"   "+e);
			return false;
		}finally{
			db.close();
		}
	return true;
}
	
	
	public void initialize(){
		Typeface font = Typeface.createFromAsset(getAssets(), "ocr.ttf");
		
		wordMean = (TextView) findViewById(R.id.wordMean);
		wordName = (TextView) findViewById(R.id.wordName);
		wordNo = (TextView) findViewById(R.id.wordNo);
		headerText = (TextView) findViewById(R.id.headerText);
		
		biliyorum = (ImageViewSlider) findViewById(R.id.biliyorum);
		bilmiyorum = (ImageViewSlider) findViewById(R.id.bilmiyorum);
		sonraki = (ImageViewSlider) findViewById(R.id.sonraki);
		sonraki.toggle(ImageViewSlider.toRight, true);
		
		headerText.setText(type);
		wordNo.setText(lastShownId+"");
		wordMean.setMovementMethod(new ScrollingMovementMethod());
		headerText.setTypeface(font);
		wordName.setTypeface(font);
		wordNo.setTypeface(font);
		wordMean.setTypeface(font);
		
		wList = new ArrayList<Word>();
		
		Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
        
		MoPubView mpv = (MoPubView) findViewById(R.id.adLayout);
		mpv.setAdUnitId(MoPub_ID);
		mpv.loadAd();
	}
	
	public void onSonraki(View v){
		showNextWord();
		wordMean.setTextColor(Color.parseColor("#0099CC"));
		sonraki.toggle(ImageViewSlider.toRight, true);
		biliyorum.toggle(ImageViewSlider.toLeft, false);
		bilmiyorum.toggle(ImageViewSlider.toLeft, false);
	}
	public void onListenWord(View v){
		String speechText = "";
		if (Session.isNull(w.word)) {
			speechText = "Error! Couldn't find the word, please contact us";
		}else {
			speechText = w.word;
		}
		mTts.speak(speechText,TextToSpeech.QUEUE_FLUSH, null);
	}
	public void onBiliyorum(View v){
		db.open();
		db.updateWord(w.id, 1);
		db.close();
		answered();
	}
	public void onBilmiyorum(View v){
		db.open();
		db.updateWord(w.id, 0);
		db.close();
		answered();
	}
	
	public void answered(){
		wordMean.setTextColor(Color.parseColor("#292929"));
		biliyorum.toggle(ImageViewSlider.toLeft, true);
		bilmiyorum.toggle(ImageViewSlider.toLeft, true);
		sonraki.toggle(ImageViewSlider.toRight, false);
	}
	public void onSettings(View v){
		   final CharSequence[] items = {"(0)Bilemediklerim", "(1)Bildiklerim", "(2)Hepsi"};

		    AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    builder.setTitle("("+showType+")Seçili");
		    builder.setItems(items, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int item) {
		        	showType=item;
		        	session.saveShowType(showType);
		        	wList.clear();
		        	ReadActivity.this.showNextWord();
		        	dialog.dismiss();
		        }
		    }).show();
		
	   }
	  @Override
	    public boolean onCreateOptionsMenu(Menu m) {
			super.onCreateOptionsMenu(m);
			m.add(0, 5, 0, "Speech Rate");
			return true;
	    }
	    
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	       if (item.getItemId() == 5) {
	        	final CharSequence[] items = {"Very Slow", "Slow", "Normal", "Fast"};

			    AlertDialog.Builder builder = new AlertDialog.Builder(this);
			    builder.setTitle("Speech Rate");
			    builder.setItems(items, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int it) {
			        	switch (it) {
						case 0:
							session.saveSpeechRate(Session.SPEECH_RATE_VERYSLOW);
							try {
								mTts.setSpeechRate(Session.SPEECH_RATE_VERYSLOW);
							} catch (Exception e) {
								// do nothing
							}
							break;
						case 1:
							session.saveSpeechRate(Session.SPEECH_RATE_SLOW);
							try {
								mTts.setSpeechRate(Session.SPEECH_RATE_SLOW);
							} catch (Exception e) {
								// do nothing
							}
							break;
						case 2:
							session.saveSpeechRate(Session.SPEECH_RATE_NORMAL);
							try {
								mTts.setSpeechRate(Session.SPEECH_RATE_NORMAL);
							} catch (Exception e) {
								// do nothing
							}
							break;
						case 3:
							session.saveSpeechRate(Session.SPEECH_RATE_FAST);
							try {
								mTts.setSpeechRate(Session.SPEECH_RATE_FAST);
							} catch (Exception e) {
								// do nothing
							}
							break;

						default:
							break;
						}
			        	dialog.dismiss();
			        }
			    }).show();
			    return true;
	       }
	       return super.onOptionsItemSelected(item);
	    }
	public class WordLoader extends AsyncTask<Void, Void, Void> {
		
		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(ReadActivity.this);
			progressDialog.setMessage("kelimeler hazõrlanõyor, 30-40 saniye sŸrebilir, biraz sabõr");
			progressDialog.setOnKeyListener(new OnKeyListener() {
				
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					if (keyCode == KeyEvent.KEYCODE_BACK && progressDialog.isShowing())
				    {
						return true;
				        // DO SOMETHING
				    }
					return false;
				}
			});
			progressDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					//cancel(false);
				}
			});
			progressDialog.show();
		}
		
		
		@Override
		protected Void doInBackground(Void... params) {
			parseInsert(type);
			return null;
		}
		@Override
		protected void onPostExecute(Void v) {
			progressDialog.dismiss();
			showNextWord();
		}
		
	}

}
