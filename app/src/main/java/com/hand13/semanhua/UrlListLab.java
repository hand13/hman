package com.hand13.semanhua;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hd110 on 2017/7/16.
 */

public class UrlListLab {
	private List<String> strings;
	private Context context;
	private static UrlListLab urlListLab;
	private UrlListLab(Context context){
		this.context=context;
		strings=new ArrayList<>();
		for(int i=1037;i<=1232;i++){
			String s="http://wu.wuyude.com/pic/160914/p-"+i+".jpg";
			strings.add(s);
		}
	}
	public static UrlListLab getUrlListLab(Context context){
		if(urlListLab==null){
			urlListLab=new UrlListLab(context);
		}
		return urlListLab;
	}
	public List<String> getStrings(){
		return strings;
	}
}
