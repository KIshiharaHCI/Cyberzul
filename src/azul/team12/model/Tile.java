package azul.team12.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The Tiles that the player can choose from
 */
public enum Tile {
  RED_TILE("red tile"), BLACK_TILE("black tile"), WHITE_TILE("white tile"), BLUE_TILE("blue tile"),
  ORANGE_TILE("orange tile"), EMPTY_TILE("empty"),
  STARTING_PLAYER_MARKER("starting player marker");

  String name;

  Tile(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return this.name;
  }

  /**
   * The method Tile.values() returns all values of this enum. Since Empty Tile is not a real tile,
   * it often makes sense to exclude it from the list of tiles.
   * Same goes for the StartingPlayerMarker.
   *
   * @return the values of all tileable Tiles, that can be tiled to the wall.
   */
  public static List<Tile> valuesOfTilableTiles() {
    Tile[] allValues = Tile.values();
    List<Tile> valuesOfTilesThatCanBeTiled = new ArrayList<>();
    for (int i = 0; i < allValues.length; i++) {
      if (allValues[i] != Tile.EMPTY_TILE &&
          allValues[i] != Tile.STARTING_PLAYER_MARKER) {
        valuesOfTilesThatCanBeTiled.add(allValues[i]);
      }
    }
    return valuesOfTilesThatCanBeTiled;
  }

}
