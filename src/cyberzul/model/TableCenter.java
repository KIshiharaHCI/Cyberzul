package cyberzul.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * "Tischmitte" One of the two places the player can draw tiles from.
 */
public class TableCenter extends Offering {

  private static TableCenter instance;
  ArrayList<ModelTile> content;

  TableCenter() {
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

  /*
  justification = "This is a Singleton, there will be no multiple instances."
   */
  @Override
  @edu.umd.cs.findbugs.annotations.SuppressFBWarnings(value = "ST_WRITE_TO_STATIC_FROM"
      + "_INSTANCE_METHOD", justification = "This is a Singleton, there will be no multiple "
      + "instances.")
  void initializeContent() {
    this.content = new ArrayList<>();
  }

  /**
   * Add a tile to the TableCenter.
   */
  void addTile(ModelTile tile) {
    content.add(tile);
  }

  /**
   * Chose a tile on this TableCenter via its index. Return every tile that has the same type and is
   * on this TableCenter aswell. If the TableCenter contains the StartingPlayerMarker, return this
   * one aswell. Then delete every tile that was returned from this TableCenter.
   *
   * @param indexOfTheTile the index of the tile on the table center.
   * @return the tiles that will move to the pattern lines.
   */
  List<ModelTile> takeTileWithIndex(int indexOfTheTile) {
    //The player can only choose "real" tiles. The starting player marker is not a tile, but a
    //marker. So if the player chooses the first tile (index 0), but the first element in
    //this.content is the starting player marker, the index has to be raised by 1.
    if (content.contains(ModelTile.STARTING_PLAYER_MARKER)) {
      indexOfTheTile++;
    }

    ModelTile chosenColor = content.get(indexOfTheTile);
    ArrayList<ModelTile> returnedTiles = new ArrayList<>();

    Iterator<ModelTile> contentIterator = content.iterator();
    while (contentIterator.hasNext()) {
      ModelTile currentTile = contentIterator.next();
      if (currentTile == chosenColor
          || currentTile == ModelTile.STARTING_PLAYER_MARKER) {
        returnedTiles.add(currentTile);
      }
    }
    content.removeAll(returnedTiles);
    return returnedTiles;
  }

  @Override
  public List<ModelTile> getContent() {
    List<ModelTile> returnList = new ArrayList<>();
    for (ModelTile t : content) {
      returnList.add(t);
    }
    return returnList;
  }

  public void addStartPlayerMarker() {
    content.add(ModelTile.STARTING_PLAYER_MARKER);
  }
}
