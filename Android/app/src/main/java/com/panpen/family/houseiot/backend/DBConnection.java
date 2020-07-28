package com.panpen.family.houseiot.backend;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class DBConnection {
	private static final DBConnection instance = new DBConnection();
	private static final String SERVER_IP = "10.0.0.18";
	private static final int SERVER_PORT = 8866;
	
	private PrintWriter output;
	private BufferedReader input;
	
	private DBConnection() {
		try {
			Socket socket = new Socket(SERVER_IP, SERVER_PORT);
			output = new PrintWriter(socket.getOutputStream(), true);
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static DBConnection getInstance() {
		return instance;
	}
	
	public String sendCommand(String command) throws Exception {
		output.println(command);
		return input.readLine();
	}
}
