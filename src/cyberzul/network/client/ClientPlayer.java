package cyberzul.network.client;

import cyberzul.model.ModelTile;
import cyberzul.model.Player;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ArrayList;

/**
 * The Player data structure is too big and powerful for the needs of the ClientModel. This data
 * structure is only a storage for the values that are passed to the ClientModel by the server. The
 * server always makes sure to update the Clients about changes to the data so if the Client view
 * accesses the data, they can be sure that it is up-to-date.
 */
public class ClientPlayer extends Player {

  public ClientPlayer(String name) {
    super(name);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getPoints() {
    return points;
  }

  public void setPoints(int points) {
    this.points = points;
  }

  @SuppressFBWarnings("EI_EXPOSE_REP2")
  @SuppressWarnings(value = "EI_EXPOSE_REP")
  //this class is only a storage for information that the server sends to the client so the view
  //can access this information later. patternLines is indeed a mutable object, but it doesn't
  //matter because the ClientModel doesn't store a reference to it itself.
  public void setPatternLines(ModelTile[][] patternLines) {
    this.patternLines = patternLines;
  }

  //this class is only a storage for information that the server sends to the client so the view
  //can access this information later. floorLine is indeed a mutable object, but it doesn't matter
  //because the ClientModel doesn't store a reference to it itself.
  @SuppressFBWarnings("EI_EXPOSE_REP2")
  public void setFloorLine(ArrayList<ModelTile> floorLine) {
    this.floorLine = floorLine;
  }

  public void setHasStartingPlayerMarker(boolean hasStartingPlayerMarker) {
    this.hasStartingPlayerMarker = hasStartingPlayerMarker;
  }

}
