package cyberzul.model;

import cyberzul.model.events.GameCanceledEvent;
import cyberzul.model.events.GameFinishedEvent;
import cyberzul.model.events.GameForfeitedEvent;
import cyberzul.model.events.GameInIllegalStateEvent;
import cyberzul.model.events.GameNotStartableEvent;
import cyberzul.model.events.GameStartedEvent;
import cyberzul.model.events.IllegalTurnEvent;
import cyberzul.model.events.LoggedInEvent;
import cyberzul.model.events.LoginFailedEvent;
import cyberzul.model.events.NextPlayersTurnEvent;
import cyberzul.model.events.NoValidTurnToMakeEvent;
import cyberzul.model.events.PlayerHasChosenTileEvent;
import cyberzul.model.events.PlayerHasEndedTheGameEvent;
import cyberzul.model.events.RoundFinishedEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The very core of the backend. All requests that aim to change the game state are running through
 * this class and are either computed here or delegated to other backend classes.
 * <p>
 * Contains the playing field of the Azul game, the list of players and the list of offerings.
 */
public class GameModel extends CommonModel implements ModelStrategy {

  public static final int MIN_PLAYER_NUMBER = 2;
  public static final int MAX_PLAYER_NUMBER = 4;
  private static final Logger LOGGER = LogManager.getLogger(GameModel.class);
  private boolean hasGameEnded = false;
  private Offering currentOffering;
  private int currentIndexOfTile;
  private Random ran = new Random();


  /**
   * Constructs a new game, initializes the property change support, the player list, and the
   * offerings.
   */
  public GameModel() {
    super();
    playerList = new ArrayList<>();
    offerings = new ArrayList<>();
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
        notifyListeners(new LoggedInEvent());
      }
    }
  }

  @Override
  public void startGame() {
    if (playerList.size() < MIN_PLAYER_NUMBER) {
      notifyListeners(new GameNotStartableEvent(GameNotStartableEvent.NOT_ENOUGH_PLAYER));
    } else if (isGameStarted) {
      notifyListeners(new GameNotStartableEvent(GameNotStartableEvent.GAME_ALREADY_STARTED));
    } else {
      setUpOfferings();
      isGameStarted = true;
      notifyListeners(new GameStartedEvent());
    }
  }

  @Override
  public void restartGame() {

    TableCenter.getInstance().initializeContent();
    BagToStoreUsedTiles.getInstance().initializeContent();
    BagToDrawNewTiles.getInstance().initializeContent();
    isGameStarted = true;
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
    indexOfActivePlayer = 0;

    notifyListeners(new GameStartedEvent());
  }

  @Override
  public void cancelGame() {
    LOGGER.info(getNickOfActivePlayer() + " wants to end the game for all player.");
    TableCenter.getInstance().initializeContent();
    BagToStoreUsedTiles.getInstance().initializeContent();
    BagToDrawNewTiles.getInstance().initializeContent();
    isGameStarted = false;
    hasGameEnded = true;
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

  private void endTurn() {
    String nameOfPlayerWhoEndedHisTurn = getNickOfActivePlayer();
    boolean roundFinished = checkRoundFinished();
    indexOfActivePlayer = getIndexOfNextPlayer();
    if (roundFinished) {
      RoundFinishedEvent roundFinishedEvent = new RoundFinishedEvent();
      startTilingPhase();
      if (!hasGameEnded) {
        setUpOfferings();
      }
      notifyListeners(roundFinishedEvent);
    }
    NextPlayersTurnEvent nextPlayersTurnEvent = new NextPlayersTurnEvent(getNickOfActivePlayer(),
        nameOfPlayerWhoEndedHisTurn);
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

  @Override
  public void replacePlayerByAi(String playerName) {
    LOGGER.info(playerName + " wants to forfeit the game.");
    GameForfeitedEvent gameForfeitedEvent = new GameForfeitedEvent(playerName);
    notifyListeners(gameForfeitedEvent);

    //TODO: If player has already chosen something and then forfeits
    LOGGER.info(playerName + " is set to be an AI Player. ");
    getPlayerByName(playerName).setAiPlayer(true);
    makeAiPlayerMakeMove(playerName);
  }

  /**
   * makes a given player place a tile randomly. Used for AI-Players.
   *
   * @param nickOfAiPlayer the name of the active player.
   */
  private void makeAiPlayerMakeMove(String nickOfAiPlayer) {
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

  @Override
  public String getPlayerName() {
    String playerName = getNickOfActivePlayer();
    return playerName;
  }

  @Override
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
      //Player nextPlayer = playerList.get(indexOfNextPlayer);
      PlayerHasChosenTileEvent playerHasChosenTileEvent =
          new PlayerHasChosenTileEvent(getNickOfActivePlayer());
      notifyListeners(playerHasChosenTileEvent);
    } else {
      NoValidTurnToMakeEvent noValidTurnToMakeEvent = new NoValidTurnToMakeEvent();
      notifyListeners(noValidTurnToMakeEvent);
    }
  }

  @Override
  public void makeActivePlayerPlaceTile(int rowOfPatternLine) {
    LOGGER.info(
        getNickOfActivePlayer() + " tries to place a tile on patter line " + rowOfPatternLine +
            ".");
    String nickActivePlayer = getNickOfActivePlayer();
    Player activePlayer = getPlayerByName(nickActivePlayer);
    if (!activePlayer.drawTiles(rowOfPatternLine, currentOffering, currentIndexOfTile)) {
      notifyListeners(new IllegalTurnEvent());
    } else {
      endTurn();
    }
  }

  @Override
  public void tileFallsDown() {
    String nickActivePlayer = getNickOfActivePlayer();
    Player activePlayer = getPlayerByName(nickActivePlayer);
    LOGGER.info(nickActivePlayer + " tries to place a tile directly into the floor line.");
    // check for null, because offering is none, if player die not choose on an offering before
    if (currentOffering == null) {
      notifyListeners(new IllegalTurnEvent());
    } else {
      activePlayer.placeTileInFloorLine(currentOffering, currentIndexOfTile);
      endTurn();
    }
  }

  /**
   * Tell each player to tile the wall and get the points accordingly. Fires
   * {@link PlayerHasEndedTheGameEvent} if a player has ended the game in this tiling phase, fires
   * {@link GameFinishedEvent} at the end of this tiling phase in which someone has ended the game.
   */
  private void startTilingPhase() {
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
}
