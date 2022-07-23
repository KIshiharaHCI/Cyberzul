package cyberzul.view.board;

import cyberzul.controller.Controller;
import cyberzul.model.events.ChatMessageRemovedEvent;
import cyberzul.model.events.NextPlayersTurnEvent;
import cyberzul.model.events.PlayerAddedMessageEvent;
import cyberzul.model.events.PlayerJoinedChatEvent;
import cyberzul.network.client.messages.Message;
import cyberzul.network.client.messages.NextPlayersTurnMessage;
import cyberzul.network.client.messages.PlayerJoinedChatMessage;
import cyberzul.view.ChatCellRenderer;
import cyberzul.view.IconButton;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serial;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;


/**
 * The Chat Panel at the right side of the GameBoard.
 * */

public class ChatPanel extends JPanel implements PropertyChangeListener {

  public static final DefaultListModel<Message> listModel = new DefaultListModel<>();
  private static final int DEFAULT_HEIGHT = 300;
  private static final int INPUTFIELD_WIDTH = 20;
  private static final int INPUTFIELD_HEIGHT = 3;
  @Serial
  private static final long serialVersionUID = 13L;
  private static final int defaultInset = 5;
  private static final String chaticon = "img/chaticon.png";
  private final transient Controller controller;
  private JTextArea inputArea;
  private JScrollPane scrollPane;
  private IconButton closeChatButton;
  private IconButton openChatButton;
  private JPanel chatButtonPanel;

  /**
   * create a new chat panel with the respective widgets.
   */
  @SuppressFBWarnings("EI_EXPOSE_REP2")
  public ChatPanel(Controller controller) {
    this.controller = controller;
    initializeWidgets();
    createChatPanel();

    addEventListeners();
  }

  /**
   * Instantiate all ChatPanel widgets and specify config options where appropriate.
   */
  private void initializeWidgets() {

    JList<Message> chatList = new JList<>(listModel);
    chatList.setCellRenderer(new ChatCellRenderer());

    setOpaque(false);
    scrollPane = new JScrollPane(chatList);
    chatList.setBackground(new Color(0, 0, 0, 210));
    scrollPane.setPreferredSize(new Dimension(INPUTFIELD_WIDTH, DEFAULT_HEIGHT));
    scrollPane.setMaximumSize(new Dimension(INPUTFIELD_WIDTH, DEFAULT_HEIGHT));
    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setBackground(new Color(0, 0, 0, 210));
    scrollPane.getViewport().setBackground(new Color(0, 0, 0, 210));
    scrollPane.setBorder(
        BorderFactory.createEmptyBorder(defaultInset, defaultInset, defaultInset, defaultInset));

    inputArea = new JTextArea(INPUTFIELD_HEIGHT, INPUTFIELD_WIDTH);
    inputArea.setLineWrap(true);
    inputArea.setWrapStyleWord(true);
    inputArea.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
    inputArea.setFont(new Font("Dialog", Font.BOLD, 14));
    inputArea.setBackground(new Color(0, 0, 0, 210));
    inputArea.setForeground(Color.white);
    inputArea.setBorder(
        BorderFactory.createEmptyBorder(defaultInset, defaultInset, defaultInset, defaultInset));

    chatButtonPanel = new JPanel(null);
    chatButtonPanel.setPreferredSize(new Dimension(150, 25));
    chatButtonPanel.setBackground(new Color(0, 0, 0, 210));

    closeChatButton = new IconButton(chaticon, 5, 5, 40, 22);
    closeChatButton.addActionListener(
        closeEvent -> {
          chatButtonPanel.setOpaque(false);
          scrollPane.setVisible(false);
          inputArea.setVisible(false);
          openChatButton.setVisible(true);
          closeChatButton.setVisible(false);
        });
    chatButtonPanel.add(closeChatButton);

    openChatButton = new IconButton(chaticon, 300, 5, 40, 22);
    openChatButton.setHorizontalAlignment(JLabel.RIGHT);
    openChatButton.setVisible(false);
    openChatButton.addActionListener(
        openEvent -> {
          chatButtonPanel.setOpaque(true);
          scrollPane.setVisible(true);
          inputArea.setVisible(true);
          openChatButton.setVisible(false);
          closeChatButton.setVisible(true);
        });
    chatButtonPanel.add(openChatButton);
  }

  /**
   * Sets the layout and adds the components to "this".
   */
  private void createChatPanel() {
    this.setLayout(new BorderLayout());
    this.add(chatButtonPanel, BorderLayout.NORTH);
    this.add(scrollPane, BorderLayout.CENTER);
    this.add(inputArea, BorderLayout.SOUTH);
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
