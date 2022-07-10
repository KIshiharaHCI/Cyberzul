package azul.team12.model;

import java.util.ArrayList;

/**
 * The ClientModel uses this kind of Factory Displays to store data.
 * Since every ClientModel stores its own data, and since they are updated regularly by the server,
 * they are allowed to have the additional method "setConent()". In the worst case, a ClientModel
 * is manipulating the data that it sees itself. By this, the server where the game actually runs
 * is not attacked.
 */
public class ClientFactoryDisplay extends FactoryDisplay{

  /**
   * Method to set and manipulate the data that is stored in this kind of FactoryDisplay.
   *
   * @param content the tiles that are stored in this Offering.
   */
  public void setContent(ArrayList<ModelTile> content){
    this.content = content;
  }
}
