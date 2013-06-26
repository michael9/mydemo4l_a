package com.cqvip.moblelib.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.util.Log;

/**
 * 访问网络
 * @author luojiang
 *
 */
public class HttpClientNewWork {

	
	private static HttpClient client;
	
	
	public static final String HTTPMETHOD_POST = "POST";
	public static final String HTTPMETHOD_GET = "GET";
	
	private static final int CONNECTION_TIMEOUT = 10000;
	private static final int SOCKET_TIMEOUT = 20 * 1000;
	/**
	 * 网络请求
	 * @param url 服务器url
	 * @param method GET 或者  POST
	 * @param params 请求参数
	 * @param file  文件路径
	 * @return 响应结果
	 * @throws BookException 
	 */
	public String requestUrl(String url, String method, BookParameters params) throws BookException{
		String result = "";
		try {
			
			Log.i("mobile","=======method========="+method);
		HttpClient client = getHttpclient();
		HttpUriRequest request = null;
		ByteArrayOutputStream bos = null;
		if(method.equals(HTTPMETHOD_GET)){
			Log.i("mobile","=======GET=========");
			if(params!=null){
				url = url +"?"+NetUtil.encodeUrl(params);
			}
			HttpGet get = new HttpGet(url);
			request = get;
		}else if(method.equals(HTTPMETHOD_POST)){
			Log.i("mobile","=======POST=========");
			HttpPost post = new HttpPost(url);
			
			byte[] data = null;
			bos = new ByteArrayOutputStream();
//			if(!TextUtils.isEmpty(file)){
//			//上传图片
//				
//			}
			post.setHeader("Content-Type", "application/x-www-form-urlencoded");
			String postParam = NetUtil.encodeParameters(params);
			data = postParam.getBytes();
//			bos.write(data);
//			data = bos.toByteArray();
			bos.close();
			ByteArrayEntity formEntity = new ByteArrayEntity(data);
			post.setEntity(formEntity);
			request = post;
		}
		    HttpResponse response;
			response = client.execute(request);
		    StatusLine status = response.getStatusLine();
		if(status.getStatusCode()!=200){
		   result = readHttpResponse(response);
		   throw new BookException(result, status.getStatusCode()); 
		}
		result = readHttpResponse(response);
		return result;
		} catch (IOException e) {
			throw new BookException(e);
		} 
	}


	/**
	 * 设置超时
	 * @return
	 */
	private static synchronized HttpClient getHttpclient() {
		if(client == null){
		HttpParams params = new BasicHttpParams();

//		HttpConnectionParams.setConnectionTimeout(params, 10000);
//		HttpConnectionParams.setSoTimeout(params, 10000);

		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

		SchemeRegistry registry = new SchemeRegistry();
		//支持http
		registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		//支持https
//		registry.register(new Scheme("https", sf, 443));

		ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
		//设置连接和响应超时
		HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, SOCKET_TIMEOUT);
		 client = new DefaultHttpClient(ccm, params);
		
		}
		return client;
		
	}
	
	
	
	/**
	 * 读取HttpResponse数据
	 * 
	 * @param response
	 * @return
	 */
	private static String readHttpResponse(HttpResponse response) {
		String result = "";
		HttpEntity entity = response.getEntity();
		InputStream inputStream;
		try {
			inputStream = entity.getContent();
			ByteArrayOutputStream content = new ByteArrayOutputStream();

			int readBytes = 0;
			byte[] sBuffer = new byte[512];
			
			while ((readBytes = inputStream.read(sBuffer)) != -1) {
				content.write(sBuffer, 0, readBytes);
			}
			result = new String(content.toByteArray());
			return result;
		} catch (IllegalStateException e) {
		} catch (IOException e) {
		}
		return result;
	}
	
}
