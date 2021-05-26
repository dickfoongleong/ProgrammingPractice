package com.lafarleaf.dfplanner.backend;

import android.annotation.SuppressLint;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class ServerConnection {
	public static final String API_ROOT = "http://10.0.0.18:8080/dfplanner";
	
	@SuppressLint("StaticFieldLeak")
	private static ServerConnection instance;
	private RequestQueue queue;
	private final Context context;
	
	private ServerConnection(Context context) {
		this.context = context;
		queue = getRequestQueue();
	}
	
	public static synchronized ServerConnection getInstance(Context context) {
		if (instance == null) {
			instance = new ServerConnection(context);
		}
		return instance;
	}
	
	public RequestQueue getRequestQueue() {
		if (queue == null) {
			queue = Volley.newRequestQueue(context.getApplicationContext());
		}
		
		return queue;
	}
	
	public <T> void addRequestToQueue(Request<T> req) {
		getRequestQueue().add(req);
	}
	
}
