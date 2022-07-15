package cyberzul.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Class to test ModelTile methods.
 */
public class ModelTileTests {

  @Test
  public void test_toTile() {
    ModelTile tile1 = ModelTile.RED_TILE;
    ModelTile tile2 = ModelTile.toTile("red tile");

    Assertions.assertEquals(tile1, tile2);
  }
}
