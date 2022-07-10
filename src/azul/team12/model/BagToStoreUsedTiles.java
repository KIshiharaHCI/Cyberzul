package azul.team12.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the "lid of the game box" from the Azul Game. This is where used tiles are moved.
 */
public class BagToStoreUsedTiles extends Bag {

  private static BagToStoreUsedTiles instance;
  private ArrayList<ModelTile> content;

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
   * @return a new BagToStoreUsedTiles, if it doesn't exist already. The instance to the existing
   * one else.
   */
  static synchronized BagToStoreUsedTiles getInstance() {
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
  void addTile(ModelTile tile) {
    content.add(tile);
  }

  /**
   * This method gets invoked if the BagToDrawNewTiles has no tiles left. Every tile that was stored
   * in this bag gets returned and this bag is subsequently empty.
   *
   * @return all Tiles that have been temporarily stored in here.
   */
  List<ModelTile> giveAllTilesBack() {
    List<ModelTile> returnList = content;
    content = new ArrayList<>();
    return returnList;
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
