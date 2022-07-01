package azul.team12.view.board;

import azul.team12.model.Offering;
import azul.team12.view.listeners.TileClickListener;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

/**
 * This is the Panel on which the Factory Displays get positioned.
 */
public class PlatesPanel extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;

  private int numberOfPlates = 0;
  //private List<Plate> plateList = new ArrayList<>();

  //TODO: it makes more sense to start the for loop at one and give offerings and delete the
  // +1 where the plate is constructed
  public PlatesPanel(List<Offering> factoryDisplays, List<Tile> tileList,
      TileClickListener tileClickListener) {
    this.setLayout(new FlowLayout());

    for (int i = 0; i < factoryDisplays.size(); i++) {
      // we start at 1 because the first offering is the table center
      Plate plate = new Plate(i + 1, tileClickListener, factoryDisplays.get(i).getContent());
      add(plate);
      //this.plateList.add(plate);
    }
  }
}
