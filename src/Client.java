import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.*;

public class Client {

    private Socket socket;
    private BufferedReader buffReader;
    private BufferedWriter buffWriter;
    private String name;
    private List<ChatObserver> observers = new ArrayList<>();

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

    public void registerObserver(ChatObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers(String message) {
        for (ChatObserver observer : observers) {
            observer.update(message);
        }
    }

    public void sendMessage(String message) {
        try {
            String fullMessage = name + ": " + message;
            buffWriter.write(fullMessage);
            buffWriter.newLine();
            buffWriter.flush();

            // Обновляем окно чата с отправленным сообщением
            notifyObservers(fullMessage);
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
                    notifyObservers(msgFromGroupChat);
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


    public static void main(String[] args) {
        try {
            // Запрос имени пользователя через графический интерфейс
            String name = JOptionPane.showInputDialog(null, "Enter your name:", "Chat Client", JOptionPane.PLAIN_MESSAGE);
            if (name == null || name.isEmpty()) {
                System.out.println("No name entered. Exiting...");
                return;
            }

            Socket socket = new Socket("localhost", 1234);
            Client client = new Client(socket, name);

            // Создание GUI для чата
            SwingUtilities.invokeLater(() -> new ChatWindow(client));

            // Запуск потока для чтения сообщений
            client.readMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
