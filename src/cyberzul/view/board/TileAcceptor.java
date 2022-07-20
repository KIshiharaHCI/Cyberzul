package cyberzul.view.board;

import cyberzul.view.listeners.OnClickVisitor;

/**
 * Implements a dispatching operation that delegates the request to the accepted visitor object
 * (DestinationTile or SourceTile)
 */
public interface TileAcceptor {

  void acceptClick(OnClickVisitor visitor);
}
