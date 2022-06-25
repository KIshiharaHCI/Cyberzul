package azul.team12.view.board.playerBoard;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class Plates extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;

  private final int padding = 5;
  private final int radius = 40;
  private int numberOfPlates = 0;
//  private List<Plate> plateList;

  public Plates(int numberOfPlates) {
    this.numberOfPlates = numberOfPlates;
    setMinimumSize(new Dimension(600, 600));
  }

//  public Plates(int numberOfPlates, List<Plate> plateList) {
//    this.numberOfPlates = numberOfPlates;
//    setMinimumSize(new Dimension(600, 600));
//    this.plateList = plateList;
//    for (int i = 1; i <= this.numberOfPlates; i++) {
//      int centerX = padding + (radius * 2 + padding) * i;
//      Plate plate = new Plate(centerX, radius, radius, i);
//      this.plateList.add(plate);
//    }
//  }


  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    for (int i = 1; i <= this.numberOfPlates; i++) {
      int centerX = padding + (radius * 2 + padding) * i;
      Plate plate = new Plate(centerX, radius, radius, i);
      plate.paintComponent(g2d);
    }

  }


}
