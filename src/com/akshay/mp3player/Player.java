package com.akshay.mp3player;

import java.io.File;
import java.io.FileInputStream;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.ToggleButton;

public class Player extends Activity implements Runnable {
	private MediaPlayer mp;
	private SeekBar progressBar;
	private ToggleButton button;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		progressBar = (SeekBar) findViewById(R.id.progress);
		button = (ToggleButton) findViewById(R.id.start_stop);
		
		
		initMediaPlayer();
		File mp3 = getMP3File("/test.mp3");
		setFileForPlayBack(mp3);

		
		button.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton button, boolean isChecked) {
				if (isChecked){
					startPlayback();
				} else {
					pausePlayback();
				}
			}			
		});
		
		progressBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (fromUser){
					mp.seekTo(progress);
				}
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {				
			}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {				
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
			progressBar.setProgress(0);
			progressBar.setMax(mp.getDuration());
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
		int currentPosition= 0;
		int total = mp.getDuration();
		while(mp!=null && currentPosition<total){
			try {
				Thread.sleep(1000);
				currentPosition= mp.getCurrentPosition();
			} catch (InterruptedException e) {
				return;
			} catch (Exception e){
				return;
			}            
			progressBar.setProgress(currentPosition);
		}
	}

}