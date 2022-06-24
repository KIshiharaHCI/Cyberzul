package azul.team12.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class BagToDrawNewTiles extends Bag {

  private int initialNumberOfEachTile;
  private Tile[] listOfAllTileKinds;
  private BagToStoreUsedTiles box;

  public BagToDrawNewTiles(int initialNumberOfEachTile, Tile[] listOfAllTileKinds) {
    this.initialNumberOfEachTile = initialNumberOfEachTile;
    this.listOfAllTileKinds = listOfAllTileKinds;
    this.box = new BagToStoreUsedTiles();
    initializeContent();

    //TODO: TEST
    for (int i = 0; i < 20; i++) {
      System.out.println(content);
      System.out.println(drawRandomTile());
    }
  }

  /**
   * Draw a random tile from this bag and delete it from this bag.
   *
   * @return a random tile.
   */
  public Tile drawRandomTile() {
    if (content.size() == 0) {
      //the bag is emtpy, so it gets filled with the tiles that were stored in the "box"
      //in our implementation we call the box the "BagtoStoreUsedTiles"

      content.addAll(box.giveAllTilesBack());
      Collections.shuffle(content);

      //TODO: TEST
      System.out.println(content);
    }
    box.addTile(content.get(0));

    //List#remove is equal to the pop method of a stack
    return content.remove(0);
  }

  @Override
  void initializeContent() {
    ArrayList<Tile> contentList = new ArrayList<>();

    for (Tile tile : listOfAllTileKinds) {
      addTilesTo(tile, initialNumberOfEachTile, contentList);
    }

    //permutes the contentList list
    Collections.shuffle(contentList);
    this.content = contentList;
  }

  private void addTilesTo(Tile tile, int amount, List list) {
    for (int i = 0; i < amount; i++) {
      list.add(tile);
    }
  }
}
