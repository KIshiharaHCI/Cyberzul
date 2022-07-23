package cyberzul.view;

import cyberzul.network.client.messages.Message;
import cyberzul.network.client.messages.PlayerForfeitedMessage;
import cyberzul.network.client.messages.PlayerJoinedChatMessage;
import cyberzul.network.client.messages.PlayerLoggedInMessage;
import cyberzul.network.client.messages.PlayerNeedHelpMessage;
import cyberzul.network.client.messages.PlayerTextMessage;
import java.awt.Component;
import java.io.Serial;
import java.text.DateFormat;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;

/**
 * The Cellrenderer that show the Backgrounds for each chat messages, depending on their value.
 */
public class ChatCellRenderer extends JTextArea implements ListCellRenderer<Message> {
  @Serial
  private static final long serialVersionUID = 1L;

  private final DateFormat dateFormat;

  /**
   * Constructor of the CellRenderer for chat messages.
   */
  public ChatCellRenderer() {
    super();
    setOpaque(true);
    dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
  }

  @Override
  public Component getListCellRendererComponent(JList<? extends Message> list,
                                                Message value, int index, boolean isSelected,
                                                boolean cellHasFocus) {


    setBackground(list.getBackground());
    setForeground(list.getForeground());

    setWrapStyleWord(true);

    if (value instanceof PlayerLoggedInMessage playerLoggedInMsg) {
      setText("Chat joined as " + playerLoggedInMsg.getNickname() + ".");
    }
    if (value instanceof PlayerJoinedChatMessage playerJoinedMsg) {
      setText(playerJoinedMsg.getNickname() + " has joined the chat.");
    }
    if (value instanceof PlayerTextMessage playerTextMsg) {
      String time = dateFormat.format(playerTextMsg.getTime());
      setText(String.format("%s (%s): %s", playerTextMsg.getNameOfSender(), time,
          playerTextMsg.getContent()));
    }
    if (value instanceof PlayerForfeitedMessage playerLeftMsg) {
      setText(playerLeftMsg.getNickname() + " has left the chat.");
    }
    if (value instanceof PlayerNeedHelpMessage playerNeedHelpMessage) {
      setText(playerNeedHelpMessage.getContent());
    }

    return this;
  }

}
