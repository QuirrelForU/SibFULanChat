import javax.swing.*;
import java.awt.*;

public class ChatWindow extends JFrame implements ChatObserver {
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private Client client;

    public ChatWindow(Client client) {
        this.client = client;
        client.registerObserver(this);

        setTitle("Chat Client");
        chatArea = new JTextArea(16, 30);
        chatArea.setEditable(false);
        inputField = new JTextField(20);
        sendButton = new JButton("Send");

        sendButton.addActionListener(e -> {
            String message = inputField.getText();
            client.sendMessage(message);
            inputField.setText("");
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new JScrollPane(chatArea), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(panel, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setVisible(true);
    }

    @Override
    public void update(String message) {
        chatArea.append(message + "\n");
    }
}
