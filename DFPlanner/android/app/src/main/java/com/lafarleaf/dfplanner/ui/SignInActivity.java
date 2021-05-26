package com.lafarleaf.dfplanner.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.lafarleaf.dfplanner.R;

import java.util.UUID;
import java.util.concurrent.Executors;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
	private static final String APP_ID = "app_id";
	private static final String USER_ID = "username";
	private static final int REQUEST_EXIT = 1;
	private static final String FINGERPRINT = "fingerprint_enabled";
	
	public static String appID = null;
	public static String username;
	public static boolean isFingerprintEnabled;
	private static BiometricPrompt fingerprintPrompt;
	private static boolean isEnabled;
	
	private Context context;
	private EditText usernameEditText;
	private EditText passwordEditText;
	private ImageView visibilityImageView;
	private SwitchCompat fingerprintSwitch;
	private View loadingLayout;
	private LinearLayout inputLayout;
	private Intent intent;
	private InputMethodManager inputMethodManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (appID == null) {
			generateAppID();
		}
		
		if (getSupportActionBar() != null) {
			getSupportActionBar().hide();
		}
		setContentView(R.layout.activity_sign_in);
		
		context = getApplicationContext();
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		
		loadingLayout = findViewById(R.id.sign_in_loading);
		inputLayout = findViewById(R.id.credentials_input_layout);
