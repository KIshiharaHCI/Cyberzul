package azul.team12;

import azul.team12.model.BagToDrawNewTiles;
import azul.team12.model.ClientNetworkConnection;
import azul.team12.model.GameClientModel;
import azul.team12.model.Player;
import azul.team12.model.Tile;
import java.util.ArrayList;

public class AzulMain {
  public static void main(String[] args) {

    ArrayList<Player> playerList = new ArrayList<>();

    GameClientModel model = new GameClientModel(playerList);

  }
}
