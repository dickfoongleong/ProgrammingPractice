import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
  private static final List<Socket> CLIENT_LIST = new ArrayList<Socket>();
  private static final List<String> USER_LIST = new ArrayList<String>();

  private ServerSocket server;

  public Server() {
    try {
      System.out.println("Server started...");
      server = new ServerSocket(9000);

      while (true) {
        Socket client = server.accept();
        System.out.println(client);
        System.out.println("Client Above Connected");

        setupClient(client);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void setupClient(Socket client) {
    try {
      CLIENT_LIST.add(client);

      final BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
      final String usr = input.readLine();
      USER_LIST.add(usr);
      printAllUser();
      
      Thread inputReader = new Thread(new Runnable() {
        public void run() {
          try {
              msgFromClient(input, usr, client);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      });

      inputReader.start();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private void msgFromClient(BufferedReader inputReader, String usr, Socket socket) throws Exception {
    String line = "";
    while (line != null) {
      line = inputReader.readLine();
      if (line != null) {
        msgToAllClient(usr + ": " + line);
      }
    }
    msgToAllClient("****** " + usr + " has left ******");
    USER_LIST.remove(usr);
    CLIENT_LIST.remove(socket);
  }

  private void msgToAllClient(String msg) throws Exception {
    System.out.println("MSG: " + msg);
    for (Socket client : CLIENT_LIST) {
      PrintWriter outputWriter = new PrintWriter(client.getOutputStream(), true);
      outputWriter.println(msg);
    }
  }
  
  private void printAllUser() throws Exception {
    for (String user : USER_LIST) {
      msgToAllClient("****** " + user + " has joined ******");
    }
  }

  public static void main(String[] args) {
    new Server();
  }
}
