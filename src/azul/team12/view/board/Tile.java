package azul.team12.view.board;

import javax.swing.*;
import java.util.Map;

public interface Tile {
  String BLACK_TILE_PATH = "img/black-tile.png";
  String BLUE_TILE_PATH = "img/blue-tile.png";
  String RED_TILE_PATH = "img/red-tile.png";
  String WHITE_TILE_PATH = "img/white-tile.png";
  String ORANGE_TILE_PATH = "img/yellow-tile.png";
  String STARTING_PLAYER_MARKER_PATH = "img/start-player-button.png";

  Map<String, String> pathList = Map.of(
          "black tile", BLACK_TILE_PATH,
          "blue tile", BLUE_TILE_PATH,
          "red tile", RED_TILE_PATH,
          "white tile", WHITE_TILE_PATH,
          "orange tile", ORANGE_TILE_PATH,
          "starting player marker", STARTING_PLAYER_MARKER_PATH
  );
  int TILE_SIZE = 30;

  /**
   * Set a Tile Image for the paint Method to call
   *
   * @param opacity how transparent the Tile should be.
   */
  void setIcon(Float opacity);

  /**
   * Returns the Column of where the Tile is placed.
   *
   * @return
   */
  int getColumn();

  /**
   * Returns the Row of where the Tile is placed.
   *
   * @return
   */
  int getRow();

  /**
   * Returns the JLabel where the ImageIcon of the Tile is added.
   *
   * @return
   */
  JLabel getLabel();
}