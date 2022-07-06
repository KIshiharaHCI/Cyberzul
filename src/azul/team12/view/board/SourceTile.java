package azul.team12.view.board;

import azul.team12.model.ModelTile;
import azul.team12.view.listeners.TileClickListener;

public class SourceTile extends TileDecorator {

    private final int tileId;
    private final int plateId;

    public SourceTile(int col, int row, ModelTile modelTile, int tileId, int plateId,
                      TileClickListener listener) {
        super(col,row,modelTile);
        this.tileId = tileId;
        this.plateId = plateId;
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

}
