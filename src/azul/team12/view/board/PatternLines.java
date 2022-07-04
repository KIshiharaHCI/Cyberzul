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
    setPreferredSize(new Dimension((DEFAULT_TILE_SIZE + 2) * ROWS, (DEFAULT_TILE_SIZE + 2) * COLS));
    setMaximumSize(new Dimension((DEFAULT_TILE_SIZE + 2) * ROWS, (DEFAULT_TILE_SIZE + 2) * COLS));
    setMinimumSize(new Dimension((DEFAULT_TILE_SIZE + 2) * ROWS, (DEFAULT_TILE_SIZE + 2) * COLS));
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setAlignmentX(1.0f);
    setAlignmentY(1.0f);
    this.tileSize = DEFAULT_TILE_SIZE;
    int numberOfSkippedColumns = 1;
    for (int yRow = 0; yRow < ROWS; yRow++) {
      final JPanel currentRow = new JPanel();
      currentRow.setAlignmentX(1.0f);
      currentRow.setAlignmentY(1.0f);
      currentRow.setLayout(new GridLayout(1, numberOfSkippedColumns));
      currentRow.setMaximumSize(
          new Dimension(numberOfSkippedColumns * tileSize, COLS * tileSize));

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
    setMaximumSize(new Dimension((tileSize + 2) * ROWS, (tileSize + 2) * COLS));
    setMinimumSize(new Dimension((tileSize + 2) * ROWS, (tileSize + 2) * COLS));
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setAlignmentX(1.0f);
    setAlignmentY(1.0f);
    this.tileSize = tileSize;
    int numberOfSkippedColumns = 1;
    for (int row = 0; row < ROWS; row++) {
      JPanel currentRow = new JPanel();
      currentRow.setLayout(new GridLayout(1, numberOfSkippedColumns, 2, 2));
      currentRow.setAlignmentX(1.0f);
      currentRow.setAlignmentY(1.0f);
      currentRow.setMaximumSize(
          new Dimension(numberOfSkippedColumns * (tileSize + 2) + 2, row * (tileSize + 2) + 2));
      currentRow.setBorder(BorderFactory.createEmptyBorder(1, 2, 1, 2));
      currentRow.setOpaque(false);

      for (int col = 0; col < COLS; col++) {
        if (col < numberOfSkippedColumns) {
          ModelTile modelTile = currentPatternLines[row][col];
          if (modelTile.toString().equals(ModelTile.EMPTY_TILE.toString())) {
            currentRow.add(
                new TileDestinationPatternLines(col, row, tileSize, tileClickListener,
                    ModelTile.EMPTY_TILE));

          } else {
            currentRow.add(
                new TileDestinationPatternLines(col, row, tileSize, tileClickListener, modelTile));
          }
        }
      }
      numberOfSkippedColumns++;
      add(currentRow);
    }
  }

}
