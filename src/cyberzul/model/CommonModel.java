package cyberzul.model;

import static java.util.Objects.requireNonNull;

import cyberzul.model.events.GameEvent;
import cyberzul.model.events.PlayerDoesNotExistEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Contains methods and fields that are common within Model classes. */
public abstract class CommonModel implements ModelStrategy {

  private static final Logger LOGGER = LogManager.getLogger(CommonModel.class);
  protected final PropertyChangeSupport support;
  protected int indexOfActivePlayer;
  protected ArrayList<Player> playerList = new ArrayList<>();
  protected ArrayList<Offering> offerings;
  protected boolean isGameStarted = false;

  public CommonModel() {
    support = new PropertyChangeSupport(this);
  }

  @Override
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    requireNonNull(listener);
    support.addPropertyChangeListener(listener);
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
  public String getPlayerWithMostPoints() {
    // TODO: What if two players have the same points?
    ArrayList<Integer> playerPoints = new ArrayList<>();
    for (Player player : playerList) {
      playerPoints.add(player.getPoints());
    }
    int highestScore = Collections.max(playerPoints);
    int bestIndex = playerPoints.indexOf(highestScore);
    Player playerWithMostPoints = playerList.get(bestIndex);
    return playerWithMostPoints.getName();
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
}
