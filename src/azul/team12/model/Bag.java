package azul.team12.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Bag {

  List<Tile> content;

  public Bag() {initializeContent();
  }

  abstract void initializeContent();
}
