import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {

    private Socket socket;
    private BufferedReader buffReader;
    private BufferedWriter buffWriter;
    private String clientName;
    private IMessageBroadcaster broadcaster;

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

    public void broadcastMessage(String messageToSend) {
        broadcaster.broadcastMessage(messageToSend, this.clientName);
    }

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

    public String getClientName() {
        return clientName;
    }

    public BufferedWriter getBufferedWriter() {
        return buffWriter;
    }
}
