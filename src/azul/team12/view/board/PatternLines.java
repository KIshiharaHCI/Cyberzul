package azul.team12.view.board;

import azul.team12.view.listeners.TileClickListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class PatternLines extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;

  private static final int DEFAULT_TILE_SIZE = 20;

  // List<Tile> stones;
  private final int ROWS = 5;
  private final int COLS = 5;

  private final int tileSize;

  /**
   * Constructor for the other players.
   */
  public PatternLines() {
    this.tileSize = DEFAULT_TILE_SIZE;
    setProps();
    addTilesWithoutListener();
  }

  public PatternLines(int tileSize, TileClickListener tileClickListener) {
    this.tileSize = tileSize;
    setProps();
    addTilesWithListener(tileSize, tileClickListener);
  }

  private void setProps() {
    setBackground(new Color(110, 150, 100));
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setAlignmentX(1.0f);

  }

  private void addTilesWithListener(int tileSize, TileClickListener tileClickListener) {
    int numberOfSkippedColumns = 1;
    for (int row = 0; row < ROWS; row++) {
      final JPanel currentRow = new JPanel();
      setPropsToCurrRow(tileSize, numberOfSkippedColumns, row, currentRow);

      for (int x = 0; x < COLS; x++) {
        if (x < numberOfSkippedColumns) {
          currentRow.add(new TileDestination(row, x, tileSize, tileClickListener, null));
        }
      }
      numberOfSkippedColumns++;
      add(currentRow);
    }
  }

  private void setPropsToCurrRow(int tileSize, int numberOfSkippedColumns, int row,
      JPanel currentRow) {
    currentRow.setLayout(new GridLayout(1, numberOfSkippedColumns, 4, 4));
    // for alignment on the right
    currentRow.setAlignmentX(1.0f);
    //padding
    currentRow.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
    // TileDestination is not transparent, but everything else on the current row is transparent
    currentRow.setOpaque(false);
    // for no stretching of the row
    currentRow.setMaximumSize(
        new Dimension(numberOfSkippedColumns * (tileSize + 4) + 4, row * (tileSize + 4) + 4));
  }

  private void addTilesWithoutListener() {
    int numberOfSkippedColumns = 1;
    for (int row = 0; row < ROWS; row++) {
      final JPanel currentRow = new JPanel();
      setPropsToCurrRow(tileSize, numberOfSkippedColumns, row, currentRow);
      for (int col = 0; col < COLS; col++) {
        if (col < numberOfSkippedColumns) {
          currentRow.add(new TileWithoutListener(row, col, tileSize));
        }
      }
      numberOfSkippedColumns++;
      add(currentRow);
    }
  }

}