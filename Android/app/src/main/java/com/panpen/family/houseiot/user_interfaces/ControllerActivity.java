package com.panpen.family.houseiot.user_interfaces;

import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.panpen.family.houseiot.R;
import com.panpen.family.houseiot.user_interfaces.fragments.ControllerFragment;

public class ControllerActivity extends FragmentContainer {
	@Override
	protected Fragment createFragment() {
		setTitle(R.string.controller);
		return new ControllerFragment();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_logout, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		if (item.getItemId() == R.id.logout_btn) {
			finish();
		}
		
		return super.onOptionsItemSelected(item);
	}
}
