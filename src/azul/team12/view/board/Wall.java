package azul.team12.view.board;

import azul.team12.controller.Controller;
import azul.team12.model.ModelTile;
import azul.team12.view.listeners.TileClickListener;

import javax.swing.*;
import java.awt.*;

public class Wall extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;
  private final int SMALL_TILE_SIZE = Tile.SMALL_TILE_SIZE;
  private final int ROWS = 5;
  private final int COLS = 5;
  private final int buttonSize;
  private transient Controller controller;
  private ModelTile[][] wall;
  private ModelTile[][] templateWall;
  private JPanel currentRow;
//TODO: remove other players side panel methods

  /**
   * Constructor solely used to create other players side panel.
   */
  public Wall(Controller controller) {
    this.controller = controller;

    setPreferredSize(new Dimension((SMALL_TILE_SIZE + 2) * ROWS, (SMALL_TILE_SIZE + 2) * COLS));
    setMaximumSize(new Dimension((SMALL_TILE_SIZE + 2) * ROWS, (SMALL_TILE_SIZE + 2) * COLS));
    setMinimumSize(new Dimension((SMALL_TILE_SIZE + 2) * ROWS, (SMALL_TILE_SIZE + 2) * COLS));
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setOpaque(false);
    setAlignmentX(1.0f);
    setAlignmentY(1.0f);
    buttonSize = SMALL_TILE_SIZE;

    for (int y = 0; y < ROWS; y++) {
      currentRow = new JPanel();
      currentRow.setAlignmentX(0.2f);
      currentRow.setAlignmentY(1.0f);
      currentRow.setLayout(new GridLayout(1, COLS));
      currentRow.setMaximumSize(
          new Dimension(ROWS * buttonSize, COLS * buttonSize));

      for (int x = 0; x < COLS; x++) {
        currentRow.add(new TileWithoutListener(y, x, buttonSize));
      }
      add(currentRow);
    }
  }

  /**
   * Constructor used by current playerBoard. Starts empty and should appear with Tile images after
   * Tiling phase.
   *
   * @param tileClickListener //TODO: remove after decoupling other players panel
   */
  public Wall(String playerName, Controller controller, int tileSize,TileClickListener tileClickListener) {
    this.controller = controller;
    wall = controller.getWallOfPlayerAsTiles(playerName);
    templateWall = controller.getTemplateWall();

    ViewHelper.setProperties(tileSize, ROWS, COLS, this);

    this.buttonSize = tileSize;
    for (int row = 0; row < ROWS; row++) {
      currentRow = new JPanel();
      //currentRow.setAlignmentX(0.1f);
      ViewHelper.setPropertiesOfCurrentRow(tileSize, COLS, ROWS, currentRow);

      for (int col = 0; col < COLS; col++) {
        ModelTile tileXY = wall[row][col];
        if (tileXY.equals(ModelTile.EMPTY_TILE)) {
          currentRow.add(new WallTile(col, row, templateWall[row][col],tileSize, 0.3f));
        } else {
          currentRow.add(new WallTile(col, row, tileXY, tileSize, 1f)
          );
        }
      }
      add(currentRow);
    }
  }
}