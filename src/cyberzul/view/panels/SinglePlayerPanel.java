package cyberzul.view.panels;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serial;
import java.net.URL;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/** Lobby Screen after selecting Single Player Mode. */
public class SinglePlayerPanel extends JPanel {
  @Serial private static final long serialVersionUID = 17L;
  private transient BufferedImage image;
  private final Dimension containerDimension;

  /**
   * Constructor for the SinglePlayerPanel.

   * @param frameDimension the dimensions for the frame.
   */
  public SinglePlayerPanel(Dimension frameDimension) {

    setMinimumSize(frameDimension);
    setMaximumSize(frameDimension);
    setLayout(null);
    setOpaque(false);

    containerDimension =
        new Dimension((int) (frameDimension.width * 0.7), (int) (frameDimension.height * 0.7));

    JPanel container =
        new JPanel() {
          @Override
          protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, null);
          }
        };
    container.setBounds(200, 80, containerDimension.width, containerDimension.height);
    container.setOpaque(false);
    add(container);

    try {
      URL imgUrl = getClass().getClassLoader().getResource("img/lobby-panel.png");
      image = ImageIO.read(Objects.requireNonNull(imgUrl));
      image.getScaledInstance(
          containerDimension.width, containerDimension.height, Image.SCALE_SMOOTH);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
