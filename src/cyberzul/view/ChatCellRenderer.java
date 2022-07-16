package cyberzul.view;

import cyberzul.network.client.messages.*;

import java.awt.Component;
import java.io.Serial;
import java.text.DateFormat;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;

public class ChatCellRenderer extends JTextArea implements ListCellRenderer<Message> {
    @Serial
    private static final long serialVersionUID = 1L;

    private final DateFormat dateFormat;

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

        if (value instanceof PlayerTextMessage playerTextMsg) {
            String time = dateFormat.format(playerTextMsg.getTime());
            setText(String.format("%s (%s): %s", playerTextMsg.getNameOfSender(), time, playerTextMsg.getContent()));
        }
        if (value instanceof PlayerJoinedChatMessage playerJoinedMsg) {
            setText(playerJoinedMsg.getNickname() + " has joined the chat.");
        }
        if (value instanceof PlayerLeftGameMessage playerLeftMsg) {
            setText(playerLeftMsg.getNickname() + " has left the chat.");
        }
        if (value instanceof PlayerLoggedInMessage playerLoggedInMsg) {
            setText("Chat joined as " + playerLoggedInMsg.getNickname() + ".");
        }

        return this;
    }

}
