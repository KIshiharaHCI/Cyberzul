package azul.team12.view.board;

import azul.team12.view.listeners.TileClickListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class TableCenterTiles extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;

  private final int ROWS = 3;
  private final int COLS = 12;
  private final int TILE_CENTER_SIZE = 40;


  public TableCenterTiles(TileClickListener tileClickListener) {
    //setLayout(new GridLayout(4, 9));
    JPanel content = new JPanel(new GridLayout(ROWS, COLS, 10, 10));
    content.setPreferredSize(new Dimension(TILE_CENTER_SIZE * COLS + 10 * (COLS - 1),
        TILE_CENTER_SIZE * ROWS + 10 * (ROWS - 1)));
    setBackground(new Color(255, 255, 204));
    content.setBackground(new Color(255, 255, 204));
    content.setOpaque(false);
    for (int row = 0; row < ROWS; row++) {
      for (int col = 0; col < COLS; col++) {
        ImageIcon icon = getResizedRoundImageIcon("img/orange-tile.png", TILE_CENTER_SIZE);
        TileCenter tileCenter = new TileCenter(row, col, TILE_CENTER_SIZE, icon, tileClickListener);
        tileCenter.setOpaque(false);
        content.add(tileCenter);
      }
    }
    add(content);

  }

  private ImageIcon getResizedRoundImageIcon(String path, int size) {
    URL imgURL1 = getClass().getClassLoader().getResource(path);
    return new ImageIcon(
        new ImageIcon(imgURL1).getImage()
            .getScaledInstance(size, size, Image.SCALE_DEFAULT));
  }
}
