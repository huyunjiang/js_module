package com.sound.chat;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.Date;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.os.Environment;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

/**
*录音管理类
**/
public class RecordManger {
	/**录音后文件*/
	private File file; 
	/**android媒体录音类*/
	private MediaRecorder mr;
	/**声波振幅监听器*/
    private SoundAmplitudeListen soundAmplitudeListen;
    /**启动计时器监听振幅波动*/
	private Handler mHandler;
	public static final String TAG = "RecordManger";
	private Long startTime;
	public static Long duration;

	public RecordManger(Handler handler){
		mHandler=handler;
	}

	private Runnable mUpdateMicStatusTimer = new Runnable() {
		/**
		 * 分贝的计算公式K=20lg(Vo/Vi) Vo当前振幅值 Vi基准值为600
		 */
		private int BASE = 600;
        private int RATIO=5;
    	private int postDelayed =200;
		public void run() {
			  // int vuSize = 10 * mMediaRecorder.getMaxAmplitude() / 32768;  
            int ratio =mr.getMaxAmplitude() / BASE;  
            int db = (int) (20 * Math.log10(Math.abs(ratio)));  
            int value=db / RATIO;
            if(value<0)value=0;
            if(soundAmplitudeListen!=null)
            	soundAmplitudeListen.amplitude(ratio, db, value);
			mHandler.postDelayed(mUpdateMicStatusTimer, postDelayed);

		}
	};

	 /**启动录音并生成文件*/
	@SuppressWarnings("static-access")
	public void startRecordCreateFile() throws IOException {
		if (!Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return;
		}
		startTime=new Date().getTime();
		String path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/redpack/";
		File f = new File(path);
		if(!f.exists()){
			f.mkdirs();
		}
		path=f.getAbsolutePath()+"/"+ new DateFormat().format("yyyyMMdd_HHmmss",Calendar.getInstance(Locale.CHINA)) + ".amr";
		file = new File(path);
		// 创建文件
		file.createNewFile();
		mr = new MediaRecorder(); // 创建录音对象
		mr.setAudioSource(MediaRecorder.AudioSource.MIC);// 从麦克风源进行录音
		mr.setAudioChannels(1);// 从麦克风源进行录音
		mr.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);// 设置输出格式
		mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);// 设置编码格式
		mr.setAudioEncodingBitRate(16);
		mr.setAudioSamplingRate(8000);
		mr.setOutputFile(file.getAbsolutePath());// 设置输出文件
		// 开始录制
		mr.prepare();
		// 开始录制
		mr.start();
		//启动振幅监听计时器
		mHandler.post(mUpdateMicStatusTimer);
		Log.d(TAG,"startRecord");
	}
	
	 /**停止录音并返回录音时长和路径*/
	public String stopRecord() {
		duration=new Date().getTime()-startTime;
		String filePath=file.getAbsolutePath();
		Log.d(TAG,"stopRecord");
		if (mr != null) {
			mr.stop();
			mr.release();
			mr = null;
			mHandler.removeCallbacks(mUpdateMicStatusTimer);
		}
		String result = duration.toString()+","+filePath;
		return result;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public MediaRecorder getMr() {
		return mr;
	}

	public void setMr(MediaRecorder mr) {
		this.mr = mr;
	}
	public SoundAmplitudeListen getSoundAmplitudeListen() {
		return soundAmplitudeListen;
	}

	public void setSoundAmplitudeListen(SoundAmplitudeListen soundAmplitudeListen) {
		this.soundAmplitudeListen = soundAmplitudeListen;
	}
    public interface SoundAmplitudeListen{
	   public void amplitude(int amplitude,int db,int value);
    }

}