//		intent = new Intent(context, SignOnOptionActivity.class);
		
		TextView forgotPasswordTextView = findViewById(R.id.sign_in_forgot_password);
		forgotPasswordTextView.setOnClickListener(this);
		
		TextView registerTextView = findViewById(R.id.sign_in_register);
		registerTextView.setOnClickListener(this);
		
		Button signInBtn = findViewById(R.id.sign_in_btn);
		signInBtn.setOnClickListener(this);
		
		fingerprintSwitch = findViewById(R.id.sign_in_fingerprint_switch);
		
		BiometricManager bioManager = BiometricManager.from(context);
		if (bioManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL) == BiometricManager.BIOMETRIC_SUCCESS) {
			isFingerprintEnabled = isFingerprintEnabled();
			final boolean defaultState = isFingerprintEnabled;
			fingerprintSwitch.setChecked(isFingerprintEnabled);
			fingerprintSwitch.setOnCheckedChangeListener((btn, isChecked) -> {
				if (defaultState) {
					isFingerprintEnabled = isChecked;
					saveFingerprintPreference(context);
				}
			});
			
			if (isFingerprintEnabled) {
				SharedPreferences settings = getSharedPreferences(APP_ID, MODE_PRIVATE);
				username = settings.getString(USER_ID, null);
				
				if (username != null) {
					fingerprintPrompt = new BiometricPrompt(this, Executors.newSingleThreadExecutor(), new BiometricCallback());
					
					BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
							.setTitle(getResources().getString(R.string.sign_in))
							.setSubtitle(getResources().getString(R.string.biometric_subtitle, username))
							.setDescription(getResources().getString(R.string.biometric_description))
							.setNegativeButtonText(getResources().getString(R.string.cancel))
							.build();
					
					fingerprintPrompt.authenticate(promptInfo);
				}
			}
		} else {
			fingerprintSwitch.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onClick(View view) {
		Log.i("Btn_Click", "Clicked........");
		if (isEnabled) {
			isEnabled = false;
			int option = view.getId();
			if (option == R.id.sign_in_btn) {
				if (!usernameEditText.getText().toString().isEmpty() && !passwordEditText.getText().toString().isEmpty()) {
					new Thread(this::verifyInfo).start();
					setLoadingScreen(true);
				}
			} else if (option == R.id.sign_in_forgot_password) {
				// TODO: Forgot password...
				Toast.makeText(context, "Forgot", Toast.LENGTH_LONG).show();
				// TODO: startActivityForResult deprecated...
			} else if (option == R.id.sign_in_register) {
				// TODO: Register new account...
				Toast.makeText(context, "Register", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	private void login() {
		// Update TouchID Enable preference.
		isFingerprintEnabled = fingerprintSwitch.isChecked();
		
		Thread userInitializer = new Thread(() -> {
				// TODO: Load user info...
		});
		userInitializer.start();
		
		saveFingerprintPreference(context);
		saveUsername(context);
		
		// TODO: Start new Activity...
		
		// TODO: startActivityForResult is deprecated...
	}
	
	private void verifyInfo() {
		// Hide the keyboard.
		inputMethodManager.hideSoftInputFromWindow(inputLayout.getWindowToken(), 0);
		
		username = usernameEditText.getText().toString().toUpperCase();
		final String password = passwordEditText.getText().toString();
		
		final String verifyCode = "YES"; // TODO: Code from backend...
		Handler handler = new Handler(getMainLooper());
		handler.post(() -> {
				if (verifyCode == null) {
					setLoadingScreen(false);
					usernameEditText.requestFocus();
				} else if (verifyCode.equals("NO")) {
					setLoadingScreen(false);
					Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show();
					
					passwordEditText.requestFocus();
					inputMethodManager.showSoftInput(passwordEditText, InputMethodManager.SHOW_IMPLICIT);
					if (usernameEditText.getText().toString().equals("")) {
						usernameEditText.requestFocus();
						inputMethodManager.showSoftInput(usernameEditText, InputMethodManager.SHOW_IMPLICIT);
					}
				} else if (verifyCode.equals("INVALID")) { // If the verify code is INVALID, then user's email is not verified.
					setLoadingScreen(false);
					Toast.makeText(context, "Invalid", Toast.LENGTH_LONG).show();
				} else if (verifyCode.equals("YES")) {
					login();
				} else {
					setLoadingScreen(false);
					Toast.makeText(context, "System error.", Toast.LENGTH_LONG).show();
				}
		});
	}
	
	private void setLoadingScreen(boolean isLoading) {
		if (isLoading) {
			loadingLayout.setVisibility(View.VISIBLE);
		} else {
			loadingLayout.setVisibility(View.GONE);
		}
	}
	
	private void generateAppID() {
		SharedPreferences sharedPrefs = getSharedPreferences(APP_ID, MODE_PRIVATE);
		appID = sharedPrefs.getString(APP_ID, null);
		if (appID == null) {
			appID = "ANDROID" + UUID.randomUUID().toString();
			SharedPreferences.Editor editor = sharedPrefs.edit();
			editor.putString(APP_ID, appID);
			editor.apply();
		}
	}
	
	private boolean isFingerprintEnabled() {
		SharedPreferences settings = getSharedPreferences(APP_ID, MODE_PRIVATE);
		return settings.getBoolean(FINGERPRINT, false);
	}
	
	public static void saveFingerprintPreference(Context context) {
		SharedPreferences settings = context.getSharedPreferences(APP_ID, MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(FINGERPRINT, isFingerprintEnabled);
		editor.apply();
	}
	
	public static void saveUsername(Context context) {
		SharedPreferences settings = context.getSharedPreferences(APP_ID, MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(USER_ID, username);
		editor.apply();
	}
	
	private class BiometricCallback extends BiometricPrompt.AuthenticationCallback {
		@Override
		public void onAuthenticationError(int errorCode, @NonNull final CharSequence errString) {
			super.onAuthenticationError(errorCode, errString);
			
			Log.d("BiometricCallback", "Code: " + errorCode + "\tMessage:\n" + errString.toString());
			Handler handler = new Handler(Looper.getMainLooper());
			handler.post(() -> Toast.makeText(context, errString.toString(), Toast.LENGTH_LONG).show());
		}
		
		@Override
		public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
			super.onAuthenticationSucceeded(result);
			
			isEnabled = false;
			
			Handler handler = new Handler(Looper.getMainLooper());
			handler.post(() -> setLoadingScreen(true));
			
			login();
			Log.d("BiometricCallback", "Fingerprint recognized successfully");
		}
		
		@Override
		public void onAuthenticationFailed() {
			super.onAuthenticationFailed();
			
			Handler handler = new Handler(Looper.getMainLooper());
			handler.post(() -> Toast.makeText(context, "Failed...Try again.", Toast.LENGTH_LONG).show());
			Log.d("BiometricCallback", "Fingerprint recognized failed");
		}
	}
}
