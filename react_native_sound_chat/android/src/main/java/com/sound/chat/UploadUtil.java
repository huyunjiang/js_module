package com.sound.chat;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
*上传工具类
**/
public class UploadUtil {
	private static final String TAG = "uploadFile";
	private static final int TIME_OUT = 10 * 1000; // 超时时间
	private static final String CHARSET = "utf-8"; // 设置编码

	/**
	 * android上传文件到服务器
	 * 
	 * @param file
	 *            需要上传的文件
	 * @param RequestURL
	 *            请求的rul
	 * @return 返回响应的内容
	 */
	public static String uploadFile(File file, String RequestURL) {
		String result = null;
		String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
		String PREFIX = "--", LINE_END = "\r\n";
		String CONTENT_TYPE = "multipart/form-data"; // 内容类型
		String charset = "utf-8";
		try {
			URL url = new URL(RequestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(TIME_OUT);
			conn.setConnectTimeout(TIME_OUT);
			conn.setDoInput(true); // 允许输入流
			conn.setDoOutput(true); // 允许输出流
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestMethod("POST"); // 请求方式
			conn.setRequestProperty("Charset", CHARSET); // 设置编码
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
			if (file != null) {
				/**
				 * 当文件不为空，把文件包装并且上传
				 */
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(RequestURL);
				MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
				multipartEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
				multipartEntity.addPart("img", new FileBody(file));
				multipartEntity.addTextBody("filename", file.getName());
				post.setEntity(multipartEntity.build());
				HttpResponse response = client.execute(post);
				HttpEntity entity = response.getEntity();
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					String line = "";
					StringBuilder total = new StringBuilder();
					BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
					while ((line = rd.readLine()) != null) {
						total.append(line);
					}
					Log.e(TAG, "上传结果返回" + total.toString());
					JSONObject backObj = new JSONObject(total.toString());
					result=backObj.getString("imgurl");
					entity.consumeContent();
					client.getConnectionManager().shutdown();
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
}