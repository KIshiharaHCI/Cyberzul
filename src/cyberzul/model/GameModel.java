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
import cyberzul.model.events.PlayerHasChosenTileEvent;
import cyberzul.model.events.PlayerHasEndedTheGameEvent;
import cyberzul.model.events.RoundFinishedEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The very core of the backend. All requests that aim to change the game state are running through
 * this class and are either computed here or delegated to other backend classes.
 *
 * <p>Contains the playing field of the Azul game, the list of players and the list of offerings.
 * </p>
 */
public class GameModel extends CommonModel implements ModelStrategy {
  public static final int MIN_PLAYER_NUMBER = 2;
  public static final int MAX_PLAYER_NUMBER = 4;
  private static final Logger LOGGER = LogManager.getLogger(GameModel.class);
  //private final static int sleepTime = 100;
  private final Random ran = new Random();
  //the timer because player has to make a move within 30 seconds
  private Timer timer;
  private static final int DELAYTIME = 8000;

  private boolean hasGameEnded = false;
  private Offering currentOffering;
  private int currentIndexOfTile;

  private String hotSeatStory = "HotSeatStory is not yet set!";
  private String networkStory = "NetworkStory is not yet set!";
  private String singlePlayerStory = "SinglePlayerStory is not yet set!";

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
    startTimerForPlayer(getNickOfActivePlayer());
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
    //players formerly set to AI-Players, will remain AI-Players
    for (Player player : playerList) {
      player.initializePatternLines();
      player.clearFloorline();
      player.clearWallPattern();
      player.setPoints(0);
    }
    indexOfActivePlayer = 0;

    notifyListeners(new GameStartedEvent());

    // if we restart the game and the first player is an AI-Player, he/she needs to start making
    // a move
    if (getPlayerByName(getNickOfActivePlayer()).isAiPlayer()) {
      makeAiPlayerMakeMove(getNickOfActivePlayer());
    } else {
      startTimerForPlayer(getNickOfActivePlayer());
    }
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
    timer.cancel();
    LOGGER.info("Timer was cancelled.");
    String nameOfPlayerWhoEndedHisTurn = getNickOfActivePlayer();
    boolean roundFinished = checkRoundFinished();
    indexOfActivePlayer = getIndexOfNextPlayer();
    if (roundFinished) {
      startTilingPhase();
      if (!hasGameEnded) {
        setUpOfferings();
      }
      // no player has the SPM
      LOGGER.info("We are now resetting the player with the SPM.");
      playerList.get(getIndexOfPlayerWithSpm()).setHasStartingPlayerMarker(false);
      RoundFinishedEvent roundFinishedEvent = new RoundFinishedEvent();
      notifyListeners(roundFinishedEvent);
    }
    NextPlayersTurnEvent nextPlayersTurnEvent =
        new NextPlayersTurnEvent(getNickOfActivePlayer(), nameOfPlayerWhoEndedHisTurn);
    notifyListeners(nextPlayersTurnEvent);
    startTimerForPlayer(getNickOfActivePlayer());

    LOGGER.info(
        playerList.get(indexOfActivePlayer).getName()
            + " is now active player. Is he an "
            + "AI-Player? "
            + playerList.get(indexOfActivePlayer).isAiPlayer());

