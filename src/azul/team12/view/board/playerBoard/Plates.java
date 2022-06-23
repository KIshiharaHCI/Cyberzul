package azul.team12.view.board.playerBoard;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class Plates extends JPanel {
  private int numberOfPlates = 0;

  public Plates(int numberOfPlates) {
    this.numberOfPlates = numberOfPlates;
    setMinimumSize(new Dimension(600, 600));
//    createTableCenter();
  }
//  private void createTableCenter() {
//    setLayout(new BorderLayout());
//    createSouth();
//    createCenter();
  //}

//  private void createCenter() {
//    JPanel center = new JPanel();
//    center.setLayout(new FlowLayout());
//    add(center, BorderLayout.CENTER);


  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    int padding = 5;
    int radius = 30;
    for (int i = 1; i <=this.numberOfPlates; i++) {

      Graphics2D g2d = (Graphics2D) g;
      int centerX = padding + (radius * 2 + padding)*i;
      Plate plate = new Plate(centerX, radius, radius);
      plate.paintComponent(g2d);
    }
  }

}
