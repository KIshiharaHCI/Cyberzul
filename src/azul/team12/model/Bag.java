package azul.team12.model;

import java.util.HashMap;
import java.util.Map;

public abstract class Bag {
  Map<Tile,Integer> content;

  public Bag(){
    this.content = initializeContent();
  }

  HashMap<Tile, Integer> initializeContent(){
    HashMap<Tile,Integer> content = new HashMap<>();
    content.put(Tile.BLACK_TILE,0);
    content.put(Tile.BLUE_TILE,0);
    content.put(Tile.WHITE_TILE,0);
    content.put(Tile.RED_TILE,0);
    content.put(Tile.ORANGE_TILE,0);
    return content;
  }
}
