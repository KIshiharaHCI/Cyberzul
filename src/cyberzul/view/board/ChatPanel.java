package cyberzul.view.board;

import cyberzul.controller.Controller;
import cyberzul.model.events.*;
import cyberzul.network.client.messages.Message;
import cyberzul.network.client.messages.NextPlayersTurnMessage;
import cyberzul.network.client.messages.PlayerJoinedChatMessage;
import cyberzul.network.client.messages.PlayerLoggedInMessage;
import cyberzul.view.ChatCellRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ChatPanel extends JPanel implements PropertyChangeListener {

    private static final int DEFAULT_HEIGHT = 500;
    private static final int INPUTFIELD_WIDTH = 40;
    private static final int INPUTFIELD_HEIGHT = 3;
    private static final long serialVersionUID = 13L;
    private static final int defaultInset = 5;
    private final Controller controller;
    private JTextArea inputArea;
    private JScrollPane scrollPane;
    public static DefaultListModel<Message> listModel;

    public ChatPanel(Controller controller) {
        this.controller = controller;

        setLayout(new GridBagLayout());
        initializeWidgets();
        createChatPanel();

        addEventListeners();
    }

    private void initializeWidgets() {
        listModel = new DefaultListModel<>();
        JList<Message> chatList = new JList<>(listModel);
        chatList.setCellRenderer(new ChatCellRenderer());

        scrollPane = new JScrollPane(chatList);
        scrollPane.setPreferredSize(new Dimension(this.getWidth(), DEFAULT_HEIGHT));
        scrollPane.setMaximumSize(new Dimension(this.getWidth(), DEFAULT_HEIGHT));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        inputArea = new JTextArea(INPUTFIELD_HEIGHT, INPUTFIELD_WIDTH);
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputArea.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

    }

    private void createChatPanel() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(defaultInset, defaultInset, defaultInset, defaultInset);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy = 0;
        gbc.weighty = 0.9;
        this.add(scrollPane, gbc);

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(defaultInset, defaultInset, defaultInset, defaultInset);
        gbc.gridy = 1;
        this.add(inputArea, gbc);
    }

    private void addEventListeners() {

        inputArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() != KeyEvent.VK_ENTER) {
                    return;
                }

                e.consume();
                controller.postMessage(inputArea.getText());
                inputArea.setText(null);
            }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        SwingUtilities.invokeLater(() -> handleModelUpdate(propertyChangeEvent));
    }

    private void handleModelUpdate(PropertyChangeEvent event) {
        Object newValue = event.getNewValue();
        if (newValue instanceof ChatMessageRemovedEvent msgRemovedEvent) {
            listModel.removeElement(msgRemovedEvent.getMessage());
        } else if (newValue instanceof PlayerAddedMessageEvent msgAddedEvent) {
            listModel.addElement(msgAddedEvent.getMessage());
        } else if (newValue instanceof PlayerJoinedChatEvent playerJoinedChatEvent) {
            listModel.addElement(new PlayerJoinedChatMessage(playerJoinedChatEvent.getName()));
        } else if (newValue instanceof NextPlayersTurnEvent nextPlayersTurnEvent) {
            listModel.addElement(new NextPlayersTurnMessage(nextPlayersTurnEvent.getName()));
        }

    }



}
