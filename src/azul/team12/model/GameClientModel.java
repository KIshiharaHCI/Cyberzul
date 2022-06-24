package azul.team12.model;

import java.util.ArrayList;

public class GameClientModel {

  private ArrayList<Player> playerList;


  public GameClientModel(ArrayList<Player> namesOfPlayers){

    this.playerList = namesOfPlayers;

    /*
    // todo: to be deleted:
    loginWithName("Fritz");

     */
  }

  public void loginWithName(String nickname ) {
    Player newPlayer = new Player(nickname);
    playerList.add(newPlayer);
  }

}
