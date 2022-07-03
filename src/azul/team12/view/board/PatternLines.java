package azul.team12.view.board;

import azul.team12.controller.Controller;
import azul.team12.model.ModelTile;
import azul.team12.view.listeners.TileClickListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class PatternLines extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;
  private static final int DEFAULT_TILE_SIZE = 25;
  private final Controller controller;
  private final int ROWS = 5;
  private final int COLS = 5;
  private final int tileSize;
  private ModelTile[][] currentPatternLines;

  public PatternLines(Controller controller) {
    this.controller = controller;
    currentPatternLines = this.controller.getPatternLinesOfPlayer(
        controller.getNickOfActivePlayer());

    setBackground(new Color(110, 150, 100));
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    this.tileSize = DEFAULT_TILE_SIZE;
    int numberOfSkippedColumns = 1;
    for (int yRow = 0; yRow < ROWS; yRow++) {
      final JPanel currentRow = new JPanel();
      this.setPropsToCurrRow(tileSize, numberOfSkippedColumns, yRow, currentRow);

      for (int xCol = 0; xCol < COLS; xCol++) {
        if (xCol < numberOfSkippedColumns) {

          currentRow.add(new TileWithoutListener(yRow, xCol, tileSize));
        }
      }
      numberOfSkippedColumns++;
      add(currentRow);
    }
  }

  public PatternLines(Controller controller, int tileSize, TileClickListener tileClickListener) {
    this.controller = controller;
    currentPatternLines = this.controller.getPatternLinesOfPlayer(
        controller.getNickOfActivePlayer());

    setBackground(new Color(110, 150, 100));
    setPreferredSize(new Dimension((tileSize + 2) * ROWS, (tileSize + 2) * COLS));
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    this.tileSize = tileSize;
    int numberOfSkippedColumns = 1;
    for (int y = 0; y < ROWS; y++) {
      JPanel currentRow = new JPanel();
      this.setPropsToCurrRow(tileSize, numberOfSkippedColumns, y, currentRow);

      for (int x = 0; x < COLS; x++) {
        if (x < numberOfSkippedColumns) {

          ModelTile modelTile = currentPatternLines[y][x];
          if (modelTile.toString().equals(ModelTile.EMPTY_TILE.toString())) {
            currentRow.add(
                new TileDestination(y, x, tileSize, tileClickListener, ModelTile.EMPTY_TILE));

          } else {
            currentRow.add(new TileDestination(y, x, tileSize, tileClickListener, modelTile));
          }
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
    currentRow.setMinimumSize(
        new Dimension((DEFAULT_TILE_SIZE + 2) * ROWS, (DEFAULT_TILE_SIZE + 2) * COLS));
  }


}
