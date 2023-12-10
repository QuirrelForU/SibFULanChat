import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private BufferedReader buffReader;
    private BufferedWriter buffWriter;
    private String name;

    public Client(Socket socket, String name) {
        try {
            this.socket = socket;
            this.buffWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.buffReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.name = name;
        } catch (IOException e) {
            closeResources();
        }
    }

    public void sendMessage() {
        try {
            buffWriter.write(name);
            buffWriter.newLine();
            buffWriter.flush();

            Scanner scanner = new Scanner(System.in);

            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();
                buffWriter.write(name + ": " + messageToSend);
                buffWriter.newLine();
                buffWriter.flush();
            }
        } catch (IOException e) {
            closeResources();
        }
    }

    public void readMessage() {
        new Thread(() -> {
            String msgFromGroupChat;
            while (socket.isConnected()) {
                try {
                    msgFromGroupChat = buffReader.readLine();
                    System.out.println(msgFromGroupChat);
                } catch (IOException e) {
                    closeResources();
                }
            }
        }).start();
    }

    public void closeResources() {
        try {
            if (buffReader != null) {
                buffReader.close();
            }
            if (buffWriter != null) {
                buffWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws UnknownHostException, IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your name:");
        String name = scanner.nextLine();
        Socket socket = new Socket("localhost", 1234);
        Client client = new Client(socket, name);
        client.readMessage();
        client.sendMessage();
    }
}
