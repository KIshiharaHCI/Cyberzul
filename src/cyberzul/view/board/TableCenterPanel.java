package cyberzul.view.board;

import cyberzul.controller.Controller;
import cyberzul.model.ModelTile;
import cyberzul.model.TableCenter;
import cyberzul.view.listeners.TileClickListener;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** The tiles on the center of the table ("Tischmitte") (Center Board class). */
@SuppressFBWarnings(
    value = "EI_EXPOSE_REP",
    justification =
        "We are aware that data "
            + "encapsulation is violated here and that this is in principle bad. However, "
            + "as here just information of the view is possible to be changed from an "
            + "external source and the model is safe, we think it is ok to "
            + "suppress this warning.")
public class TableCenterPanel extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;
  private static final int SPACE_FROM_LEFT_AND_TOP_EDGE_IN_PXL = 13;
  private static final int SPACE_BETWEEN_TILES_IN_PXL = 44;
  private static final int plateId = 0;
  private final transient Controller controller;
  private static final int spaceFromTopEdgeInPxl = 13;
  private static final int tableCenterSizeWidthInPxl = 1100;
  private static final int tableCenterHeightInPxl = 260;
  private final transient List<SourceTile> tileList;
  private JLabel tableCenterImageLabel;
  private transient TableCenter tableCenter;
  private Dimension panelDimension;
  private transient BufferedImage image;

  /**
   * Constructor for Table Center Panel.
   *
   * @param controller the Controller given to CyberzulView
   * @param tileClickListener listener class to be used when creating SourceTiles
   * @param panelDimension Dimensions of the parent Panel PlayerBoardAndTablePanel
   */
  public TableCenterPanel(
      Controller controller, TileClickListener tileClickListener, Dimension panelDimension) {
    this.controller = controller;
    this.tableCenter = (TableCenter) controller.getOfferings().get(0);
    this.tileList = new ArrayList<>();
    this.panelDimension = panelDimension;

    setBackground(new Color(80, 145, 250, 130));
    setTableCenterPanelSize();
    initialize(tileClickListener, tableCenter);

    try {
      URL imgUrl = getClass().getClassLoader().getResource("img/table-center.png");
      image = ImageIO.read(Objects.requireNonNull(imgUrl));
      image.getScaledInstance(panelDimension.width, panelDimension.height, Image.SCALE_DEFAULT);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Calculates the relative Size for "this" based on the Dimensions of
   * PlayerBoardAndTableCenterPanel.
   */
  private void setTableCenterPanelSize() {
    panelDimension =
        new Dimension((int) (panelDimension.getWidth() * 0.45), (int) (panelDimension.getHeight()));
    setMinimumSize(panelDimension);
    setMaximumSize(panelDimension);
  }

  /**
   * initializes the table center in the view in accordance with the model.
   *
   * @param tileClickListener listens to clicks on a tile.
   * @param tableCenter the instance of the table center.
   */
  public void initialize(TileClickListener tileClickListener, TableCenter tableCenter) {
    this.tableCenter = tableCenter;
    tableCenterImageLabel = new JLabel(getResizedImageIcon("img/table-center.png"));
    add(tableCenterImageLabel);
    tableCenterImageLabel.setBounds(
        0, 0, (int) panelDimension.getWidth(), (int) panelDimension.getHeight());
    List<ModelTile> modelTiles = tableCenter.getContent();
    for (int i = 0; i < modelTiles.size(); i++) {
      int col = i / 6 + 1;
      int row = i % 6 + 1;
      SourceTile tile =
          new SourceTile(
              col, row, modelTiles.get(i), i, plateId, Tile.NORMAL_TILE_SIZE, tileClickListener);
      // System.out.println("tile: " + tile);
      tileList.add(tile);
      tile.setBounds(
          SPACE_FROM_LEFT_AND_TOP_EDGE_IN_PXL + ((row - 1) * SPACE_BETWEEN_TILES_IN_PXL),
          spaceFromTopEdgeInPxl + SPACE_BETWEEN_TILES_IN_PXL * (col - 1),
          Tile.NORMAL_TILE_SIZE,
          Tile.NORMAL_TILE_SIZE);
      tableCenterImageLabel.add(tile);
    }
  }

  public void remove() {
    for (SourceTile tile : this.tileList) {
      this.tableCenterImageLabel.remove(tile);
    }
    this.remove(tableCenterImageLabel);
  }

  /**
   * Resizes according to the given height and width of the table center.
   *
   * @param path path of the icon.
   * @return ImageIcon with given width and height.
   */
  private ImageIcon getResizedImageIcon(String path) {
    URL imgUrl1 = getClass().getClassLoader().getResource(path);
    return new ImageIcon(
        new ImageIcon(imgUrl1)
            .getImage()
            .getScaledInstance(panelDimension.width, panelDimension.height, Image.SCALE_DEFAULT));
  }
}
