package azul.team12;

import azul.team12.model.ClientNetworkConnection;
import azul.team12.model.GameClientModel;
import azul.team12.view.AzulView;
import azul.team12.view.board.GameBoard;
import azul.team12.model.Player;
import java.util.ArrayList;

public class AzulMain {
  public static void main(String[] args){
    AzulView azulView = new AzulView();
    azulView.setVisible(true);

    //TODO: initialize and start view
    //TODO: in view: two buttons: "Local Hot Seat Mode" and "Play via Network-Connection"
    //implement with card layout
    //TODO: probably needs to be deleted:
    ArrayList<Player> playerList = new ArrayList<>();

    GameClientModel model = new GameClientModel(playerList);
    //ClientNetworkConnection clientNetworkConnection = new ClientNetworkConnection(model);



  }
}
