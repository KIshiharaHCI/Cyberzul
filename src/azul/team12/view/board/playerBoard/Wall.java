package azul.team12.view.board.playerBoard;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import javax.swing.JPanel;

public class Wall extends JPanel {
  private final int ROWS = 5;
  private final int COLS = 5;

  private int buttonSize = 20;

  public Wall() {
    setBackground(new Color(110,150,100));
  }

  public Wall(int buttonSize) {
    setBackground(new Color(110,150,100));
    this.buttonSize = buttonSize;
  }

  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2D = (Graphics2D) g;
    int idx;
    int idy = 5;

    for (int y = 0; y < ROWS; y++) {

      for (int x = 0; x < COLS; x++) {
        idx =  (buttonSize + 5) * x;
        g2D.setColor(Color.WHITE);
        g2D.drawRect(idx, idy, buttonSize, buttonSize);
        g2D.fillRect(idx, idy, buttonSize, buttonSize);


      }

      idy = idy + (buttonSize + 5);
    }

  }
}
