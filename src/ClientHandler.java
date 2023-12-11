import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;

/**
 * Класс `ClientHandler` представляет собой компонент серверной стороны, отвечающий за обработку
 * коммуникации с одним клиентом. Он управляет входными и выходными потоками для сокета клиента,
 * осуществляет рассылку сообщений всем клиентам и обрабатывает отключение клиента.
 */
public class ClientHandler implements Runnable {

    private Socket socket;
    private BufferedReader buffReader;
    private BufferedWriter buffWriter;
    private String clientName;
    private IMessageBroadcaster broadcaster;

    /**
     * Инициализирует новый экземпляр класса `ClientHandler`.
     *
     * @param socket      Сокет клиента для обмена данными.
     * @param broadcaster Диктор для отправки сообщений всем клиентам.
     */
    public ClientHandler(Socket socket, IMessageBroadcaster broadcaster) {
        try {
            this.socket = socket;
            this.buffWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.buffReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientName = buffReader.readLine();
            this.broadcaster = broadcaster;
            broadcaster.addClient(this);
            broadcastMessage("SERVER: " + clientName + " has entered the room");
        } catch (IOException e) {
            closeResources();
        }
    }

    /**
     * Непрерывно прослушивает сообщения от клиента и рассылает их всем клиентам.
     * Закрывает ресурсы и удаляет клиента при возникновении исключения или отключении клиента.
     */
    @Override
    public void run() {
        String messageFromClient;
        while (socket.isConnected()) {
            try {
                messageFromClient = buffReader.readLine();
                broadcastMessage(messageFromClient);
            } catch (IOException e) {
                closeResources();
                break;
            }
        }
    }

    /**
     * Рассылает сообщение всем клиентам в чате.
     *
     * @param messageToSend Сообщение для рассылки.
     */
    public void broadcastMessage(String messageToSend) {
        broadcaster.broadcastMessage(messageToSend, this.clientName);
    }

    /**
     * Закрывает ресурсы и удаляет клиента из списка активных клиентов.
     */
    public void closeResources() {
        broadcaster.removeClient(this);
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

    /**
     * Возвращает имя клиента.
     *
     * @return Имя клиента.
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * Возвращает выходной поток для отправки сообщений клиенту.
     *
     * @return Выходной поток для сообщений клиенту.
     */
    public BufferedWriter getBufferedWriter() {
        return buffWriter;
    }
}
