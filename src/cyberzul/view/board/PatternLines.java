package cyberzul.view.board;

import cyberzul.controller.Controller;
import cyberzul.model.ModelTile;
import cyberzul.view.listeners.TileClickListener;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

public class PatternLines extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;
  private static final int DEFAULT_TILE_SIZE = 25;
  private transient final Controller controller;
  private final int ROWS = 5;
  private final int COLS = 5;
  private final int tileSize;
  private ModelTile[][] currentPatternLines;

  private transient List<JPanel> currentRows = new ArrayList<>();

  public PatternLines(Controller controller) {
    this.controller = controller;
    currentPatternLines = this.controller.getPatternLinesOfPlayer(
        controller.getNickOfActivePlayer());

    ViewHelper.setProperties(DEFAULT_TILE_SIZE, ROWS, COLS, this);
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

  /**
   * The constructor of {@link PatternLines}.
   *
   * @param controller:        The controller.
   * @param tileSize:          The size of the smallest element on the {@link PatternLines} board.
   * @param tileClickListener: The listener for click events.
   */
  public PatternLines(String userName, Controller controller, int tileSize,
      TileClickListener tileClickListener) {
    this.controller = controller;

    ViewHelper.setProperties(tileSize, ROWS, COLS, this);
    this.tileSize = tileSize;
    initialize(tileSize, userName, tileClickListener);
  }

  /**
   * Initializes all the components of the {@link PatternLines}.
   *
   * @param tileSize:          The size of the smallest element.
   * @param tileClickListener: The listener to react on click events on the {@link PatternLines}
   *                           board.
   */
  public void initialize(int tileSize, String userName, TileClickListener tileClickListener) {
    currentPatternLines = this.controller.getPatternLinesOfPlayer(
        userName);
    int numberOfSkippedColumns = 1;
    for (int row = 0; row < ROWS; row++) {
      JPanel currentRow = new JPanel();
      currentRow.setAlignmentX(1.0f);
      ViewHelper.setPropertiesOfCurrentRow(tileSize, numberOfSkippedColumns, row, currentRow);
      for (int col = 0; col < COLS; col++) {
        if (col < numberOfSkippedColumns) {
          ModelTile modelTile = currentPatternLines[row][col];
          if (modelTile.toString().equals(ModelTile.EMPTY_TILE.toString())) {
            currentRow.add(
                new DestinationTile(col, row, ModelTile.EMPTY_TILE, tileClickListener, tileSize)
            );
          } else {
            currentRow.add(new DestinationTile(col, row, modelTile, tileClickListener, tileSize));
          }
        }
      }
      numberOfSkippedColumns++;
      add(currentRow);
      this.currentRows.add(currentRow);
    }
  }

  public void remove() {
    for (JPanel currentRow : this.currentRows) {
      this.remove(currentRow);
    }
  }

  public int getTileSize() {
    return tileSize;
  }
}
