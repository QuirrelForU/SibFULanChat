import javax.swing.*;
import java.awt.*;

public class ServerGUI {
    private JFrame frame;
    private JTextArea textArea;

    public ServerGUI() {
        frame = new JFrame("Server Status");
        textArea = new JTextArea(10, 30);
        textArea.setEditable(false);

        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void updateStatus(String message) {
        SwingUtilities.invokeLater(() -> textArea.append(message + "\n"));
    }
}
