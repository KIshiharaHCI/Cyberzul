package cyberzul.model;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a Factory Display that can be manipulated for test purposes. It's better than to change
 * access modifiers or tinker with the original classes, because one might forget to change it back.
 * Also, by changing it back, the test class might not compile anymore.
 */
@SuppressWarnings("processing")

public class ManipulableFactoryDisplay extends FactoryDisplay {

  private ArrayList<ModelTile> content;

  /**
   * Create a Manipulable Factory Display.
   *
   * @param tiles the tiles that it should contain.
   */
  public ManipulableFactoryDisplay(ModelTile[] tiles) {
    manipulateContent(tiles);
  }

  /**
   * Create a ManipulableFactoryDisplay with only one color of tiles.
   *
   * @param tile all tiles in this Offering should have only this color.
   */
  public ManipulableFactoryDisplay(ModelTile tile) {
    ModelTile[] tiles = new ModelTile[4];
    for (int i = 0; i < tiles.length; i++) {
      tiles[i] = tile;
    }
    manipulateContent(tiles);
  }

  /**
   * This method can be used to change the content of a Manipulable Factory Display. It uses an
   * array instead of List, because that way it's easier to set them up in the test classes.
   *
   * @param tiles the tiles that this FactoryDisplay should hold.
   */
  void manipulateContent(ModelTile[] tiles) {
    ArrayList<ModelTile> manipulatedContent = new ArrayList<>();
    for (int i = 0; i < tiles.length; i++) {
      manipulatedContent.add(tiles[i]);
    }
    content = manipulatedContent;
  }

  /**
   * This method does not delete the content of this MFD!! So it can be picked from again and
   * again.
   *
   * @param indexOfTheTile the index of the Tile that should be chosen, e.g. the first tile (Index
   *                       0)
   * @return all tiles from this factory display that have the same color as the specified one.
   */
  @Override
  List<ModelTile> takeTileWithIndex(int indexOfTheTile) {
    ModelTile chosenColor = content.get(indexOfTheTile);
    ArrayList<ModelTile> returnedTiles = new ArrayList<>();
    for (ModelTile tile : content) {
      if (tile == chosenColor) {
        returnedTiles.add(tile);
      } else {
        TableCenter.getInstance().addTile(tile);
      }
    }
    return returnedTiles;
  }

  @Override
  @SuppressFBWarnings("EI_EXPOSE_REP")
  //this class is only a storage for information that the server sends to the client so the view
  //can access this information later. content is indeed a mutable object, but it doesn't matter
  //because the Model doesn't store a reference to it itself.
  public List<ModelTile> getContent() {
    return content;
  }
}
