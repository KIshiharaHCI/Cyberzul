package azul.team12.view.board;

import azul.team12.view.listeners.TileClickListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class Wall extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;

  private final int DEFAULT_TILE_SIZE = 20;
  private final int ROWS = 5;
  private final int COLS = 5;

  private final int buttonSize;

  public Wall() {
    setBackground(new Color(110, 150, 100));
    setPreferredSize(new Dimension((DEFAULT_TILE_SIZE + 2) * ROWS, (DEFAULT_TILE_SIZE + 2) * COLS));
    setMaximumSize(new Dimension((DEFAULT_TILE_SIZE + 2) * ROWS, (DEFAULT_TILE_SIZE + 2) * COLS));
    setMinimumSize(new Dimension((DEFAULT_TILE_SIZE + 2) * ROWS, (DEFAULT_TILE_SIZE + 2) * COLS));
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setAlignmentX(1.0f);
    setAlignmentY(1.0f);
    this.buttonSize = DEFAULT_TILE_SIZE;
    for (int y = 0; y < ROWS; y++) {
      final JPanel currentRow = new JPanel();
      currentRow.setAlignmentX(0.1f);
      currentRow.setAlignmentY(1.0f);
      currentRow.setLayout(new GridLayout(1, COLS));
      currentRow.setMaximumSize(
          new Dimension(ROWS * buttonSize, COLS * buttonSize));

      for (int x = 1; x <= COLS; x++) {
        currentRow.add(new TileWithoutListener(y, x, buttonSize));

      }
      add(currentRow);
    }
  }


  public Wall(int tileSize, TileClickListener tileClickListener) {
    setBackground(new Color(110, 150, 100));
    setPreferredSize(new Dimension((tileSize + 2) * ROWS, (tileSize + 2) * COLS));
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setAlignmentX(1.0f);
    this.buttonSize = tileSize;
    for (int y = 0; y < ROWS; y++) {
      final JPanel currentRow = new JPanel();
      currentRow.setAlignmentX(0.1f);
      currentRow.setLayout(new GridLayout(1, ROWS));
      currentRow.setMaximumSize(
          new Dimension(ROWS * tileSize, COLS * tileSize));
      for (int x = 0; x < COLS; x++) {
        currentRow.add(new TileDestinationWall(y, x, tileSize, tileClickListener, null));
      }
      add(currentRow);
    }

    this.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
      }
    });
  }


  public Wall(int buttonSize) {
    setBackground(new Color(110, 150, 100));
    this.buttonSize = buttonSize;
  }

  /*
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2D = (Graphics2D) g;
    int idx;
    int idy = 5;

    for (int y = 0; y < ROWS; y++) {
      for (int x = 0; x < COLS; x++) {
        idx = (buttonSize + 5) * x;
        g2D.setColor(Color.WHITE);
        g2D.drawRect(idx, idy, buttonSize, buttonSize);
        g2D.fillRect(idx, idy, buttonSize, buttonSize);
      }
      idy = idy + (buttonSize + 5);
    }
  }
  */

}