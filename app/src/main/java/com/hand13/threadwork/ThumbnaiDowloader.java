package com.hand13.threadwork;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;

import com.hand13.semanhua.GetResouceFromWeb;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by hd110 on 2017/7/16.
 */

public class ThumbnaiDowloader<T> extends HandlerThread {
	private static final String TAG="ThumbnailDownloader";
	private Boolean hasQuit=false;
	private Handler requestHandler;
	private Handler resposeHandler;
	private ThumbnaiDowloadListener<T> thumbnaiDowloadListener;
	private ConcurrentMap<T,String> requestMap=new ConcurrentHashMap<>();
	private static final int MESSAGE_DOWNLOAD=0;
	public interface ThumbnaiDowloadListener<T>{
		void onThumbnailDownloaded(T target,Bitmap bitmap);
	}
	public void setThumbnaiDowloadListener(ThumbnaiDowloadListener<T> listener){
		thumbnaiDowloadListener=listener;
	}
	public ThumbnaiDowloader(Handler resposeHandler){
		super(TAG);
		this.resposeHandler=resposeHandler;
	}
	@Override
	protected void onLooperPrepared(){
		requestHandler=new Handler(){
			@Override
			public void handleMessage(Message msg){
				if(msg.what==MESSAGE_DOWNLOAD){
					T target=(T)msg.obj;
					handleRequest(target);
				}
			}
		};
	}
	@Override
	public boolean quit(){
		hasQuit=true;
		return super.quit();
	}
	public void queueTumbnail(T target,String url){
		Log.i(TAG,"got a url "+url);
		if(url==null){
			requestMap.remove(target);
		}
		else{
			requestMap.put(target,url);
			requestHandler.obtainMessage(MESSAGE_DOWNLOAD,target).sendToTarget();
			Log.d("TAG","send message");
		}
	}
	private void handleRequest(final T target){
		try{
			final String url=requestMap.get(target);
			if(url==null){
				Log.d("haha","you die");
				return;
			}
			byte[] bitmapBytes=new GetResouceFromWeb().getUrlBytes(url);
			if(bitmapBytes==null){
				Log.d("die","no data");
			}
			final Bitmap bitmap= BitmapFactory.decodeByteArray(bitmapBytes,0,bitmapBytes.length);
			Log.i(TAG,"bitmap create");
			resposeHandler.post(new Runnable() {
				@Override
				public void run() {
					if(requestMap.get(target)!=url||hasQuit){
						return;
					}
					requestMap.remove(target);
					thumbnaiDowloadListener.onThumbnailDownloaded(target,bitmap);
				}
			});
		}
		catch (IOException ioe){
			Log.d(TAG,"no no no no  no ...");
		}
	}
	public void clearQueue(){
		requestHandler.removeMessages(MESSAGE_DOWNLOAD);
	}
}
