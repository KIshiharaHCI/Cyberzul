package azul.team12.view.board.playerBoard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;

public class PatternLines extends JPanel {
  List<PatternLinesStone> stones;
  private final int ROWS = 5;
  private final int COLS = 5;

  private int buttonSize = 20;

  public PatternLines() {

    initializeStones(buttonSize);

  }

  public PatternLines(int buttonSize) {
    this.buttonSize = buttonSize;
    initializeStones(buttonSize);
  }

  private void initializeStones(int btnSize) {
    JPanel patternLines = new JPanel(new GridBagLayout());


    this.stones = new ArrayList<>();

    int a = 4;
    for (int y = 0; y < ROWS; y++) {
      for (int x = COLS -1; x >= 0; x--) {
        if(x >= a) {
          GridBagConstraints gbc = new GridBagConstraints();
          gbc.fill = GridBagConstraints.HORIZONTAL;
          gbc.insets = new Insets(1,1,1,1);
          PatternLinesStone button = new PatternLinesStone(y, x);
          button.setPreferredSize(new Dimension(btnSize, btnSize));
          //button.setIcon(new ImageIcon(MainWindow.class.getResource("/images/button_next.jpg")));
          //button.addActionListener(new ProcessorNextStepListener(this));
          PatternLinesStone stone = new PatternLinesStone(x, y);
          gbc.gridx = x;
          gbc.gridy = y;

          patternLines.add(button, gbc);
          stones.add(stone);
          //add(stone);
        }
      }
    a--;
    }
    add(patternLines);

  }

  public int getButtonSize() {
    return buttonSize;
  }

  public void setButtonSize(int buttonSize) {
    this.buttonSize = buttonSize;
  }

  private void setButtonBackground(PatternLinesStone button) {
    button.setBackground(Color.GRAY);
  }
}
