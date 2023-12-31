package cyberzul.model;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ArrayList;

/**
 * The ClientModel uses this kind of Factory Displays to store data. Since every ClientModel stores
 * its own data, and since they are updated regularly by the server, they are allowed to have the
 * additional method "setConent()". In the worst case, a ClientModel is manipulating the data that
 * it sees itself. By this, the server where the game actually runs is not attacked.
 *
 * <p>Creating more than one TableCenter infringes the Singleton-Design Pattern, but only in the
 * ClientModels (where it has to be infringed anyway, because different ClientModels can't share the
 * same TableCenter.
 */
public class ClientTableCenter extends TableCenter {

  /**
   * Method to set and manipulate the data that is stored in this kind of TableCenter.
   *
   * @param content the tiles that are stored in this Offering.
   */
  @SuppressFBWarnings("EI_EXPOSE_REP2")
  //this class is only a storage for information that the server sends to the client so the view
  //can access this information later. content is indeed a mutable object, but it doesn't matter
  //because the ClientModel doesn't store a reference to it itself.
  public void setContent(ArrayList<ModelTile> content) {
    this.content = content;
  }
}
