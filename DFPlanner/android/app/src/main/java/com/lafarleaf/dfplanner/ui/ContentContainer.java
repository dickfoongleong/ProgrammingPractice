package com.lafarleaf.dfplanner.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.lafarleaf.dfplanner.R;

public abstract class ContentContainer extends AppCompatActivity {
	protected abstract Fragment createFragment();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.content_container);
		
		// Initialize main fragment container.
		FragmentManager fragmentManager = getSupportFragmentManager();
		Fragment fragment = fragmentManager.findFragmentById(R.id.content_container);
		
		// If fragment is null, then create and add a new fragment to the container.
		if (fragment == null) {
			fragment = createFragment();
			fragmentManager.beginTransaction().add(R.id.content_container, fragment).commit();
		}
	}
	
	protected void logout() {
		Intent intent = new Intent(ContentContainer.this, SignInActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
	}
}
