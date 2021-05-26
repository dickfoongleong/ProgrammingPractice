package com.lafarleaf.dfplanner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;

import androidx.appcompat.app.AppCompatActivity;

public class LauncherActivity extends AppCompatActivity {
	public static final String LAUNCH = "LAUNCH";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
		intent.putExtra(LAUNCH, true);
		startActivity(intent);
		finish();
	}
}
