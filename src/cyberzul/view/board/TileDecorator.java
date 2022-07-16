package cyberzul.view.board;

import cyberzul.model.ModelTile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * Wrapper Class for Tiles such as SourceTiles,DestinationTiles,WallTiles. Contains the base
 * functionality for all Tiles. (Decorators allow adding new behaviours to Objects)
 */
public abstract class TileDecorator extends JPanel implements Tile {

    private static final long serialVersionUID = 1L;
    final int COLUMN;
    final int ROW;
    final String path;
    final JLabel label = new JLabel();
    final int tileSize;
    private final BufferedImage image;

    /**
     * Constructor to be called from subclasses. Used for initializing Image URL path and cell XY
     * coordinates in a given Component such as Plate, TableCenter, PatternLines, Wall.
     *
     * @param col       X-Coordinate in given container
     * @param row       Y-Coordinate in given container
     * @param modelTile contains the tile color information.
     */
    public TileDecorator(int col, int row, ModelTile modelTile, int tileSize) {
        COLUMN = col;
        ROW = row;
        path = modelTile.toString();
        this.tileSize = tileSize;

        setBackground(new Color(80, 145, 250, 130));
        try {
            URL imgURL = getClass().getClassLoader().getResource("img/tile-outline.png");
            image = ImageIO.read(Objects.requireNonNull(imgURL));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the Tile image and sets it on the JLabel.
     *
     * @param opacity how transparent the Tile should be.
     */
    @Override
    public void setIcon(Float opacity) {
        if (path.equals("empty")) {
            add(label);
            return;
        }
        URL imgURL1 = getClass().getClassLoader().getResource(pathList.get(path));
//        Image img = resizeImage(new ImageIcon(Objects.requireNonNull(imgURL1)));
        ImageIcon img = new ImageIcon(new ImageIcon(imgURL1).getImage()
                .getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH));
        ImageIcon icon = new TransparentImageIcon(img, opacity);
        label.setIcon(icon);
        add(label);
    }

    /**
     * Function to resize Tile ImageIcon to fit given TileSize
     *
     * @param icon passed tile Image.
     * @return resized Image based on given Tile Size.
     */
    private Image resizeImage(ImageIcon icon) {
        BufferedImage resizedimage = new BufferedImage(tileSize, tileSize,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedimage.createGraphics();
        g2.drawImage(icon.getImage(), 0, 0, tileSize, tileSize, null);
        return resizedimage;
    }

    @Override
    public int getColumn() {
        return COLUMN;
    }

    @Override
    public int getRow() {
        return ROW;
    }
/*
justification = "We are aware that data "
            + "encapsulation is violated here and that this is in principle bad. However, as here just "
            + "information of the view is possible to be changed from an external source and the "
            + "model is safe, we think it is ok to suppress this warning."
 */
    @Override
    @SuppressWarnings(value = "EI_EXPOSE_REP")
    public JLabel getLabel() {
        return label;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }
}
