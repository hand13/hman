package com.hand13.semanhua;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.FloatRange;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.hand13.threadwork.ThumbnaiDowloader;

import java.net.URL;
import java.util.List;

/**
 * Created by hd110 on 2017/7/16.
 */

public class MainFragment extends Fragment {
	public static final String TAG="fragemnt";
	private RecyclerView recyclerView;
	private ThumbnaiDowloader<ImageHolder> thumbnaiDowloader;
	@Override
	public void onCreate(Bundle args){
		super.onCreate(args);
		setRetainInstance(true);
		Handler responseHandler=new Handler();
		thumbnaiDowloader=new ThumbnaiDowloader<>(responseHandler);
		thumbnaiDowloader.setThumbnaiDowloadListener(new ThumbnaiDowloader.ThumbnaiDowloadListener<ImageHolder>() {
			@Override
			public void onThumbnailDownloaded(ImageHolder target, Bitmap bitmap) {
				Drawable drawable=new BitmapDrawable(getResources(),bitmap);
				target.bindImage(drawable);
			}
		});
		thumbnaiDowloader.start();
		thumbnaiDowloader.getLooper();
		Log.i(TAG,"backwork start");
	}
	@Override
	public View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, Bundle args){
		View v=layoutInflater.inflate(R.layout.hfragment_main,parent,false);
		recyclerView=(RecyclerView)v.findViewById(R.id.recycle_layout);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		recyclerView.setAdapter(new ViewAdapter(UrlListLab.getUrlListLab(getActivity()).getStrings()));
		return v;
	}
	private class ImageHolder extends RecyclerView.ViewHolder{
		private ImageView imageView;
		public ImageHolder(View view){
			super(view);
			imageView=(ImageView)view;
		}
		public void bindImage(Drawable drawable){
			imageView.setImageDrawable(drawable);
		}
	}
	private class ViewAdapter extends RecyclerView.Adapter<ImageHolder>{
		private List<String> strings;
		public ViewAdapter(List<String> s){
			strings=s;
		}
		@Override
		public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View v=LayoutInflater.from(getActivity()).inflate(R.layout.image_view,parent,false);
			return new ImageHolder(v);
		}

		@Override
		public void onBindViewHolder(ImageHolder holder, int position) {
			Drawable drawable=getResources().getDrawable(R.drawable.back);
			holder.bindImage(drawable);
			thumbnaiDowloader.queueTumbnail(holder,strings.get(position));
		}

		@Override
		public int getItemCount() {
			return strings.size();
		}
	}
	@Override
	public void onDestroyView(){
		super.onDestroyView();
		thumbnaiDowloader.clearQueue();
	}
	@Override
	public void onDestroy(){
		super.onDestroy();
		thumbnaiDowloader.quit();
	}
}
