package azul.team12.view.board;

import azul.team12.controller.Controller;
import azul.team12.model.ModelTile;
import azul.team12.view.listeners.TileClickListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PatternLines extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;
  private static final int DEFAULT_TILE_SIZE = 25;
  private final Controller controller;
  private final int ROWS = 5;
  private final int COLS = 5;
  private final int tileSize;
  private ModelTile[][] currentPatternLines;

  private List<JPanel> currentRows = new ArrayList<>();

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
  public PatternLines(Controller controller, int tileSize, TileClickListener tileClickListener) {
    this.controller = controller;
    currentPatternLines = this.controller.getPatternLinesOfPlayer(
        controller.getNickOfActivePlayer());

    ViewHelper.setProperties(tileSize, ROWS, COLS, this);
    this.tileSize = tileSize;
    initialize(tileSize, tileClickListener);
  }

  /**
   * Initializes all the components of the {@link PatternLines}.
   *
   * @param tileSize:          The size of the smallest element.
   * @param tileClickListener: The listener to react on click events on the {@link PatternLines}
   *                           board.
   */
  public void initialize(int tileSize, TileClickListener tileClickListener) {
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
                    new DestinationTile(col, row, ModelTile.EMPTY_TILE, tileClickListener,tileSize)
            );
          } else {
            currentRow.add(new DestinationTile(col, row, modelTile, tileClickListener,tileSize));
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

}
