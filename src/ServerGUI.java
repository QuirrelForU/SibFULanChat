
import javax.swing.*;
import java.awt.*;

/**
 * Класс `ServerGUI` представляет собой графический интерфейс (GUI) для отображения статуса сервера.
 * Он создает окно с текстовой областью для отображения сообщений о состоянии сервера.
 */

public class ServerGUI {
    private JFrame frame;
    private JTextArea textArea;

    /**
     * Инициализирует новый экземпляр класса `ServerGUI` и создает окно для отображения статуса сервера.
     */
    public ServerGUI() {
        frame = new JFrame("Server Status");
        textArea = new JTextArea(10, 30);
        textArea.setEditable(false);

        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Обновляет текстовую область в GUI сообщением.
     *
     * @param message Сообщение для отображения.
     */
    public void updateStatus(String message) {
        SwingUtilities.invokeLater(() -> textArea.append(message + "\n"));
    }
}
