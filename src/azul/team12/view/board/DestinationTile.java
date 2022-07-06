package azul.team12.view.board;

import azul.team12.model.ModelTile;

/**
 * Tiles on the Pattern Line and the Floor Line
 */
public class DestinationTile extends TileDecorator {
    public DestinationTile(int col, int row, ModelTile modelTile) {
        super(col, row, modelTile);
    }
}
