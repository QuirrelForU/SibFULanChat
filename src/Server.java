import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 * Класс `Server` представляет собой сервер для обработки клиентских подключений в чате.
 * Он создает сокет для прослушивания клиентских подключений, управляет максимальным количеством
 * клиентов и взаимодействует с GUI сервера.
 */
public class Server {
    private ServerSocket serverSocket;
    private IMessageBroadcaster broadcaster;
    private int maxClients;
    private ServerGUI serverGUI;  // Добавляем ссылку на GUI

    /**
     * Инициализирует новый экземпляр класса `Server`.
     *
     * @param serverSocket Серверный сокет для прослушивания клиентских подключений.
     * @param maxClients   Максимальное количество клиентов, которые могут подключиться к серверу.
     */
    public Server(ServerSocket serverSocket, int maxClients) {
        this.serverSocket = serverSocket;
        this.maxClients = maxClients;
        this.broadcaster = new MessageBroadcaster(maxClients);
        this.serverGUI = new ServerGUI();
        displayServerInfo();
    }

    /**
     * Отображает информацию о сервере в GUI, включая адрес и порт сервера.
     */
    private void displayServerInfo() {
        try {
            String hostAddress = InetAddress.getLocalHost().getHostAddress(); // Получаем локальный IP-адрес
            int port = serverSocket.getLocalPort();
            serverGUI.updateStatus("Server running at " + hostAddress + ":" + port);
        } catch (Exception e) {
            serverGUI.updateStatus("Error retrieving server info: " + e.getMessage());
        }
    }

    /**
     * Запускает сервер и ожидает подключения клиентов. Создает обработчики клиентов и запускает их в отдельных потоках.
     */
    public void startServer() {
        try {
            while (!serverSocket.isClosed()) {
                if (broadcaster.getClientCount() < maxClients) {
                    Socket socket = serverSocket.accept();
                    serverGUI.updateStatus("New client connected: " + socket.getRemoteSocketAddress());
                    ClientHandler clientHandler = new ClientHandler(socket, broadcaster);
                    Thread thread = new Thread(clientHandler);
                    thread.start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            serverGUI.updateStatus("Server error: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        int option = JOptionPane.showOptionDialog(null, "Select Chat Type", "Chat Server",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                new String[]{"Private (2 users)", "Public (10 users)"}, "Public (10 users)");

        if (option == -1) {
            System.out.println("No option selected, server will not start.");
            return;
        }

        int maxClients = option == 0 ? 2 : 10;

        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket, maxClients);
        server.startServer();
    }
}
