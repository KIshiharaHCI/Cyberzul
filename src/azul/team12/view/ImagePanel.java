package azul.team12.view;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {

  private static final long serialVersionUID = 10L;
  private transient BufferedImage image;

  public ImagePanel(final URL imgURL1) {
    try {
      image = ImageIO.read(imgURL1);
    } catch (IOException ex) {
      throw new IllegalStateException(ex);
    }
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(image, 0, 0, this); // see javadoc for more info on the parameters
  }

}
