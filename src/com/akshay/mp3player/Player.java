package com.akshay.mp3player;

import java.io.File;
import java.io.FileInputStream;

import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;

public class Player implements Runnable {

	private final MediaPlayer player;
	private Cursor songCursor;
	private IPlayer callback;

	public Player(){
		this.player = new MediaPlayer();
	}

	public Player(Cursor songCursor){
		this.songCursor = songCursor;
		this.player = new MediaPlayer();
	}

	public int setFileForPlayback(File mp3){
		int duration = 0;
		try {
			FileInputStream fis = new FileInputStream(mp3);
			player.setDataSource(fis.getFD());
			player.prepare();
			duration = getMP3Duration();
		} catch (Exception e){
			Log.e("Akshay", e.getMessage());
		}
		return duration;
	}

	private File getMP3File(String fileName){
		File folder = Environment.getExternalStorageDirectory();
		String url = folder.toString() + fileName;
		File tempMp3 = new File(url);
		return tempMp3;
	}

	public void startPlayback(){
		try{
			if(player != null && player.isPlaying()) return;
			player.start();                      
			new Thread(Player.this).start();
		} catch (Exception e){
			Log.e("Akshay", e.getMessage()+"");
		}
	}

	public void pausePlayback(){
		player.pause();
	}

	public void stopPlayback(){
		if (player!=null){
			player.stop();
			player.release();
		}
	}

	public int nextSongInCursor(){
		if (songCursor!=null && songCursor.moveToNext()){
			String fileName = songCursor.getString(songCursor.getColumnIndex("data"));
			File mp3 = getMP3File(fileName);
			return setFileForPlayback(mp3);
		}
		return 0;
	}

	public int previousSongInCursor(){
		if (songCursor!=null&&songCursor.moveToPrevious()){
			songCursor.moveToPrevious();
			String fileName = songCursor.getString(songCursor.getColumnIndex("data"));
			File mp3 = getMP3File(fileName);
			return setFileForPlayback(mp3);
		}
		return 0;
	}

	private int getMP3Duration(){
		return player.getDuration();
	}

	public void moveToPositionInTrack(int position){
		player.seekTo(position);
	}

	public int loadTestMP3(){
		File mp3 = getMP3File("/test.mp3");
		return setFileForPlayback(mp3);
	}

	public void setIPlayerCallback(IPlayer callback){
		this.callback = callback;
	}

	@Override
	public void run() {
		int currentPosition= 0;
		int total = getMP3Duration();
		while(player!=null && currentPosition<total){
			try {
				Thread.sleep(1000);
				currentPosition= player.getCurrentPosition();
			} catch (InterruptedException e) {
				return;
			} catch (Exception e){
				return;
			}  
			if (callback!=null){
				callback.onProgress(currentPosition);
			}
		}
	}

}
