package com.sound.chat;

import android.content.Context;

public interface IVoiceAPI {
	void initApi(Context content,String url);
	void recordStart();
	void recordPlay(String url);
	String recordStop();
	void recordUpload();
	void recordDownload(String url);
}
