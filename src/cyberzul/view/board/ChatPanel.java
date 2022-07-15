package cyberzul.view.board;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ChatPanel extends JPanel implements PropertyChangeListener {

    private static final int DEFAULT_HEIGHT = 500;
    private static final int INPUTFIELD_WIDTH = 40;
    private static final int INPUTFIELD_HEIGHT = 3;
    private static final long serialVersionUID = 13L;
    private static final int defaultInset = 5;
    private JTextArea inputArea;
    private JScrollPane scrollPane;

    public ChatPanel() {
        setLayout(new GridBagLayout());
        initializeWidgets();
        createChatPanel();
    }

    private void initializeWidgets() {
        scrollPane = new JScrollPane();
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

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        SwingUtilities.invokeLater(() -> handleModelUpdate(propertyChangeEvent));
    }

    private void handleModelUpdate(PropertyChangeEvent event) {

    }


}
