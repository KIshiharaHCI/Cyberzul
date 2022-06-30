package azul.team12.view.board;

import azul.team12.view.listeners.TileClickListener;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.JPanel;

public class Plates extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;

  private final int padding = 5;
  private final int radius = 58;
  private int numberOfPlates = 0;
  private List<Plate> plateList;

  private List<Tile> tileList;

//  public Plates(int numberOfPlates) {
//    this.numberOfPlates = numberOfPlates;
//    setMinimumSize(new Dimension(600, 600));
//  }

  public Plates(int numberOfPlates, List<Plate> plateList, List<Tile> tileList,
      TileClickListener tileClickListener) {
    this.setLayout(new FlowLayout());
    this.numberOfPlates = numberOfPlates;

    this.plateList = plateList;
    this.tileList = tileList;

    for (int i = 1; i <= this.numberOfPlates; i++) {
      int centerX = padding + radius + (radius * 2 + padding) * (i - 1);
      Plate plate = new Plate(centerX, radius, radius, i, tileClickListener);
      add(plate);
      this.plateList.add(plate);
    }
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(1200, 180);
  }

//  protected void paintComponent(Graphics g) {
//    super.paintComponent(g);
//    Graphics2D g2d = (Graphics2D) g;
//    for (int i = 1; i <= this.numberOfPlates; i++) {
//      int centerX = padding + radius + (radius * 2 + padding) * (i - 1);
//      if (i == 1) {
//        centerX = padding + radius;
//      }
//      Plate plate = new Plate(centerX, radius, radius, i);
//      plate.paintComponent(g2d);
//
//    }
//  }


  public int getPadding() {
    return padding;
  }

  public int getRadius() {
    return radius;
  }

  public int getNumberOfPlates() {
    return numberOfPlates;
  }

  public void setNumberOfPlates(int numberOfPlates) {
    this.numberOfPlates = numberOfPlates;
  }

  public List<Plate> getPlateList() {
    return plateList;
  }

  public void setPlateList(List<Plate> plateList) {
    this.plateList = plateList;
  }
}
