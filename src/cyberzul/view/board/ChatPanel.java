package cyberzul.view.board;

import cyberzul.view.IconButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serial;

/** The Chat Panel at the right side of the GameBoard. */
public class ChatPanel extends JPanel implements PropertyChangeListener {

  private static final int DEFAULT_HEIGHT = 300;
  private static final int INPUTFIELD_WIDTH = 20;
  private static final int INPUTFIELD_HEIGHT = 3;
  @Serial private static final long serialVersionUID = 13L;
  private static final int defaultInset = 5;
  private JTextArea inputArea;
  private JScrollPane scrollPane;
  private static final String chaticon = "img/chaticon.png";
  private IconButton closeChatButton;
  private IconButton openChatButton;
  private JPanel chatButtonPanel;

  /** create a new chat panel with the respective widgets. */
  public ChatPanel() {
    initializeWidgets();
    createChatPanel();
  }

  /** Instantiate all ChatPanel widgets and specify config options where appropriate. */
  private void initializeWidgets() {
    setOpaque(false);
    scrollPane = new JScrollPane();
    scrollPane.setPreferredSize(new Dimension(INPUTFIELD_WIDTH, DEFAULT_HEIGHT));
    scrollPane.setMaximumSize(new Dimension(INPUTFIELD_WIDTH, DEFAULT_HEIGHT));
    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setBackground(Color.DARK_GRAY);
    scrollPane.getViewport().setBackground(new Color(54, 51, 51));
    scrollPane.setBorder(
        BorderFactory.createEmptyBorder(defaultInset, defaultInset, defaultInset, defaultInset));

    inputArea = new JTextArea(INPUTFIELD_HEIGHT, INPUTFIELD_WIDTH);
    inputArea.setLineWrap(true);
    inputArea.setWrapStyleWord(true);
    inputArea.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
    inputArea.setFont(new Font("Dialog", Font.BOLD, 14));
    inputArea.setBackground(Color.black);
    inputArea.setForeground(Color.white);
    inputArea.setBorder(
        BorderFactory.createEmptyBorder(defaultInset, defaultInset, defaultInset, defaultInset));

    chatButtonPanel = new JPanel(null);
    chatButtonPanel.setPreferredSize(new Dimension(150, 25));
    chatButtonPanel.setBackground(Color.DARK_GRAY);

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

  @Override
  public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
    SwingUtilities.invokeLater(() -> handleModelUpdate(propertyChangeEvent));
  }

  private void handleModelUpdate(PropertyChangeEvent event) {}
}
