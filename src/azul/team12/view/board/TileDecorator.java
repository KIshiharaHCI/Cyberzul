package azul.team12.view.board;

import azul.team12.model.ModelTile;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Objects;

public abstract class TileDecorator extends JPanel implements Tile {
    private Tile tile;
    final int COLUMN;
    final int ROW;
    final String path;
    final JLabel label = new JLabel();

    public TileDecorator(int col, int row, ModelTile modelTile) {
        COLUMN = col;
        ROW = row;
        path = modelTile.toString();

        setLayout(new GridLayout(1, 1));
    }

    @Override
    public void setIcon(Float opacity) {
        if (path.equals("empty")) {
            return;
        }
        URL imgURL1 = getClass().getClassLoader().getResource(pathList.get(path));
        Image img = resizeImage(new ImageIcon(Objects.requireNonNull(imgURL1)));

        ImageIcon icon = new TransparentImageIcon(new ImageIcon(img),opacity);
        label.setIcon(icon);
        add(label);
    }

    private Image resizeImage(ImageIcon icon) {
        BufferedImage resizedimage = new BufferedImage(TILE_SIZE, TILE_SIZE,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedimage.createGraphics();
        g2.drawImage(icon.getImage(), 0, 0, TILE_SIZE, TILE_SIZE, null);

        return resizedimage;
    }

    @Override
    public int getColumn() {
        return tile.getColumn();
    }

    @Override
    public int getRow() {
        return tile.getRow();
    }

    @Override
    public JLabel getLabel() {
        return tile.getLabel();
    }
}
