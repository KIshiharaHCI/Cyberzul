package cyberzul.view.listeners;

import cyberzul.view.board.DestinationTile;
import cyberzul.view.board.SourceTile;

public interface OnClickVisitor {

  public void visitOnClick(DestinationTile destinationTile);

  public void visitOnClick(SourceTile sourceTile);

}
