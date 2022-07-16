package cyberzul.view;

import java.awt.GridLayout;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * //TODO Iurii, Kenji
 */
public class ImagePanel extends JPanel {

  //    private transient BufferedImage image;

  private static final long serialVersionUID = 10L;
  private final JPanel childPanel;
  private final String path;
  private final int width;
  private final int height;
  private final double scaleFactor;
  private JLabel backgroundLabel;

  /**
   * Constructor for creating a panel with a background image.
   *
   * @param childPanel  Panel to set transparent.
   * @param path        Path of the image to set.
   * @param width       Basic width from which the scale should start.
   * @param height      Basic height from which the scale should start.
   * @param scaleFactor The factor to make image larger to.
   */
  public ImagePanel(JPanel childPanel, String path, int width, int height, double scaleFactor) {
    this.childPanel = childPanel;
    this.path = path;
    this.width = width;
    this.height = height;
    this.scaleFactor = scaleFactor;
    setLayout(new GridLayout(1, 1));
    createBackgroundLabel();
  }

  /**
   * Creates a background with image for the game.
   *
   * @return: The {@link JPanel} with background image.
   */
  private void createBackgroundLabel() {
    URL imgUrl = getClass().getClassLoader().getResource(path);
    assert imgUrl != null;
    ImageIcon icon =
        new ImageIcon(
            new ImageIcon(imgUrl)
                .getImage()
                .getScaledInstance(
                    (int) Math.round(width * scaleFactor),
                    (int) Math.round(height * scaleFactor),
                    Image.SCALE_DEFAULT));

    childPanel.setOpaque(false);
    backgroundLabel = new JLabel(icon);
    backgroundLabel.setLayout(new GridLayout(1, 1));
    backgroundLabel.add(childPanel);
    add(backgroundLabel);
  }

  //    public ImagePanel(final URL imgURL1) {
  //        try {
  //            image = ImageIO.read(imgURL1);
  //        } catch (IOException ex) {
  //            throw new IllegalStateException(ex);
  //        }
  //    }

  //    @Override
  //    protected void paintComponent(Graphics g) {
  //        super.paintComponent(g);
  //        g.drawImage(image, 0, 0, this); // see javadoc for more info on the parameters
  //    }

}
