package com.whitefamily.web.bean.ajax;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class PaymentReaderUtil {
	
	public static byte[] readGet(String url) {
		InputStream in = null;
		CloseableHttpResponse resp = null;
		
		int pos = 0;
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet(url);
			resp = httpclient.execute(httpGet);
			HttpEntity entity1 = resp.getEntity();
			in = entity1.getContent();
			byte[] data = new byte[1024];
			byte[] buf = new byte[100];
			int n = 0;
			while ((n = in.read(buf)) > 0) {
				if (n + pos > data.length) {
					byte[] newData = new byte[data.length * 2];
					System.arraycopy(data, 0, newData, 0, pos);
					data = newData;
				}
				System.arraycopy(buf, 0, data, pos, n);
				pos += n;
			}
			
			return data;
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (resp != null) {
				try {
					resp.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}
	
	
	
	public static byte[] readPost(String url, byte[] rdata) {
		InputStream in = null;
		CloseableHttpResponse resp = null;
		
		int pos = 0;
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httppost = new HttpPost(url);
			HttpEntity ren = new ByteArrayEntity(rdata);
			httppost.setEntity(ren);
			resp = httpclient.execute(httppost);
			HttpEntity entity1 = resp.getEntity();
			in = entity1.getContent();
			byte[] data = new byte[1024];
			byte[] buf = new byte[100];
			int n = 0;
			while ((n = in.read(buf)) > 0) {
				if (n + pos > data.length) {
					byte[] newData = new byte[data.length * 2];
					System.arraycopy(data, 0, newData, 0, pos);
					data = newData;
				}
				System.arraycopy(buf, 0, data, pos, n);
				pos += n;
			}
			
			return data;
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (resp != null) {
				try {
					resp.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}

}
