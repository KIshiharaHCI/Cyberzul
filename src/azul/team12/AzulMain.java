package azul.team12;

import azul.team12.model.GameModel;
import azul.team12.view.AzulView;
import azul.team12.model.Player;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class AzulMain {


  private static final Logger LOGGER = LogManager.getLogger(AzulMain.class.getName());


  public static void main(String[] args){

    LOGGER.info("Maybe my first Logger works?");
    AzulView azulView = new AzulView();
    //azulView.setVisible(true);

    ArrayList<Player> playerList = new ArrayList<>();

    GameModel model = new GameModel(playerList);

  }
}
