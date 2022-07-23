package cyberzul.view;

import cyberzul.view.board.TransparentImageIcon;
import java.awt.Image;
import java.io.Serial;
import java.net.URL;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/** A JButton class used for setting buttons with Images and absolute sizing. */
public class IconButton extends JButton {
  @Serial private static final long serialVersionUID = 15L;

  private final int xposition;
  private final int yposition;
  private final int buttonWidth;
  private final int buttonHeight;
  private String iconPath;
  private float opacity;

  /**
   * Constructor of a IconButton.
   *
   * @param path for the ImageIcon to set
   * @param xposition on the parent panel
   * @param yposition on the parent panel
   * @param buttonWidth width in px of the button
   * @param buttonHeight height in px of the button
   */
  public IconButton(String path, int xposition, int yposition, int buttonWidth, int buttonHeight) {
    this.xposition = xposition;
    this.yposition = yposition;
    this.iconPath = path;
    this.buttonWidth = buttonWidth;
    this.buttonHeight = buttonHeight;
    this.opacity = 1.0f;
    createIconButton();
  }

  private void createIconButton() {
    createImageIcon();
    this.setBounds(xposition, yposition, buttonWidth, buttonHeight);
    this.setOpaque(false);
    this.setFocusPainted(false);
    this.setBorderPainted(false);
    this.setContentAreaFilled(false);
    this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
  }

  private void createImageIcon() {
    URL iconUrl = getClass().getClassLoader().getResource(iconPath);
    try {
      Image image =
          ImageIO.read(Objects.requireNonNull(iconUrl))
              .getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
      ImageIcon imageIcon = new TransparentImageIcon(new ImageIcon(image), this.opacity);
      this.setIcon(imageIcon);
    } catch (Exception e) {
      e.printStackTrace();
      iconPath = null;
    }
  }

  public void setOpacity(float opacity) {
    this.opacity = opacity;
    createImageIcon();
  }

  public float getOpacity() {
    return opacity;
  }
}
