package com.panpen.family.houseiot.user_interfaces;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.panpen.family.houseiot.R;

public abstract class FragmentContainer extends AppCompatActivity {
	protected abstract Fragment createFragment();
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.container_fragment);
		FragmentManager fragmentManager = getSupportFragmentManager();
		Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
		if (fragment == null) {
			fragment = createFragment();
			fragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit();
		}
	}
}
