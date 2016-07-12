package com.example.tjwx_person.tool.voice;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.security.auth.callback.Callback;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

public class SoundMeter {
	static final private double EMA_FILTER = 0.6;
   static SoundMeter soundMeter;
	private static MediaRecorder mRecorder = null;
	private double mEMA = 0.0;
	private Timer mTimer; // 计时组件
	Context mContext;
	int time = 0;

	public static SoundMeter getSoundMeter(Context context){
		if(mRecorder==null){
			soundMeter=new SoundMeter(context);
		}
		return soundMeter;
	}
	
	
	public SoundMeter(Context context) {
		mContext = context;
	}

	public void start(String name) {
		if (!Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return;
		}
		if (mRecorder == null) {
			mTimer = new Timer();
			mTimer.schedule(new TimerTask() {
				@Override
				public void run() {

					mHandler.sendEmptyMessage(0);
				}
			}, 1 * 1000);

			mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			mRecorder.setOutputFile(Environment.getExternalStorageDirectory()
					+ "/" + name);

			try {
				mRecorder.prepare();
				mRecorder.start();

				mEMA = 0.0;
			} catch (IllegalStateException e) {
				System.out.print(e.getMessage());
			} catch (IOException e) {
				System.out.print(e.getMessage());
			}

		}
	}

	public boolean stop() {
		if (mRecorder != null) {
			try {
				mTimer = null;
				mRecorder.stop();
				mRecorder.release();
				mRecorder = null;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	public void pause() {
		if (mRecorder != null) {
			mRecorder.stop();
		}
	}

	public void start() {
		if (mRecorder != null) {

			mRecorder.start();
		}
	}

	public double getAmplitude() {
		if (mRecorder != null)
			return (mRecorder.getMaxAmplitude() / 1500.0);
		else
			return 0;

	}

	public double getAmplitudeEMA() {
		double amp = getAmplitude();
		mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
		return mEMA;
	}

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			time++;
			if (time < 60 && time >= 40) {

			}
		};
	};

}
