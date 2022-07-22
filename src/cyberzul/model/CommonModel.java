package cyberzul.model;

import static java.util.Objects.requireNonNull;

import cyberzul.model.events.GameEvent;
import cyberzul.model.events.PlayerAddedMessageEvent;
import cyberzul.model.events.PlayerDoesNotExistEvent;
import cyberzul.network.client.messages.PlayerTextMessage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Contains methods and fields that are common within Model classes.
 */
public abstract class CommonModel implements ModelStrategy {

  private static final Logger LOGGER = LogManager.getLogger(CommonModel.class);
  protected final PropertyChangeSupport support;
  protected int indexOfActivePlayer;
  protected ArrayList<Player> playerList = new ArrayList<>();
  protected ArrayList<Offering> offerings;
  protected boolean isGameStarted = false;

  public CommonModel(List<PropertyChangeListener> listenerList) {
    support = new PropertyChangeSupport(this);
    addListenersToTheModel(listenerList);
  }

  /**
   * The ModelStrategyChooser saves the PropertyChangeListener that should be added to the model in
   * a list and adds them if they should be added.
   */
  private void addListenersToTheModel(List<PropertyChangeListener> listenerList) {
    for (PropertyChangeListener listener : listenerList) {
      this.addPropertyChangeListener(listener);
    }
  }

