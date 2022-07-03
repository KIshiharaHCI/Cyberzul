package azul.team12.view.board;

import azul.team12.view.listeners.TileClickListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class Wall extends JPanel {

  private static final long serialVersionUID = 7526472295622776147L;

  private final int DEFAULT_TILE_SIZE = 25;
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
      this.setPropsToCurrRow(buttonSize, COLS, y, currentRow);
      for (int x = 0; x < COLS; x++) {
        currentRow.add(new TileWithoutListener(y, x, buttonSize));

      }
      add(currentRow);
    }
  }


  public Wall(int tileSize, TileClickListener tileClickListener) {
    setBackground(new Color(110, 150, 100));
    setPreferredSize(new Dimension((tileSize + 2) * ROWS, (tileSize + 2) * COLS));
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setAlignmentX(0.1f);
    this.buttonSize = tileSize;
    for (int y = 0; y < ROWS; y++) {
      final JPanel currentRow = new JPanel();
      this.setPropsToCurrRow(tileSize, COLS, y, currentRow);

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

  private void setPropsToCurrRow(int tileSize, int COLS, int row,
      JPanel currentRow) {
    currentRow.setLayout(new GridLayout(1, COLS, 4, 4));
    // for alignment on the right
    currentRow.setAlignmentX(0.1f);
    //padding
    currentRow.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
    // TileDestination is not transparent, but everything else on the current row is transparent
    currentRow.setOpaque(false);
    // for no stretching of the row
    currentRow.setMaximumSize(
        new Dimension(COLS * (tileSize + 4) + 4, row * (tileSize + 4) + 4));
    currentRow.setMinimumSize(
        new Dimension((DEFAULT_TILE_SIZE + 2) * ROWS, (DEFAULT_TILE_SIZE + 2) * COLS));
  }


}