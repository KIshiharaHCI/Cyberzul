package azul.team12.view.board;

import azul.team12.model.ModelTile;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.net.URL;

public class TFactoryTile extends JPanel implements TTile {

    final int COLUMN;
    final int ROW;
    final int tileId;
    final int plateId;
    final String path;
    final JLabel label = new JLabel();

    public TFactoryTile(int col, int row, ModelTile modelTile, int tileId, int plateId) {
        COLUMN = col;
        ROW = row;
        this.tileId = tileId;
        this.plateId = plateId;
        path = modelTile.toString().concat("_PATH");
    }

    @Override
    public void setIcon(Float opacity) {
        if (path.equals("EMPTY_TILE_PATH")) {
            return;
        }
        URL imgURL = getClass().getClassLoader().getResource(pathList.get(path));
        Image img = new ImageIcon(imgURL).getImage().getScaledInstance(TILE_SIZE,TILE_SIZE, Image.SCALE_DEFAULT);
        ImageIcon icon = new TransparentImageIcon(new ImageIcon(img),opacity);
        label.setIcon(icon);
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
