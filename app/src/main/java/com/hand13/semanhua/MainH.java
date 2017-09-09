package com.hand13.semanhua;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainH extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_h);
		FragmentManager fragmentManager=getSupportFragmentManager();
		Fragment fragment;
		fragment=fragmentManager.findFragmentById(R.id.fragment_place);
		if(fragment==null){
			fragment=new MainFragment();
			fragmentManager.beginTransaction().add(R.id.fragment_place,fragment).commit();
		}
	}
}