  @Override
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    requireNonNull(listener);
    support.addPropertyChangeListener(listener);
    System.out.println("propertychangelisteners added");
  }

  @Override
  public void removePropertyChangeListener(PropertyChangeListener listener) {
    requireNonNull(listener);
    support.removePropertyChangeListener(listener);
  }

  /**
   * Notify subscribed listeners that the state of the model has changed. To this end, a specific
   * {@link GameEvent} gets fired such that the attached observers (i.e., {@link
   * PropertyChangeListener}) can distinguish between what exactly has changed. {@link GameEvent}
   * gets fired such that the attached observers (i.e., {@link PropertyChangeListener}) can
   * distinguish between what exactly has changed.
   *
   * @param event A concrete implementation of {@link GameEvent}
   */
  protected void notifyListeners(GameEvent event) {
    support.firePropertyChange(event.getName(), null, event);
  }

  @Override
  public boolean checkRoundFinished() {
    for (Offering offering : offerings) {
      // if any of the offerings still has a content, the round is not yet finished
      if (!offering.getContent().isEmpty()) {
        return false;
      }
    }
    return true;
  }

  @Override
  public int getIndexOfPlayerWithSpm() {
    for (int i = 0; i < playerList.size(); i++) {
      Player player = playerList.get(i);
      if (player.hasStartingPlayerMarker()) {
        LOGGER.info(player.getName() + " with index " + i + " was the player " + "with the SPM.");
        return i;
      }
    }
    LOGGER.debug(
        "We called giveIndexOfPlayer with Start Player Marker when no player had "
            + "the SPM. Probably this is the case because at the end of the turn noone had the "
            + "SPM.");
    return 0;
  }

  @Override
  public boolean isGameStarted() {
    return isGameStarted;
  }

  @Override
  public ModelTile[][] getWallOfPlayer(String playerName) {

    Player player = getPlayerByName(playerName);
    ModelTile[][] templateWall = player.getWallPattern().pattern;
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

  @Override
  public List<String> getPlayerNamesList() {
    List<String> list = new ArrayList<>();
    for (Player player : playerList) {
      list.add(player.getName());
    }
    return list;
  }

  @Override
  public ModelTile[][] getPatternLinesOfPlayer(String playerName) {
    Player player = getPlayerByName(playerName);
    return player.getPatternLines();
  }

  @Override
  public List<ModelTile> getFloorLineOfPlayer(String playerName) {
    Player player = getPlayerByName(playerName);
    return player.getFloorLine();
  }

  @Override
  public List<Offering> getOfferings() {
    @SuppressWarnings("unchecked")
    List<Offering> offeringsClone = (List<Offering>) offerings.clone();
    return offeringsClone;
  }

  @Override
  public int getIndexOfActivePlayer() {
    return indexOfActivePlayer;
  }

  @Override
  public int getPoints(String nickname) {
    for (Player player : playerList) {
      if (player.getName().equals(nickname)) {
        return player.getPoints();
      }
    }
    notifyListeners(new PlayerDoesNotExistEvent(nickname));
    return 0;
  }

  @Override
  public int getMinusPoints(String nickname) {
    for (Player player : playerList) {
      if (player.getName().equals(nickname)) {
        return player.getMinusPoints();
      }
    }
    notifyListeners(new PlayerDoesNotExistEvent(nickname));
    return 0;
  }

  @Override
  public String getNickOfActivePlayer() {
    return playerList.get(indexOfActivePlayer).getName();
  }

  @Override
  public Player getPlayerByName(String nickname) {
    for (Player player : playerList) {
      if (player.getName().equals(nickname)) {
        return player;
      }
    }
    LOGGER.debug("The model was given a name by view that is not in the playerList.");
    return null;
  }


  @Override
  public String getWinningMessage() {
    String messageToBeReturned = "";

    ArrayList<Integer> playerPoints = new ArrayList<>();
    for (Player player : playerList) {
      playerPoints.add(player.getPoints());
      LOGGER.info(player.getName() + " points: " + player.getPoints());
    }
    int highestScore = Collections.max(playerPoints);
    ArrayList<Player> playerWithSameMostPoints = new ArrayList<>();
    // checking if two or more players have the highest score
    for (Player player : playerList) {
      if (player.getPoints() == highestScore) {
        playerWithSameMostPoints.add(player);
      }
    }
    // most likely case --> one player has most points
    if (playerWithSameMostPoints.size() == 1) {
      LOGGER.info("We have one winner because he/she has the most points.");
      int bestIndex = playerPoints.indexOf(highestScore);
      Player playersWithMostPoints = playerList.get(bestIndex);
      messageToBeReturned = "Hurray! " + playersWithMostPoints.getName()
          + " has won the game! You shall be "
          + "allowed to help Queen MaXIne build her Cyber Palace with our "
          + "beautiful cyber tiles!";
      return  messageToBeReturned;
    } else {
      LOGGER.info("We have >= two players with the same amount of points at the end of the game.");
      ArrayList<Integer> amountOfCompleteHorizontalLines = new ArrayList<>();
      // add all values for complete horizontal lines of the players with same points to list
      for (Player player : playerWithSameMostPoints) {
        LOGGER.debug(player.getName() + " complete lines: "
            + player.getNumberOfCompleteHorizontalLines());
        amountOfCompleteHorizontalLines.add(player.getNumberOfCompleteHorizontalLines());
      }
      // get max of this list
      int scoreMostHorizontalComplete = Collections.max(amountOfCompleteHorizontalLines);
      // create new list with all the players that have this score
      ArrayList<Player> playersWithSameMostHorizontallyCompleteRows = new ArrayList<>();
      for (Player player : playerWithSameMostPoints) {
        if (player.getNumberOfCompleteHorizontalLines() == scoreMostHorizontalComplete) {
          playersWithSameMostHorizontallyCompleteRows.add(player);
        }
      }
      if (playersWithSameMostHorizontallyCompleteRows.size() == 1) {
        Player playerWithMostHoriz = playersWithSameMostHorizontallyCompleteRows.get(0);
        messageToBeReturned = "Hurray! " + playerWithMostHoriz.getName()
            + " has won the game! You shall be "
            + "allowed to help Queen MaXIne build her Cyber Palace with our "
            + "beautiful cyber tiles!";
      } else {
        LOGGER.info("The victory is shared! O.O");
        messageToBeReturned = "It is a tie! The victory is shared between: \n";
        String winners = "";
        for (Player player : playersWithSameMostHorizontallyCompleteRows) {
          winners = winners + player.getName() + "\n";
        }
        messageToBeReturned = messageToBeReturned + winners + "Hurray!!!! All of you shall be "
            + "allowed to help Queen MaXIne build her Cyber Palace with "
            + "our beautiful cyber tiles!";
      }
      return messageToBeReturned;

    }

  }

  @Override
  public List<String> rankingPlayerWithPoints() {
    // making a copy of player names list as ArrayList, so we can give back a clone later
    List<String> playerNamesRankingList = getPlayerNamesList();
    ArrayList<String> orderedPlayerNamesRankingList = new ArrayList<>();
    for (String playerName : playerNamesRankingList) {
      orderedPlayerNamesRankingList.add(playerName);
    }

    Collections.sort(
        orderedPlayerNamesRankingList, (o1, o2) -> Integer.compare(getPoints(o1), getPoints(o2)));
    Collections.reverse(orderedPlayerNamesRankingList);
    try {
      @SuppressWarnings("unchecked")
      List<String> toBeReturnedList = (List<String>) orderedPlayerNamesRankingList.clone();
      return toBeReturnedList;
    } catch (ClassCastException e) {
      throw new AssertionError(
          "orderedPlayerNamesRankingList couldn't be casted to " + "List<String>.");
    }
  }

  @Override
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

  @Override
  public void postChatMessage(String message) {
    PlayerTextMessage playerTextMessage = new PlayerTextMessage(getPlayerName(),
        new Date(), message);
    notifyListeners(new PlayerAddedMessageEvent(playerTextMessage));
  }

}
