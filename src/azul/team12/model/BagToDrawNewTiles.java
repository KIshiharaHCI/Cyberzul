package azul.team12.model;

import java.util.HashMap;
import java.util.Random;

public class BagToDrawNewTiles extends Bag{
  public BagToDrawNewTiles(){
    super();
  }

  /**
   * Draw a random tile from this bag.
   * @return a random tile.
   */
  public Tile drawRandomTile(){
    //TODO: Substract this Tile from the remaining ones in the HashMap.
    HashMap<Tile,Integer> bagToDraw = initializeContent();
    Tile drawTile = null;
    Random rd = new Random();
    int randomInt = rd.nextInt(4);
    switch (randomInt) {
      case 0 -> {
        drawTile = Tile.BLACK_TILE;
        bagToDraw.remove(Tile.BLACK_TILE);
      }
      case 1 -> {
        drawTile = Tile.BLUE_TILE;
        bagToDraw.remove(Tile.BLUE_TILE);
      }
      case 2 -> {
        drawTile = Tile.ORANGE_TILE;
        bagToDraw.remove(Tile.ORANGE_TILE);
      }
      case 3 -> {
        drawTile = Tile.RED_TILE;
        bagToDraw.remove(Tile.RED_TILE);
      }
      case 4 -> {
        drawTile = Tile.WHITE_TILE;
        bagToDraw.remove(Tile.WHITE_TILE);
      }
      default -> {
        //do nothing
      }

    }
    return drawTile;
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
