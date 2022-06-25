package azul.team12.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * "Tischmitte"
 * One of the two places the player can draw tiles from.
 */
public class TableCenter extends Offering {

  private static TableCenter instance;
  private ArrayList<Tile> content;

  private TableCenter() {
    super();
  }

  /**
   * This method is used so this class can be a Singleton: Only one single instance of this class
   * can be created. If it already exists, the existing instance is returned.
   *
   * @return a new TableCenter, if it doesn't exist already. The instance to the existing one else.
   */
  static synchronized TableCenter getInstance() {
    if (instance == null) {
      instance = new TableCenter();
    }
    return instance;
  }

  @Override
  void initializeContent() {
    this.content = new ArrayList<>();
    content.add(Tile.STARTING_PLAYER_MARKER);
  }

  /**
   * Add a tile to the TableCenter.
   */
  void addTile(Tile tile) {
    content.add(tile);
  }

  /**
   * Chose a tile on this TableCenter via its index. Return every tile that has the same type and is
   * on this TableCenter aswell. If the TableCenter contains the StartingPlayerMarker, return this
   * one aswell.
   * Then delete every tile that was returned from this TableCenter.
   *
   * @param indexOfTheTile
   * @return
   */
  List<Tile> takeTileWithIndex(int indexOfTheTile) {
    //The player can only choose "real" tiles. The starting player marker is not a tile, but a
    //marker. So if the player chooses the first tile (index 0), but the first element in
    //this.content is the starting player marker, the index has to be raised by 1.
    if(content.contains(Tile.STARTING_PLAYER_MARKER)){
      indexOfTheTile++;
    }

    Tile chosenColor = content.get(indexOfTheTile);
    ArrayList<Tile> returnedTiles = new ArrayList<>();

    Iterator<Tile> contentIterator = content.iterator();
    while(contentIterator.hasNext()){
      Tile currentTile = contentIterator.next();
      if (currentTile == chosenColor
          || currentTile == Tile.STARTING_PLAYER_MARKER) {
        returnedTiles.add(currentTile);
      }
    }
    content.removeAll(returnedTiles);
    return returnedTiles;
  }

  @Override
  public List<Tile> getContent() {
    return (List<Tile>) content.clone();
  }
}
