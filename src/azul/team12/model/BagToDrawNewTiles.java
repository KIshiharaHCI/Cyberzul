package azul.team12.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the "bag" from the Azul Game. Players are drawing new tiles each round randomly from
 * this bag to put them on the "factory displays".
 */
public class BagToDrawNewTiles extends Bag {

  public static final int INITIAL_NUMBER_OF_EACH_TILE = 20;
  private static BagToDrawNewTiles instance;
  private ArrayList<ModelTile> content;
  private BagToStoreUsedTiles box;

  /**
   * Has to be private. That is important for the Singleton Design Pattern.
   */
  private BagToDrawNewTiles() {
    super();
    this.box = BagToStoreUsedTiles.getInstance();
  }

  /**
   * This method is used so this class can be a Singleton: Only one single instance of this class
   * can be created. If it already exists, the existing instance is returned.
   *
   * @return a new BagToDrawNewTiles, if it doesn't exist already. Else instance of existing.
   */
  static synchronized BagToDrawNewTiles getInstance() {
    if (instance == null) {
      instance = new BagToDrawNewTiles();
    }
    return instance;
  }

  /**
   * Draw a random tile from this bag and delete it from this bag.
   *
   * @return a random tile.
   */
  ModelTile drawRandomTile() {
    if (content.size() == 0) {
      //the bag is emtpy, so it gets filled with the tiles that were stored in the "box"
      //in our implementation we call the box the "BagtoStoreUsedTiles"

      if(box.getContent().size() > 0){
        content.addAll(box.giveAllTilesBack());
      }
      else{
        //there are only 100 tiles in the game (that is said by the rule book).
        //But it is possible (although unlikely) that more than 100 tiles are needed because the
        //walls and pattern lines are filled in such an unfortunate manner. For that case, we create
        //another 100 tiles, so the game fun does not end.
        initializeContent();
      }
      Collections.shuffle(content);
    }

    //List#remove is equal to the pop method of a stack
    return content.remove(0);
  }

  @Override
  void initializeContent() {
    ArrayList<ModelTile> contentList = new ArrayList<>();

    List<ModelTile> kindsOfTilableTiles = ModelTile.valuesOfTilableTiles();
    for (ModelTile tile : kindsOfTilableTiles) {
      addTilesToThisBag(tile, INITIAL_NUMBER_OF_EACH_TILE, contentList);
    }

    //permutes the contentList list
    Collections.shuffle(contentList);
    this.content = contentList;
  }

  /**
   * Add the constant number of tiles to the bag.
   *
   * @param tile   - enum of a tileable tile
   * @param amount - number of tiles (constant)
   * @param list   - the content list of this bag.
   */
  private void addTilesToThisBag(ModelTile tile, int amount, List<ModelTile> list) {
    for (int i = 0; i < amount; i++) {
      list.add(tile);
    }
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
