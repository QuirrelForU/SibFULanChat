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
    private JTextField shiftField;
    private JButton updateShiftButton;
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

        saveButton.addActionListener(e -> saveChatToFile());

        shiftField = new JTextField("3", 3); // Default shift value
        updateShiftButton = new JButton("Update Shift");

        updateShiftButton.addActionListener(e -> {
            try {
                int shift = Integer.parseInt(shiftField.getText());
                client.updateShift(shift);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Shift Value", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel shiftPanel = new JPanel();
        shiftPanel.add(new JLabel("Shift:"));
        shiftPanel.add(shiftField);
        shiftPanel.add(updateShiftButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(shiftPanel, BorderLayout.NORTH);
        topPanel.add(new JScrollPane(chatArea), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(inputPanel, BorderLayout.CENTER);
        bottomPanel.add(saveButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.CENTER);
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