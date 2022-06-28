package azul.team12.controller;

import azul.team12.model.Bag;
import azul.team12.model.FactoryDisplay;
import azul.team12.model.Offering;
import azul.team12.model.TableCenter;
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
   * @return the factory displays
   */
  List<Offering> getFactoryDisplays();

  /**
   * Return the table center.
   *
   * @return the table center.
   */
  Offering getTableCenter();

  /**
   * Returns the Name of the player who has to make his turn.
   *
   * @return
   */
  String getNickOfActivePlayer();

  /**
   * Return the points that the player has.
   *
   * @param playerName
   * @return
   */
  int getPoints(String playerName);

  /**
   * Informs the model that a player chose a tile from a manufacturing plate or from the table
   * center.
   * The model should inform the view with an error message via notify listeners, if the player
   * can't place it on a patter line.
   *
   * @param playerName the name of the player who makes his move.
   * @param indexOfTile which of the tiles on the Offering was clicked on.
   * @param offering Either the reference to a Factory Display or a reference to the Table Center.
   */
  void chooseTileFrom(String playerName, int indexOfTile, Offering offering);

  /**
   * The player playerName tries to finish his turn.
   *
   * @param playerName the name of the player who tries to finish his turn.
   */
  void endTurn(String playerName);

  /**
   * TODO: @Nils - ich denke, diese Methode habe ich zu stark verändert und sie überhaupt nicht so
   * gemacht, wie du das angedacht hattest.
   *
   * Informs the model on which pattern row the player chose to place his tile.
   *
   * @param rowOfPatternLine the row of the pattern line selected
   * @return <code>true</code> if it was a valid pick, <code>false</code> if not
   */
  boolean placeTileAtPatternLine(int rowOfPatternLine);

  /**
   * Returns a list with the nicknames of all players.
   * @return a list with nicknames.
   */
  List<String> getPlayerNamesList();
}
