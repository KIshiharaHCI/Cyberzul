package azul.team12.view.board;

import azul.team12.view.listeners.OnClickVisitor;

public interface TileAcceptor {

  public void acceptClick(OnClickVisitor visitor);

}
