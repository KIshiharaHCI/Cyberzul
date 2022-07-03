package azul.team12.view.board;

import azul.team12.controller.Controller;
import azul.team12.model.ModelTile;
import azul.team12.view.listeners.TileClickListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class Wall extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;
  private Controller controller;
  private ModelTile[][] wall;
  private ModelTile[][] templateWall;

  private final int DEFAULT_TILE_SIZE = 25;
  private JPanel currentRow;
  private final int ROWS = 5;
  private final int COLS = 5;

  private final int buttonSize;

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
   * Constructor used by current playerBoard. Starts empty and should appear with Tile images after Tiling phase.
   * @param tileClickListener
   */
  public Wall(Controller controller,TileClickListener tileClickListener) {
    this.controller = controller;
    wall = controller.getWallOfPlayerAsTiles(controller.getNickOfActivePlayer());
    templateWall = controller.getTemplateWall();

    setBackground(new Color(110, 150, 100));
    setPreferredSize(new Dimension((Tile.TILE_SIZE + 2) * ROWS, (Tile.TILE_SIZE + 2) * COLS));
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setAlignmentX(1.0f);
    this.buttonSize = Tile.TILE_SIZE;
    for (int row = 0; row < ROWS; row++) {
      currentRow = new JPanel();
      currentRow.setAlignmentX(0.1f);
      currentRow.setLayout(new GridLayout(1, COLS));
      currentRow.setMaximumSize(
          new Dimension(ROWS * Tile.TILE_SIZE, COLS * Tile.TILE_SIZE));

      for (int col = 0; col < COLS; col++) {
        ModelTile tileXY =  wall[row][col];
        if (tileXY.equals(ModelTile.EMPTY_TILE)) {
          currentRow.add(new TileDestination(col,row,Tile.TILE_SIZE,templateWall[row][col]));
        } else {
          currentRow.add(new TileDestinationWall(col, row, Tile.TILE_SIZE, tileClickListener,tileXY));
        }
      }
      add(currentRow);
    }

    this.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
      }
    });
  }

  /*
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2D = (Graphics2D) g;
    int idx;
    int idy = 5;

    for (int y = 0; y < ROWS; y++) {
      for (int x = 0; x < COLS; x++) {
        idx = (buttonSize + 5) * x;
        g2D.setColor(Color.WHITE);
        g2D.drawRect(idx, idy, buttonSize, buttonSize);
        g2D.fillRect(idx, idy, buttonSize, buttonSize);
      }
      idy = idy + (buttonSize + 5);
    }
  }
  */

}