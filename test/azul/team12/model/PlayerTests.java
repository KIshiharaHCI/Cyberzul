package azul.team12.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerTests {

  private Player player;

  @BeforeEach
  public void createPlayer() {
    player = new Player("Fritz");
  }

  @Test
  public void testGetName() {
    Assertions.assertTrue(player.getName().equals("Fritz"));
  }

  @Test
  public void testDrawTiles_getFilledFromRightToLeft() {
    Offering offering1 = new FactoryDisplay();
    Tile chosenTileKind = offering1.getContent().get(2);

    player.drawTiles(4,offering1, 2);
    Tile[][] patternLines = player.getPatternLines();

    Assertions.assertEquals(Tile.EMPTY_TILE,patternLines[4][0]);
    Assertions.assertEquals(chosenTileKind,patternLines[4][4]);
  }
}
