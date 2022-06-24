package azul.team12.controller;

import azul.team12.model.Bag;
import azul.team12.model.Tile;
import java.util.List;

/**
 * //TODO: The information about the implementation insiede the model gets deleted later on.
 */
public interface Controller {

  /**
   * Try to create a new player with a name.
   * Model informs the view via a notifyListener Event, if that was successful.
   *
   * @param name the name of the player.
   * @return <code>true</code> if the player was successfully created. <code>false</code> else.
   */
  void addPlayer(String name);

  /**
   * Signals the model that all players have entered their name.
   * The model should then initialize everything and starts the game. Players, Bags.
   *
   */
  void startGame();

  /**
   * Gives the view the information about the manufacturing plates and the table center.
   * Tiles are saved in the Bags as ArrayList<Tile>
   *
   *
   * @return
   */
  List<Bag> getManufacturingPlates();

  /**
   * Return the table center.
   *
   * @return the table center.
   */
  Bag getTableCenter();

  /**
   * Returns the Name of the player who has to make his turn.
   *
   * @return
   */
  String whosTurnIsIt();

  /**
   * Return
   * @param playerName
   * @return
   */
  int getPoints(String playerName);

  int getNegativeTiles(String playerName);

  boolean[][] getWall(String playerName);

  Tile[][] getPatternRows(String playerName);


  /**
   * Tells the model that the player made his turn.
   * Ideally, the model informs the view via a notifyListeners Method what operations the player
   * has to do before he can finish his turn.
   */
  void endTurn();

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
