package com.sound.chat;

import android.content.*;
import android.os.*;
import android.util.*;
import android.app.Activity;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONObject;
import org.apache.http.util.EncodingUtils;
import android.content.SharedPreferences;

/**
 * 下载类
 * */
public class FileHelper {

	private SharedPreferences sharedPreferences=null;

	public static File DownloadFromUrlToData(String serverUrl,String fileName,String type,
			Context context) {
		try {
			if (!Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
				return null;
			}
			URL url = new URL(serverUrl);
			HttpURLConnection urlCon = (HttpURLConnection)url.openConnection();
			urlCon.setConnectTimeout(10000);
			urlCon.setReadTimeout(10000);
			long startTime = System.currentTimeMillis();
			/* Open a connection to that URL. */
			URLConnection ucon = url.openConnection();
			InputStream is = ucon.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(1024);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}
			// File dir = new File(Environment.getExternalStorageDirectory()
			// 		.getCanonicalFile() + "/" + dirName);
			String path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/redpack/"+type+"/";
			File f = new File(path);
			if(!f.exists()){
				f.mkdirs();
			}
			path=f.getAbsolutePath()+"/"+fileName;
			File file = new File(path);
			// File file = new File(Environment.getExternalStorageDirectory()
			// 		.getCanonicalFile() + "/juyouim/" + fileName);
			FileOutputStream fout = new FileOutputStream(file);
			fout.write(baf.toByteArray());
			fout.close();
			return file;
		} catch (IOException e) {
			return null;
		}
	}

	public File createSDFile(String fileName) throws IOException {
		File file = new File(Environment.getExternalStorageDirectory()
				.getCanonicalFile() + "/" + fileName);
		file.createNewFile();
		return file;
	}

	public static String getFileName() {
		GregorianCalendar currentDay = new GregorianCalendar();
		int year = currentDay.get(Calendar.YEAR);
		int month = currentDay.get(Calendar.MONTH);
		int today = currentDay.get(Calendar.DAY_OF_MONTH);
		int hour = currentDay.get(Calendar.HOUR_OF_DAY);
		int min = currentDay.get(Calendar.MINUTE);
		int second = currentDay.get(Calendar.SECOND);
		int ms = currentDay.get(Calendar.MILLISECOND);
		String s = "";
		s = s + new Integer(year).toString() + new Integer(month).toString()
				+ new Integer(today).toString();
		s = s + new Integer(hour).toString() + new Integer(min).toString()
				+ new Integer(second).toString();
		s = s + new Integer(ms).toString();
		return s;
	}

	public static void writeFileData(String fileName, byte[] bytes,
			Context context) {
		try {
			FileOutputStream fout = context.openFileOutput(fileName,
					context.MODE_PRIVATE);
			fout.write(bytes);
			fout.close();
			// PrintWriter pw= new PrintWriter(fout);
			// pw.write(new String(bytes,0,bytes.length,"utf-8"));
			// pw.close();
			// fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getConfig(Context context,String url){
		if(sharedPreferences==null)
			sharedPreferences=context.getSharedPreferences("img_list",Activity.MODE_PRIVATE);
		String nativePath=sharedPreferences.getString(url,"");
		return nativePath;
	}

	public void saveConfig(Context context,String url,String nativePath){
		if(sharedPreferences==null)
			sharedPreferences=context.getSharedPreferences("img_list",Activity.MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //用putString的方法保存数据
        editor.putString(url, nativePath);
        //提交当前数据
        editor.apply();
	}
}
