import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс `MessageBroadcaster` реализует интерфейс `IMessageBroadcaster` и отвечает за управление
 * списком активных клиентов и рассылку сообщений между ними.
 */
public class MessageBroadcaster implements IMessageBroadcaster {

    private List<ClientHandler> clientHandlers = new ArrayList<>();
    private final int maxClients;

    /**
     * Инициализирует новый экземпляр класса `MessageBroadcaster` с максимальным количеством клиентов.
     *
     * @param maxClients Максимальное количество клиентов, которые могут быть подключены.
     */
    public MessageBroadcaster(int maxClients) {
        this.maxClients = maxClients;
    }

    /**
     * Добавляет клиента в список активных клиентов, если не достигнут лимит.
     * Отправляет сообщение о входе нового клиента в чат.
     *
     * @param client Клиент, который будет добавлен.
     */
    @Override
    public void addClient(ClientHandler client) {
        if (clientHandlers.size() < maxClients) {
            clientHandlers.add(client);
            broadcastMessage("SERVER: " + client.getClientName() + " has entered the chat", null);
        } else {
            System.out.println("Maximum number of clients reached. Cannot add more clients.");
        }
    }

    /**
     * Удаляет клиента из списка активных клиентов и отправляет сообщение о выходе клиента из чата.
     *
     * @param client Клиент, который будет удален.
     */
    @Override
    public void removeClient(ClientHandler client) {
        clientHandlers.remove(client);
        broadcastMessage("SERVER: " + client.getClientName() + " has left the chat", null);
    }

    /**
     * Рассылает сообщение всем активным клиентам в чате, исключая отправителя.
     *
     * @param message    Сообщение для рассылки.
     * @param senderName Имя отправителя сообщения.
     */
    @Override
    public void broadcastMessage(String message, String senderName) {
        for (ClientHandler client : clientHandlers) {
            try {
                if (!client.getClientName().equals(senderName)) {
                    BufferedWriter buffWriter = client.getBufferedWriter();
                    buffWriter.write(message);
                    buffWriter.newLine();
                    buffWriter.flush();
                }
            } catch (IOException e) {
                client.closeResources();
            }
        }
    }

    /**
     * Возвращает текущее количество активных клиентов в чате.
     *
     * @return Количество активных клиентов.
     */
    public int getClientCount() {
        return clientHandlers.size();
    }
}
