package azul.team12.model;

import java.util.ArrayList;
import java.util.List;

/**
 * "Manufakturplättchen"
 * One of the two places the player can draw tiles from.
 */
public class FactoryDisplay extends Offering {

  private static final int INITIAL_NUMBER_OF_TILES = 4;
  private ArrayList<ModelTile> content;

  FactoryDisplay() {
    super();
  }

  @Override
  void initializeContent() {
    content = new ArrayList<>();
    BagToDrawNewTiles bagToDrawNewTiles = BagToDrawNewTiles.getInstance();
    for (int i = 0; i < INITIAL_NUMBER_OF_TILES; i++) {
      content.add(bagToDrawNewTiles.drawRandomTile());
    }
  }

  /**
   * The Tiles on this Factory Display are saved in a List. By telling this Factory Display the index of the tile that
   * should be chosen, it returns all tiles of the same color and puts the rest on the table center.
   *
   * @param indexOfTheTile the index of the Tile that should be chosen, e.g. the first tile (Index 0)
   * @return all tiles of the same color on this plate.
   */
  @Override
  List<ModelTile> takeTileWithIndex(int indexOfTheTile) {
    ModelTile chosenColor = content.get(indexOfTheTile);
    ArrayList<ModelTile> returnedTiles = new ArrayList<>();
    for (ModelTile tile : content) {
      if (tile == chosenColor) {
        returnedTiles.add(tile);
      } else {
        TableCenter.getInstance().addTile(tile);
      }
    }
    //delete the tiles from this Factory Display
    content = new ArrayList<>();

    return returnedTiles;
  }

  @Override
  public List<ModelTile> getContent() {
    List<ModelTile> returnList = new ArrayList<>();
    for (ModelTile t : content) {
      returnList.add(t);
    }
    return returnList;
  }
}
