package cyberzul.view.listeners;

import cyberzul.view.board.DestinationTile;
import cyberzul.view.board.SourceTile;

/**
 * Interface for two different implementations based on the Visitor type.
 */
public interface OnClickVisitor {

  void visitOnClick(DestinationTile destinationTile);

  void visitOnClick(SourceTile sourceTile);
}
