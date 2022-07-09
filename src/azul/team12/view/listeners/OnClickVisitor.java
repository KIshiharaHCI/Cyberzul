package azul.team12.view.listeners;

import azul.team12.view.board.*;

public interface OnClickVisitor {
  public void visitOnClick(DestinationTile destinationTile);
  public void visitOnClick(SourceTile sourceTile);

}
