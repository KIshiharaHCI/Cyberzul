package azul.team12.controller;

import azul.team12.model.Bag;
import azul.team12.model.Tile;
import java.util.ArrayList;

public interface Controller {

  /**
   * Create a new player with a name.
   * @param name the name of the player.
   * @return <code>true</code> if the player was successfully created. <code>false</code> else.
   */
  boolean createPlayer(String name);

  /**
   * Signals the model that all players have entered their name.
   */
  void startGame();

  /**
   * The view wants the model to place all tiles on the manufacturing plates and the table center.
   * @return
   */
  ArrayList<Bag> getManufacturingPlatesAndTableCenter();

  /**
   * Returns the index of the player who has to make his turn.
   *
   * @return
   */
  int whosTurnIsIt();

  /**
   * Tells the model that the player made his turn.
   * Ideally, the model informs the view via a notifyListeners Method what operations the player
   * has to do before he can finish his turn.
   *
   * @return <code>false</code> if the player still has to do something before the turn can be
   * finished. <code>true</code> else.
   */
  boolean turnFinished();

  /**
   * Informs the model that a player chose a tile from a manufacturing plate or from the table
   * center.
   * The model should inform the view with an error message via notify listeners, if the player
   * can't place it on a patter line.
   *
   * @param tile
   * @param indexOfBag
   */
  void chooseTileFrom(Tile tile,int indexOfBag);

  /**
   * Informs the model on which pattern row the player choose to place his tile.
   *
   * @param indexOfPatternLine
   * @return
   */
  boolean placeTileAtPatternLine(int indexOfPatternLine);
}
