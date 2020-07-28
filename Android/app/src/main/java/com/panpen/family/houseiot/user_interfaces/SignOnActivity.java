package com.panpen.family.houseiot.user_interfaces;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;

import com.panpen.family.houseiot.R;
import com.panpen.family.houseiot.backend.DBConnection;
import com.panpen.family.houseiot.utils.User;

import java.util.concurrent.Executors;

public class SignOnActivity extends AppCompatActivity implements View.OnClickListener {
	private static final String APP_ID_PREF = "Home IoT Preference";
	private static final String USER_ID = "User ID";
	private static final String USER_PASS = "User Password";
	
	public static User user;
	private static String username;
	private static String password;
	
	private Context context;
	private EditText usernameEditText;
	private EditText passwordEditText;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		setContentView(R.layout.activity_sign_on);
		context = getApplicationContext();
		
		usernameEditText = findViewById(R.id.username_edit_text);
		passwordEditText = findViewById(R.id.password_edit_text);
		Button logInBtn = findViewById(R.id.login_btn);
		logInBtn.setOnClickListener(this);
		TextView signUpBtn = findViewById(R.id.signup_btn);
		signUpBtn.setOnClickListener(this);
		TextView biometricBtn = findViewById(R.id.biometric);
		biometricBtn.setOnClickListener(this);
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		user = null;
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.biometric:
				SharedPreferences settings = getSharedPreferences(APP_ID_PREF, MODE_PRIVATE);
				username = settings.getString(USER_ID, null);
				password = settings.getString(USER_PASS, null);
				
				if (username != null && password != null) {
					BiometricPrompt biometricPrompt =
							new BiometricPrompt(this, Executors.newSingleThreadExecutor(),
							                    new BiometricCallback());
					
					BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
							.setTitle(getResources().getString(R.string.login))
							.setSubtitle(getResources().getString(R.string.biometric_subtitle, username))
							.setDescription(getResources().getString(R.string.biometric_description))
							.setNegativeButtonText(getResources().getString(R.string.cancel))
							.build();
					
					biometricPrompt.authenticate(promptInfo);
				} else {
					Toast.makeText(context, "Login Failed", Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.login_btn:
				username = usernameEditText.getText().toString();
				password = passwordEditText.getText().toString();
				login();
				break;
			case R.id.signup_btn:
				Intent intent = new Intent(context, SignUpActivity.class);
				startActivity(intent);
				break;
		}
	}
	
	private void login() {
		String command = "{'CMD':'login','USERNAME':'%s','PASSWORD':'%s'}";
		try {
			if (username == null || username.isEmpty()) {
				Toast.makeText(context, "Enter Username", Toast.LENGTH_LONG).show();
			} else if (password == null || password.isEmpty()) {
				Toast.makeText(context, "Enter Password", Toast.LENGTH_LONG).show();
			} else {
				DBConnection connection = DBConnection.getInstance();
				String userInfo = connection.sendCommand(String.format(command, username, password));
				if (userInfo.startsWith("Invalid")) {
					Toast.makeText(context, userInfo, Toast.LENGTH_LONG).show();
				} else {
					String[] info = userInfo.trim().split(",");
					user = new User(info[0], info[1], info[2]);
					
					saveCredentials();
					usernameEditText.setText("");
					passwordEditText.setText("");
					
					Intent intent = new Intent(context, ControllerActivity.class);
					startActivity(intent);
				}
			}
		} catch (Exception e) {
			Toast.makeText(context, "Connection Failed", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void saveCredentials() {
		SharedPreferences settings = context.getSharedPreferences(APP_ID_PREF, MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(USER_ID, username);
		editor.putString(USER_PASS, password);
		editor.apply();
	}
	
	private class BiometricCallback extends BiometricPrompt.AuthenticationCallback {
		@Override
		public void onAuthenticationError(int errorCode, @NonNull final CharSequence errString) {
			super.onAuthenticationError(errorCode, errString);
			Handler handler = new Handler(Looper.getMainLooper());
			handler.post(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(context, errString.toString(), Toast.LENGTH_LONG).show();
				}
			});
		}
		
		@Override
		public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
			super.onAuthenticationSucceeded(result);
			Handler handler = new Handler(getMainLooper());
			handler.post(new Runnable() {
				@Override
				public void run() {
					login();
				}
			});
		}
		
		@Override
		public void onAuthenticationFailed() {
			super.onAuthenticationFailed();
		}
	}
}
