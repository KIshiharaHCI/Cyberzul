package azul.team12.view.board.playerBoard;

import java.awt.Graphics;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Plate extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;

  private int position;

  private int centerX;
  private int centerY;
  private int radius;

  public Plate(int centerX, int centerY, int radius, int position) {
    this.centerX = centerX;
    this.centerY = centerY;
    this.radius = radius;
    this.position = position;
  }

  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    URL imgURL = getClass().getClassLoader().getResource("img/manufacturing-plate.png");
    ImageIcon icon = new ImageIcon(imgURL);
    g.drawImage(icon.getImage(),
        centerX - radius, centerY - radius,
        radius * 2, radius * 2, null);
  }

}
