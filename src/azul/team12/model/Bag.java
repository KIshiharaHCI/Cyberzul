package azul.team12.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Bag {

  Bag() {
    initializeContent();
  }

  /**
   * Set up the initial content of this bag.
   */
  abstract void initializeContent();

  /**
   * Safely return the content of this bag, without enabling someone to tinker with its content.
   *
   * @return a copy of the content of this bag.
   */
  public abstract List<Tile> getContent();

}
