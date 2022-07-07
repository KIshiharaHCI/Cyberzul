package azul.team12.view.board;

import azul.team12.model.ModelTile;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Objects;

/**
 * Wrapper Class for Tiles such as SourceTiles,DestinationTiles,WallTiles.
 * Contains the base functionality for all Tiles.
 * (Decorators allow adding new behaviours to Objects)
 */
public abstract class TileDecorator extends JPanel implements Tile {
    final int COLUMN;
    final int ROW;
    final String path;
    final JLabel label = new JLabel();

    /**
     * Constructor to be called from subclasses. Used for initializing Image URL path and
     * cell XY coordinates in a given Component such as Plate, TableCenter, PatternLines, Wall.
     *
     * @param col X-Coordinate in given container
     * @param row Y-Coordinate in given container
     * @param modelTile contains the tile color information.
     */
    public TileDecorator(int col, int row, ModelTile modelTile) {
        COLUMN = col;
        ROW = row;
        path = modelTile.toString();

        setLayout(new GridLayout(1, 1));
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
        Image img = resizeImage(new ImageIcon(Objects.requireNonNull(imgURL1)));

        ImageIcon icon = new TransparentImageIcon(new ImageIcon(img),opacity);
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
        BufferedImage resizedimage = new BufferedImage(TILE_SIZE, TILE_SIZE,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedimage.createGraphics();
        g2.drawImage(icon.getImage(), 0, 0, TILE_SIZE, TILE_SIZE, null);

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

    @Override
    public JLabel getLabel() {
        return label;
    }
}
