package cyberzul.view.board;

import cyberzul.model.Offering;
import cyberzul.view.listeners.TileClickListener;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/** This is the Panel on which the Factory Displays get positioned. */
public class PlatesPanel extends JPanel {
  @Serial
  private static final long serialVersionUID = 7526472295622776147L;

  private final transient List<Plate> plateList = new ArrayList<>();

  // TODO: it makes more sense to start the for loop at one and give offerings and delete the
  // +1 where the plate is constructed

  /**
   * Constructor for the Platespanel. Shared by all players.
   * @param factoryDisplays contents of each offering
   * @param tileClickListener listener class to handle mouse clicked events.
   */
  public PlatesPanel(List<Offering> factoryDisplays, TileClickListener tileClickListener) {
    setAlignmentX(Component.LEFT_ALIGNMENT);
    setOpaque(false);

    initialize(factoryDisplays, tileClickListener);
  }

  /**
   * Used to initialize the number of Plates based on how many players are playing the game.
   * @param factoryDisplays used to get the content for each offering
   * @param tileClickListener listener class to handle mouse clicked events.
   */
  public void initialize(List<Offering> factoryDisplays, TileClickListener tileClickListener) {
    for (int i = 0; i < factoryDisplays.size(); i++) {
      // we start at 1 because the first offering is the table center
      Plate plate = new Plate(i + 1, tileClickListener, factoryDisplays.get(i).getContent());
      add(plate);
      this.plateList.add(plate);
    }
  }

  /**
   * Used by TileClickListener to remove the Plates of the last player.
   */
  public void remove() {
    for (Plate plate : this.plateList) {
      this.remove(plate);
    }
  }
}
