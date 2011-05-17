package com.akshay.mp3player;

import java.io.File;
import java.io.FileInputStream;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ProgressBar;
import android.widget.ToggleButton;

public class Player extends Activity implements Runnable {
	private MediaPlayer mp;
	private ProgressBar progressBar;
	private ToggleButton button;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initMediaPlayer();
		File mp3 = getMP3File("/test.mp3");
		setFileForPlayBack(mp3);

		progressBar = (ProgressBar) findViewById(R.id.progress);
		button = (ToggleButton) findViewById(R.id.start_stop);
		button.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton button, boolean isChecked) {
				if (isChecked){
					Log.i("Akshay", "is for start checked " + isChecked);
					startPlayback();
				} else {
					Log.i("Akshay", "is for pause checked " + isChecked);
					pausePlayback();
				}
			}			
		});
	}

	private void initMediaPlayer(){
		mp = new MediaPlayer();
	}

	private void  setFileForPlayBack(File mp3){
		try {
			FileInputStream fis = new FileInputStream(mp3);
			mp.setDataSource(fis.getFD());
			mp.prepare();
		} catch (Exception e){
			Log.e("Akshay", e.getMessage());
		} finally {

		}
	}

	private File getMP3File(String fileName){
		File folder = Environment.getExternalStorageDirectory();
		String url = folder.toString() + fileName;
		File tempMp3 = new File(url);
		return tempMp3;
	}

	private void startPlayback(){
		if(mp != null && mp.isPlaying()) return;
		progressBar.setProgress(0);
		progressBar.setMax(mp.getDuration());
		mp.start();                      
		new Thread(Player.this).start();
	}

	private void pausePlayback(){
		mp.pause();
	}
	
	private void stopPlayback(){
		if (mp!=null){
			mp.stop();
			mp.release();
			Log.i("Akshay", "pause");

		}
	}

	@Override
	public void run() {
		int CurrentPosition= 0;
		int total = mp.getDuration();
		while(mp!=null && CurrentPosition<total){
			try {
				Thread.sleep(1000);
				CurrentPosition= mp.getCurrentPosition();
			} catch (InterruptedException e) {
				return;
			} catch (Exception e){
				return;
			}            
			progressBar.setProgress(CurrentPosition);
		}
	}

}