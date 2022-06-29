package azul.team12.view.board.playerBoard;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class FactoryDisplayBar extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;

  private final int padding = 5;
  private final int radius = 40;
  private int numberOfPlates;

//  private List<Plate> plateList;

  public FactoryDisplayBar() {
  }


  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    try {
      BufferedImage image = ImageIO.read(new File("img/factory_display.png"));
      g.drawImage(image,0,0,null);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }


}
