package azul.team12.view.board.playerBoard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JPanel;

public class PatternLines extends JPanel {
 // List<Tile> stones;
  private final int ROWS = 5;
  private final int COLS = 5;

  private int buttonSize = 20;

  public PatternLines() {

    //initializeStones(buttonSize);

  }

  public PatternLines(int buttonSize) {
    this.buttonSize = buttonSize;
    //initializeStones(buttonSize);
  }

  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2D = (Graphics2D) g;
    int a = 4;
    int idx;
    int idy = 5;

    for (int y = 0; y < ROWS; y++) {
      for (int x = COLS -1; x >= 0; x--) {
        idx = (buttonSize + 5)*x;

        if(x >= a) {
          g2D.setColor(Color.WHITE);
          g2D.drawRect(idx, idy, buttonSize, buttonSize);
          g2D.fillRect(idx, idy, buttonSize, buttonSize);
        }
      }
      a--;
      idy = idy + (buttonSize + 5);
    }
  }

  private void initializeStones(int btnSize) {
    JPanel patternLines = new JPanel(new GridBagLayout());
   // this.stones = new ArrayList<>();
    int a = 4;
    for (int y = 0; y < ROWS; y++) {
      for (int x = COLS -1; x >= 0; x--) {
        if(x >= a) {
          GridBagConstraints gbc = new GridBagConstraints();
          gbc.fill = GridBagConstraints.HORIZONTAL;
          gbc.insets = new Insets(1,1,1,1);
          Tile button = new Tile(y, x);
          button.setPreferredSize(new Dimension(btnSize, btnSize));
          //button.setIcon(new ImageIcon(MainWindow.class.getResource("/images/button_next.jpg")));
          //button.addActionListener(new ProcessorNextStepListener(this));
          Tile stone = new Tile(x, y);
          gbc.gridx = x;
          gbc.gridy = y;

          patternLines.add(button, gbc);
          //stones.add(stone);
          //add(stone);
        }
      }
    a--;
    }
    add(patternLines);
  }
}
