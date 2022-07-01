package azul.team12.view.board;

import azul.team12.model.ModelTile;
import azul.team12.view.listeners.TileClickListener;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D.Double;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 * Represents a Factory Display on the view.
 */
public class Plate extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;
  private final int PLATE_SIZE = 110;

  private final int SPACE_FROM_LEFT_AND_TOP_EDGE_IN_PXL = 13;
  private final int SPACE_BETWEEN_TILES_IN_PXL = 4;
  private int plateId;
  private List<Tile> tileList;

  /**
   * Create one Plate.
   *
   * @param plateId the number with which the Plate can be identified.
   * @param tileClickListener
   */
  public Plate(int plateId,
      TileClickListener tileClickListener, List<ModelTile> content) {
    setLayout(new GridLayout(1, 1));
    JLabel plateImageLabel = new JLabel(
        getResizedRoundImageIcon("img/manufacturing-plate.png", PLATE_SIZE));
    add(plateImageLabel);
    // GridLayout plateLayout = new GridLayout(2, 2);
    plateImageLabel.setBounds(0, 0, PLATE_SIZE, PLATE_SIZE);
    // plateImageLabel.setLayout(plateLayout);
    setBorder(new LineBorder(Color.WHITE));
    this.plateId = plateId;
    this.tileList = new ArrayList<>();
    int space_from_left = SPACE_FROM_LEFT_AND_TOP_EDGE_IN_PXL;
    int space_from_top = SPACE_FROM_LEFT_AND_TOP_EDGE_IN_PXL;
    for (int i = 0; i < content.size(); i++) {
          Tile tile = new Tile(i, this.plateId, content.get(i), tileClickListener);
          System.out.println("tile: " + tile);
          tileList.add(tile);
          tile.setBounds(space_from_left, space_from_top, Tile.TILE_SIZE, Tile.TILE_SIZE);
          plateImageLabel.add(tile);
          System.out.println("i: " + i);
          // move tiles to the right after first and second tile
          // move down after second tile
          if (i == 0 || i == 2) {
            space_from_left += Tile.TILE_SIZE + SPACE_BETWEEN_TILES_IN_PXL;
          } else if (i == 1) {
            space_from_left = SPACE_FROM_LEFT_AND_TOP_EDGE_IN_PXL;
            space_from_top += Tile.TILE_SIZE + SPACE_BETWEEN_TILES_IN_PXL;
          }
      }
    }

  private ImageIcon getResizedRoundImageIcon(String path, int size) {
    URL imgURL1 = getClass().getClassLoader().getResource(path);
    ImageIcon icon1 = new ImageIcon(imgURL1);
    BufferedImage resizedimage = new BufferedImage(size, size,
        BufferedImage.TYPE_INT_RGB);
    Graphics2D g2 = resizedimage.createGraphics();
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g2.setClip(new Double(0, 0, size, size));
    g2.drawImage(icon1.getImage(), 0, 0, size, size, null);

    return new ImageIcon(resizedimage);
  }

}
