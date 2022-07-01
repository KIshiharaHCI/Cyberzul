package azul.team12.view.board;

import azul.team12.view.listeners.TileClickListener;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.JPanel;

public class Plates extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;

  private int numberOfPlates = 0;
  private List<Plate> plateList;

  public Plates(int numberOfPlates, List<Plate> plateList, List<Tile> tileList,
      TileClickListener tileClickListener) {
    this.setLayout(new FlowLayout());
    this.numberOfPlates = numberOfPlates;
    this.plateList = plateList;

    for (int i = 1; i <= this.numberOfPlates; i++) {
      Plate plate = new Plate(i, tileClickListener);
      add(plate);
      this.plateList.add(plate);
    }
  }
}
