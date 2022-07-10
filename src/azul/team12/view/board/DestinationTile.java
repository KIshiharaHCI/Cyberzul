package azul.team12.view.board;

import azul.team12.model.ModelTile;
import azul.team12.view.listeners.OnClickVisitor;
import azul.team12.view.listeners.TileClickListener;

import java.awt.*;

/**
 * Tiles on the Pattern Line and the Floor Line
 */
public class DestinationTile extends TileDecorator implements TileAcceptor {
    public DestinationTile(int col, int row, ModelTile modelTile, TileClickListener listener,int preferredTileSize) {
        super(col, row, modelTile,preferredTileSize);
        setPreferredSize(new Dimension(preferredTileSize,preferredTileSize));

        setIcon(1f);
        addMouseListener(listener);
    }

    @Override
    public void acceptClick(OnClickVisitor visitor) {
        visitor.visitOnClick(this);
    }
}
