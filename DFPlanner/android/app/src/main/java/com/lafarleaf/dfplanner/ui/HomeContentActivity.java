package com.lafarleaf.dfplanner.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.lafarleaf.dfplanner.ui.fragments.HomeFragment;

public class HomeContentActivity extends ContentContainer {
	
	@Override
	protected Fragment createFragment() {
		try {
			Fragment homeFragment = new HomeFragment();
			Bundle args = new Bundle();
			args.putString("username", getIntent().getStringExtra("username"));
			homeFragment.setArguments(args);
			return homeFragment;
		} catch (Exception e) {
			Log.e("Home_Content", "Failed to start.", e);
			logout();
			return null;
		}
	}
}
