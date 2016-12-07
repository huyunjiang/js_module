package com.sound.chat;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.AutoCompleteTextView.Validator;
import android.os.Handler;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.ReactApplicationContext;

/**
* 统一接口
**/
public class VoiceChatApi implements IVoiceAPI{
	/** 录音管理类 */
	private RecordManger recordManger;
	/** 上传文件服务器地址 */
	private String uploadFileServerUrl;
	private OnStateListener uploadFileStateListener;
	private OnStateListener downloadFileFileStateListener;
	private Context _context;
	public static final String TAG = "VoiceChatApi";
	private ReactApplicationContext _reactContext;

	public VoiceChatApi(ReactApplicationContext reactContext){
		_reactContext=reactContext;
		Handler handler = new Handler(reactContext.getMainLooper());
		recordManger = new RecordManger(handler);
	}

	public void initApi(Context content,String serverUrl) {
		_context=content;
		uploadFileServerUrl=serverUrl;      
	}
	
	/** 启动录音不进行网络上传 */
	public void recordStart() {
		try {
			recordManger.startRecordCreateFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** 停止录音 */
	public String recordStop() {
		return recordManger.stopRecord();// 停止录音
	}

	//播放录音
	public void recordPlay(String filePath){
		MediaPlayer mediaPlayer = new MediaPlayer();
		try {
			File f = new File(filePath);
			mediaPlayer.setDataSource(f.getAbsolutePath());
			mediaPlayer.prepare();
			mediaPlayer.start();
			Log.d(TAG,"play");
			if (downloadFileFileStateListener != null) {
				downloadFileFileStateListener.onState(0, "成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 停止录音后上传 */
	public void recordUpload() {
		File file = recordManger.getFile();
		if (file == null || !file.exists() || file.length() == 0) {
			if (uploadFileStateListener != null)
				uploadFileStateListener.onState(-1, "文件不存在或已经损坏");
			return;
		}
		new UpLoadecordFile().execute();// 调用异步任务
	}

	/** 异步任务-录音上传 */
	public class UpLoadecordFile extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... parameters) {
			return UploadUtil.uploadFile(recordManger.getFile(),uploadFileServerUrl,RecordManger.duration);
		}

		@Override
		protected void onPostExecute(String result) {
			if (result == null) {
				sendEvent("uploadFail","上传失败");
				if (uploadFileStateListener != null)
					uploadFileStateListener.onState(-2, "上传文件失败");
			}else{
				File f = recordManger.getFile();
				sendEvent("uploadSuccess",result);
				if (uploadFileStateListener != null)
					uploadFileStateListener.onState(0, "上传文件成功");
			}
		}
	}

	/** 下载*/
	public void recordDownload(String uploadFilename) {
		new DownloadRecordFile().execute(uploadFilename);
	}

	/** 异步任务-下载后播放 */
	public class DownloadRecordFile extends AsyncTask<String, Integer, File> {
		@Override
		protected File doInBackground(String... parameters) {
			try {
				String filename = parameters[0];
				int index=filename.lastIndexOf("/");
				filename=filename.substring(index+1);
				return FileHelper.DownloadFromUrlToData(parameters[0], filename, _context);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(File result) {
			if (result == null || !result.exists() || result.length() == 0) {
				if (downloadFileFileStateListener != null) {
					downloadFileFileStateListener.onState(-1, "下载文件失败");
					return;
				}
			}
			// MediaPlayer mediaPlayer = new MediaPlayer();
			// mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			// 	@Override
			// 	public void onCompletion(MediaPlayer mediaPlayer) {
			// 		// TODO Auto-generated method stub
			// 		mediaPlayer.release();
			// 	}
			// });
			try {
				sendEvent("downloadSuccess",result.getAbsolutePath());
				if (downloadFileFileStateListener != null) {
					downloadFileFileStateListener.onState(0, "成功");
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean fileIsExits(String path){
		File f= new File(path);
		if(f.exists()){
			return true;
		}
		return false;
	}

	public void fileDelete(String path){
		File f = new File(path);
		if (f.isFile() && f.exists()) {  
	        f.delete();  
	    }   
	}

	private void sendEvent(String eventName,String returnMsg){
        _reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
        .emit(eventName, returnMsg);
    }

	public RecordManger getRecordManger() {
		return recordManger;
	}

	public void setRecordManger(RecordManger recordManger) {
		this.recordManger = recordManger;
	}

	public Context getContext() {
		return _context;
	}

	public void setContext(Context context) {
		this._context = context;
	}


	public String getUploadFileServerUrl() {
		return uploadFileServerUrl;
	}

	public void setUploadFileServerUrl(String uploadFileServerUrl) {
		this.uploadFileServerUrl = uploadFileServerUrl;
	}

	public OnStateListener getUploadFileStateListener() {
		return uploadFileStateListener;
	}

	public void setUploadFileStateListener(
			OnStateListener uploadFileStateListener) {
		this.uploadFileStateListener = uploadFileStateListener;
	}

	public OnStateListener getDownloadFileFileStateListener() {
		return downloadFileFileStateListener;
	}

	public void setDownloadFileFileStateListener(
			OnStateListener downloadFileFileStateListener) {
		this.downloadFileFileStateListener = downloadFileFileStateListener;
	}

}
