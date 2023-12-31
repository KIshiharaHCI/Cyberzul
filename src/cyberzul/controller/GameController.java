package cyberzul.controller;

import cyberzul.model.Model;
import cyberzul.model.ModelTile;
import cyberzul.model.Offering;
import cyberzul.model.WallBackgroundPattern;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Implementation of the controller interface which handles the interaction between the main model-
 * and view-classes.
 */
public class GameController implements Controller {

  private static final Logger LOGGER = LogManager.getLogger(GameController.class);
  private final Model model;


  @SuppressFBWarnings(
      value = "EI_EXPOSE_REP",
      justification =
          "We are aware that data "
              + "encapsulation is violated here and that this is in principle bad. However, it is "
              + "intended, because the controller needs to be able to "
              + "invoke methods of the model. ")
  public GameController(Model model) {
    this.model = model;
  }

  @Override
  public void addPlayer(String name) {
    model.loginWithName(name);
  }

  @Override
  public void startGame() {
    model.startGame();
  }

  @Override
  public void restartGame() {
    model.restartGame();
  }

  @Override
  public List<Offering> getOfferings() {
    return model.getOfferings();
  }

  @Override
  public String getNickOfActivePlayer() {
    return model.getPlayerNamesList().get(model.getIndexOfActivePlayer());
  }

  @Override
  public String getNickOfNextPlayer() {
    return model.getPlayerNamesList().get(model.getIndexOfNextPlayer());
  }

  @Override
  public int getPoints(String playerName) {
    return model.getPoints(playerName);
  }

  @Override
  public int getMinusPoints(String playerName) {
    return model.getMinusPoints(playerName);
  }

  @Override
  public void chooseTileFrom(String playerName, int indexOfTile, int offeringIndex) {
    model.notifyTileChosen(playerName, indexOfTile, offeringIndex);
  }

  @Override
  public void placeTileAtPatternLine(int rowOfPatternLine) {
    LOGGER.info(
        "Player "
            + getNickOfActivePlayer()
            + " tries to place a tile on the "
            + rowOfPatternLine
            + ". row of pattern lines.");
    model.makeActivePlayerPlaceTile(rowOfPatternLine);
  }

  public void placeTileAtFloorLine() {
    model.tileFallsDown();
  }

  @Override
  public List<String> getPlayerNamesList() {
    return model.getPlayerNamesList();
  }

  @Override
  public ModelTile[][] getPatternLinesOfPlayer(String playerName) {
    return model.getPatternLinesOfPlayer(playerName);
  }

  @Override
  public List<ModelTile> getFloorLineOfPlayer(String playerName) {
    return model.getFloorLineOfPlayer(playerName);
  }

  @Override
  public ModelTile[][] getWallOfPlayerAsTiles(String playerName) {
    return model.getWallOfPlayer(playerName);
  }

  @Override
  public ModelTile[][] getTemplateWall() {
    return WallBackgroundPattern.getTemplateWall();
  }

  @Override
  public List<String> rankingPlayerWithPoints() {
    return model.rankingPlayerWithPoints();
  }

  @Override
  public void replacePlayerByAi(String playerName) {
    model.replacePlayerByAi(playerName);
  }

  @Override
  public void cancelGameForAllPlayers() {
    model.cancelGame();
  }

  @Override
  public boolean isGameStarted() {
    return model.isGameStarted();
  }

  @Override
  public void startSinglePlayerMode(int numberOfAiPlayers) {
    model.startSinglePlayerMode(numberOfAiPlayers);
  }

  @Override
  public void postMessage(String text) {
    model.postChatMessage(text);
  }

  @Override
  public void setBulletMode(boolean isBulletMode) {
    model.setBulletMode(isBulletMode);
  }

  @Override
  public boolean getBulletMode() {
    return model.getBulletMode();
  }

  @Override
  public void setMode(int mode) {
    model.setMode(mode);
  }

  @Override
  public int getMode() {
    return model.getMode();
  }

  @Override
  public String getPlayerName() {
    return model.getPlayerName();
  }

}
