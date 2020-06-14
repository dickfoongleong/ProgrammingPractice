import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	private Socket socket;
	private PrintWriter output;
	private BufferedReader input;

	public Client() {
		try {
			String ip = "10.0.0.118";
			socket = new Socket(ip, 9000);

			output = new PrintWriter(socket.getOutputStream(), true);
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			Thread inputReader = new Thread(new Runnable() {
				public void run() {
					try {
						msgFromServer();
					} catch (Exception e) {
						System.exit(0);
					}
				}
			});

			Thread outputWriter = new Thread(new Runnable() {
				public void run() {
					try {
						msgToServer();
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

	private void msgFromServer() throws Exception {
		String line = "";
		while ((line = input.readLine()) != null) {
			System.out.println("Server: " + line);
		}
		System.exit(0);
	}

	private void msgToServer() throws Exception {
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
		new Client();
	}
}
