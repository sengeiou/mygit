package com.beanu.l3_gensee.playerdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.beanu.l3_gensee.R;
import com.beanu.l3_gensee.voddemo.VodActivity;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_gensee);
		
		findViewById(R.id.live_btn).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, PlayerDemoActivity.class));
			}
		});
		
		findViewById(R.id.vod_btn).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, VodActivity.class));
			}
		});
	}
}
