package azul.team12.view.board;

import azul.team12.controller.Controller;
import azul.team12.model.ModelTile;
import azul.team12.view.listeners.TileClickListener;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FloorLinePanel extends JPanel {

  private final Controller controller;
  private final int NUMBER_OF_FLOOR_TILES = 7;
  private TileClickListener tileClickListener;
  private JPanel contentBottom, contentUpper;

  public FloorLinePanel(Controller controller, TileClickListener tileClickListener,
      int minusPoints) {
    this.controller = controller;
    this.tileClickListener = tileClickListener;
    setProperties(Tile.TILE_SIZE, 2, 1, this);
    setBackground(new Color(110, 150, 100));
    // add(new JLabel("   Minus Points: " + minusPoints));
//    JButton floorLineButton = new JButton("Floor Line");
//    floorLineButton.addActionListener(e -> {
//      controller.placeTileAtFloorLine();
//      controller.endTurn(controller.getNickOfActivePlayer());
//    });
    // add(floorLineButton);
    addUpperNumbersRow();
    addBottomTilesRow();
  }

  private void setProperties(int tileSize, int rows, int cols, JPanel panel) {
    panel.setBackground(new Color(110, 150, 100));
    panel.setPreferredSize(
        new Dimension((tileSize + 2) * NUMBER_OF_FLOOR_TILES, (tileSize + 2) * rows));
    panel.setMaximumSize(
        new Dimension((tileSize + 2) * NUMBER_OF_FLOOR_TILES, (tileSize + 2) * rows));
    panel.setMinimumSize(
        new Dimension((tileSize + 2) * NUMBER_OF_FLOOR_TILES, (tileSize + 2) * rows));
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setAlignmentX(1.0f);
    panel.setAlignmentY(1.0f);
  }

  private void addBottomTilesRow() {
    contentBottom = new JPanel();
    ViewHelper.setPropertiesOfCurrentRow(Tile.TILE_SIZE, 7, 1, contentBottom);
    for (int col = 1; col <= NUMBER_OF_FLOOR_TILES; col++) {

      List<ModelTile> floorLineOfPlayer = controller.getFloorLineOfPlayer(controller.getNickOfActivePlayer());
      if (floorLineOfPlayer.size() >= col) {
        WallTile filledFloorLineTile = new WallTile(col,1, floorLineOfPlayer.get(col - 1), 1f);
        contentBottom.add(filledFloorLineTile);
      } else {
        DestinationTile emptyFloorLineTile = new DestinationTile(col, 1, ModelTile.EMPTY_TILE, tileClickListener);
        contentBottom.add(emptyFloorLineTile);
      }
    }
    this.add(contentBottom);
  }

  private void addUpperNumbersRow() {
    contentUpper = new JPanel();
    ViewHelper.setPropertiesOfCurrentRow(Tile.TILE_SIZE, 7, 1, contentUpper);
    for (int i = 0; i < NUMBER_OF_FLOOR_TILES; i++) {
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

  public void updateBottomTilesRow() {
    remove(contentBottom);
    addBottomTilesRow();
  }
}
