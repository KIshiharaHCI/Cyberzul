package azul.team12.view.board.playerBoard;

import java.awt.*;
import javax.swing.JPanel;

public class FactoryDisplayBar extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;

  private final int padding = 5;
  private final int radius = 40;
  private int numberOfPlates;

//  private List<Plate> plateList;

  public FactoryDisplayBar(int numberOfPlates) {
    this.numberOfPlates = numberOfPlates;
  }

  public void setPanels(){
    for (int i = 0; i < numberOfPlates; i++) {
      int centerX = padding + (radius * 2 + padding) * i;
      FactoryDisplayPlate displayPlate = new FactoryDisplayPlate(100, 100);
      add(displayPlate);
    }
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
    /*
    Graphics2D g2d = (Graphics2D) g;
    for (int i = 1; i <= this.numberOfPlates; i++) {
      int centerX = padding + (radius * 2 + padding) * i;
      FactoryDisplayPlate displayPlate = new FactoryDisplayPlate(centerX, radius, radius, i);
      displayPlate.paintComponent(g2d);
    }

     */

  }


}
