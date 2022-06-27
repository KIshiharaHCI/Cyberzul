package azul.team12;

import azul.team12.model.GameModel;
import azul.team12.view.AzulView;
import azul.team12.model.Player;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class AzulMain {


  private static final Logger LOGGER = Logger.getLogger("AzulMain");

  public static void main(String[] args){

    try {

      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
      LocalDateTime now = LocalDateTime.now();

      // This block configure the logger with handler and formatter
      FileHandler fh = new FileHandler("log/"+now+".log");
      LOGGER.addHandler(fh);
      SimpleFormatter formatter = new SimpleFormatter();
      fh.setFormatter(formatter);

      System.out.println("Calling");
      LOGGER.info("My first Logger");
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    AzulView azulView = new AzulView();
    //azulView.setVisible(true);

    ArrayList<Player> playerList = new ArrayList<>();

    GameModel model = new GameModel(playerList);

    LOGGER.info("A much later logger");

    //LOGGER.info("This is my first lof4j's statement");


  }
}
