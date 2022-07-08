package azul.team12.view.board;

import azul.team12.controller.Controller;
import azul.team12.model.ModelTile;
import azul.team12.view.listeners.TileClickListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class Wall extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;
  private final int DEFAULT_TILE_SIZE = 25;
  private final int ROWS = 5;
  private final int COLS = 5;
  private final int buttonSize;
  private Controller controller;
  private ModelTile[][] wall;
  private ModelTile[][] templateWall;
  private JPanel currentRow;

  /**
   * Constructor solely used to create other players side panel.
   */
  public Wall(Controller controller) {
    this.controller = controller;

    setBackground(new Color(110, 150, 100));
    setPreferredSize(new Dimension((DEFAULT_TILE_SIZE + 2) * ROWS, (DEFAULT_TILE_SIZE + 2) * COLS));
    setMaximumSize(new Dimension((DEFAULT_TILE_SIZE + 2) * ROWS, (DEFAULT_TILE_SIZE + 2) * COLS));
    setMinimumSize(new Dimension((DEFAULT_TILE_SIZE + 2) * ROWS, (DEFAULT_TILE_SIZE + 2) * COLS));
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setAlignmentX(1.0f);
    setAlignmentY(1.0f);
    this.buttonSize = DEFAULT_TILE_SIZE;
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
   * @param tileClickListener
   */
  public Wall(Controller controller, TileClickListener tileClickListener) {
    this.controller = controller;
    wall = controller.getWallOfPlayerAsTiles(controller.getNickOfActivePlayer());
    templateWall = controller.getTemplateWall();

    ViewHelper.setProperties(Tile.TILE_SIZE, ROWS, COLS, this);

    this.buttonSize = Tile.TILE_SIZE;
    for (int row = 0; row < ROWS; row++) {
      currentRow = new JPanel();
      currentRow.setAlignmentX(0.1f);
      ViewHelper.setPropertiesOfCurrentRow(Tile.TILE_SIZE, COLS, row, currentRow);

      for (int col = 0; col < COLS; col++) {
        ModelTile tileXY = wall[row][col];
        if (tileXY.equals(ModelTile.EMPTY_TILE)) {
          currentRow.add(new TileDestinationWallTransparent(col, row, templateWall[row][col]));
        } else {
          currentRow.add(
              new TileDestinationWall(col, row, tileClickListener, tileXY));
        }
      }
      add(currentRow);
    }

//    this.addMouseListener(new MouseAdapter() {
//      @Override
//      public void mouseClicked(MouseEvent e) {
//        super.mouseClicked(e);
//      }
//    });
  }


}