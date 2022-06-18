package azul.team12;

import azul.team12.model.ClientNetworkConnection;
import azul.team12.model.GameClientModel;

public class AzulMain {
  public static void main(String[] args){
    System.out.println("Marco und Xue sind toll");
    GameClientModel model = new GameClientModel();
    ClientNetworkConnection clientNetworkConnection = new ClientNetworkConnection(model);
  }
}
