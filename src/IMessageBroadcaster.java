public interface IMessageBroadcaster {
    void addClient(ClientHandler client);
    void removeClient(ClientHandler client);
    void broadcastMessage(String message, String senderName);
    int getClientCount();
}
