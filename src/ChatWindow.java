import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ChatWindow extends JFrame implements ChatObserver {
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private JButton saveButton;
    private Client client;

    public ChatWindow(Client client) {
        this.client = client;
        client.registerObserver(this);

        setTitle("Chat Client");
        chatArea = new JTextArea(16, 30);
        chatArea.setEditable(false);
        inputField = new JTextField(20);
        sendButton = new JButton("Send");
        saveButton = new JButton("Save Chat");

        sendButton.addActionListener(e -> {
            String message = inputField.getText();
            client.sendMessage(message);
            inputField.setText("");
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveChatToFile();
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new JScrollPane(chatArea), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(inputPanel, BorderLayout.CENTER);
        bottomPanel.add(saveButton, BorderLayout.EAST);

        add(panel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setVisible(true);
    }

    @Override
    public void update(String message) {
        chatArea.append(message + "\n");
    }

    private void saveChatToFile() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Specify a file to save");
            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                    writer.write(chatArea.getText());
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
