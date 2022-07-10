package azul.team12.view.board;

import azul.team12.model.ModelTile;
import azul.team12.view.listeners.OnClickVisitor;
import azul.team12.view.listeners.TileClickListener;

import java.awt.*;

/**
 * The Tiles on the Factory Displays and Table Center which can be selected
 * to place on the Pattern Lines or the Floor Line.
 */
public class SourceTile extends TileDecorator implements TileAcceptor {

    private final int tileId;
    private final int plateId;

    private static final long serialVersionUID = 2L;

    /**
     * Calls the Constructor of the Superclass and
     * sets the Tile ID and Plate ID used when notifying the GameModel.
     *
     * @param col X-Coordinate in given container
     * @param row Y-Coordinate in given container
     * @param modelTile contains the tile color information.
     * @param tileId identifier for which Tile on Plate/Table Center was selected
     * @param plateId identifier for which Plate/Table Center was selected
     * @param listener used for listening for MouseClickedEvents
     */
    public SourceTile(int col, int row, ModelTile modelTile, int tileId, int plateId, int tileSize,
                      TileClickListener listener) {
        super(col,row,modelTile,tileSize);
        this.tileId = tileId;
        this.plateId = plateId;
        setPreferredSize(new Dimension(tileSize,tileSize));
        setIcon(1f);
        addMouseListener(listener);
    }

    @Override
    public void setIcon(Float opacity) {
        super.setIcon(opacity);
    }

    public int getPlateId() {
        return plateId;
    }

    public int getTileId() {
        return tileId;
    }

    @Override
    public void acceptClick(OnClickVisitor visitor) {
        visitor.visitOnClick(this);
    }
}
