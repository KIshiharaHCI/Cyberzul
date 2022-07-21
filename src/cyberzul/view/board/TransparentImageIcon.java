package cyberzul.view.board;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/** An Icon Wrapper class that paints the contained icon with a specified transparency. */
@SuppressFBWarnings(
    value = "EI_EXPOSE_REP",
    justification =
        "We are aware that data "
            + "encapsulation is violated here and that this is in principle bad. "
            + "However, as here just "
            + "information of the view is possible to be changed from an external source and the "
            + "model is safe, we think it is ok to suppress this warning.")
public class TransparentImageIcon extends ImageIcon {

  private static final long serialVersionUID = 6L;
  private final transient Icon icon;
  private final float opacity;
  private transient Image image;

  public TransparentImageIcon(ImageIcon icon, float opacity) {
    this.icon = icon;
    this.opacity = opacity;
  }

  /**
   * Overridden to return the image of a wrapped ImageIcon, or null if the wrapped icon is not an
   * ImageIcon.
   *
   * @return the Image object for a wrapped ImageIcon, or null
   */
  @Override
  public Image getImage() {
    return image;
  }

  /**
   * Overridden to forward to a wrapped ImageIcon. Does nothing if the wrapped icon is not an
   * ImageIcon.
   *
   * <p>In common with <code>ImageIcom</code>, the newly set image will only be shown when the
   * concerned component(s) are repainted.
   *
   * @param image Sets the image displayed by a wrapped ImageIcon
   */
  @Override
  public void setImage(Image image) {
    if (icon != null) {
      ((ImageIcon) icon).setImage(image);
    }
  }

  /**
   * Paints the wrapped icon with this TransparentImageIcons transparency.
   *
   * @param c The component to which the icon is painted
   * @param g the graphics context
   * @param x the X coordinate of the icon's top-left corner
   * @param y the Y coordinate of the icon's top-left corner
   */
  @Override
  public void paintIcon(Component c, Graphics g, int x, int y) {
    if (icon != null) {
      image = ((ImageIcon) icon).getImage();
      Graphics2D g2 = (Graphics2D) g.create();
      g2.setComposite(AlphaComposite.SrcAtop.derive(opacity));
      icon.paintIcon(c, g2, x, y);
      g2.dispose();
    } else {
      image = null;
    }
  }

  /**
   * Gets the width of the bounding of this TransparentImageIcon. Overridden to return the width of
   * the wrapped icon.
   *
   * @return the width in pixels
   */
  @Override
  public int getIconWidth() {
    return icon.getIconWidth();
  }

  /**
   * Gets the height of the bounding of this TransparentImageIcon. Overridden to return the height
   * of the wrapped icon.
   *
   * @return the height in pixels
   */
  @Override
  public int getIconHeight() {
    return icon.getIconHeight();
  }
}
