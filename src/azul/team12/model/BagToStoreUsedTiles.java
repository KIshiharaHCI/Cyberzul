package azul.team12.model;

import java.util.ArrayList;
import java.util.List;

public class BagToStoreUsedTiles extends Bag {

  private static BagToStoreUsedTiles instance;

  /**
   * Has to be private. That is important for the Singleton Design Pattern.
   */
  private BagToStoreUsedTiles() {
    super();
  }

  /**
   * This method is used so this class can be a Singleton: Only one single instance of this class
   * can be created. If it already exists, the existing instance is returned.
   *
   * @return a new BagToStoreUsedTiles, if it doesn't exist already. The instance to the existing one else.
   */
  public static synchronized BagToStoreUsedTiles getInstance() {
    if (instance == null) {
      instance = new BagToStoreUsedTiles();
    }
    return instance;
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
  public void addTile(Tile tile) {
    content.add(tile);
  }

  /**
   * This method gets invoked if the BagToDrawNewTiles has no tiles left. Every tile that was
   * stored in this bag gets returned and this bag is subsequently empty.
   *
   * @return all Tiles that have been temporarily stored in here.
   */
  public List<Tile> giveAllTilesBack() {
    List<Tile> returnList = content;
    content = new ArrayList<>();
    return returnList;
  }
}
