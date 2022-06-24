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

  /**
   * The method Tile.values() returns all values of this enum. Since Empty Tile is not a real tile,
   * it often makes sense to exclude it from the list of tiles.
   *
   * @return the values of this enum without the empty tile.
   */
  public static Tile[] valuesWithoutEmptyTile(){
    Tile[] valuesWithEmptyTile = Tile.values();
    Tile[] valuesWithoutEmptyTile = new Tile[valuesWithEmptyTile.length - 1];
    for(int i = 0; i < valuesWithoutEmptyTile.length; i++){
      if(valuesWithEmptyTile[i] != Tile.EMPTY_TILE){
        valuesWithoutEmptyTile[i] = valuesWithEmptyTile[i];
      }
      else{
        i = i--;
      }
    }
    return valuesWithoutEmptyTile;
  }
}
