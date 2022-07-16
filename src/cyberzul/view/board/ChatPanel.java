package cyberzul.view.board;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

/**
 * The Chat Panel at the right side of the GameBoard.
 */
public class ChatPanel extends JPanel implements PropertyChangeListener {

  private static final int DEFAULT_HEIGHT = 300;
  private static final int INPUTFIELD_WIDTH = 20;
  private static final int INPUTFIELD_HEIGHT = 3;
  private static final long serialVersionUID = 13L;
  private static final int defaultInset = 5;
  private JTextArea inputArea;
  private JScrollPane scrollPane;

  /**
   * create a new chat panel with the respective widgets.
   */
  public ChatPanel() {
    initializeWidgets();
    createChatPanel();
  }

  private void initializeWidgets() {
    setOpaque(false);
    scrollPane = new JScrollPane();
    scrollPane.setPreferredSize(new Dimension(INPUTFIELD_WIDTH, DEFAULT_HEIGHT));
    scrollPane.setMaximumSize(new Dimension(INPUTFIELD_WIDTH, DEFAULT_HEIGHT));
    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setBackground(Color.DARK_GRAY);
    scrollPane.setBorder( BorderFactory.createEmptyBorder(defaultInset, defaultInset, defaultInset, defaultInset) );

    inputArea = new JTextArea(INPUTFIELD_HEIGHT, INPUTFIELD_WIDTH);
    inputArea.setLineWrap(true);
    inputArea.setWrapStyleWord(true);
    inputArea.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
    inputArea.setBackground(Color.black);
    inputArea.setForeground(Color.white);
    inputArea.setBorder( BorderFactory.createEmptyBorder(defaultInset, defaultInset, defaultInset, defaultInset) );
  }

  private void createChatPanel() {
    this.setLayout(new BorderLayout());
    this.add(scrollPane, BorderLayout.CENTER);
    this.add(inputArea, BorderLayout.SOUTH);
  }
  @Override
  public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
    SwingUtilities.invokeLater(() -> handleModelUpdate(propertyChangeEvent));
  }

  private void handleModelUpdate(PropertyChangeEvent event) {
  }
}
