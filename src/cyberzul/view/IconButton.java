package cyberzul.view;

import java.awt.Image;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * //TODO Kenji, Iurii.
 */
public class IconButton extends JButton {
  private static final long serialVersionUID = 15L;


  private final int xposition;
  private final int yposition;
  private final int buttonWidth;
  private final int buttonHeight;
  private String iconPath;

  /**
   * //TODO Kenji, Iurii.
   *
   * @param path
   * @param xposition
   * @param yposition
   * @param buttonWidth
   * @param buttonHeight
   */
  public IconButton(String path, int xposition, int yposition, int buttonWidth, int buttonHeight) {
    this.xposition = xposition;
    this.yposition = yposition;
    this.iconPath = path;
    this.buttonWidth = buttonWidth;
    this.buttonHeight = buttonHeight;
    createIconButton();
  }

  private void createIconButton() {
    URL iconUrl = getClass().getClassLoader().getResource(iconPath);
    try {
      Image icon =
          ImageIO.read(iconUrl).getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
      this.setIcon(new ImageIcon(icon));
    } catch (Exception e) {
      e.printStackTrace();
      iconPath = null;
    }
    this.setBounds(xposition, yposition, buttonWidth, buttonHeight);
    this.setOpaque(false);
    this.setFocusPainted(false);
    this.setBorderPainted(false);
    this.setContentAreaFilled(false);
    this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
  }
}
