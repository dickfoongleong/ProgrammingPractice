import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
	private ServerSocket server;
	private PrintWriter output;
	private BufferedReader input;

	public Server() {
		try {
			System.out.println("Server started...");
			server = new ServerSocket(9000);

			Socket client = server.accept();
			System.out.println(client);
			System.out.println("Client Above Connected");

			output = new PrintWriter(client.getOutputStream(), true);
			input = new BufferedReader(new InputStreamReader(client.getInputStream()));

			Thread inputReader = new Thread(new Runnable() {
				public void run() {
					try {
						msgFromClient();
					} catch (Exception e) {
						System.exit(0);
					}
				}
			});

			Thread outputWriter = new Thread(new Runnable() {
				public void run() {
					try {
						msgToClient();
					} catch (Exception e) {
						System.exit(0);
					}
				}
			});

			inputReader.start();
			outputWriter.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void msgFromClient() throws Exception {
		String line = "";
		while (line != null) {
			line = input.readLine();
			if (line != null) {
				System.out.println("Client: " + line);
			}
		}
		System.exit(0);
	}

	private void msgToClient() throws Exception {
		String line = "";
		Scanner scanner = new Scanner(System.in);
		while (!line.contains("exit")) {
			line = scanner.nextLine();
			if (!line.trim().isEmpty()) {
				output.println(line);
			}
		}
		scanner.close();
		System.exit(0);
	}

	public static void main(String[] args) {
		new Server();
	}
}
