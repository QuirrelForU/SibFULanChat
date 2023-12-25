import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/**
 * Класс Client управляет подключением к серверу чата, отправкой и получением сообщений.
 */
public class Client {

    private Socket socket;
    private BufferedReader buffReader;
    private BufferedWriter buffWriter;
    private String name;
    private List<ChatObserver> observers = new ArrayList<>();
    private IMessageEncryptor encryptor = new CaesarCipherEncryptor();

    /**
     * Конструктор для создания нового клиента чата.
     *
     * @param socket Сокет для подключения к серверу.
     * @param name   Имя пользователя в чате.
     */
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

    /**
     * Регистрирует наблюдателя для получения уведомлений о сообщениях чата.
     *
     * @param observer Наблюдатель, который будет уведомляться о новых сообщениях.
     */
    public void registerObserver(ChatObserver observer) {
        observers.add(observer);
    }

    /**
     * Оповещает всех зарегистрированных наблюдателей о новом сообщении.
     *
     * @param message Сообщение для отправки наблюдателям.
     */
    private void notifyObservers(String message) {
        for (ChatObserver observer : observers) {
            observer.update(message);
        }
    }

    /**
     * Отправляет сообщение на сервер чата.
     *
     * @param message Сообщение, которое нужно отправить.
     */
    public void sendMessage(String message) {
        try {
            String encryptedMessage = encryptor.encrypt(message);
            String fullMessage = name + ": " + encryptedMessage;
            buffWriter.write(fullMessage);
            buffWriter.newLine();
            buffWriter.flush();

            notifyObservers(name + ": " + message);
        } catch (IOException e) {
            closeResources();
        }
    }


    /**
     * Запускает поток для непрерывного чтения входящих сообщений от сервера.
     */
    public void readMessage() {
        new Thread(() -> {
            String msgFromGroupChat;
            while (socket.isConnected()) {
                try {
                    msgFromGroupChat = buffReader.readLine();
                    String [] parts = msgFromGroupChat.split(": ",2);
                    String name = parts[0];
                    String encryptedMessage = parts[1];
                    String decryptedMessage = encryptor.decrypt(encryptedMessage);
                    String fullMessage = name + ": " + decryptedMessage;
                    notifyObservers(fullMessage);
                } catch (IOException e) {
                    closeResources();
                }
            }
        }).start();
    }

    /**
     * Закрывает сокет и потоки ввода\вывода.
     */
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

    /**
     * Обновляет значение сдвига для шифрования сообщений.
     *
     * @param newShift Новое значение сдвига для шифра Цезаря.
     */
    public void updateShift(int newShift) {
        if (encryptor instanceof CaesarCipherEncryptor) {
            ((CaesarCipherEncryptor) encryptor).setShift(newShift);
        }
    }


    public static void main(String[] args) {
        try {
            String serverIP = JOptionPane.showInputDialog(null, "Enter server IP:", "Chat Client - Server IP", JOptionPane.PLAIN_MESSAGE);
            if (serverIP == null || serverIP.isEmpty()) {
                System.out.println("No server IP entered. Exiting...");
                return;
            }

            String portStr = JOptionPane.showInputDialog(null, "Enter server port:", "Chat Client - Server Port", JOptionPane.PLAIN_MESSAGE);
            if (portStr == null || portStr.isEmpty()) {
                System.out.println("No server port entered. Exiting...");
                return;
            }
            int port = Integer.parseInt(portStr);

            String name = JOptionPane.showInputDialog(null, "Enter your name:", "Chat Client", JOptionPane.PLAIN_MESSAGE);
            if (name == null || name.isEmpty()) {
                System.out.println("No name entered. Exiting...");
                return;
            }

            Socket socket = new Socket(serverIP, port);
            Client client = new Client(socket, name);

            SwingUtilities.invokeLater(() -> new ChatWindow(client));

            client.readMessage();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Invalid port number. Exiting...");
        }
    }

}

