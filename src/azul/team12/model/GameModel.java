package azul.team12.model;

import azul.team12.model.events.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class GameModel implements Model {

  public static final int MIN_PLAYER_NUMBER = 2;
  public static final int MAX_PLAYER_NUMBER = 4;

  private int SLEEP_TIME = 100;

  private final PropertyChangeSupport support;
  private ArrayList<Player> playerList;
  private ArrayList<Offering> offerings;
  private boolean isGameStarted = false;
  private boolean hasGameEnded = false;
  private int indexOfActivePlayer = 0;
  private Offering currentOffering;
  private int currentIndexOfTile;

  private static final Logger LOGGER = LogManager.getLogger(GameModel.class);

  public GameModel() {
    support = new PropertyChangeSupport(this);
    playerList = new ArrayList<>();
    offerings  = new ArrayList<>();
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    requireNonNull(listener);
    support.addPropertyChangeListener(listener);
  }

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

  public void startGame() {
    if (playerList.size() < MIN_PLAYER_NUMBER) {
      notifyListeners(new GameNotStartableEvent(GameNotStartableEvent.NOT_ENOUGH_PLAYER));
    } else if (isGameStarted) {
      notifyListeners(new GameNotStartableEvent(GameNotStartableEvent.GAME_ALREADY_STARTED));
    }
    else{
      setUpOfferings();
      notifyListeners(new GameStartedEvent());
    }
  }

  //TODO: @Marco test it if it works, when the buttons are there
  public void restartGame() {

    TableCenter.getInstance().initializeContent();
    BagToStoreUsedTiles.getInstance().initializeContent();
    BagToDrawNewTiles.getInstance().initializeContent();
    isGameStarted = false;
    hasGameEnded = false;
    offerings = new ArrayList<>();
    setUpOfferings();
    for (Player player : playerList) {
      player.initializePatternLines();
      player.clearFloorline();
      player.setAIPlayer(false);
      player.setPoints(0);
    }

    notifyListeners(new GameStartedEvent());


  }

  public void cancelGame() {
    LOGGER.info(getNickOfActivePlayer() + " wants to end the game for all player.");
    GameCanceledEvent gameCanceledEvent = new GameCanceledEvent(getNickOfActivePlayer());
    TableCenter.getInstance().initializeContent();
    BagToStoreUsedTiles.getInstance().initializeContent();
    BagToDrawNewTiles.getInstance().initializeContent();
    isGameStarted = false;
    hasGameEnded = true;
    playerList = new ArrayList<>();
    offerings = new ArrayList<>();
    notifyListeners(gameCanceledEvent);
  }


    /**
     * Creates the Table Center and as many Factory Displays as needed and saves it in the offerings
     * list.
     */
  private void setUpOfferings(){
    offerings = new ArrayList<>();
    offerings.add(TableCenter.getInstance());
    TableCenter.getInstance().addStartPlayerMarker();
    int numberOfFactoryDisplays = (playerList.size() * 2) + 1;
    for(int i = 0; i < numberOfFactoryDisplays; i++){
      offerings.add(new FactoryDisplay());
    }
  }

  public void endTurn(){
    boolean roundFinished = checkRoundFinished();
    if (roundFinished) {
      RoundFinishedEvent roundFinishedEvent = new RoundFinishedEvent();
      startTilingPhase();
      if(!hasGameEnded){
        setUpOfferings();
      }
      notifyListeners(roundFinishedEvent);
    }
    indexOfActivePlayer = getIndexOfNextPlayer();
    NextPlayersTurnEvent nextPlayersTurnEvent = new NextPlayersTurnEvent(getNickOfActivePlayer());
    notifyListeners(nextPlayersTurnEvent);

    //TODO: Check if SPM is used in the right way --> makes player be first in next round. @Marco
    //TODO: Fix bug, when 4 players are playing and more than one is AI player @Marco
    LOGGER.info(playerList.get(indexOfActivePlayer).getName() + " is now active player. Is he an "
        + "AI-Player? " + playerList.get(indexOfActivePlayer).isAIPlayer());



    //checking if the next Player has left the game / is an AI-Player
    if (playerList.get(indexOfActivePlayer).isAIPlayer() && !hasGameEnded) {
      LOGGER.info(getNickOfActivePlayer() + " makes an move automatically, because he/she "
          + "is an AI-Player.");
      String nickOfAIPlayer = getNickOfActivePlayer();
      makeAIPlayerMakeAMove(nickOfAIPlayer);
    }

  }

  public void replaceActivePlayerByAI() {
    LOGGER.info(getNickOfActivePlayer() + " wants to forfeit the game.");
    GameForfeitedEvent gameForfeitedEvent = new GameForfeitedEvent(getNickOfActivePlayer());
    notifyListeners(gameForfeitedEvent);

    //TODO: If player has already chosen something and then forfeits
    LOGGER.info(getNickOfActivePlayer() + " is set to be an AI Player. ");
    getPlayerByName(getNickOfActivePlayer()).setAIPlayer(true);
    makeAIPlayerMakeAMove(getNickOfActivePlayer());
  }

  public void makeAIPlayerMakeAMove(String nickOfAIPlayer) {
    // get not empty offerings
    List<Offering> offeringsClone = getOfferings();
    for (Offering offering : getOfferings()) {
      if (offering.getContent().isEmpty()) {
        if (!(offering instanceof TableCenter)) {
          offeringsClone.remove(offering);
        }
      }
    }

    // if there are no offerings anymore and the table center is empty, it is not possible to make
    // another turn
    if (TableCenter.getInstance().getContent().size() == 0 && getOfferings().size() == 1) {
      LOGGER.info("No player was able to make a turn anymore.");
      notifyListeners(new GameInIllegalStateEvent());
      restartGame();
      throw new IllegalStateException("The game has reached an illegal state. Noone was able to "
          + "make a turn. Game was restarted automatically.");
    } else {

      // get a random offering
      int randomOfferingIndex = (int) (Math.random() * offeringsClone.size());
      Offering randomOffering = offeringsClone.get(randomOfferingIndex);
      int offeringsSize = randomOffering.getContent().size();

      // get a random tile on that offering
      int randomOfferingTileIndex = (int) (Math.random() * offeringsSize);
      notifyTileChosen(nickOfAIPlayer, randomOfferingTileIndex, randomOfferingIndex);

      Player activeAIPlayer = getPlayerByName(nickOfAIPlayer);
      //check all pattern lines from first to last if we can place the tile there
      for (int i = 0; i < activeAIPlayer.getPatternLines().length; i++) {
        if (activeAIPlayer.isValidPick(i, randomOffering, randomOfferingTileIndex)) {
          LOGGER.info(nickOfAIPlayer + " tries to place a " +
              randomOffering.getContent().get(randomOfferingTileIndex).toString() + " on pattern "
              + "line " + i);
          makeActivePlayerPlaceTile(i);
          break;
        } else if (i == activeAIPlayer.getPatternLines().length - 1) {
          LOGGER.info(nickOfAIPlayer + " was not able to place the tile on a pattern line "
              + "places it on the floor line");
          tileFallsDown();
        }
      }

      endTurn();
    }
  }

  public void notifyTileChosen(String playerName, int indexOfTile, int offeringIndex) {
    boolean thereIsAValidPick = false;
    List<Offering> offeringsClone = getOfferings();
    currentOffering = offeringsClone.get(offeringIndex);
    currentIndexOfTile = indexOfTile;
    Player player = getPlayerByName(playerName);
    // check for each line in the pattern lines if there is a valid pick
    for (int line = 0; line < Player.NUMBER_OF_PATTERN_LINES; line++) {
      if (player.isValidPick(line, currentOffering, indexOfTile)) {
        thereIsAValidPick = true;
      }
    }
    // inform listeners if there is a valid pick, who the next player is
    // if not: that there is not valid turn to make
    if (thereIsAValidPick) {
      int indexOfNextPlayer = getIndexOfNextPlayer();
      Player nextPlayer = playerList.get(indexOfNextPlayer);
      String nextPlayerNick = nextPlayer.getName();
      PlayerHasChosenTileEvent playerHasChosenTileEvent = new PlayerHasChosenTileEvent(nextPlayerNick);
      notifyListeners(playerHasChosenTileEvent);
    } else {
      NoValidTurnToMakeEvent noValidTurnToMakeEvent = new NoValidTurnToMakeEvent();
      notifyListeners(noValidTurnToMakeEvent);
    }
  }

  public boolean makeActivePlayerPlaceTile(int rowOfPatternLine) {
    LOGGER.info(getNickOfActivePlayer() + " tries to place a tile on patter line " + rowOfPatternLine + ".");
    String nickActivePlayer = getNickOfActivePlayer();
    Player activePlayer = getPlayerByName(nickActivePlayer);
    return activePlayer.drawTiles(rowOfPatternLine, currentOffering, currentIndexOfTile);
  }

  public void tileFallsDown() {
    String nickActivePlayer = getNickOfActivePlayer();
    Player activePlayer = getPlayerByName(nickActivePlayer);
    LOGGER.info(nickActivePlayer + " tries to place a tile directly into the floor line.");
    // check for null, because offering is none, if player die not choose on an offering before
    if(currentOffering == null){
      notifyListeners(new IllegalTurnEvent());
    }
    else {
      activePlayer.placeTileInFloorLine(currentOffering, currentIndexOfTile);
    }
  }

  public boolean checkRoundFinished() {
    for (Offering offering : offerings) {
      // if any of the offerings still has a content, the round is not yet finished
      if (!offering.getContent().isEmpty()) {
        return false;
        }
    }
    return true;
  }

  public int getIndexOfPlayerWithSPM() {
    for (int i = 0; i < playerList.size(); i++) {
      Player player = playerList.get(i);
      if (player.hasStartingPlayerMarker()) {
        LOGGER.info(player.getName() + " with index " + i + " was the player "
            + "with the SPM.");
        return i;
      }
    }
    LOGGER.debug("We called giveIndexOfPlayer with Start Player Marker when no player had "
        + "the SPM. Probably this is the case because at the end of the turn noone had the "
        + "SPM.");
    throw new IllegalStateException("We called giveIndexOfPlayer with Start Player Marker when "
        + "no player had the SPM.");
  }

  public void startTilingPhase() {
    hasGameEnded = false;
    for (Player player : playerList) {
      player.tileWallAndGetPoints();
      if (player.hasEndedTheGame()) {
        PlayerHasEndedTheGameEvent playerHasEndedTheGameEvent =
            new PlayerHasEndedTheGameEvent(player.getName());
        notifyListeners(playerHasEndedTheGameEvent);
        hasGameEnded = true;
      }
      // Loop has to finish, because all players have to finish tiling phase
    }
    if (hasGameEnded) {
      String winnerName = getPlayerWithMostPoints();
      GameFinishedEvent gameFinishedEvent = new GameFinishedEvent(winnerName);
      notifyListeners(gameFinishedEvent);
    }
  }

  public String getPlayerWithMostPoints() {
    //TODO: What if two players have the same points?
    ArrayList<Integer> playerPoints = new ArrayList<>();
    for (Player player : playerList) {
      playerPoints.add(player.getPoints());
    }
    int highestScore = Collections.max(playerPoints);
    int bestIndex = playerPoints.indexOf(highestScore);
    Player playerWithMostPoints = playerList.get(bestIndex);
    return playerWithMostPoints.getName();
  }

  public List<Player> rankingPlayerWithPoints() {
    List<Player> playerRankingList = playerList;
    Collections.sort(playerRankingList, (o1, o2) -> -Integer.compare(getPoints(o1.getName()), getPoints(o2.getName())));
    return playerRankingList;

  }

  public int getIndexOfNextPlayer() {
    int indexOfNextPlayer;
    if (checkRoundFinished()) {
      indexOfNextPlayer = getIndexOfPlayerWithSPM();
    } else if (indexOfActivePlayer == playerList.size() - 1) {
      indexOfNextPlayer = 0;
    } else {
      indexOfNextPlayer = indexOfActivePlayer + 1;
    }
    return indexOfNextPlayer;
  }

  public Player getPlayerByName(String nickname) {
    for(Player player : playerList) {
      if(player.getName().equals(nickname)) {
        return player;
      }
    }
    LOGGER.debug("The model was given a name by view that is not in the playerList.");
    return null;
  }


  public ModelTile[][] getPatternLinesOfPlayer(String playerName) {
    Player player = getPlayerByName(playerName);
    return player.getPatternLines();
  }

  public List<ModelTile> getFloorLineOfPlayer(String playerName) {
    Player player = getPlayerByName(playerName);
    return player.getFloorLine();
  }

  public ModelTile[][] getWallOfPlayer(String playerName) {

    Player player = getPlayerByName(playerName);
    ModelTile[][] templateWall = WallBackgroundPattern.getTemplateWall();
    boolean[][] wallAsBools = player.getWall();
    ModelTile[][] playerWall = new ModelTile[5][5];

    for (int row = 0; row < wallAsBools.length; row++) {
      for (int col = 0; col < wallAsBools[row].length; col++) {
        if (wallAsBools[row][col]) {
          playerWall[row][col] = templateWall[row][col];
        } else {
          playerWall[row][col] = ModelTile.EMPTY_TILE;
        }
      }
    }

    return playerWall;

  }

  public List<String> getPlayerNamesList() {
    List<String> list = new ArrayList<>();
    for (Player player: playerList) {
      list.add(player.getName());
    }
    return list;
  }

  public List<Offering> getOfferings() {
    return (List<Offering>) offerings.clone();
  }

  public int getIndexOfActivePlayer() {
    return indexOfActivePlayer;
  }

  public int getPoints(String nickname){
    for(Player player : playerList){
      if(player.getName().equals(nickname)){
        return player.getPoints();
      }
    }
    notifyListeners(new PlayerDoesNotExistEvent(nickname));
    return 0;
  }

  public int getMinusPoints(String nickname){
    for(Player player : playerList){
      if(player.getName().equals(nickname)){
        return player.getMinusPoints();
      }
    }
    notifyListeners(new PlayerDoesNotExistEvent(nickname));
    return 0;
  }

  public List<Offering> getFactoryDisplays(){
    // return the factory displays being the all but the first offering
    return offerings.subList(1, offerings.size());
  }

  public Offering getTableCenter(){
    return TableCenter.getInstance();
  }

  public String getNickOfActivePlayer(){
    return playerList.get(indexOfActivePlayer).getName();
  }

}
