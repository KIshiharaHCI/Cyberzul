package cyberzul.view.board;

import cyberzul.controller.Controller;
import cyberzul.model.ModelTile;
import cyberzul.view.listeners.TileClickListener;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FloorLinePanel extends JPanel {

    private static final long serialVersionUID = 4L;
    private transient final Controller controller;
    private final int NUMBER_OF_FLOOR_TILES = 7;
    private final transient TileClickListener tileClickListener;
    private JPanel contentBottom, contentUpper;
    private final int tileSize;

    public FloorLinePanel(String userName, Controller controller, TileClickListener tileClickListener, int minusPoints, int tileSize) {
        this.controller = controller;
        this.tileClickListener = tileClickListener;
        this.tileSize = tileSize;
        setProperties(tileSize, 2, 1, this);

        add(Box.createVerticalStrut(tileSize / 2));
        addBottomTilesRow(userName);
    }

    private void setProperties(int tileSize, int rows, int cols, JPanel panel) {
        panel.setOpaque(false);
        panel.setPreferredSize(
                new Dimension((tileSize + 2) * NUMBER_OF_FLOOR_TILES, (tileSize + 2) * rows));
        panel.setMaximumSize(
                new Dimension((tileSize + 2) * NUMBER_OF_FLOOR_TILES, (tileSize + 2) * rows));
        panel.setMinimumSize(
                new Dimension((tileSize + 2) * NUMBER_OF_FLOOR_TILES, (tileSize + 2) * rows));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(1.0f);
        panel.setAlignmentY(1.0f);
    }

    private void addBottomTilesRow(String userName) {
        contentBottom = new JPanel();
        ViewHelper.setPropertiesOfCurrentRow(tileSize, 7, 1, contentBottom);
        for (int col = 1; col <= NUMBER_OF_FLOOR_TILES; col++) {

            List<ModelTile> floorLineOfPlayer = controller.getFloorLineOfPlayer(userName);
            if (floorLineOfPlayer.size() >= col) {

                WallTile filledFloorLineTile = new WallTile(col, 1, floorLineOfPlayer.get(col - 1), tileSize, 1f);
                contentBottom.add(filledFloorLineTile);
            } else {
                DestinationTile emptyFloorLineTile = new DestinationTile(col, 1, ModelTile.EMPTY_TILE, tileClickListener, tileSize);
                contentBottom.add(emptyFloorLineTile);
            }
        }
        this.add(contentBottom);
    }

    private void addUpperNumbersRow() {
        contentUpper = new JPanel();
        ViewHelper.setPropertiesOfCurrentRow(tileSize, 7, 1, contentUpper);
        for (int i = 0; i < NUMBER_OF_FLOOR_TILES; i++) {
            String text;
            if (i < 2) {
                text = "-1";
            } else if (i > 4) {
                text = "-3";
            } else {
                text = "-2";
            }
            contentUpper.add(new JLabel(text));
        }
        this.add(contentUpper);
    }

    public void updateBottomTilesRow(String userName) {
        remove(contentBottom);
        addBottomTilesRow(userName);
    }
}
