package azul.team12.model;

import azul.team12.network.client.ClientPlayer;
import java.util.ArrayList;

/**
 * Contains methods and fields that are common within Model classes.
 */
public abstract class CommonModel implements Model{

  protected int indexOfActivePlayer;
  protected ArrayList<Player> playerList = new ArrayList<>();
  protected ArrayList<Offering> offerings;
}
