package azul.team12.model;

import static java.util.Objects.requireNonNull;

import azul.team12.model.events.GameEvent;
import azul.team12.model.events.GameNotStartableEvent;
import azul.team12.model.events.GameStartedEvent;
import azul.team12.model.events.IllegalTurnEvent;
import azul.team12.model.events.LoginFailedEvent;
import azul.team12.model.events.NextPlayersTurnEvent;
import azul.team12.model.events.NoValidTurnToMakeEvent;
import azul.team12.model.events.PlayerDoesNotExistEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class GameModel {

  public static final int MIN_PLAYER_NUMBER = 2;
  public static final int MAX_PLAYER_NUMBER = 4;

  private final PropertyChangeSupport support;
  private List<Player> playerList = new ArrayList<Player>();
  //TODO: @Nils please check what I did: Ich habe die factoryDisplays und die Tischmitte in
  // einem ArrayList<Offering> zusammengefasst. Das hat keine Auswirkungen für die View
  // wir geben einfach index 0 für die Tischmitte zurück, aber es war praktisch für die weitere
  // Implementierung - ändere es jederzeit zurück. Außerdem glaube ich, dass uns dadurch die
  // View gar nicht mehr das ganze Offering sondern nur noch einen Index zurückgeben muss.
  // Das habe ich jetzt aber noch nicht geändert. Die Variable unten "currentOffering" könnte dann
  // durch die Variable "indexOfOffering" ersetzt werden. Mir gefällt diese Lösung, aber vllt
  // übersehe ich etwas.
  private ArrayList<Offering> offerings = new ArrayList<Offering>();
  private boolean isGameStarted = false;
  private int indexOfActivePlayer = 0;
  private int indexOfOffering;
  private Offering currentOffering;
  private int currentIndexOfTile;


  public GameModel() {
    support = new PropertyChangeSupport(this);
  }

  /**
   * Add a {@link PropertyChangeListener} to the model for getting notified about any changes that
   * are published by this model.
   *
   * @param listener the view that subscribes itself to the model.
   */
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    requireNonNull(listener);
    support.addPropertyChangeListener(listener);
  }

  /**
   * Remove a listener from the model. From then on it will no longer get notified about any events
   * fired by the model.
   *
   * @param listener the view that is to be unsubscribed from the model.
   */
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    requireNonNull(listener);
    support.removePropertyChangeListener(listener);
  }

  /**
   * Notify subscribed listeners that the state of the model has changed. To this end, a specific
   * {@link azul.team12.model.events.GameEvent} gets fired such that the attached observers (i.e., {@link
   * PropertyChangeListener}) can distinguish between what exactly has changed.
   *
   * @param event A concrete implementation of {@link azul.team12.model.events.GameEvent}
   */
  private void notifyListeners(GameEvent event) {
    support.firePropertyChange(event.getName(), null, event);
  }

  /**
   * A player trys to log in. Fires {@link LoginFailedEvent} if that was not possible.
   *
   * @param nickname the name that the player chose with his login attempt.
   */
  public void loginWithName(String nickname) {
    boolean nicknameFree = true;
    if (playerList.size() >= MAX_PLAYER_NUMBER) {
      notifyListeners(new LoginFailedEvent(LoginFailedEvent.LOBBY_IS_FULL));
    } else {
      for (Player player : playerList) {
        if (player.getName().equals(nickname)) {
          nicknameFree = false;
          notifyListeners(new LoginFailedEvent(LoginFailedEvent.NICKNAME_ALREADY_TAKEN));
        }
      }
      if (nicknameFree) {
        Player newPlayer = new Player(nickname);
        playerList.add(newPlayer);
      }
    }
  }

  /**
   * Tries to start the game. Fires {@link GameNotStartableEvent} if that is
   * not possible.
   */
  public void startGame() {
    if (playerList.size() < MIN_PLAYER_NUMBER) {
      notifyListeners(new GameNotStartableEvent(GameNotStartableEvent.NOT_ENOUGH_PLAYER));
    } else if (isGameStarted) {
      notifyListeners(new GameNotStartableEvent(GameNotStartableEvent.GAME_ALREADY_STARTED));
    }
    else{
      //TODO: siehe TODO von Zeilen 25 - 28.
      offerings.add(TableCenter.getInstance());
      int numberOfFactoryDisplays = (playerList.size() * 2) + 1;
      for(int i = 0; i < numberOfFactoryDisplays; i++){
        offerings.add(new FactoryDisplay());
      }
      notifyListeners(new GameStartedEvent());
    }
  }

  public List<Offering> getFactoryDisplays(){
    //TODO: siehe TODO von Zeilen 25 - 28.
    List<Offering> factoryDisplays = offerings.subList(1, offerings.size());
    return factoryDisplays;
  }

  public Offering getTableCenter(){
    return TableCenter.getInstance();
  }

  /**
   * Returns the nickname of the player who has to make his turn.
   * @return the nickname of the player who has to make his turn.
   */
  public String getNickOfActivePlayer(){
    return playerList.get(indexOfActivePlayer).getName();
  }

  /**
   * Returns the number of points the player with the specified nickname has.
   *
   * @param nickname name of the player.
   * @return the points this player has.
   */
  public int getPoints(String nickname){
    for(Player player : playerList){
      if(player.getName().equals(nickname)){
        return player.getPoints();
      }
    }
    notifyListeners(new PlayerDoesNotExistEvent(nickname));
    return 0;
  }

  public void endTurn(){
    NextPlayersTurnEvent nextPlayersTurnEvent = new NextPlayersTurnEvent(getNickOfActivePlayer());
    notifyListeners(nextPlayersTurnEvent);
    indexOfActivePlayer++;
  }

  /**
   * Return player by name (given by view).
   *
   * @param nickname the nickname of the player
   * @return the player
   */
  public Player getPlayerByName(String nickname) {
    for(Player player : playerList) {
      if(player.getName().equals(nickname)) {
        return player;
      }
    }
    System.out.println("To be log - given name by view that is not in the playerList.");
    return null;
  }

  /**
   * Returns the nicknames of all players.
   * @return List of nicknames.
   */
  public List<String> getPlayerList() {
    List<String> list = new ArrayList<>();
    for (Player player: playerList) {
      list.add(player.getName());
    }
    return list;
  }

  /**
   * informs the view via listeners that it is the next players turn. If the player cannot
   * place the tile on a pattern line, it still informs the model.
   *
   * @param playerName  the player's name
   * @param indexOfTile the index of the tile that was drawn
   * @param offering    the offering (factory display or center of the table)
   */
  public void notifyTileChosen(String playerName, int indexOfTile, Offering offering) {
    boolean thereIsAValidPick = false;
    //TODO: Müssen wir hier einen Thread erstellen, weil: Was ist, wenn ein Spieler bereits ein
    // Offering auswählt während die andere noch nicht ihre Row ausgewählt hat, um sie zu
    // platzieren.
    currentOffering = offering;
    currentIndexOfTile = indexOfTile;
    Player player = getPlayerByName(playerName);
    // check for each line in the pattern lines if there is a valid pick
    for (int line = 0; line < Player.NUMBER_OF_PATTERN_LINES; line++) {
      if (player.isValidPick(line, offering, indexOfTile)) {
        thereIsAValidPick = true;
      }
    }
    // inform listeners if there is a valid pick, if not that it is the next players turn
    if (!thereIsAValidPick) {
      IllegalTurnEvent illegalTurnEvent = new IllegalTurnEvent();
      notifyListeners(illegalTurnEvent);
    } else {
      NoValidTurnToMakeEvent noValidTurnToMakeEvent = new NoValidTurnToMakeEvent();
      notifyListeners(noValidTurnToMakeEvent);
    }
  }

  /**
   * make the active player place the tile he/she has chosen.
   *
   * @param rowOfPatternLine the row of the chosen pattern line
   * @return <code>true</code> if it was a valid pick, <code>false</code> if not
   */
  public boolean makeActivePlayerPlaceTile(int rowOfPatternLine) {
    String nickActivePlayer = getNickOfActivePlayer();
    Player activePlayer = getPlayerByName(nickActivePlayer);
    return activePlayer.drawTiles(rowOfPatternLine, currentOffering, currentIndexOfTile);
  }

}
