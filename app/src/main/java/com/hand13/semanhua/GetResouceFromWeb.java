package com.hand13.semanhua;

import android.content.Intent;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by hd110 on 2017/7/16.
 *
 */

public class GetResouceFromWeb {
	public static final String TAG="resources";
	public byte[] getUrlBytes(String urlSpec)throws IOException{
		URL url=new URL(urlSpec);
		HttpURLConnection connection=(HttpURLConnection)url.openConnection();
		try{
			ByteArrayOutputStream out=new ByteArrayOutputStream();
			InputStream inputStream=connection.getInputStream();
			if(connection.getResponseCode()!=HttpURLConnection.HTTP_OK){
				Log.d(TAG,"could nor get your data");
				throw new IOException("could not get your data");
			}
			int byteRead=0;
			byte [] buffer=new byte[1024];
			while((byteRead=inputStream.read(buffer))>0){
				out.write(buffer,0,byteRead);
			}
			Log.d(TAG,""+out.toByteArray().length);
			out.close();
			return out.toByteArray();
		}
		finally {
			connection.disconnect();
		}
	}
}
