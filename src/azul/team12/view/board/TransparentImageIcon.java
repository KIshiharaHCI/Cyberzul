package azul.team12.view.board;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * An Icon Wrapper class that paints the contained icon with a specified transparency.
 *
 */

public class TransparentImageIcon extends ImageIcon {

    private transient Icon icon;
    private transient Image image;
    private float opacity;
    private static final long serialVersionUID = 6L;

    public TransparentImageIcon(ImageIcon icon, float opacity) {
        this.icon = icon;
        this.opacity = opacity;
    }

    /**
     * Overridden to return the image of a wrapped ImageIcon, or null if the wrapped icon
     * is not an ImageIcon.
     *
     * @return the Image object for a wrapped ImageIcon, or null
     */
    @Override
    public Image getImage() {
        return image;
    }
    /**
     * Overridden to forward to a wrapped ImageIcon.  Does nothing if the wrapped icon
     * is not an ImageIcon.
     * <P>
     * In common with <code>ImageIcom</code>, the newly set image will only be shown when the
     * concerned component(s) are repainted.
     *
     * @param image Sets the image displayed by a wrapped ImageIcon
     */
    @Override
    public void setImage(Image image) {
        if (icon instanceof ImageIcon) {
            ((ImageIcon) icon).setImage(image);
        }
    }
    /**
     * Gets the icon wrapped by this <CODE>AlphaIcon</CODE>
     * @return the wrapped icon
     */
    public Icon getIcon() {
        return icon;
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
        if (icon instanceof ImageIcon) {
            image = ((ImageIcon) icon).getImage();
        } else {
            image = null;
        }
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.SrcAtop.derive(opacity));
        icon.paintIcon(c, g2, x, y);
        g2.dispose();
    }

    /**
     * Gets the width of the bounding of this TransparentImageIcon.
     * Overridden to return the width of the wrapped icon.
     *
     * @return the width in pixels
     */
    @Override
    public int getIconWidth() {
        return icon.getIconWidth();
    }
    /**
     * Gets the height of the bounding of this TransparentImageIcon.
     * Overridden to return the height of the wrapped icon.
     *
     * @return the height in pixels
     */
    @Override
    public int getIconHeight() {
        return icon.getIconHeight();
    }
}
