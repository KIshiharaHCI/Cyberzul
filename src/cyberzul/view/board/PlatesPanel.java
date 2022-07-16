package cyberzul.view.board;

import cyberzul.model.Offering;
import cyberzul.view.listeners.TileClickListener;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

/**
 * This is the Panel on which the Factory Displays get positioned.
 */
public class PlatesPanel extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;

  private final transient List<Plate> plateList = new ArrayList<>();

  // TODO: it makes more sense to start the for loop at one and give offerings and delete the
  // +1 where the plate is constructed

  public PlatesPanel(List<Offering> factoryDisplays, TileClickListener tileClickListener) {
    setAlignmentX(Component.LEFT_ALIGNMENT);
    setOpaque(false);

    initialize(factoryDisplays, tileClickListener);
  }

  public void initialize(List<Offering> factoryDisplays, TileClickListener tileClickListener) {
    for (int i = 0; i < factoryDisplays.size(); i++) {
      // we start at 1 because the first offering is the table center
      Plate plate = new Plate(i + 1, tileClickListener, factoryDisplays.get(i).getContent());
      add(plate);
      this.plateList.add(plate);
    }
  }

  public void remove() {
    for (Plate plate : this.plateList) {
      this.remove(plate);
    }
  }
}
