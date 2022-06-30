package azul.team12.view.board;

import azul.team12.view.listeners.TileClickListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class PatternLines extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;

  private static final int DEFAULT_TILE_SIZE = 20;

  // List<Tile> stones;
  private final int ROWS = 5;
  private final int COLS = 5;

  private final int tileSize;

  public PatternLines() {
    setBackground(new Color(110, 150, 100));
    setPreferredSize(new Dimension((DEFAULT_TILE_SIZE + 2) * ROWS, (DEFAULT_TILE_SIZE + 2) * COLS));
    setMaximumSize(new Dimension((DEFAULT_TILE_SIZE + 2) * ROWS, (DEFAULT_TILE_SIZE + 2) * COLS));
    setMinimumSize(new Dimension((DEFAULT_TILE_SIZE + 2) * ROWS, (DEFAULT_TILE_SIZE + 2) * COLS));
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setAlignmentX(1.0f);
    setAlignmentY(1.0f);
    this.tileSize = DEFAULT_TILE_SIZE;
    int numberOfSkippedColumns = 1;
    for (int y = 0; y < ROWS; y++) {
      final JPanel currentRow = new JPanel();
      currentRow.setAlignmentX(1.0f);
      currentRow.setAlignmentY(1.0f);
      currentRow.setLayout(new GridLayout(1, numberOfSkippedColumns));
      currentRow.setMaximumSize(
          new Dimension(numberOfSkippedColumns * tileSize, COLS * tileSize));

      for (int x = 1; x <= COLS; x++) {
        if (x <= numberOfSkippedColumns) {
          currentRow.add(new TileWithoutListener(y, x, tileSize));
        }
      }
      numberOfSkippedColumns++;
      add(currentRow);
    }
  }

  public PatternLines(int tileSize, TileClickListener tileClickListener) {
    setBackground(new Color(110, 150, 100));
    setPreferredSize(new Dimension((tileSize + 2) * ROWS, (tileSize + 2) * COLS));
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setAlignmentX(1.0f);
    this.tileSize = tileSize;
    int numberOfSkippedColumns = 1;
    for (int y = 0; y < ROWS; y++) {
      final JPanel currentRow = new JPanel();
      currentRow.setAlignmentX(1.0f);
      currentRow.setLayout(new GridLayout(1, numberOfSkippedColumns));
      currentRow.setMaximumSize(
          new Dimension(numberOfSkippedColumns * tileSize, y * tileSize));

      for (int x = 1; x <= COLS; x++) {
        if (x <= numberOfSkippedColumns) {
          currentRow.add(new TileDestination(y, x, tileSize, tileClickListener));
        }
      }
      numberOfSkippedColumns++;
      add(currentRow);
    }

    this.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
      }
    });
  }

  /*
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
   */
}
