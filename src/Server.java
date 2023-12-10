import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;

public class Server {
    private ServerSocket serverSocket;
    private IMessageBroadcaster broadcaster;
    private int maxClients;

    public Server(ServerSocket serverSocket, int maxClients) {
        this.serverSocket = serverSocket;
        this.maxClients = maxClients;
        this.broadcaster = new MessageBroadcaster(maxClients);
    }

    public void startServer() {
        try {
            while (!serverSocket.isClosed()) {
                if (broadcaster.getClientCount() < maxClients) {
                    Socket socket = serverSocket.accept();
                    System.out.println("A new client has connected.");
                    ClientHandler clientHandler = new ClientHandler(socket, broadcaster);
                    Thread thread = new Thread(clientHandler);
                    thread.start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        int maxClients = JOptionPane.showOptionDialog(null, "Select Chat Type", "Chat Server",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                new String[]{"Private (2 users)", "Public (10 users)"}, "Public (10 users)") == 0 ? 2 : 10;

        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket, maxClients);
        server.startServer();
    }
}
