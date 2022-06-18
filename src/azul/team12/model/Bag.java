package azul.team12.model;

import java.util.HashMap;
import java.util.Map;

public abstract class Bag {
  protected Map<Tile,Integer> content;

  private HashMap<Tile, Integer> createEmptyContent(){
    HashMap<Tile,Integer> content = new HashMap<>();
    content.put(Tile.BLACK_TILE,0);
    content.put(Tile.BLUE_TILE,0);
    content.put(Tile.WHITE_TILE,0);
    content.put(Tile.RED_TILE,0);
    content.put(Tile.ORANGE_TILE,0);
    return content;
  }

  public Bag(){
    this.content = createEmptyContent();
  }
}
