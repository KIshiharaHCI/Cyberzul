package azul.team12.model;

import java.util.ArrayList;

/**
 * "Manufakturplättchen"
 * One of the two places the player can draw tiles from.
 */
public class ManufacturingPlates extends Bag {

  public final int INITIAL_NUMBER_OF_TILES = 4;

  public ManufacturingPlates() {
    super();
  }

  //TODO: ArrayList mit 4 Tiles

  @Override
  void initializeContent() {
    content = new ArrayList<>();
    BagToDrawNewTiles bagToDrawNewTiles = BagToDrawNewTiles.getInstance();
    for(int i = 0; i < INITIAL_NUMBER_OF_TILES; i++){
      content.add(bagToDrawNewTiles.drawRandomTile());
    }
  }

}
