package azul.team12.view.board.playerBoard;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import javax.swing.JPanel;

public class Plate extends JPanel {

  private int centerX;
  private int centerY;
  private int radius;

  public Plate(int centerX, int centerY, int radius) {
    this.centerX = centerX;
    this.centerY = centerY;
    this.radius = radius;
  }

  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    Ellipse2D.Double circle = new Ellipse2D.Double(centerX - radius, centerY - radius, radius * 2, radius * 2);
    g2d.setColor(Color.YELLOW);
    g2d.fill(circle);
    g2d.setColor(Color.GREEN);
    g2d.draw(circle);
  }




    public void paintCircleAt(Graphics2D g2d, int radius, int centerX, int centerY, Color stroke, Color fill) {

  }

}
