package azul.team12.view.listeners;

import azul.team12.view.board.TileDestinationFloorLine;
import azul.team12.view.board.TileDestinationPatternLines;
import azul.team12.view.board.TileDestinationWall;
import azul.team12.view.board.TileSource;

public interface OnClickVisitor {

  public void visitOnClick(TileDestinationPatternLines patternLinesTile);

  public void visitOnClick(TileSource tileSource);

  public void visitOnClick(TileDestinationFloorLine floorLineTile);

  public void visitOnClick(TileDestinationWall tileDestinationWall);

}
