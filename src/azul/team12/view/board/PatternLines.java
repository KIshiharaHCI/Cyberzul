package azul.team12.view.board;

import azul.team12.controller.Controller;
import azul.team12.model.ModelTile;
import azul.team12.view.listeners.TileClickListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class PatternLines extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;
  private final Controller controller;
  private ModelTile[][] currentPatternLines;
  private static final int DEFAULT_TILE_SIZE = 25;

  private final int ROWS = 5;
  private final int COLS = 5;

  private final int tileSize;

  public PatternLines(Controller controller) {
    this.controller = controller;
    currentPatternLines = this.controller.getPatternLinesOfPlayer(controller.getNickOfActivePlayer());

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
    currentPatternLines = this.controller.getPatternLinesOfPlayer(controller.getNickOfActivePlayer());

    setBackground(new Color(110, 150, 100));
    setPreferredSize(new Dimension((tileSize + 2) * ROWS, (tileSize + 2) * COLS));
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setAlignmentX(1.0f);
    this.tileSize = tileSize;
    int numberOfSkippedColumns = 1;
    for (int y = 0; y < ROWS; y++) {
      JPanel currentRow = new JPanel();
      currentRow.setAlignmentX(1.0f);
      currentRow.setLayout(new GridLayout(1, numberOfSkippedColumns));
      currentRow.setMaximumSize(
          new Dimension(numberOfSkippedColumns * tileSize, y * tileSize));

      for (int x = 0; x < COLS; x++) {
        if (x < numberOfSkippedColumns) {
          //TODO: if tileAtXY == true -> getTileEnumXY -> currentRow.add(new TileWithoutListener)
          //TODO: repaint tiles
          currentRow.add(new TileDestination(y, x, tileSize, tileClickListener, null));
        }
      }
      numberOfSkippedColumns++;
      add(currentRow);
    }
  }

}
