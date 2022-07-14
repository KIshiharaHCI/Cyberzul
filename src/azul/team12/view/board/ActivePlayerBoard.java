package azul.team12.view.board;

import azul.team12.controller.Controller;
import azul.team12.view.listeners.TileClickListener;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class ActivePlayerBoard extends PlayerBoard {
    private final BufferedImage image;

    /**
     * The constructor to create a playerboard for a given player.
     *
     * @param controller
     * @param tileClickListener
     * @param playerName
     */
    public ActivePlayerBoard(Controller controller, TileClickListener tileClickListener,
                             String playerName, Dimension panelDimension) {
        super(controller, tileClickListener, playerName, Tile.NORMAL_TILE_SIZE, panelDimension);
        setOpaque(false);

        setMaximumSize(panelDimension);
        setMinimumSize(panelDimension);
        try {
            URL imgURL = getClass().getClassLoader().getResource("img/hud.png");
            image = ImageIO.read(Objects.requireNonNull(imgURL));
            image.getScaledInstance(panelDimension.width, panelDimension.height, Image.SCALE_DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }
}
