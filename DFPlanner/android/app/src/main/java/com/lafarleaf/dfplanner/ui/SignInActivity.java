package com.lafarleaf.dfplanner.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.lafarleaf.dfplanner.R;
import com.lafarleaf.dfplanner.backend.ServerConnection;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
	private static final String APP_ID = "app_id";
	private static final String USERNAME = "username";
	private static final String FINGERPRINT = "fingerprint_enabled";
	
	private static String username;
	
	private boolean isFingerprintEnabled;
	private boolean isEnabled;
	private String appID = null;
	private Context context;
	private EditText usernameEditText;
	private EditText passwordEditText;
	private SwitchCompat fingerprintSwitch;
	private View loadingLayout;
	private LinearLayout inputLayout;
	private InputMethodManager inputMethodManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (appID == null) {
			generateAppID();
		}
		
		setContentView(R.layout.activity_sign_in);
		
		context = getApplicationContext();
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		
		loadingLayout = findViewById(R.id.sign_in_loading);
		inputLayout = findViewById(R.id.credentials_input_layout);
		
		usernameEditText = findViewById(R.id.sign_in_username);
		
		passwordEditText = findViewById(R.id.sign_in_password);
		passwordEditText.setText("");
		passwordEditText.setOnEditorActionListener(new SignInEditorActionListener());
		
		Button signInBtn = findViewById(R.id.sign_in_btn);
		signInBtn.setOnClickListener(this);
		
		fingerprintSwitch = findViewById(R.id.sign_in_fingerprint_switch);
		
		boolean isLaunched = getIntent().getBooleanExtra(LauncherActivity.LAUNCH, false);
		if (isLaunched) {
			startFingerprint();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		setLoadingScreen(false);
		isEnabled = true;
	}
	
	@Override
	public void onClick(View view) {
		if (isEnabled) {
			isEnabled = false;
			int option = view.getId();
			if (option == R.id.sign_in_btn) {
				if (isCredentialFilled()) {
					new Thread(this::verifyInfo).start();
					setLoadingScreen(true);
				} else {
					showAlert("Please fill in Username and Password.");
				}
			}
		}
	}
	
	private void signIn() {
		isFingerprintEnabled = fingerprintSwitch.isChecked();
		saveFingerprintPreference(context);
		saveUsername(context);
		
		Intent intent = new Intent(context, HomeContentActivity.class);
		intent.putExtra("username", username);
		startActivity(intent);
	}
	
	private void verifyInfo() {
		// Hide the keyboard.
		inputMethodManager.hideSoftInputFromWindow(inputLayout.getWindowToken(), 0);
		
		username = usernameEditText.getText().toString().toUpperCase();
		String password = passwordEditText.getText().toString();
		
		String url = ServerConnection.API_ROOT + "/setup/login";
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("username", username);
		paramMap.put("password", password);
		JSONObject params = new JSONObject(paramMap);
		
		JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params, response -> {
			boolean isPassed = response.optBoolean("result");
	          new Handler(Looper.getMainLooper()).post(() -> {
	          	if (isPassed) {
	          		signIn();
	            } else {
	          		String message = response.optString("message");
	          		switch (message) {
			            case "Inactive user.":
				            showAlert("Activate your account.");
				            break;
			            case "Incorrect password.":
				            showAlert("Username and password not matched.");
				            break;
			            default:
			            	if (message.matches("Active username .* not found")) {
			            		showAlert("Username not found.");
				            } else {
					            showAlert("Service not available. Try again later.");
				            }
		            }
	            }
	          });
		  }, error -> {
			new Handler(Looper.getMainLooper()).post(() -> showAlert("Service not available. Try again later."));
			Log.d("sign_in_conn", error.getMessage());
	      });
		ServerConnection.getInstance(this).addRequestToQueue(request);
	}
	
	private void setLoadingScreen(boolean isLoading) {
		if (isLoading) {
			loadingLayout.setVisibility(View.VISIBLE);
		} else {
			loadingLayout.setVisibility(View.GONE);
		}
	}
	
	private void showAlert(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msg)
				.setCancelable(false)
				.setPositiveButton(R.string.ok, (dialog, id) -> {
					setLoadingScreen(false);
					isEnabled = true;
				});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private boolean isCredentialFilled() {
		return !usernameEditText.getText().toString().isEmpty() &&
				!passwordEditText.getText().toString().isEmpty();
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
	
	private void startFingerprint() {
		BiometricManager bioManager = BiometricManager.from(context);
		if (bioManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK | BiometricManager.Authenticators.DEVICE_CREDENTIAL) == BiometricManager.BIOMETRIC_SUCCESS) {
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
				username = settings.getString(USERNAME, null);
				
				if (username != null) {
					BiometricPrompt fingerprintPrompt =
							new BiometricPrompt(this, Executors.newSingleThreadExecutor(),
							                    new BiometricCallback());
					
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
	
	private boolean isFingerprintEnabled() {
		SharedPreferences settings = getSharedPreferences(APP_ID, MODE_PRIVATE);
		return settings.getBoolean(FINGERPRINT, false);
	}
	
	private void saveFingerprintPreference(Context context) {
		SharedPreferences settings = context.getSharedPreferences(APP_ID, MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(FINGERPRINT, isFingerprintEnabled);
		editor.apply();
	}
	
	private void saveUsername(Context context) {
		SharedPreferences settings = context.getSharedPreferences(APP_ID, MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(USERNAME, username);
		editor.apply();
	}
	
	private class BiometricCallback extends BiometricPrompt.AuthenticationCallback {
		@Override
		public void onAuthenticationError(int errorCode, @NonNull final CharSequence errString) {
			super.onAuthenticationError(errorCode, errString);
			Log.d("BiometricCallback", "Code: " + errorCode + "\tMessage:\n" + errString.toString());
		}
		
		@Override
		public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
			super.onAuthenticationSucceeded(result);
			
			isEnabled = false;
			
			Handler handler = new Handler(Looper.getMainLooper());
			handler.post(() -> {
				setLoadingScreen(true);
				signIn();
			});
			Log.d("BiometricCallback", "Fingerprint recognized successfully");
		}
		
		@Override
		public void onAuthenticationFailed() {
			super.onAuthenticationFailed();
			
			Log.d("BiometricCallback", "Fingerprint recognized failed");
		}
	}
	
	private class SignInEditorActionListener implements TextView.OnEditorActionListener {
		
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (actionId == EditorInfo.IME_ACTION_DONE) {
				if (isCredentialFilled()) {
					new Thread(SignInActivity.this::verifyInfo).start();
					setLoadingScreen(true);
				} else {
					showAlert("Please fill in Username and Password.");
				}
				return true;
			}
			
			return false;
		}
	}
}
