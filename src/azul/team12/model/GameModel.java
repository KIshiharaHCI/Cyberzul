package azul.team12.model;

import static java.util.Objects.requireNonNull;

import azul.team12.model.events.GameCanceledEvent;
import azul.team12.model.events.GameEvent;
import azul.team12.model.events.GameFinishedEvent;
import azul.team12.model.events.GameForfeitedEvent;
import azul.team12.model.events.GameInIllegalStateEvent;
import azul.team12.model.events.GameNotStartableEvent;
import azul.team12.model.events.GameStartedEvent;
import azul.team12.model.events.IllegalTurnEvent;
import azul.team12.model.events.LoginFailedEvent;
import azul.team12.model.events.NextPlayersTurnEvent;
import azul.team12.model.events.NoValidTurnToMakeEvent;
import azul.team12.model.events.PlayerDoesNotExistEvent;
import azul.team12.model.events.PlayerHasChosenTileEvent;
import azul.team12.model.events.PlayerHasEndedTheGameEvent;
import azul.team12.model.events.RoundFinishedEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Contains the playing field of the Azul game, the list of players and the list of offerings.
 *
 */
public class GameModel implements Model {

  public static final int MIN_PLAYER_NUMBER = 2;
  public static final int MAX_PLAYER_NUMBER = 4;
  private static final Logger LOGGER = LogManager.getLogger(GameModel.class);
  private final PropertyChangeSupport support;
  private int sleepTime = 100;
  private ArrayList<Player> playerList;
  private ArrayList<Offering> offerings;
  private boolean isGameStarted = false;
  private boolean hasGameEnded = false;
  private int indexOfActivePlayer = 0;
  private Offering currentOffering;
  private int currentIndexOfTile;
  private Random ran = new Random();


  /**
   * Constructs a new game, initializes the property change support, the player list, and the
   * offerings.
   */
  public GameModel() {
    support = new PropertyChangeSupport(this);
    playerList = new ArrayList<>();
    offerings = new ArrayList<>();
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
   * {@link azul.team12.model.events.GameEvent} gets fired such that the attached observers (i.e.,
   * {@link PropertyChangeListener}) can distinguish between what exactly has changed.
   *
   * @param event A concrete implementation of {@link azul.team12.model.events.GameEvent}
   */
  private void notifyListeners(GameEvent event) {
    support.firePropertyChange(event.getName(), null, event);
  }

  /**
   * see {@link Model}.
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
   * see {@link Model}.
   */
  public void startGame() {
    if (playerList.size() < MIN_PLAYER_NUMBER) {
      notifyListeners(new GameNotStartableEvent(GameNotStartableEvent.NOT_ENOUGH_PLAYER));
    } else if (isGameStarted) {
      notifyListeners(new GameNotStartableEvent(GameNotStartableEvent.GAME_ALREADY_STARTED));
    } else {
      setUpOfferings();
      notifyListeners(new GameStartedEvent());
    }
  }

  /**
   * see {@link Model}.
   */
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
      player.clearWallPattern();
      player.setAiPlayer(false);
      player.setPoints(0);
    }

