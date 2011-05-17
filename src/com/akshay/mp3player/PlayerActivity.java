package com.akshay.mp3player;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.ToggleButton;

public class PlayerActivity extends Activity{

	private SeekBar progressBar;
	private ToggleButton button;
	private Player player;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		progressBar = (SeekBar) findViewById(R.id.progress);
		button = (ToggleButton) findViewById(R.id.start_stop);
		progressBar.setProgress(0);
		
		player = new Player();				
		player.setIPlayerCallback(new IPlayer(){
			@Override
			public void onProgress(int currentPosition) {
				progressBar.setProgress(currentPosition);
			}			
		});
		
		
		progressBar.setMax(player.loadTestMP3());

		button.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton button, boolean isChecked) {
				if (isChecked){
					player.startPlayback();
				} else {
					player.pausePlayback();
				}
			}			
		});

		progressBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (fromUser){
					player.moveToPositionInTrack(progress);
				}
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}			
		});
	}
}