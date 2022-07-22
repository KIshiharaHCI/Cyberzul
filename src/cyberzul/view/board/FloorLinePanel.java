package cyberzul.view.board;

import cyberzul.controller.Controller;
import cyberzul.model.ModelTile;
import cyberzul.view.listeners.TileClickListener;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.awt.Dimension;
import java.io.Serial;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A FloorLine of a given player.
 * One of the destinations where Tiles can be placed.
 */
public class FloorLinePanel extends JPanel {
  @Serial private static final long serialVersionUID = 4L;
  private final transient Controller controller;
  private static final int numberOfFloorTiles = 7;
  private final transient TileClickListener tileClickListener;
  private final int tileSize;
  private JPanel contentBottom;
  private JPanel contentUpper;

  /**
   * Constructor for a given FloorLine.
   *
   * @param userName name to be used when using the controller
   * @param controller used to call getter of floorline
   * @param tileClickListener used by Tiles to listen for mouseclick events
   * @param minusPoints sets the minus points of a player
   * @param tileSize Tilesize differs between ActivePlayerBoard and SmallPlayerBoard
   */
  @SuppressFBWarnings({"EI_EXPOSE_REP2", "EI_EXPOSE_REP2"})
  @SuppressWarnings(value = "EI_EXPOSE_REP")
  // this class needs these references to these mutable objects.
  public FloorLinePanel(
      String userName,
      Controller controller,
      TileClickListener tileClickListener,
      int minusPoints,
      int tileSize) {
    this.controller = controller;
    this.tileClickListener = tileClickListener;
    this.tileSize = tileSize;
    setProperties(tileSize, 2, 1, this);

    add(Box.createVerticalStrut(tileSize / 2));
    addBottomTilesRow(userName);
  }

  private void setProperties(int tileSize, int rows, int cols, JPanel panel) {
    panel.setOpaque(false);
    panel.setPreferredSize(
        new Dimension((tileSize + 2) * numberOfFloorTiles, (tileSize + 2) * rows));
    panel.setMaximumSize(new Dimension((tileSize + 2) * numberOfFloorTiles, (tileSize + 2) * rows));
    panel.setMinimumSize(new Dimension((tileSize + 2) * numberOfFloorTiles, (tileSize + 2) * rows));
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setAlignmentX(1.0f);
    panel.setAlignmentY(1.0f);
  }

  private void addBottomTilesRow(String userName) {
    contentBottom = new JPanel();
    ViewHelper.setPropertiesOfCurrentRow(tileSize, 7, 1, contentBottom);
    for (int col = 1; col <= numberOfFloorTiles; col++) {

      List<ModelTile> floorLineOfPlayer = controller.getFloorLineOfPlayer(userName);
      if (floorLineOfPlayer.size() >= col) {

        WallTile filledFloorLineTile =
            new WallTile(col, 1, floorLineOfPlayer.get(col - 1), tileSize, 1f);
        contentBottom.add(filledFloorLineTile);
      } else {
        DestinationTile emptyFloorLineTile =
            new DestinationTile(col, 1, ModelTile.EMPTY_TILE, tileClickListener, tileSize);
        contentBottom.add(emptyFloorLineTile);
      }
    }
    this.add(contentBottom);
  }

  private void addUpperNumbersRow() {
    contentUpper = new JPanel();
    ViewHelper.setPropertiesOfCurrentRow(tileSize, 7, 1, contentUpper);
    for (int i = 0; i < numberOfFloorTiles; i++) {
      String text;
      if (i < 2) {
        text = "-1";
      } else if (i > 4) {
        text = "-3";
      } else {
        text = "-2";
      }
      contentUpper.add(new JLabel(text));
    }
    this.add(contentUpper);
  }

  public void updateBottomTilesRow(String userName) {
    remove(contentBottom);
    addBottomTilesRow(userName);
  }
}
