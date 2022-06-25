package azul.team12.model;

import java.util.ArrayList;

public class GameClientModel {

  private ArrayList<Player> playerList;


  public GameClientModel(ArrayList<Player> namesOfPlayers) {

    this.playerList = namesOfPlayers;

    /*
    // todo: to be deleted:
    loginWithName("Fritz");

     */

    //TODO:TEST METHOD FOR THE BAGS
    testTheBags();
  }

  public void loginWithName(String nickname) {
    Player newPlayer = new Player(nickname);
    playerList.add(newPlayer);
  }

  //TODO:TEST METHOD FOR THE BAGS
  /**
   * Test method in Order to test the bags.
   */
  private void testTheBags() {
    ArrayList<FactoryDisplay> factoryDisplays = new ArrayList<>();
    for (int i = 0; i < 9; i++) {
      factoryDisplays.add(new FactoryDisplay());
    }
    System.out.println("Content of first Factory Display: " + factoryDisplays.get(0).getContent());
    System.out.println("Content of the Table Center " + TableCenter.getInstance().getContent());
    System.out.println(
        "Take tile with index 1 (second tile) and all tiles from the same kind from the first " +
            "Factory Display " +
            factoryDisplays.get(0).takeTileWithIndex(1));
    System.out.println("Content of the first Factory Display " + factoryDisplays.get(0).getContent());
    System.out.println("Content of the Table Center " + TableCenter.getInstance().getContent());

    System.out.println("Take tile with index 0 and all tiles from the same kind from the " +
        "Table Center " + TableCenter.getInstance().takeTileWithIndex(0));

    System.out.println("Content of the Table Center " + TableCenter.getInstance().getContent());
  }

}
