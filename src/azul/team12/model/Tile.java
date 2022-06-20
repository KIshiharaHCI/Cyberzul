package azul.team12.model;

/**
 * The Tiles that the player can choose from
 */
public enum Tile {
  RED_TILE("red tile"), BLACK_TILE("black tile"), WHITE_TILE("white tile"), BLUE_TILE("blue tile"),
  ORANGE_TILE("orange tile"), EMPTY_TILE("empty tile");

  String name;

  /**
   * The pattern on the wall defining where which tiles may be placed.
   */
  private static Tile[][] templateWall =
      {{BLUE_TILE, ORANGE_TILE, RED_TILE, BLACK_TILE, WHITE_TILE},
          {WHITE_TILE, BLUE_TILE, ORANGE_TILE, RED_TILE, BLACK_TILE},
          {BLACK_TILE, WHITE_TILE, BLUE_TILE, ORANGE_TILE, RED_TILE},
          {RED_TILE, BLACK_TILE, WHITE_TILE, BLUE_TILE, ORANGE_TILE},
          {ORANGE_TILE, RED_TILE, BLACK_TILE, WHITE_TILE, BLUE_TILE}};

  Tile(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return this.name;
  }

  public Tile[][] getTemplateWall(){
    return templateWall.clone();
  }
}
