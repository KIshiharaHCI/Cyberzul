package azul.team12.model;

import java.util.List;

/**
 * Super class for FactoryDisplays and Table Centers. Intentionally not an Interface but an abstract
 * class, because that way, the abstract method can be package friendly instead of public. Ensures
 * encapsulation.
 */
public abstract class Offering extends Bag {

  /**
   * The Tiles on this Offering are saved in a List. By telling this Offering the index of the tile
   * that should be chosen, it returns all tiles of the same color. This method has side effects
   * based on the specific type (e.g. Factory Display or Table Center). These side effects don't
   * effect the return value or the way how this Offering should be used.
   *
   * @param indexOfTheTile the index of the Tile that should be chosen, e.g. the first tile (Index
   *                       0)
   * @return all tiles of the same color on this plate.
   */
  abstract List<ModelTile> takeTileWithIndex(int indexOfTheTile);
}
