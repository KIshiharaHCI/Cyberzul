package cyberzul.view.board;

import cyberzul.model.ModelTile;
import cyberzul.view.listeners.TileClickListener;
import java.awt.Color;
import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** Represents a Factory Display on the view. */
public class Plate extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;
  private static final int PLATE_SIZE = 80;

  private static final int SPACE_FROM_LEFT_AND_TOP_EDGE_IN_PXL = 13;
  private static final int SPACE_BETWEEN_TILES_IN_PXL = 4;
  private final int plateId;

  private final JLabel plateImageLabel;
  private final ArrayList<SourceTile> tileList = new ArrayList<>();

  /**
   * Create one Plate.
   *
   * @param plateId the number with which the Plate can be identified.
   * @param tileClickListener
   */
  public Plate(int plateId, TileClickListener tileClickListener, List<ModelTile> content) {
    // setLayout(new GridLayout(1, 1));
    setBackground(Color.black);
    plateImageLabel = new JLabel(getResizedImageIcon("img/manufacturing-plate.png", PLATE_SIZE));
    // setOpaque(false);
    add(plateImageLabel);
    // GridLayout plateLayout = new GridLayout(2, 2);
    plateImageLabel.setBounds(0, 0, PLATE_SIZE, PLATE_SIZE);
    // plateImageLabel.setLayout(plateLayout);
    this.plateId = plateId;

    initialize(tileClickListener, content);
  }

  public void initialize(TileClickListener tileClickListener, List<ModelTile> content) {
    int space_from_left = SPACE_FROM_LEFT_AND_TOP_EDGE_IN_PXL;
    int space_from_top = SPACE_FROM_LEFT_AND_TOP_EDGE_IN_PXL;
    for (int i = 0; i < content.size(); i++) {
      // calculates the col and row from left-right,top-down
      // column: 1 1 2 2; row 1 2 1 2
      int column = i / 2 + 1;
      int row = i % 2 + 1;

      SourceTile tile =
          new SourceTile(
              column, row, content.get(i), i, plateId, Tile.NORMAL_TILE_SIZE, tileClickListener);
      tileList.add(tile);
      tile.setBounds(space_from_left, space_from_top, Tile.NORMAL_TILE_SIZE, Tile.NORMAL_TILE_SIZE);
      plateImageLabel.add(tile);
      // move tiles to the right after first and second tile
      // move down after second tile

      if (row == 1) {
        space_from_left += Tile.TILE_SIZE + SPACE_BETWEEN_TILES_IN_PXL;
      } else {
        space_from_left = SPACE_FROM_LEFT_AND_TOP_EDGE_IN_PXL;
        space_from_top += Tile.TILE_SIZE + SPACE_BETWEEN_TILES_IN_PXL;
      }
    }
  }

  private ImageIcon getResizedImageIcon(String path, int size) {
    URL imgURL1 = getClass().getClassLoader().getResource(path);
    return new ImageIcon(
        new ImageIcon(imgURL1).getImage().getScaledInstance(size, size, Image.SCALE_DEFAULT));
  }
}
