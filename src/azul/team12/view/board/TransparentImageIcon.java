package azul.team12.view.board;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.Icon;

/**
 * An Icon Wrapper class that paints the contained icon with a specified transparency.
 *
 */

public class TransparentIcon implements Icon {

    private Icon icon;
    private float opacity;

    public TransparentIcon(Icon icon, float opacity) {
        this.icon = icon;
        this.opacity = opacity;
    }

    /**
     * Paints the wrapped icon with this TransparentIcon's transparency.
     *
     * @param c The component to which the icon is painted
     * @param g the graphics context
     * @param x the X coordinate of the icon's top-left corner
     * @param y the Y coordinate of the icon's top-left corner
     */
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.SrcAtop.derive(opacity));
        icon.paintIcon(c,g2,x,y);
        g2.dispose();
    }

    @Override
    public int getIconWidth() {
        return icon.getIconWidth();
    }

    @Override
    public int getIconHeight() {
        return 0;
    }
}
