package com.panpen.family.houseiot.user_interfaces.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.panpen.family.houseiot.R;
import com.panpen.family.houseiot.backend.DBConnection;

public class SignUpFragment extends Fragment implements View.OnClickListener {
	private EditText usernameEditText;
	private EditText passwordEditText;
	private EditText conPasswordEditText;
	private EditText firstNameEditText;
	private EditText lastNameEditText;
	private EditText emailEditText;
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View signUpView = inflater.inflate(R.layout.fragment_sign_up, container, false);
		
		usernameEditText = signUpView.findViewById(R.id.username_input_text);
		passwordEditText = signUpView.findViewById(R.id.password_input_text);
		conPasswordEditText = signUpView.findViewById(R.id.password_confirm_input_text);
		firstNameEditText = signUpView.findViewById(R.id.first_name_input_text);
		lastNameEditText = signUpView.findViewById(R.id.last_name_input_text);
		emailEditText = signUpView.findViewById(R.id.email_input_text);
		
		Button signUpBtn = signUpView.findViewById(R.id.sign_up_btn);
		signUpBtn.setOnClickListener(this);
		
		return signUpView;
	}
	
	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.sign_up_btn) {
			signUp();
		}
	}
	
	private void signUp() {
		String username = usernameEditText.getText().toString();
		String password = passwordEditText.getText().toString();
		String conPass = conPasswordEditText.getText().toString();
		String fName = firstNameEditText.getText().toString();
		String lName = lastNameEditText.getText().toString();
		String email = emailEditText.getText().toString();
		
		if (username.isEmpty() || password.isEmpty() || conPass.isEmpty()
			|| fName.isEmpty() || lName.isEmpty() || email.isEmpty()) {
			Toast.makeText(getContext(), "Missing Information", Toast.LENGTH_LONG).show();
		} else if (!password.equals(conPass)) {
			Toast.makeText(getContext(), "Password Not Confirmed", Toast.LENGTH_LONG).show();
		} else {
			String cmdFormat = "{'CMD':'create','USERNAME':'%s','PASSWORD':'%s','FIRST_NAME':'%s','LAST_NAME':'%s','EMAIL':'%s'}";
			try {
				String command = String.format(cmdFormat, username, password, fName, lName, email);
				
				String result = DBConnection.getInstance().sendCommand(command);
				Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
				
				if (result.equals("User created")) {
					FragmentActivity parentActivity = getActivity();
					if (parentActivity != null) {
						parentActivity.finish();
					}
				}
			} catch (Exception e) {
				Toast.makeText(getContext(), "Connection Failed", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
