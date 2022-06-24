package azul.team12.model;

import java.util.ArrayList;
import java.util.List;

public class BagToStoreUsedTiles extends Bag{

  public BagToStoreUsedTiles(){
    initializeContent();
  }

  @Override
  void initializeContent() {
    content = new ArrayList<>();
  }

  /**
   * This method gets invoked if a Tile should be put into this bag.
   *
   * @param tile the tile that is temporarily stored in this box.
   */
  public void addTile(Tile tile){
    content.add(tile);
  }

  /**
   * This method gets invoked if the BagToDrawNewTiles has no tiles left. Every tile that was
   * stored in this bag gets returned and this bag is subsequently empty.
   *
   * @return all Tiles that have been temporarily stored in here.
   */
  public List<Tile> giveAllTilesBack(){
    List<Tile> returnList = content;
    content = new ArrayList<>();
    return returnList;
  }
}
