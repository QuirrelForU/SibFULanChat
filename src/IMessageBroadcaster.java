
/**
 * Интерфейс `IMessageBroadcaster` определяет методы, которые должны быть реализованы диктором для
 * рассылки сообщений между клиентами чата и управления списком активных клиентов.
 */
public interface IMessageBroadcaster {
    /**
     * Добавляет клиента в список активных клиентов.
     *
     * @param client Клиент, который будет добавлен.
     */
    void addClient(ClientHandler client);

    /**
     * Удаляет клиента из списка активных клиентов.
     *
     * @param client Клиент, который будет удален.
     */
    void removeClient(ClientHandler client);

    /**
     * Рассылает сообщение всем активным клиентам чата.
     *
     * @param message    Сообщение для рассылки.
     * @param senderName Имя отправителя сообщения.
     */
    void broadcastMessage(String message, String senderName);

    /**
     * Возвращает количество активных клиентов в чате.
     *
     * @return Количество активных клиентов.
     */
    int getClientCount();
}
