package azul.team12.view.board;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public interface TTile {
    final String BLACK_TILE_PATH = "img/black-tile.png";
    final String BLUE_TILE_PATH = "img/blue-tile.png";
    final String RED_TILE_PATH = "img/red-tile.png";
    final String WHITE_TILE_PATH = "img/white-tile.png";
    final String ORANGE_TILE_PATH = "img/yellow-tile.png";
    final String STARTING_PLAYER_MARKER_PATH = "img/start-player-button.png";

    final Map<String,String> pathList = Map.of(
            "BLACK_TILE_PATH",BLACK_TILE_PATH,
            "BLUE_TILE_PATH",BLUE_TILE_PATH,
            "RED_TILE_PATH",RED_TILE_PATH,
            "WHITE_TILE_PATH",WHITE_TILE_PATH,
            "ORANGE_TILE_PATH",ORANGE_TILE_PATH,
            "STARTING_PLAYER_MARKER_PATH",STARTING_PLAYER_MARKER_PATH
    );
    static final int TILE_SIZE = 40;

    /**
     * Set a Tile Image for the paint Method to call
     * @param opacity how transparent the Tile should be.
     */
    void setIcon(Float opacity);

    /**
     * Returns the Column of where the Tile is placed.
     *
     * @return
     */
    int getColumn();

    /**
     * Returns the Row of where the Tile is placed.
     *
     * @return
     */
    int getRow();

    /**
     * Returns the JLabel where the ImageIcon of the Tile is added.
     *
     * @return
     */
    JLabel getLabel();

}
