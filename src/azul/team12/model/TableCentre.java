package azul.team12.model;

import java.util.HashMap;
import java.util.Map;

/**
 * "Tischmitte"
 * One of the two places the player can draw tiles from.
 */
public class TableCentre extends ManufacturingPlates {
  private boolean containsPunishmentStone;

  public TableCentre() {
    super();
    this.containsPunishmentStone = true;
  }


}
