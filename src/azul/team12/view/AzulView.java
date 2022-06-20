package azul.team12.view;

import azul.team12.view.board.GameBoard;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import javax.swing.*;

public class AzulView extends JFrame{

  public AzulView() throws HeadlessException {
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    GridBagLayout gbl = new GridBagLayout();
    //setLayout(new GridBagLayout());
    setSize (1200, 800);
    setMinimumSize(new Dimension(1200, 800));
    GameBoard gameBoard = new GameBoard(gbl);
    add(gameBoard);
  }
}
