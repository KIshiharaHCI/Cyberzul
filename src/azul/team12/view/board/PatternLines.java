package azul.team12.view.board;

import azul.team12.view.listeners.TileClickListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class PatternLines extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;

  private static final int DEFAULT_TILE_SIZE = 25;

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

      for (int x = 0; x < COLS; x++) {
        if (x < numberOfSkippedColumns) {
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

      for (int x = 0; x < COLS; x++) {
        if (x < numberOfSkippedColumns) {
          currentRow.add(new TileDestination(y, x, tileSize, tileClickListener, null));
        }
      }
      numberOfSkippedColumns++;
      add(currentRow);
    }
  }

}