    // checking if the next Player has left the game / is an AI-Player
    if (playerList.get(indexOfActivePlayer).isAiPlayer() && !hasGameEnded) {
      LOGGER.info(
          getNickOfActivePlayer()
              + " makes an move automatically, because he/she "
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
    LOGGER.info(playerName + " is set to be an AI Player. ");
    Player aiPlayer = getPlayerByName(playerName);
    aiPlayer.setAiPlayer(true);
    makeAiPlayerMakeMove(aiPlayer.getName());
  }

  /**
   * makes a given player place a tile randomly. Used for AI-Players.
   *
   * @param nickOfAiPlayer the name of the active player.
   */
  private void makeAiPlayerMakeMove(String nickOfAiPlayer) {
    // get a List with the indizes of all non-empty Offerings on our offeringsList
    List<Integer> nonEmptyOfferingIndizes = new ArrayList<>();
    for (int i = 0; i < getOfferings().size(); i++) {
      if (!getOfferings().get(i).getContent().isEmpty()) {
        nonEmptyOfferingIndizes.add(i);
      }
    }

    // if there are no offerings anymore and the table center is empty, it is not possible to make
    // another turn
    if (TableCenter.getInstance().getContent().size() == 0 && getOfferings().size() == 1) {
      LOGGER.info("No player was able to make a turn anymore.");
      notifyListeners(new GameInIllegalStateEvent());
      restartGame();
      throw new IllegalStateException(
          "The game has reached an illegal state. Noone was able to "
              + "make a turn. Game was restarted automatically.");
    } else {
      // get a random offering
      int randomOfferingIndex = nonEmptyOfferingIndizes.get(
          ran.nextInt(0, nonEmptyOfferingIndizes.size()));
      Offering randomOffering = getOfferings().get(randomOfferingIndex);

      // get a random tile on that offering
      int offeringsSize = randomOffering.getContent().size();
      int randomOfferingTileIndex = ran.nextInt(0, offeringsSize);

      notifyTileChosen(nickOfAiPlayer, randomOfferingTileIndex, randomOfferingIndex);

      Player activeAiPlayer = getPlayerByName(nickOfAiPlayer);
      // check all pattern lines from first to last if we can place the tile there
      for (int i = 0; i < activeAiPlayer.getPatternLines().length; i++) {
        LOGGER.info("Is it possible to place a "
            + randomOffering.getContent().get(randomOfferingTileIndex).toString()
            + " on "
            + "pattern line " + i + "? "
            + activeAiPlayer.isValidPick(i, randomOffering, randomOfferingTileIndex));
        if (activeAiPlayer.isValidPick(i, randomOffering, randomOfferingTileIndex)) {
          LOGGER.info(
              nickOfAiPlayer
                  + " tries to place a "
                  + randomOffering.getContent().get(randomOfferingTileIndex).toString()
                  + " (index: " + randomOfferingTileIndex + ") on pattern "
                  + "line "
                  + i);
          makeActivePlayerPlaceTile(i);
          break;
        } else if (i == activeAiPlayer.getPatternLines().length - 1) {
          LOGGER.info(
              nickOfAiPlayer
                  + " was not able to place the tile on a pattern line "
                  + "places it on the floor line");
          tileFallsDown();
        }
      }
    }
  }

  @Override
  public String getPlayerName() {
    String playerName = getNickOfActivePlayer();
    return playerName;
  }

  @Override
  public void notifyTileChosen(String playerName, int indexOfTile, int offeringIndex) {
    List<Offering> offeringsClone = getOfferings();
    LOGGER.debug(playerName + " will be updating currentOffering now.");
    currentOffering = offeringsClone.get(offeringIndex);
    LOGGER.debug("currentOffering is " + currentOffering.getContent());
    currentIndexOfTile = indexOfTile;
    LOGGER.debug("current tile on that offering is "
        + currentOffering.getContent().get(currentIndexOfTile));
    PlayerHasChosenTileEvent playerHasChosenTileEvent =
        new PlayerHasChosenTileEvent(getNickOfActivePlayer());
    notifyListeners(playerHasChosenTileEvent);
  }

  @Override
  public void makeActivePlayerPlaceTile(int rowOfPatternLine) {
    LOGGER.info(
        getNickOfActivePlayer()
            + " tries to place a " + currentOffering.getContent().get(currentIndexOfTile)
            + " (index: " + currentIndexOfTile + ") on patter line "
            + rowOfPatternLine
            + ".");
    String nickActivePlayer = getNickOfActivePlayer();
    Player activePlayer = getPlayerByName(nickActivePlayer);
    if (!activePlayer.isValidPick(rowOfPatternLine, currentOffering, currentIndexOfTile)) {
      if (activePlayer.isAiPlayer()) {
        LOGGER.debug(nickActivePlayer + ", an AI-Player "
            + "tried to place a tile, where it is not possible. "
            + "This should not happen, as it is checked before.");
      }
      notifyListeners(new IllegalTurnEvent());
    } else {
      activePlayer.drawTiles(rowOfPatternLine, currentOffering, currentIndexOfTile);
      LOGGER.info(nickActivePlayer + " will end the turn now.");
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
   * Tell each player to tile the wall and get the points accordingly. Fires {@link
   * PlayerHasEndedTheGameEvent} if a player has ended the game in this tiling phase, fires {@link
   * GameFinishedEvent} at the end of this tiling phase in which someone has ended the game.
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
      for (Player player : playerList) {
        player.addEndOfGamePoints();
      }
      String winningMessage = getWinningMessage();
      GameFinishedEvent gameFinishedEvent = new GameFinishedEvent(winningMessage);
      notifyListeners(gameFinishedEvent);
    }
  }

  /**
   * The players should make their moves within a certain time span. Starts the timer and
   * if it is not cancelled by the player making a move before, will make the AI make a move
   * for it.
   *
   * @param playerName the name of the player.
   */
  private void startTimerForPlayer(String playerName) {
    timer = new Timer();
    timer.schedule(
        new java.util.TimerTask() {
          @Override
          public void run() {
            if (!hasGameEnded && !checkRoundFinished()) {
              LOGGER.info("Timer made move for Player " + playerName);
              makeAiPlayerMakeMove(playerName);
            }
          }
        },
        DELAYTIME
    );
  }
}
