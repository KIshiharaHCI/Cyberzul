package azul.team12.view.board.playerBoard;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class PatternLines extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;

  // List<Tile> stones;
  private final int ROWS = 5;
  private final int COLS = 5;

  public int tileSize = 20;

  public PatternLines() {
    setBackground(new Color(110, 150, 100));
  }

  public PatternLines(int tileSize) {
    setBackground(new Color(110, 150, 100));
    this.tileSize = tileSize;

  }

  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2D = (Graphics2D) g;
    int numberOfSkippedColumns = 4;
    int leftUpperCornerY = 5;
    for (int y = 0; y < ROWS; y++) {
      for (int x = COLS - 1; x >= 0; x--) {
        int leftUpperCornerX = (tileSize + 5) * x;

        if (x >= numberOfSkippedColumns) {
          g2D.setColor(Color.WHITE);
          g2D.drawRect(leftUpperCornerX, leftUpperCornerY, tileSize, tileSize);
          g2D.fillRect(leftUpperCornerX, leftUpperCornerY, tileSize, tileSize);
        }
      }
      numberOfSkippedColumns--;
      leftUpperCornerY = leftUpperCornerY + (tileSize + 5);
    }
  }
}
