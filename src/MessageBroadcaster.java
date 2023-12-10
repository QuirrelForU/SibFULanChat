import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MessageBroadcaster implements IMessageBroadcaster {

    private List<ClientHandler> clientHandlers = new ArrayList<>();
    private final int maxClients;

    public MessageBroadcaster(int maxClients) {
        this.maxClients = maxClients;
    }

    @Override
    public void addClient(ClientHandler client) {
        if (clientHandlers.size() < maxClients) {
            clientHandlers.add(client);
            broadcastMessage("SERVER: " + client.getClientName() + " has entered the chat", null);
        } else {
            System.out.println("Maximum number of clients reached. Cannot add more clients.");
        }
    }

    @Override
    public void removeClient(ClientHandler client) {
        clientHandlers.remove(client);
        broadcastMessage("SERVER: " + client.getClientName() + " has left the chat", null);
    }

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

    public int getClientCount() {
        return clientHandlers.size();
    }
}
