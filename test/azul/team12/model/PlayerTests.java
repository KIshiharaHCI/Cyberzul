package azul.team12.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Contains Tests for the Player class.
 */
public class PlayerTests {

  private Player player;
  ManipulableFactoryDisplay redOffering;
  ManipulableFactoryDisplay blueOffering;
  ManipulableFactoryDisplay blackOffering;
  ManipulableFactoryDisplay whiteOffering;
  ManipulableFactoryDisplay orangeOffering;

  @BeforeEach
  public void createPlayer() {
    player = new Player("Fritz");
    Tile[] redTiles = {Tile.RED_TILE, Tile.RED_TILE, Tile.RED_TILE, Tile.RED_TILE};
    Tile[] blueTiles = {Tile.BLUE_TILE, Tile.BLUE_TILE, Tile.BLUE_TILE, Tile.BLUE_TILE};
    Tile[] blackTiles = {Tile.BLACK_TILE, Tile.BLACK_TILE, Tile.BLACK_TILE, Tile.BLACK_TILE};
    Tile[] whiteTiles = {Tile.WHITE_TILE, Tile.WHITE_TILE, Tile.WHITE_TILE, Tile.WHITE_TILE};
    Tile[] orangeTiles = {Tile.ORANGE_TILE, Tile.ORANGE_TILE, Tile.ORANGE_TILE, Tile.ORANGE_TILE};
    redOffering = new ManipulableFactoryDisplay(redTiles);
    blueOffering = new ManipulableFactoryDisplay(blueTiles);
    blackOffering = new ManipulableFactoryDisplay(blackTiles);
    whiteOffering = new ManipulableFactoryDisplay(whiteTiles);
    orangeOffering = new ManipulableFactoryDisplay(orangeTiles);
  }

  /**
   * This method tests if a player is successfully created and has a name.
   */
  @Test
  public void testGetName() {
    Assertions.assertTrue(player.getName().equals("Fritz"));
  }

  /**
   * This method tests if the tiles that are drawn from an Offering are filled into the pattern
   * lines from right to left.
   */
  @Test
  public void testDrawTiles_getFilledFromRightToLeft() {
    Offering offering1 = new FactoryDisplay();
    Tile chosenTileKind = offering1.getContent().get(2);

    player.drawTiles(4, offering1, 2);
    Tile[][] patternLines = player.getPatternLines();

    Assertions.assertEquals(Tile.EMPTY_TILE, patternLines[4][0]);
    Assertions.assertEquals(chosenTileKind, patternLines[4][4]);
  }

  /**
   * Tests if the player gets awarded one point for the very first tile he placed on his wall.
   */
  @Test
  public void testTileWallAndGetPoints_getOnePointForFirstTile() {
    player.drawTiles(3, redOffering, 0);
    player.tileWallAndGetPoints();
    Assertions.assertEquals(1, player.getPoints());
  }

  /**
   * This method tests if the player gets 2 points for each horizontal line he fills completely on
   * the wall.
   */
  @Test
  public void testAddEndOfGamePoints_twoPointsForHorizontalLine() {
    //fill the first horizontal line
    player.drawTiles(3, redOffering, 0);
    player.tileWallAndGetPoints();
    player.drawTiles(3, blueOffering, 0);
    player.tileWallAndGetPoints();
    player.drawTiles(3, blackOffering, 0);
    player.tileWallAndGetPoints();
    player.drawTiles(3, whiteOffering, 0);
    player.tileWallAndGetPoints();
    player.drawTiles(3, orangeOffering, 0);
    player.tileWallAndGetPoints();
    int pointsBeforeEndOfGame = player.getPoints();

    player.addEndOfGamePoints();

    Assertions.assertEquals((pointsBeforeEndOfGame + Player.POINTS_FOR_COMPLETE_HORIZONTAL_LINE),
        player.getPoints());

    //fill the second horizontal line
    player.drawTiles(2, redOffering, 0);
    player.tileWallAndGetPoints();
    player.drawTiles(2, blueOffering, 0);
    player.tileWallAndGetPoints();
    player.drawTiles(2, blackOffering, 0);
    player.tileWallAndGetPoints();
    player.drawTiles(2, whiteOffering, 0);
    player.tileWallAndGetPoints();
    player.drawTiles(2, orangeOffering, 0);
    player.tileWallAndGetPoints();
    pointsBeforeEndOfGame = player.getPoints();

    player.addEndOfGamePoints();

    Assertions.assertEquals(
        (pointsBeforeEndOfGame + (2 * Player.POINTS_FOR_COMPLETE_HORIZONTAL_LINE)),
        player.getPoints());
  }

  /**
   * This method tests if the player gets 7 points for each vertical line he fills completely on
   * the wall.
   */
  @Test
  public void testAddEndOfGamePoints_sevenPointsForVerticalLine() {
    player.drawTiles(0, blueOffering, 0);
    player.tileWallAndGetPoints();
    player.drawTiles(1, whiteOffering, 0);
    player.tileWallAndGetPoints();
    player.drawTiles(2, blackOffering, 0);
    player.tileWallAndGetPoints();
    player.drawTiles(3, redOffering, 0);
    player.tileWallAndGetPoints();
    //has to be repeated because the 5th line is 5 long.
    player.drawTiles(4, orangeOffering, 0);
    player.drawTiles(4, orangeOffering, 0);
    player.tileWallAndGetPoints();

    int pointsBeforeEndOfGame = player.getPoints();

    player.addEndOfGamePoints();

    Assertions.assertEquals((pointsBeforeEndOfGame + Player.POINTS_FOR_COMPLETE_VERTICAL_LINE),
        player.getPoints());
  }

  /**
   * This method tests if the player gets 10 points for every tile which he has five times on his
   * wall.
   */
  @Test
  public void testAddEndOfGamePoints_tenPointsForAllTilesOfOneColor() {
    player.drawTiles(0, redOffering, 0);
    player.drawTiles(1, redOffering, 0);
    player.drawTiles(2, redOffering, 0);
    player.drawTiles(3, redOffering, 0);
    player.drawTiles(4, redOffering, 0);
    player.drawTiles(4, redOffering, 0);
    player.tileWallAndGetPoints();

    int pointsBeforeEndOfGame = player.getPoints();

    player.addEndOfGamePoints();

    Assertions.assertEquals(
        (pointsBeforeEndOfGame + Player.POINTS_FOR_PLACING_ALL_STONES_OF_ONE_COLOR),
        player.getPoints());
  }


  /**
   * This test tests if a pattern line that already contains tiles will add new tiles to it.
   * E.g. If line 4 contains 4 blue tiles already, will it add a new tile on top?
   */
  @Test
  public void testDrawTiles_Overflow() {
    player.drawTiles(4, blueOffering, 0);
    player.drawTiles(4, blueOffering, 0);

    Assertions.assertEquals(Tile.BLUE_TILE, player.getPatternLines()[4][0]);
  }

  /**
   * This test tests if overflowing tiles that don't fit into a pattern line get added to the
   * floorline.
   */
  @Test
  public void testFillFloorLine_OverflowingTilesGetAdded() {
    player.drawTiles(0, blueOffering, 0);
    Assertions.assertEquals(3, player.getFloorLine().size());

    player.drawTiles(2, redOffering, 0);
    Assertions.assertEquals(4, player.getFloorLine().size());

    player.drawTiles(4, orangeOffering, 0);
    player.drawTiles(4, orangeOffering, 0);
    Assertions.assertEquals(7, player.getFloorLine().size());
  }
}
