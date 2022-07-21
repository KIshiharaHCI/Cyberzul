package cyberzul.view.board;

import cyberzul.model.ModelTile;
import cyberzul.view.listeners.TileClickListener;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Represents a Factory Display on the view. */
public class Plate extends JPanel {
  @Serial private static final long serialVersionUID = 7526472295622776147L;
  private static final int PLATE_SIZE = 90;

  private static final int spaceFromLeftAndTopInPxl = 13;
  private static final int SPACE_BETWEEN_TILES_IN_PXL = 4;
  private final int plateId;

  private final JLabel plateImageLabel;
  private final ArrayList<SourceTile> tileList = new ArrayList<>();

  /**
   * Create one Plate.
   *
   * @param plateId the number with which the Plate can be identified.
   * @param tileClickListener the tile click listener
   */
  public Plate(int plateId, TileClickListener tileClickListener, List<ModelTile> content) {
    // setLayout(new GridLayout(1, 1));

    plateImageLabel = new JLabel(getResizedImageIcon("img/manufacturing-plate.png", PLATE_SIZE));
    setOpaque(false);
    add(plateImageLabel);
    // GridLayout plateLayout = new GridLayout(2, 2);
    plateImageLabel.setBounds(0, 0, PLATE_SIZE, PLATE_SIZE);
    // plateImageLabel.setLayout(plateLayout);
    this.plateId = plateId;

    initialize(tileClickListener, content);
  }

  /**
   * Adds the correct Source Tiles on the Plate and sets the correct positioning for each Tile.
   * @param tileClickListener listener for checking for a mouse click.
   * @param content contains the color of the Source Tile returned by the model.
   */
  public void initialize(TileClickListener tileClickListener, List<ModelTile> content) {
    int spaceFromLeft = spaceFromLeftAndTopInPxl;
    int spaceFromTop = spaceFromLeftAndTopInPxl;
    for (int i = 0; i < content.size(); i++) {
      // calculates the col and row from left-right,top-down
      // column: 1 1 2 2; row 1 2 1 2
      int column = i / 2 + 1;
      int row = i % 2 + 1;

      SourceTile tile =
          new SourceTile(
              column, row, content.get(i), i, plateId, Tile.NORMAL_TILE_SIZE, tileClickListener);
      tileList.add(tile);
      tile.setBounds(spaceFromLeft, spaceFromTop, Tile.NORMAL_TILE_SIZE, Tile.NORMAL_TILE_SIZE);
      plateImageLabel.add(tile);
      // move tiles to the right after first and second tile
      // move down after second tile

      if (row == 1) {
        spaceFromLeft += Tile.TILE_SIZE + SPACE_BETWEEN_TILES_IN_PXL;
      } else {
        spaceFromLeft = spaceFromLeftAndTopInPxl;
        spaceFromTop += Tile.TILE_SIZE + SPACE_BETWEEN_TILES_IN_PXL;
      }
    }
  }

  /**
   * Used to get an ImageIcon for a single plate.
   * @param path
   * @param size
   * @return
   */
  private ImageIcon getResizedImageIcon(String path, int size) {
    URL imgUrl1 = getClass().getClassLoader().getResource(path);
    return new ImageIcon(
        new ImageIcon(Objects.requireNonNull(imgUrl1))
            .getImage()
            .getScaledInstance(size, size, Image.SCALE_DEFAULT));
  }
}
