package cyberzul.view;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.net.URL;

/**
 * An Image Panel is used to add a background image to a panel.
 */
public class ImagePanel extends JPanel {

  //    private transient BufferedImage image;
  @Serial
  private static final long serialVersionUID = 10L;
  private final JComponent childPanel;
  private final String path;
  private final int width;
  private final int height;
  private final double scaleFactor;

  /**
   * Constructor for creating a panel with a background image.
   *
   * @param childPanel Panel to set transparent.
   * @param path Path of the image to set.
   * @param width Basic width from which the scale should start.
   * @param height Basic height from which the scale should start.
   * @param scaleFactor The factor to make image larger to.
   */
  @SuppressFBWarnings("EI_EXPOSE_REP2")
  public ImagePanel(JComponent childPanel, String path, int width, int height, double scaleFactor) {
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
    JLabel backgroundLabel = new JLabel(icon);
    backgroundLabel.setLayout(new GridLayout(1, 1));
    backgroundLabel.add(childPanel);
    add(backgroundLabel);
  }
}
