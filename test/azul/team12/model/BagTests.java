package azul.team12.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Contains Tests for the Bag Class.
 */
@SuppressWarnings("processing")
public class BagTests {

  /**
   * Test if a newly created Factory Display contains four Tiles.
   */
  @Test
  public void newFactoryDisplay_containsFourTiles() {
    //arrange
    FactoryDisplay factoryDisplay = new FactoryDisplay();

    //act
    int numberOfTilesInFactoryDisplay = factoryDisplay.getContent().size();

    //assert
    Assertions.assertEquals(4, numberOfTilesInFactoryDisplay);
  }

  /**
   * Tests if a Factory Display is empty after the tiles have been taken from it.
   */
  @Test
  public void testTakeTileWithIndex_afterTakingTiles_FactoryDisplayIsEmpty() {
    FactoryDisplay factoryDisplay = new FactoryDisplay();

    factoryDisplay.takeTileWithIndex(3);
    int numberOfTilesInFactoryDisplay = factoryDisplay.getContent().size();

    Assertions.assertEquals(0, numberOfTilesInFactoryDisplay);
  }

  @Test
  public void testTakeTileWithIndex_returnsAtLeastOneTile() {
    FactoryDisplay factoryDisplay = new FactoryDisplay();


  }
}
