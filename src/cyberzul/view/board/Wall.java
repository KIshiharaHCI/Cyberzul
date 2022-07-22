package cyberzul.view.board;

import cyberzul.controller.Controller;
import cyberzul.model.ModelTile;
import cyberzul.view.listeners.TileClickListener;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.Serial;
import javax.swing.JPanel;

/**
 * Creates the wall of the player on the player board.
 */
public class Wall extends JPanel {
  @Serial private static final long serialVersionUID = 7526472295622776147L;
  private static final int SMALL_TILE_SIZE = Tile.SMALL_TILE_SIZE;
  private static final int ROWS = 5;
  private static final int COLS = 5;
  private final int buttonSize;
  private final transient Controller controller;
  private final ModelTile[][] wall;
  private final ModelTile[][] templateWall;
  private JPanel currentRow;

  /**
   * Constructor used by current playerBoard. Starts empty and should appear with Tile images after
   * Tiling phase.
   *
   * @param tileClickListener //TODO: remove after decoupling other players panel
   */
  @SuppressFBWarnings({"EI_EXPOSE_REP2", "EI_EXPOSE_REP2"})
  @SuppressWarnings(value = "EI_EXPOSE_REP")
  // this class needs these references to these mutable objects.
  public Wall(
      String playerName, Controller controller, int tileSize, TileClickListener tileClickListener) {
    this.controller = controller;
    wall = controller.getWallOfPlayerAsTiles(playerName);
    templateWall = controller.getTemplateWall();

    ViewHelper.setProperties(tileSize, ROWS, COLS, this);

    this.buttonSize = tileSize;
    for (int row = 0; row < ROWS; row++) {
      currentRow = new JPanel();
      // currentRow.setAlignmentX(0.1f);
      ViewHelper.setPropertiesOfCurrentRow(tileSize, COLS, ROWS, currentRow);

      for (int col = 0; col < COLS; col++) {
        ModelTile tilexy = wall[row][col];
        if (tilexy.equals(ModelTile.EMPTY_TILE)) {
          currentRow.add(new WallTile(col, row, templateWall[row][col], tileSize, 0.3f));
        } else {
          currentRow.add(new WallTile(col, row, tilexy, tileSize, 1f));
        }
      }
      add(currentRow);
    }
  }
}
