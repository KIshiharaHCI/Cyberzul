package azul.team12.model;

import java.util.HashMap;

public class BagToDrawNewTiles extends Bag{
  public BagToDrawNewTiles(){
    super();
  }

  /**
   * Draw a random tile from this bag.
   * @return a random tile.
   */
  public Tile drawRandomTile(){
    //TODO:Substract this Tile from the remaining ones in the HashMap.
    return null;
  }

  @Override
  HashMap<Tile, Integer> initializeContent(){
    HashMap<Tile,Integer> content = new HashMap<>();
    content.put(Tile.BLACK_TILE,20);
    content.put(Tile.BLUE_TILE,20);
    content.put(Tile.WHITE_TILE,20);
    content.put(Tile.RED_TILE,20);
    content.put(Tile.ORANGE_TILE,20);
    return content;
  }
}
