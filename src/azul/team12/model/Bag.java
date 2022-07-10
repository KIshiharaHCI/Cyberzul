package azul.team12.model;

import java.util.List;

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
  public abstract List<ModelTile> getContent();

}
