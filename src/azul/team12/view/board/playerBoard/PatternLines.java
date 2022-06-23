package azul.team12.view.board.playerBoard;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class PatternLines extends JPanel {
 // List<Tile> stones;
  private final int ROWS = 5;
  private final int COLS = 5;

  public int tileSize = 20;

  public PatternLines() {
    setBackground(new Color(110,150,100));
  }

  public PatternLines(int tileSize) {
    setBackground(new Color(110,150,100));
    this.tileSize = tileSize;

  }

  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2D = (Graphics2D) g;
    int a = 4;
    int idx;
    int idy = 5;

    for (int y = 0; y < ROWS; y++) {
      for (int x = COLS -1; x >= 0; x--) {
        idx = (tileSize + 5)*x;

        if(x >= a) {
          g2D.setColor(Color.WHITE);
          g2D.drawRect(idx, idy, tileSize, tileSize);
          g2D.fillRect(idx, idy, tileSize, tileSize);
        }
      }
      a--;
      idy = idy + (tileSize + 5);
    }
  }
}
