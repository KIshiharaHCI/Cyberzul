package cyberzul.model;

import cyberzul.network.client.ClientPlayer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Contains Tests for the Common Model Class.
 */
@SuppressWarnings("processing")
public class CommonModelTests {

  /**
   * Tests the case that a player has most points at the end of a game.
   */
  @Test
  public void testGetWinningMessage0() {
    //arrange
    GameModel model = new GameModel();
    ClientPlayer fritz = new ClientPlayer("Fritz");
    fritz.setPoints(3000);
    fritz.setWall(new boolean[][]{
        {true, true, true, true, true},
        {true, true, true, true, true},
        {true, true, true, true, true},
        {true, true, true, true, true},
        {false, false, false, false, true}
    });
    model.playerList.add(fritz);
    ClientPlayer marco = new ClientPlayer("Marco");
    marco.setPoints(4000);
    marco.setWall(new boolean[][]{
        {true, true, true, true, true},
        {true, true, true, true, true},
        {true, true, true, true, true},
        {true, true, true, true, true},
        {false, false, false, false, true}
    });
    model.playerList.add(marco);
    ClientPlayer lola = new ClientPlayer("Lola");
    lola.setPoints(2000);
    lola.setWall(new boolean[][]{
        {true, true, true, true, true},
        {false, false, false, false, false},
        {true, true, true, true, true},
        {false, false, false, false, false},
        {false, false, false, false, false}
    });
    model.playerList.add(lola);
    for (Player player : model.playerList) {
      player.addEndOfGamePoints();
    }
    String correctReturn = "Hurray! Marco has won the game! You shall be allowed to help Queen "
        + "MaXIne build her Cyber Palace with our beautiful cyber tiles!";

    // act
    model.getWinningMessage();

    //assert
    Assertions.assertEquals(correctReturn, model.getWinningMessage());

  }


  /**
   * Tests the case that two players have the same points, but different horizontally
   * complete lines at the end of a game.
   */
  @Test
  public void testGetWinningMessage1() {
    //arrange
    GameModel model = new GameModel();
    ClientPlayer fritz = new ClientPlayer("Fritz");
    fritz.setPoints(3002);
    fritz.setWall(new boolean[][]{
        {true, true, true, true, true},
        {false, true, false, false, false},
        {true, true, true, true, true},
        {true, true, true, true, true},
        {false, true, false, false, true}
    });
    model.playerList.add(fritz);
    ClientPlayer marco = new ClientPlayer("Marco");
    marco.setPoints(3000);
    marco.setWall(new boolean[][]{
        {true, true, true, true, true},
        {true, true, true, true, true},
        {true, true, true, true, true},
        {true, true, true, true, true},
        {false, true, false, false, false}
    });
    model.playerList.add(marco);
    ClientPlayer lola = new ClientPlayer("Lola");
    lola.setPoints(2000);
    lola.setWall(new boolean[][]{
        {true, true, true, true, true},
        {false, false, false, false, false},
        {true, true, true, true, true},
        {false, false, false, false, false},
        {false, false, false, false, false}
    });
    model.playerList.add(lola);
    for (Player player : model.playerList) {
      player.addEndOfGamePoints();
    }
    String correctReturn = "Hurray! Marco has won the game! You shall be allowed to help Queen "
        + "MaXIne build her Cyber Palace with our beautiful cyber tiles!";

    // act
    model.getWinningMessage();

    //assert
    Assertions.assertEquals(correctReturn, model.getWinningMessage());

  }

  /**
   * Tests the case that two players have the same points and same horizontally
   * complete lines at the end of a game.
   */
  @Test
  public void testGetWinningMessage2() {
    //arrange
    GameModel model = new GameModel();
    ClientPlayer fritz = new ClientPlayer("Fritz");
    fritz.setPoints(3000);
    fritz.setWall(new boolean[][]{
        {true, true, true, true, true},
        {true, true, true, true, true},
        {true, true, true, true, true},
        {true, true, true, true, true},
        {false, false, false, false, true}
    });
    model.playerList.add(fritz);
    ClientPlayer marco = new ClientPlayer("Marco");
    marco.setPoints(3000);
    marco.setWall(new boolean[][]{
        {true, true, true, true, true},
        {true, true, true, true, true},
        {true, true, true, true, true},
        {true, true, true, true, true},
        {false, false, false, false, true}
    });
    model.playerList.add(marco);
    ClientPlayer lola = new ClientPlayer("Lola");
    lola.setPoints(2000);
    lola.setWall(new boolean[][]{
        {true, true, true, true, true},
        {false, false, false, false, false},
        {true, true, true, true, true},
        {false, false, false, false, false},
        {false, false, false, false, false}
    });
    model.playerList.add(lola);
    for (Player player : model.playerList) {
      player.addEndOfGamePoints();
    }
    String correctReturn = "It is a tie! The victory is shared between: \n"
        + fritz.getName()
        + "\n"
        + marco.getName()
        + "\nHurray!!!! All of you shall be allowed to help Queen MaXIne build her Cyber Palace "
        + "with our beautiful cyber tiles!";

    // act
    model.getWinningMessage();

    //assert
    Assertions.assertEquals(correctReturn, model.getWinningMessage());

  }

}
