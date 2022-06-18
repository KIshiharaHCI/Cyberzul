package azul.team12.model;

public class BagToDrawNewTiles extends Bag{
  public BagToDrawNewTiles(){
    super();
    content.put(Tile.BLACK_TILE,20);
    content.put(Tile.BLUE_TILE,20);
    content.put(Tile.WHITE_TILE,20);
    content.put(Tile.RED_TILE,20);
    content.put(Tile.ORANGE_TILE,20);
  }

  /**
   * Draw a random tile from this bag.
   * @return a random tile.
   */
  public Tile drawRandomTile(){
    //TODO:Substract this Tile from the remaining ones in the HashMap.
    return null;
  }
}
