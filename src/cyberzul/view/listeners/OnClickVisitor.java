package cyberzul.view.listeners;

import cyberzul.view.board.DestinationTile;
import cyberzul.view.board.SourceTile;

public interface OnClickVisitor {

    void visitOnClick(DestinationTile destinationTile);

    void visitOnClick(SourceTile sourceTile);

}