    notifyListeners(new GameStartedEvent());


  }

  /**
   * see {@link Model}.
   */
  public void cancelGame() {
    LOGGER.info(getNickOfActivePlayer() + " wants to end the game for all player.");
    TableCenter.getInstance().initializeContent();
    BagToStoreUsedTiles.getInstance().initializeContent();
    BagToDrawNewTiles.getInstance().initializeContent();
    isGameStarted = false;
    hasGameEnded = true;
    playerList = new ArrayList<>();
    offerings = new ArrayList<>();
    GameCanceledEvent gameCanceledEvent = new GameCanceledEvent(getNickOfActivePlayer());
    notifyListeners(gameCanceledEvent);
  }


  /**
   * Creates the Table Center and as many Factory Displays as needed and saves it in the offerings
   * list.
   */
  private void setUpOfferings() {
    offerings = new ArrayList<>();
    offerings.add(TableCenter.getInstance());
    TableCenter.getInstance().addStartPlayerMarker();
    int numberOfFactoryDisplays = (playerList.size() * 2) + 1;
    for (int i = 0; i < numberOfFactoryDisplays; i++) {
      offerings.add(new FactoryDisplay());
    }
  }

  /**
   * see {@link Model}.
   */
  public void endTurn() {
    boolean roundFinished = checkRoundFinished();
    if (roundFinished) {
      RoundFinishedEvent roundFinishedEvent = new RoundFinishedEvent();
      startTilingPhase();
      if (!hasGameEnded) {
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
        + "AI-Player? " + playerList.get(indexOfActivePlayer).isAiPlayer());

    //checking if the next Player has left the game / is an AI-Player
    if (playerList.get(indexOfActivePlayer).isAiPlayer() && !hasGameEnded) {
      LOGGER.info(getNickOfActivePlayer() + " makes an move automatically, because he/she "
          + "is an AI-Player.");
      String nickOfAiPlayer = getNickOfActivePlayer();
      makeAiPlayerMakeMove(nickOfAiPlayer);
    }

  }

  /**
   * see {@link Model}.
   */
  public void replaceActivePlayerByAi() {
    LOGGER.info(getNickOfActivePlayer() + " wants to forfeit the game.");
    GameForfeitedEvent gameForfeitedEvent = new GameForfeitedEvent(getNickOfActivePlayer());
    notifyListeners(gameForfeitedEvent);

    //TODO: If player has already chosen something and then forfeits
    LOGGER.info(getNickOfActivePlayer() + " is set to be an AI Player. ");
    getPlayerByName(getNickOfActivePlayer()).setAiPlayer(true);
    makeAiPlayerMakeMove(getNickOfActivePlayer());
  }

  /**
   * see {@link Model}.
   */
  public void makeAiPlayerMakeMove(String nickOfAiPlayer) {
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
      int randomOfferingIndex = ran.nextInt(0, offeringsClone.size());
      Offering randomOffering = offeringsClone.get(randomOfferingIndex);

      // get a random tile on that offering
      int offeringsSize = randomOffering.getContent().size();
      int randomOfferingTileIndex = ran.nextInt(0, offeringsSize);
      notifyTileChosen(nickOfAiPlayer, randomOfferingTileIndex, randomOfferingIndex);

      Player activeAiPlayer = getPlayerByName(nickOfAiPlayer);
      //check all pattern lines from first to last if we can place the tile there
      for (int i = 0; i < activeAiPlayer.getPatternLines().length; i++) {
        if (activeAiPlayer.isValidPick(i, randomOffering, randomOfferingTileIndex)) {
          LOGGER.info(nickOfAiPlayer + " tries to place a "
              + randomOffering.getContent().get(randomOfferingTileIndex).toString() + " on pattern "
              + "line " + i);
          makeActivePlayerPlaceTile(i);
          break;
        } else if (i == activeAiPlayer.getPatternLines().length - 1) {
          LOGGER.info(nickOfAiPlayer + " was not able to place the tile on a pattern line "
              + "places it on the floor line");
          tileFallsDown();
        }
      }

      endTurn();
    }
  }

  /**
   * see {@link Model}.
   */
  public void notifyTileChosen(String playerName, int indexOfTile, int offeringIndex) {
    boolean thereIsValidPick = false;
    List<Offering> offeringsClone = getOfferings();
    currentOffering = offeringsClone.get(offeringIndex);
    currentIndexOfTile = indexOfTile;
    Player player = getPlayerByName(playerName);
    // check for each line in the pattern lines if there is a valid pick
    for (int line = 0; line < Player.NUMBER_OF_PATTERN_LINES; line++) {
      if (player.isValidPick(line, currentOffering, indexOfTile)) {
        thereIsValidPick = true;
      }
    }
    // inform listeners if there is a valid pick, who the next player is
    // if not: that there is not valid turn to make
    if (thereIsValidPick) {
      int indexOfNextPlayer = getIndexOfNextPlayer();
      Player nextPlayer = playerList.get(indexOfNextPlayer);
      String nextPlayerNick = nextPlayer.getName();
      PlayerHasChosenTileEvent playerHasChosenTileEvent = new PlayerHasChosenTileEvent(
          nextPlayerNick);
      notifyListeners(playerHasChosenTileEvent);
    } else {
      NoValidTurnToMakeEvent noValidTurnToMakeEvent = new NoValidTurnToMakeEvent();
      notifyListeners(noValidTurnToMakeEvent);
    }
  }

  /**
   * see {@link Model}.
   */
  public boolean makeActivePlayerPlaceTile(int rowOfPatternLine) {
    LOGGER.info(
        getNickOfActivePlayer() + " tries to place a tile on patter line " + rowOfPatternLine
            + ".");
    String nickActivePlayer = getNickOfActivePlayer();
    Player activePlayer = getPlayerByName(nickActivePlayer);
    return activePlayer.drawTiles(rowOfPatternLine, currentOffering, currentIndexOfTile);
  }

  /**
   * see {@link Model}.
   */
  public void tileFallsDown() {
    String nickActivePlayer = getNickOfActivePlayer();
    Player activePlayer = getPlayerByName(nickActivePlayer);
    LOGGER.info(nickActivePlayer + " tries to place a tile directly into the floor line.");
    // check for null, because offering is none, if player die not choose on an offering before
    if (currentOffering == null) {
      notifyListeners(new IllegalTurnEvent());
    } else {
      activePlayer.placeTileInFloorLine(currentOffering, currentIndexOfTile);
    }
  }

  /**
   * see {@link Model}.
   */
  public boolean checkRoundFinished() {
    for (Offering offering : offerings) {
      // if any of the offerings still has a content, the round is not yet finished
      if (!offering.getContent().isEmpty()) {
        return false;
      }
    }
    return true;
  }

  /**
   * see {@link Model}.
   */
  public int getIndexOfPlayerWithSpm() {
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

  /**
   * see {@link Model}.
   */
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

  /**
   * see {@link Model}.
   */
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

  /**
   * see {@link Model}.
   */
  @Override
  public List<String> rankingPlayerWithPoints() {
    List<String> playerNamesRankingList = getPlayerNamesList();
    Collections.sort(playerNamesRankingList,
            (o1, o2) -> -Integer.compare(getPoints(o1), getPoints(o2)));
    return playerNamesRankingList;
  }

  /**
   * see {@link Model}.
   */
  public int getIndexOfNextPlayer() {
    int indexOfNextPlayer;
    if (checkRoundFinished()) {
      indexOfNextPlayer = getIndexOfPlayerWithSpm();
    } else if (indexOfActivePlayer == playerList.size() - 1) {
      indexOfNextPlayer = 0;
    } else {
      indexOfNextPlayer = indexOfActivePlayer + 1;
    }
    return indexOfNextPlayer;
  }

  /**
   * see {@link Model}.
   */
  public Player getPlayerByName(String nickname) {
    for (Player player : playerList) {
      if (player.getName().equals(nickname)) {
        return player;
      }
    }
    LOGGER.debug("The model was given a name by view that is not in the playerList.");
    return null;
  }

  /**
   * see {@link Model}.
   */
  public ModelTile[][] getPatternLinesOfPlayer(String playerName) {
    Player player = getPlayerByName(playerName);
    return player.getPatternLines();
  }

  /**
   * see {@link Model}.
   */
  public List<ModelTile> getFloorLineOfPlayer(String playerName) {
    Player player = getPlayerByName(playerName);
    return player.getFloorLine();
  }

  /**
   * see {@link Model}.
   */
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

  /**
   * see {@link Model}.
   */
  public List<String> getPlayerNamesList() {
    List<String> list = new ArrayList<>();
    for (Player player : playerList) {
      list.add(player.getName());
    }
    return list;
  }

  /**
   * see {@link Model}.
   */
  public List<Offering> getOfferings() {
    @SuppressWarnings("unchecked") List<Offering> offeringsClone =
        (List<Offering>) offerings.clone();
    return offeringsClone;
  }

  /**
   * see {@link Model}.
   */
  public int getIndexOfActivePlayer() {
    return indexOfActivePlayer;
  }

  /**
   * see {@link Model}.
   */
  public int getPoints(String nickname) {
    for (Player player : playerList) {
      if (player.getName().equals(nickname)) {
        return player.getPoints();
      }
    }
    notifyListeners(new PlayerDoesNotExistEvent(nickname));
    return 0;
  }

  /**
   * see {@link Model}.
   */
  public int getMinusPoints(String nickname) {
    for (Player player : playerList) {
      if (player.getName().equals(nickname)) {
        return player.getMinusPoints();
      }
    }
    notifyListeners(new PlayerDoesNotExistEvent(nickname));
    return 0;
  }

  /**
   * see {@link Model}.
   */
  public List<Offering> getFactoryDisplays() {
    // return the factory displays being the all but the first offering
    return offerings.subList(1, offerings.size());
  }

  /**
   * see {@link Model}.
   */
  public Offering getTableCenter() {
    return TableCenter.getInstance();
  }

  /**
   * see {@link Model}.
   */
  public String getNickOfActivePlayer() {
    return playerList.get(indexOfActivePlayer).getName();
  }

}
