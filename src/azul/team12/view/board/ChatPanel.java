package azul.team12.view.board;

import azul.team12.controller.Controller;
import azul.team12.model.events.ChatMessageRemovedEvent;
import azul.team12.model.events.LoggedInEvent;
import azul.team12.model.events.LoginFailedEvent;
import azul.team12.model.events.PlayerAddedMessageEvent;
import azul.team12.network.client.ClientModel;
import azul.team12.network.client.messages.Message;
import azul.team12.view.ChatCellRenderer;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;

import static java.util.Objects.requireNonNull;

public class ChatPanel extends JPanel {
  private static final int DEFAULT_HEIGHT = 500;
  private static final int INPUTFIELD_WIDTH = 40;
  private static final int INPUTFIELD_HEIGHT = 3;
  private JTextArea chatInputArea;
  private JScrollPane scrollPane;

  private final Controller controller;


  private final int defaultInset = 5;

  public ChatPanel(Controller controller){
    this.controller = requireNonNull(controller);

    setLayout(new GridBagLayout());
    initializeWidgets();
    createChatPanel();
    addEventListeners();
  }

  /**
   * Add event listeners to widgets wherever needed and let them execute the respective action.
   */
  private void addEventListeners() {

    chatInputArea.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() != KeyEvent.VK_ENTER) {
          return;
        }

        e.consume();
        controller.postChatMessage(chatInputArea.getText());
        chatInputArea.setText(null);
      }
    });
  }

  private void initializeWidgets() {


    scrollPane = new JScrollPane();
    scrollPane.setPreferredSize(new Dimension(this.getWidth(), DEFAULT_HEIGHT));
    scrollPane.setMaximumSize(new Dimension(this.getWidth(), DEFAULT_HEIGHT));
    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

    chatInputArea = new JTextArea(INPUTFIELD_HEIGHT, INPUTFIELD_WIDTH);
    chatInputArea.setLineWrap(true);
    chatInputArea.setWrapStyleWord(true);
    chatInputArea.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
  }
  private void createChatPanel(){
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(defaultInset, defaultInset, defaultInset, defaultInset);
    gbc.fill = GridBagConstraints.BOTH;
    gbc.gridy = 0;
    gbc.weighty = 0.9;
    this.add(scrollPane, gbc);

    gbc = new GridBagConstraints();
    gbc.insets = new Insets(defaultInset, defaultInset, defaultInset, defaultInset);
    gbc.gridy = 1;
    this.add(chatInputArea, gbc);

  }





}
