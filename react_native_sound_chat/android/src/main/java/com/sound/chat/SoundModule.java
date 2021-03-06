package com.sound.chat;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableArray;

public class SoundModule extends ReactContextBaseJavaModule{

    private static final String TAG = "SoundModule";
  	private VoiceChatApi api;

    public SoundModule(ReactApplicationContext reactContext) {
        super(reactContext);
        api = new VoiceChatApi(getReactApplicationContext());
    }

    @Override
    public String getName() {
        return "SoundModule";
    }

    @ReactMethod
    public void initApi(String serverUrl){
    	api.initApi(getCurrentActivity(),serverUrl);
    } 

    @ReactMethod
    public void recordStart(){
    	api.recordStart();
    } 

    @ReactMethod
    public void recordStop(Callback callback){
    	String s = api.recordStop();
    	if (callback != null) {
           	callback.invoke(s);
        }
    } 

    @ReactMethod
    public void recordPlay(String path){
    	api.recordPlay(path);
    } 

    @ReactMethod
    public void recordStopPlay(){
        api.stopPlay();
    }     

    @ReactMethod
    public void recordUpload(){
    	api.recordUpload();
    } 

    @ReactMethod
    public void recordDownload(String url){
        api.recordDownload(url);
    } 

    @ReactMethod
    public void imageDownload(String url){
        api.imageDownload(getCurrentActivity(),url);
    } 

    @ReactMethod
    public void imgsUpload(ReadableArray files,String imgType){
        api.uploadImgs(files,imgType);
    } 

    @ReactMethod
    public void fileIsExits(String path,Callback callback){
        boolean isExits = api.fileIsExits(path);
        if (callback != null) {
            callback.invoke(isExits);
        }
    } 

    @ReactMethod
    public void fileDelete(String path){
        api.fileDelete(path);
    } 

    @ReactMethod
    public void isNotificationEnabled(Callback callback){
        boolean isEnabled = api.isNotificationEnabled(getCurrentActivity());
        Log.d(TAG,isEnabled+"");
        if (callback != null) {
            callback.invoke(isEnabled);
        }
    } 

    @ReactMethod
    public void getNetWorkType(Callback callback){
        String netType = NetManager.getNetWorkType(getCurrentActivity());
        Log.d(TAG,netType+"");
        if (callback != null) {
            callback.invoke(netType);
        }
    } 

    @ReactMethod
    public void getConfig(String url,Callback callback){
        String path = api.getConfig(getCurrentActivity(),url);
        if(callback != null){
            if(!path.equals("")&&path!=null){
                callback.invoke(path);
            }
            else{
                callback.invoke(false);
            }
        }
    } 
}
