package azul.team12.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Bag {

  List<Tile> content;

  public Bag(){
    this.content = initializeContent();
  }

  //TODO: umändern auf Liste (ArrayList<Tile>)
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
