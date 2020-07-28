package com.panpen.family.houseiot.user_interfaces.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.panpen.family.houseiot.R;
import com.panpen.family.houseiot.backend.DBConnection;
import com.panpen.family.houseiot.user_interfaces.SignOnActivity;
import com.panpen.family.houseiot.utils.User;

public class ControllerFragment extends Fragment implements View.OnClickListener {
	
	private static User user;
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		View controllerView = inflater.inflate(R.layout.fragment_controller, container, false);
		
		user = SignOnActivity.user;
		
		Button onBtn = controllerView.findViewById(R.id.on_btn);
		onBtn.setOnClickListener(this);
		
		Button offBtn = controllerView.findViewById(R.id.off_btn);
		offBtn.setOnClickListener(this);
		
		return controllerView;
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.on_btn:
				runOperation("Lights On");
				break;
			case R.id.off_btn:
				runOperation("Lights Off");
				break;
		}
	}
	
	private void runOperation(String op) {
		String cmdFormat = "{'CMD':'run','ID':'%s','OP':'%s'}";
		try {
			DBConnection.getInstance().sendCommand(String.format(cmdFormat, user.getID(), op));
			Toast.makeText(getContext(), op, Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(getContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
		}
	}
}
