package cyberzul.view.board;

import cyberzul.controller.Controller;
import cyberzul.model.ModelTile;
import cyberzul.view.listeners.TileClickListener;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import javax.swing.*;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * The Pattern Lines of a given player.
 * One of the destinations the player can place tiles to.
 */
public class PatternLines extends JPanel {
  @Serial private static final long serialVersionUID = 7526472295622776147L;
  private static final int DEFAULT_TILE_SIZE = 25;
  private static final int ROWS = 5;
  private static final int COLS = 5;
  private final transient Controller controller;
  private final int tileSize;
  private final transient List<JPanel> currentRows = new ArrayList<>();


  /**
   * The constructor of {@link PatternLines}.
   *
   * @param controller The controller.
   * @param tileSize The size of the smallest element on the {@link PatternLines} board.
   * @param tileClickListener The listener for click events.
   */
  @SuppressFBWarnings({"EI_EXPOSE_REP2", "EI_EXPOSE_REP2"})
  @SuppressWarnings(value = "EI_EXPOSE_REP")
  // this class needs these references to these mutable objects.
  public PatternLines(
      String userName, Controller controller, int tileSize, TileClickListener tileClickListener) {
    this.controller = controller;

    ViewHelper.setProperties(tileSize, ROWS, COLS, this);
    this.tileSize = tileSize;
    initialize(tileSize, userName, tileClickListener);
  }

  /**
   * Initializes all the components of the {@link PatternLines}.
   *
   * @param tileSize The size of the smallest element.
   * @param tileClickListener The listener to react on click events on the {@link PatternLines}
   *     board.
   */
  public void initialize(int tileSize, String userName, TileClickListener tileClickListener) {
    ModelTile[][] currentPatternLines = this.controller.getPatternLinesOfPlayer(userName);
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
                new DestinationTile(col, row, ModelTile.EMPTY_TILE, tileClickListener, tileSize));
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

  /**
   * Used by TileClickListener to remove all stored tiles from the previous player.
   */
  public void remove() {
    for (JPanel currentRow : this.currentRows) {
      this.remove(currentRow);
    }
  }

  /**
   * Used to get the correct tile size when creating a new instance of PatternLines.
   * @return
   */
  public int getTileSize() {
    return tileSize;
  }
}
